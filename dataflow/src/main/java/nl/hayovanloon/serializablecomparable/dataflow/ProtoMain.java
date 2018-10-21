package nl.hayovanloon.serializablecomparable.dataflow;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import nl.hayovanloon.serializablecomparable.dataflow.pb.NestedPb;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.sdk.Pipeline;
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

import static nl.hayovanloon.serializablecomparable.dataflow.Main.DATA_FILE_PREFIX;


public class ProtoMain {

  public static void main(String[] args) {
    final CustomPipelineOptions options = PipelineOptionsFactory
        .fromArgs(args).as(CustomPipelineOptions.class);

    String dataBucket = options.getDataBucket();
    if (dataBucket == null) {
      throw new IllegalArgumentException("missing dataBucket parameters");
    }

    options.setRunner(DataflowRunner.class);
    options.setRegion("europe-west1");
    options.setZone("europe-west1-b");
    options.setJobName("serializable-comparable-only-proto");

    final Pipeline pipeline = Pipeline.create(options);

    final TupleTag<NestedPb> TrueTag = new TupleTag<NestedPb>() {};
    final TupleTag<NestedPb> FalseTag = new TupleTag<NestedPb>() {};

    final PCollectionTuple pTuple = pipeline
        .apply("read_ndjson", TextIO.read()
            .from("gs://" + dataBucket + "/" + DATA_FILE_PREFIX + "*"))
        .apply("parse_objects", ParDo.of(new DoFn<String, NestedPb>() {
          @ProcessElement
          public void processElement(ProcessContext c) {
            final String s = c.element();
            final NestedPb.Builder builder = NestedPb.newBuilder();
            try {
              JsonFormat.parser().merge(s, builder);
              c.output(builder.build());
            } catch (InvalidProtocolBufferException e) {
              throw new IllegalArgumentException(e);
            }
          }
        }))
        .apply("split_by_boolValue",
            ParDo.of(new DoFn<NestedPb, NestedPb>() {
              @ProcessElement
              public void processElement(ProcessContext c) {
                final NestedPb nested = c.element();
                if (nested.getBoolValue()) {
                  c.output(nested);
                } else {
                  c.output(FalseTag, nested);
                }
              }
            }).withOutputTags(TrueTag, TupleTagList.of(FalseTag)));

    final PCollection<Long> trues = pTuple.get(TrueTag)
        .apply("uppercase_string", ParDo.of(new DoFn<NestedPb, NestedPb>() {
          @ProcessElement
          public void processElement(ProcessContext c) {
            final NestedPb.Builder builder = c.element().toBuilder();
            final String s = builder.getStringValue();
            if (s != null) {
              builder.setStringValue(s.toUpperCase());
            }
            c.output(builder.build());
          }
        }))
        .apply("filter", Filter.by(new FilterOnInt()))
        .apply("count", Count.globally());

    final PCollection<Long> falses = pTuple.get(FalseTag)
        .apply("lowercase_string", ParDo.of(new DoFn<NestedPb, NestedPb>() {
          @ProcessElement
          public void processElement(ProcessContext c) {
            final NestedPb.Builder builder = c.element().toBuilder();
            final String s = builder.getStringValue();
            if (s != null) {
              builder.setStringValue(s.toLowerCase());
            }
            c.output(builder.build());
          }
        }))
        .apply("filter", Filter.by(new FilterOnInt()))
        .apply("count", Count.globally());

    PCollectionList.of(trues).and(falses)
        .apply("flatten", Flatten.pCollections())
        .apply("to_string", new ToString())
        .apply("write_results", TextIO.write()
            .to("gs://" + dataBucket + "/serializablecomparable/result"));

    pipeline.run();
  }

  private static class FilterOnInt
      implements SerializableFunction<NestedPb, Boolean> {

    @Override
    public Boolean apply(NestedPb input) {
      return input.getIntValue() % 2 == 0;
    }
  }
}
