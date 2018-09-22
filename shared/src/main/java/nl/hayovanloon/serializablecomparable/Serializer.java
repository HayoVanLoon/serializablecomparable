package nl.hayovanloon.serializablecomparable;

import java.io.IOException;


public interface Serializer {

  /**
   * Returns the name of the serializer/serialization technique.
   *
   * @return a name
   */
  String getName();

  /**
   * Serializes the {@link LocalMessage} into a byte array
   *
   * @param o message to serialize
   * @return a byte array
   */
  byte[] serialize(LocalMessage o) throws IOException;

  /**
   * Deserializes the byte array into a {@link LocalMessage}.
   *
   * @param bytes byte array to deserialize
   * @return a deserialised object
   */
  <T extends LocalMessage> T deserialize(byte[] bytes,
                                         Class<T> type) throws IOException;
}
