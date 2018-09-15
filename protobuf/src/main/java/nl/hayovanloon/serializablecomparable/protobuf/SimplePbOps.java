package nl.hayovanloon.serializablecomparable.protobuf;

import nl.hayovanloon.serializablecomparable.protobuf.generated.SimplePb;
import nl.hayovanloon.serializablecomparable.Simple;


/**
 * Utility class for {@link SimplePb} operations.
 */
public final class SimplePbOps {

  private SimplePbOps() {
    throw new AssertionError();
  }

  /**
   * Creates a {@link SimplePb} message from a Simple POJO.
   *
   * @param simple  data container
   * @return  a new SimplePb
   */
  public static SimplePb from(Simple simple) {
    return SimplePb.newBuilder()
        .setStringValue(simple.getStringValue())
        .setLongValue(simple.getLongValue())
        .setIntValue(simple.getIntValue())
        .setDoubleValue(simple.getDoubleValue())
        .setFloatValue(simple.getFloatValue())
        .setBoolValue(simple.isBoolValue())
        .build();
  }
}
