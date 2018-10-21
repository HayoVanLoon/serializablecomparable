package nl.hayovanloon.serializablecomparable.dataflow;

import com.google.protobuf.Message;
import nl.hayovanloon.serializablecomparable.Nested;
import nl.hayovanloon.serializablecomparable.Simple;
import nl.hayovanloon.serializablecomparable.dataflow.pb.NestedPb;
import nl.hayovanloon.serializablecomparable.dataflow.pb.SimplePb;

import java.util.ArrayList;
import java.util.List;


/**
 * Utility class for {@link NestedPb} operations.
 */
public final class NestedPbOps {

  private NestedPbOps() {
    throw new AssertionError();
  }

  public static Message toPb(Nested nested) {
    final NestedPb.Builder builder = NestedPb.newBuilder()
        .setLongValue(nested.getLongValue())
        .setIntValue(nested.getIntValue())
        .setDoubleValue(nested.getDoubleValue())
        .setFloatValue(nested.getFloatValue())
        .setBoolValue(nested.isBoolValue());

    if (nested.getStringValue() != null) {
      builder.setStringValue(nested.getStringValue());
    }
    if (nested.getSimple() != null) {
      builder.setSimple((SimplePb) SimplePbOps.toPb(nested.getSimple()));
    }
    if (nested.getLongList() != null) {
      builder.addAllLongList(nested.getLongList());
    }
    if (nested.getSimples() != null) {
      for (Simple simple : nested.getSimples()) {
        builder.addSimples((SimplePb) SimplePbOps.toPb(simple));
      }
    }

    return builder.build();
  }

  public static Nested fromPb(NestedPb n) {
    final String nulled = n.getStringValue().isEmpty()
        ? null : n.getStringValue();

    final List<Simple> simples = new ArrayList<>();
    for (SimplePb s : n.getSimplesList()) {
      simples.add(SimplePbOps.fromPb(s));
    }

    return new Nested(nulled, n.getLongValue(),
        n.getIntValue(), n.getDoubleValue(), n.getFloatValue(),
        n.getBoolValue(), n.getLongListList(),
        SimplePbOps.fromPb(n.getSimple()), simples);
  }
}
