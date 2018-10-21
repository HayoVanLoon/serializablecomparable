package nl.hayovanloon.serializablecomparable.dataflow;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;


public class ToString extends PTransform<PCollection<?>, PCollection<String>> {

  @Override
  public PCollection<String> expand(PCollection<?> input) {
    return input
        .apply("to_string", ParDo.of(new DoFn<Object, String>() {
          @ProcessElement
          public void processElement(ProcessContext c) {
            c.output(c.element().toString());
          }
        }));
  }
}
