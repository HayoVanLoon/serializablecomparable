package nl.hayovanloon.serializablecomparable;

import java.io.IOException;
import java.util.List;


public interface Serializer<T> {

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
  byte[] serialize(T o) throws IOException;

  /**
   * Deserializes the byte array into a {@link LocalMessage}.
   *
   * @param bytes byte array to deserialize
   * @return a deserialised object
   */
  T deserialize(byte[] bytes) throws IOException;

  /**
   * Prepares the raw input. The usual implementation should simply return the
   * input.
   *
   * @param retrieved  data gathered from data set file
   * @return  a list of serializable items
   */
  List<T> prepareInput(List<LocalMessage> retrieved);
}
