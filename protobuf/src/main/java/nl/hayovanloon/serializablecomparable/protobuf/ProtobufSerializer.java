package nl.hayovanloon.serializablecomparable.protobuf;

import com.google.protobuf.Message;
import nl.hayovanloon.serializablecomparable.LocalMessage;
import nl.hayovanloon.serializablecomparable.Serializer;
import nl.hayovanloon.serializablecomparable.Simple;
import nl.hayovanloon.serializablecomparable.protobuf.pb.NestedPb;
import nl.hayovanloon.serializablecomparable.protobuf.pb.SimplePb;

import java.io.IOException;
import java.util.function.Function;


public class ProtobufSerializer implements Serializer {

  private final Function<LocalMessage, Message> fromLocal;
  private final Function<Message, LocalMessage> toLocal;


  public ProtobufSerializer(Function<LocalMessage, Message> fromLocal,
                            Function<Message, LocalMessage> toLocal) {
    this.fromLocal = fromLocal;
    this.toLocal = toLocal;
  }

  @Override
  public String getName() {
    return "Protobuf";
  }

  @Override
  public byte[] serialize(LocalMessage o) {
    return fromLocal.apply(o).toByteArray();
  }

  @Override
  public <T extends LocalMessage> T deserialize(byte[] serialized,
                                                Class<T> type)
      throws IOException {

    final Message.Builder builder = type == Simple.class
        ? SimplePb.newBuilder() : NestedPb.newBuilder();
    return type.cast(toLocal.apply(builder.mergeFrom(serialized).build()));
  }
}
