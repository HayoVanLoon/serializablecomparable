package nl.hayovanloon.serializablecomparable.protobuflean;

import com.google.protobuf.Message;
import nl.hayovanloon.serializablecomparable.LocalMessage;
import nl.hayovanloon.serializablecomparable.Serializer;
import nl.hayovanloon.serializablecomparable.Simple;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;


class ProtobufLeanSerializer<T extends Message> implements Serializer<Message> {

  private final Class<T> type;

  ProtobufLeanSerializer(Class<T> type) {
    this.type = type;
  }

  @Override
  public String getName() {
    return "Protobuf-Lean";
  }

  @Override
  public byte[] serialize(Message o) {
    return o.toByteArray();
  }

  @Override
  public Message deserialize(byte[] bytes) throws IOException {
    final Message.Builder builder = getBuilder(type);
    return type.cast(builder.mergeFrom(bytes).build());
  }

  private <T extends Message> T.Builder getBuilder(Class<T> type) {
    try {
      return (Message.Builder) type.getMethod("newBuilder").invoke(null);
    } catch (IllegalAccessException | InvocationTargetException |
        NoSuchMethodException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public List<Message> prepareInput(List<LocalMessage> retrieved) {
    final List<Message> messages = new LinkedList<>();
    for (LocalMessage localMessage : retrieved) {
      if (localMessage instanceof Simple) {
        messages.add(SimplePbOps.from(localMessage));
      } else {
        messages.add(NestedPbOps.from(localMessage));
      }
    }
    return messages;
  }
}
