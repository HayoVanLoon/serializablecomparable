package nl.hayovanloon.serializablecomparable.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hayovanloon.serializablecomparable.LocalMessage;
import nl.hayovanloon.serializablecomparable.Serializer;

import java.io.IOException;


public class JsonSerializer implements Serializer {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Override
  public String getName() {
    return "Jackson";
  }

  @Override
  public byte[] serialize(LocalMessage o) throws IOException {
    return OBJECT_MAPPER.writeValueAsBytes(o);
  }

  @Override
  public <T extends LocalMessage> T deserialize(byte[] bytes,
                                                Class<T> type)
      throws IOException {
    return OBJECT_MAPPER.readValue(bytes, type);
  }
}
