package nl.hayovanloon.serializablecomparable.dataflow;

import nl.hayovanloon.serializablecomparable.Nested;
import nl.hayovanloon.serializablecomparable.dataflow.pb.NestedPb;
import org.apache.beam.sdk.coders.CoderException;
import org.apache.beam.sdk.coders.CustomCoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


class ProtoWrapperCoder extends CustomCoder<Nested> {
  @Override
  public void encode(Nested value, OutputStream outStream)
      throws CoderException, IOException {
    NestedPbOps.toPb(value).writeDelimitedTo(outStream);
  }

  @Override
  public Nested decode(InputStream inStream)
      throws CoderException, IOException {
    final NestedPb.Builder builder = NestedPb.newBuilder();
    return NestedPbOps.fromPb(builder.mergeFrom(inStream).build());
  }

  @Override
  public void verifyDeterministic() throws NonDeterministicException {
    // no map fields in our proto, so should be deterministic
  }
}
