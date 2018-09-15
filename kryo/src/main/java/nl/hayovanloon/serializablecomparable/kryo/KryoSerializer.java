package nl.hayovanloon.serializablecomparable.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import nl.hayovanloon.serializablecomparable.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class KryoSerializer implements Serializer {

  private static final Kryo KRYO = new Kryo();

  public String getName() {
    return "Kryo";
  }

  public byte[] serialize(Object o) throws IOException {
    try (
        ByteArrayOutputStream bos = new ByteArrayOutputStream()
    ) {
      final Output output = new Output(bos);
      KRYO.writeObject(output, o);
      output.flush();
      output.close();
      return bos.toByteArray();
    }
  }

  public <T> T deserialize(byte[] serialized, Class<T> type) {
    final Input input = new Input(serialized);
    return KRYO.readObject(input, type);
  }
}
