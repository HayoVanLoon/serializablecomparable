package nl.hayovanloon.serializablecomparable.json;

import com.google.gson.Gson;
import nl.hayovanloon.serializablecomparable.LocalMessage;
import nl.hayovanloon.serializablecomparable.LocalMessageSerializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class GsonSerializer<T extends LocalMessage>
    extends LocalMessageSerializer {

  private static final Gson GSON = new Gson();

  private final Class<T> type;

  public GsonSerializer(Class<T> type) {
    this.type = type;
  }

  @Override
  public String getName() {
    return "Gson";
  }

  @Override
  public byte[] serialize(LocalMessage o) throws IOException {
    return GSON.toJson(o).getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public LocalMessage deserialize(byte[] bytes) throws IOException {
    return GSON.fromJson(new String(bytes, StandardCharsets.UTF_8), type);
  }
}
