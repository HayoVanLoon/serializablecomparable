package nl.hayovanloon.serializablecomparable.dataflow;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import nl.hayovanloon.serializablecomparable.Nested;
import nl.hayovanloon.serializablecomparable.dataflow.pb.NestedPb;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;

import java.util.ArrayList;
import java.util.List;


public class GenerateMain {

  private static final int workers = 10;

  public static void main(String[] args) {
    final CustomPipelineOptions options = PipelineOptionsFactory
        .fromArgs(args).as(CustomPipelineOptions.class);

    options.setRunner(DataflowRunner.class);
    options.setRegion("europe-west1");
    options.setZone("europe-west1-b");
    options.setNumWorkers(workers);

    String dataBucket = options.getDataBucket();
    if (dataBucket == null) {
      throw new IllegalArgumentException("missing dataBucket parameters");
    }
    long size = options.getSize();

    final Pipeline pipeline = Pipeline.create(options);

    final List<Long> input = new ArrayList<>(workers);
    for (int i = 0; i < workers; i += 1) {
      input.add(size / workers);
    }

    pipeline
        .apply("create", Create.of(input))
        .apply("generate_objects", ParDo.of(new DoFn<Long, NestedPb>() {
          @ProcessElement
          public void processElement(ProcessContext c) {
            final long n = c.element();
            for (long l = n; l > 0; l -= 1) {
              final Nested nested = (Nested) Nested.createRandom();
              c.output((NestedPb) NestedPbOps.toPb(nested));
            }
          }
        }))
        .apply("to_json", ParDo.of(new DoFn<Message, String>() {
          @ProcessElement
          public void processElement(ProcessContext c) {
            final Message m = c.element();
            try {
              final String s = JsonFormat.printer()
                  .omittingInsignificantWhitespace()
                  .print(m);
              c.output(s);
            } catch (InvalidProtocolBufferException e) {
              throw new IllegalArgumentException(e);
            }
          }
        }))
        .apply("write_to_gcs", TextIO.write()
            .to("gs://" + dataBucket + "/" + Main.DATA_FILE_PREFIX));

    pipeline.run();
  }
}
