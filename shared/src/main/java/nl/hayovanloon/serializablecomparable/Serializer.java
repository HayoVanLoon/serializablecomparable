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
   * Serializes the object into a byte array
   *
   * @param o object to serialize
   * @return a byte array
   */
  byte[] serialize(Object o) throws IOException;

  /**
   * Deserializes the byte array into an object
   *
   * @param bytes byte array to deserialize
   * @return a deserialised object
   */
  <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;
}
