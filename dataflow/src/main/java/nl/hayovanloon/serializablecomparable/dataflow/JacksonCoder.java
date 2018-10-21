package nl.hayovanloon.serializablecomparable.dataflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hayovanloon.serializablecomparable.Nested;
import org.apache.beam.sdk.coders.CoderException;
import org.apache.beam.sdk.coders.CustomCoder;
import org.apache.beam.sdk.coders.StringUtf8Coder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Mostly copied over from {@link org.apache.beam.sdk.io.gcp.bigquery.TableRowJsonCoder}.
 */
class JacksonCoder extends CustomCoder<Nested> {

  private transient ObjectMapper objectMapper = null;

  private ObjectMapper getObjectMapper() {
    if (objectMapper == null) {
      objectMapper = new ObjectMapper();
    }
    return objectMapper;
  }

  @Override
  public void encode(Nested value, OutputStream outStream)
      throws CoderException, IOException {
    final String s = getObjectMapper().writeValueAsString(value);
    StringUtf8Coder.of().encode(s, outStream);
  }

  @Override
  public Nested decode(InputStream inStream)
      throws CoderException, IOException {
    final String s = StringUtf8Coder.of().decode(inStream);
    return getObjectMapper().readValue(s, Nested.class);
  }

  @Override
  public void verifyDeterministic() throws NonDeterministicException {
    throw new NonDeterministicException(this,
        "JSON objects are non-deterministic");
  }
}
