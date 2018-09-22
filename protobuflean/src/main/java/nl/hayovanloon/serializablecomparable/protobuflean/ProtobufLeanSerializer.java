package nl.hayovanloon.serializablecomparable.protobuflean;

import com.google.protobuf.Message;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


public class ProtobufLeanSerializer {


  public String getName() {
    return "Protobuf-Lean";
  }

  public byte[] serialize(Message m) {
    return m.toByteArray();
  }

  public <T extends Message> T deserialize(byte[] serialized,
                                           Class<T> type)
      throws IOException {

    final Message.Builder builder;
    try {
      builder = (Message.Builder) type.getMethod("newBuilder").invoke(null);
      return type.cast(builder.mergeFrom(serialized).build());
    } catch (NoSuchMethodException | IllegalAccessException |
        InvocationTargetException e) {
      throw new IllegalArgumentException();
    }
  }
}
