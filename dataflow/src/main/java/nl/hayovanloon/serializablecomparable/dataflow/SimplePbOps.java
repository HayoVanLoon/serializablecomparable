package nl.hayovanloon.serializablecomparable.dataflow;

import com.google.protobuf.Message;
import nl.hayovanloon.serializablecomparable.Simple;
import nl.hayovanloon.serializablecomparable.dataflow.pb.SimplePb;


/**
 * Utility class for {@link SimplePb} operations.
 */
public final class SimplePbOps {

  private SimplePbOps() {
    throw new AssertionError();
  }

  public static Message toPb(Simple simple) {
    final SimplePb.Builder builder = SimplePb.newBuilder()
        .setLongValue(simple.getLongValue())
        .setIntValue(simple.getIntValue())
        .setDoubleValue(simple.getDoubleValue())
        .setFloatValue(simple.getFloatValue())
        .setBoolValue(simple.isBoolValue());

    if (simple.getStringValue() != null) {
      builder.setStringValue(simple.getStringValue());
    }

    return builder.build();
  }

  public static Simple fromPb(SimplePb s) {
    final String nulled = s.getStringValue().isEmpty()
        ? null : s.getStringValue();

    return new Simple(nulled, s.getLongValue(), s.getIntValue(),
        s.getDoubleValue(), s.getFloatValue(), s.getBoolValue());
  }
}
