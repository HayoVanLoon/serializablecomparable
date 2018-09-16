package nl.hayovanloon.serializablecomparable.protobuf;

import com.google.protobuf.Message;

import nl.hayovanloon.serializablecomparable.LocalMessage;
import nl.hayovanloon.serializablecomparable.Nested;
import nl.hayovanloon.serializablecomparable.Simple;
import nl.hayovanloon.serializablecomparable.protobuf.generated.NestedPb;
import nl.hayovanloon.serializablecomparable.protobuf.generated.SimplePb;

import java.util.ArrayList;
import java.util.List;


/**
 * Utility class for {@link NestedPb} operations.
 */
public final class NestedPbOps {

  private NestedPbOps() {
    throw new AssertionError();
  }

  /**
   * Creates a {@link NestedPb} message from a {@link Nested} POJO.
   *
   * @param message Nested object to query
   * @return a new NestedPb
   * @throws IllegalArgumentException when item passed is not a Nested
   */
  public static NestedPb from(LocalMessage message) {
    if (!(message instanceof Nested)) {
      throw new IllegalArgumentException("expected Simple instance");
    }
    final Nested nested = (Nested)message;
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

  public static LocalMessage toLocal(Message message) {
    if (!(message instanceof NestedPb)) {
      throw new IllegalArgumentException("expected Simple instance");
    }
    final NestedPb n = (NestedPb)message;
    final List<Simple> simples = new ArrayList<>();
    for (SimplePb s : n.getSimplesList()) {
      simples.add(SimplePbOps.toLocal(s));
    }
    return new Nested(n.getStringValue(), n.getLongValue(),
        n.getIntValue(), n.getDoubleValue(), n.getFloatValue(),
        n.getBoolValue(), n.getLongListList(),
        SimplePbOps.toLocal(n.getSimple()), simples);
  }
}
