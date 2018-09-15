package nl.hayovanloon.serializablecomparable.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hayovanloon.serializablecomparable.Serializer;

import java.io.IOException;


public class JsonSerializer implements Serializer {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public String getName() {
    return "Jackson";
  }

  public byte[] serialize(Object o) throws IOException {
    return OBJECT_MAPPER.writeValueAsBytes(o);
  }

  public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
    return OBJECT_MAPPER.readValue(bytes, type);
  }
}
