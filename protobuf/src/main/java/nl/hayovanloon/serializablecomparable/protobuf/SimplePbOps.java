package nl.hayovanloon.serializablecomparable.protobuf;

import com.google.protobuf.Message;
import nl.hayovanloon.serializablecomparable.LocalMessage;
import nl.hayovanloon.serializablecomparable.Simple;
import nl.hayovanloon.serializablecomparable.protobuf.pb.SimplePb;


/**
 * Utility class for {@link SimplePb} operations.
 */
public final class SimplePbOps {

  private SimplePbOps() {
    throw new AssertionError();
  }

  /**
   * Creates a {@link SimplePb} message from a {@link Simple} POJO.
   *
   * @param message Simple object to query
   * @return a new SimplePb
   * @throws IllegalArgumentException when item passed is not a Simple
   */
  public static Message from(LocalMessage message) {
    if (!(message instanceof Simple)) {
      throw new IllegalArgumentException("expected Simple instance");
    }

    final Simple simple = (Simple) message;

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

  public static Simple toLocal(Message message) {
    if (!(message instanceof SimplePb)) {
      throw new IllegalArgumentException("expected Simple instance");
    }

    final SimplePb s = (SimplePb) message;

    final String nulled = s.getStringValue().isEmpty()
        ? null : s.getStringValue();

    return new Simple(nulled, s.getLongValue(), s.getIntValue(),
        s.getDoubleValue(), s.getFloatValue(), s.getBoolValue());
  }
}
