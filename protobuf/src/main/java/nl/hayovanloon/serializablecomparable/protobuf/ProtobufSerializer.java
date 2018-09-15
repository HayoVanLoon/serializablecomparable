package nl.hayovanloon.serializablecomparable.protobuf;

import com.google.protobuf.Message;

import java.io.IOException;


/**
 * Custom Serializer since the deserialize method could not fulfill the generic
 * {@link nl.hayovanloon.serializablecomparable.Serializer} contract.
 */
public class ProtobufSerializer {

  public byte[] serialize(Message m) {
    return m.toByteArray();
  }

  @SuppressWarnings("unchecked")
  public <T extends Message> T deserialize(byte[] serialized, T.Builder builder)
      throws IOException {

    builder.mergeFrom(serialized);
    return (T)builder.build();
  }
}
