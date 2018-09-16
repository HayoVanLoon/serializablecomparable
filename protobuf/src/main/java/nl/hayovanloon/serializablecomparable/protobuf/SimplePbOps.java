package nl.hayovanloon.serializablecomparable.protobuf;

import com.google.protobuf.Message;

import nl.hayovanloon.serializablecomparable.LocalMessage;
import nl.hayovanloon.serializablecomparable.Simple;
import nl.hayovanloon.serializablecomparable.protobuf.generated.SimplePb;


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
  public static SimplePb from(LocalMessage message) {
    if (!(message instanceof Simple)) {
      throw new IllegalArgumentException("expected Simple instance");
    }
    final Simple simple = (Simple)message;
    return SimplePb.newBuilder()
        .setStringValue(simple.getStringValue())
        .setLongValue(simple.getLongValue())
        .setIntValue(simple.getIntValue())
        .setDoubleValue(simple.getDoubleValue())
        .setFloatValue(simple.getFloatValue())
        .setBoolValue(simple.isBoolValue())
        .build();
  }

  public static Simple toLocal(Message message) {
    if (!(message instanceof SimplePb)) {
      throw new IllegalArgumentException("expected Simple instance");
    }
    final SimplePb s = (SimplePb) message;
    return new Simple(s.getStringValue(), s.getLongValue(), s.getIntValue(),
        s.getDoubleValue(), s.getFloatValue(), s.getBoolValue());
  }
}
