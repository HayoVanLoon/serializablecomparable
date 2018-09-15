package nl.hayovanloon.serializablecomparable.protobuf;

import nl.hayovanloon.serializablecomparable.Nested;
import nl.hayovanloon.serializablecomparable.Simple;
import nl.hayovanloon.serializablecomparable.protobuf.generated.NestedPb;


/**
 * Utility class for {@link NestedPb} operations.
 */
public final class NestedPbOps {

  private NestedPbOps() {
    throw new AssertionError();
  }

  /**
   * Creates a {@link NestedPb} message from a Simple POJO.
   *
   * @param nested data container
   * @return a new NestedPb
   */
  public static NestedPb from(Nested nested) {
    final NestedPb.Builder builder = NestedPb.newBuilder()
        .setStringValue(nested.getStringValue())
        .setLongValue(nested.getLongValue())
        .setIntValue(nested.getIntValue())
        .setDoubleValue(nested.getDoubleValue())
        .setFloatValue(nested.getFloatValue())
        .setBoolValue(nested.isBoolValue())
        .setSimple(SimplePbOps.from(nested.getSimple()))
        .addAllLongList(nested.getLongList());

    for (Simple simple : nested.getSimples()) {
      builder.addSimples(SimplePbOps.from(simple));
    }

    return builder.build();
  }
}
