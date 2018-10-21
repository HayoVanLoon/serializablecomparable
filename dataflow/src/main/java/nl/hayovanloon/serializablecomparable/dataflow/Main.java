package nl.hayovanloon.serializablecomparable.dataflow;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import nl.hayovanloon.serializablecomparable.Nested;
import nl.hayovanloon.serializablecomparable.Simple;
import nl.hayovanloon.serializablecomparable.dataflow.pb.NestedPb;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.Coder;
import org.apache.beam.sdk.coders.CoderRegistry;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.Flatten;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionList;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;

import java.util.ArrayList;


public class Main {

  static final String DATA_FILE_PREFIX = "serializablecomparable/data";

  public static void main(String[] args) {
    final CustomPipelineOptions options = PipelineOptionsFactory
        .fromArgs(args).as(CustomPipelineOptions.class);

    String dataBucket = options.getDataBucket();
    if (dataBucket == null) {
      throw new IllegalArgumentException("missing dataBucket parameters");
    }

    if (options.getCoder() == null) {
      throw new IllegalArgumentException("missing target coder");
    }

    final Coder<Nested> coder;
    switch (options.getCoder()) {
      case "proto":
        coder = new ProtoWrapperCoder();
        break;
      case "json":
        coder = new JacksonCoder();
        break;
      default:
        coder = null;
    }

    options.setRunner(DataflowRunner.class);
    options.setRegion("europe-west1");
    options.setZone("europe-west1-b");
    options.setJobName("serializable-comparable-" + options.getCoder());

    final Pipeline pipeline = Pipeline.create(options);

    if (coder != null) {
      final CoderRegistry registry = pipeline.getCoderRegistry();
      registry.registerCoderForClass(Nested.class, coder);
    }

    final TupleTag<Nested> TrueTag = new TupleTag<Nested>() {};
    final TupleTag<Nested> FalseTag = new TupleTag<Nested>() {};

    final PCollectionTuple pTuple = pipeline
        .apply("read_ndjson", TextIO.read()
            .from("gs://" + dataBucket + "/" + DATA_FILE_PREFIX + "*"))
        .apply("parse_objects", ParDo.of(new DoFn<String, Nested>() {
          @ProcessElement
          public void processElement(ProcessContext c) {
            final String s = c.element();
            final NestedPb.Builder builder = NestedPb.newBuilder();
            try {
              JsonFormat.parser().merge(s, builder);
            } catch (InvalidProtocolBufferException e) {
              throw new IllegalArgumentException(e);
            }
            c.output(NestedPbOps.fromPb(builder.build()));
          }
        }))
        .apply("split_by_boolValue",
            ParDo.of(new DoFn<Nested, Nested>() {
              @ProcessElement
              public void processElement(ProcessContext c) {
                final Nested nested = c.element();
                if (nested.isBoolValue()) {
                  c.output(nested);
                } else {
                  c.output(FalseTag, nested);
                }
              }
            }).withOutputTags(TrueTag, TupleTagList.of(FalseTag)));


    final PCollection<Long> trues = pTuple.get(TrueTag)
        .apply("uppercase_string", ParDo.of(new DoFn<Nested, Nested>() {
          @ProcessElement
          public void processElement(ProcessContext c) {
            final Nested nested = c.element();

            // make a defensive copy
            final Nested nestedCopy = deepCopy(nested);

            final String s = nestedCopy.getStringValue();
            if (s != null) {
              nestedCopy.setStringValue(s.toUpperCase());
            }
            c.output(nestedCopy);
          }
        }))
        .apply("filter", Filter.by(new FilterOnInt()))
        .apply("count", Count.globally());

    final PCollection<Long> falses = pTuple.get(FalseTag)
        .apply("lowercase_string", ParDo.of(new DoFn<Nested, Nested>() {
          @ProcessElement
          public void processElement(ProcessContext c) {
            final Nested nested = c.element();

            // make a defensive copy
            final Nested nestedCopy = deepCopy(nested);

            final String s = nestedCopy.getStringValue();
            if (s != null) {
              nestedCopy.setStringValue(s.toLowerCase());
            }
            c.output(nestedCopy);
          }
        }))
        .apply("filter", Filter.by(new FilterOnInt()))
        .apply("count", Count.globally());

    PCollectionList.of(trues).and(falses)
        .apply("flatten", Flatten.pCollections())
        .apply("to_string", new ToString())
        .apply("write_results", TextIO.write()
            .to("gs://" + dataBucket + "/serializablecomparable/result"));

    ;
    pipeline.run();
  }

  private static Nested deepCopy(Nested nested) {
    final Nested nestedCopy = new Nested(nested.getStringValue(),
        nested.getLongValue(), nested.getIntValue(), nested.getDoubleValue(),
        nested.getFloatValue(), nested.isBoolValue(), new ArrayList<>(),
        deepCopy(nested.getSimple()), new ArrayList<>());
    for (Long l : nested.getLongList()) {
      nestedCopy.getLongList().add(l);
    }
    for (Simple simple : nested.getSimples()) {
      nestedCopy.getSimples().add(deepCopy(simple));
    }
    return nestedCopy;
  }

  private static Simple deepCopy(Simple simple) {
    return new Simple(simple.getStringValue(), simple.getLongValue(),
        simple.getIntValue(), simple.getDoubleValue(), simple.getFloatValue(),
        simple.isBoolValue());
  }

  private static class FilterOnInt
      implements SerializableFunction<Nested, Boolean> {

    @Override
    public Boolean apply(Nested input) {
      return input.getIntValue() % 2 == 0;
    }
  }
}
