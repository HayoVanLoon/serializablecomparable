package nl.hayovanloon.serializablecomparable.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hayovanloon.serializablecomparable.LocalMessage;
import nl.hayovanloon.serializablecomparable.LocalMessageSerializer;

import java.io.IOException;


public class JsonSerializer<T extends LocalMessage>
    extends LocalMessageSerializer {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private final Class<T> type;

  public JsonSerializer(Class<T> type) {
    this.type = type;
  }

  @Override
  public String getName() {
    return "Jackson";
  }

  @Override
  public byte[] serialize(LocalMessage o) throws IOException {
    return OBJECT_MAPPER.writeValueAsBytes(o);
  }

  @Override
  public LocalMessage deserialize(byte[] bytes) throws IOException {
    return OBJECT_MAPPER.readValue(bytes, type);
  }
}
