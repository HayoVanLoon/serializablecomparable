package nl.hayovanloon.serializablecomparable.dataflow;

import com.google.common.collect.ImmutableList;
import nl.hayovanloon.serializablecomparable.Nested;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.PCollection;
import org.junit.Rule;
import org.junit.Test;


public class JacksonCoderTest {

  @Rule
  public final TestPipeline p = createPipeline();

  private static TestPipeline createPipeline() {
    final PipelineOptions options = PipelineOptionsFactory.create();
    options.setStableUniqueNames(PipelineOptions.CheckEnabled.OFF);
    return TestPipeline.fromOptions(options);
  }

  @Test
  public void happy() {
    final Nested nested = (Nested) Nested.createRandom();
    final Nested nested2 = (Nested) Nested.createRandom();

    final PCollection<Nested> actual = p
        .apply("create",
            Create.of(nested, nested2).withCoder(new JacksonCoder()));

    PAssert.that(actual).containsInAnyOrder(ImmutableList.of(nested, nested2));

    p.run();
  }
}
