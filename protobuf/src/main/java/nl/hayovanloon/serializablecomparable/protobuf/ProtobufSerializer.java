package nl.hayovanloon.serializablecomparable.protobuf;

import com.google.protobuf.Message;
import nl.hayovanloon.serializablecomparable.LocalMessage;
import nl.hayovanloon.serializablecomparable.LocalMessageSerializer;
import nl.hayovanloon.serializablecomparable.Simple;
import nl.hayovanloon.serializablecomparable.protobuf.pb.NestedPb;
import nl.hayovanloon.serializablecomparable.protobuf.pb.SimplePb;

import java.io.IOException;
import java.util.function.Function;


public class ProtobufSerializer<T extends LocalMessage>
    extends LocalMessageSerializer {

  private final Function<T, Message> fromLocal;
  private final Function<Message, T> toLocal;
  private final Class<T> type;


  ProtobufSerializer(Function<T, Message> fromLocal,
                     Function<Message, T> toLocal,
                     Class<T> type) {
    this.fromLocal = fromLocal;
    this.toLocal = toLocal;
    this.type = type;
  }

  @Override
  public String getName() {
    return "Protobuf";
  }

  @Override
  public byte[] serialize(LocalMessage o) {
    return fromLocal.apply(type.cast(o)).toByteArray();
  }

  @Override
  public LocalMessage deserialize(byte[] serialized) throws IOException {
    final Message.Builder builder = type == Simple.class
        ? SimplePb.newBuilder() : NestedPb.newBuilder();
    return type.cast(toLocal.apply(builder.mergeFrom(serialized).build()));
  }
}
