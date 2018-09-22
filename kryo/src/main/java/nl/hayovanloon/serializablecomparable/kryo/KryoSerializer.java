package nl.hayovanloon.serializablecomparable.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import nl.hayovanloon.serializablecomparable.LocalMessage;
import nl.hayovanloon.serializablecomparable.LocalMessageSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class KryoSerializer<T extends LocalMessage>
    extends LocalMessageSerializer {

  private static final Kryo KRYO = new Kryo();

  private final Class<T> type;

  public KryoSerializer(Class<T> type) {
    this.type = type;
  }

  @Override
  public String getName() {
    return "Kryo";
  }

  @Override
  public byte[] serialize(LocalMessage o) throws IOException {
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

  @Override
  public LocalMessage deserialize(byte[] serialized) {
    final Input input = new Input(serialized);
    return KRYO.readObject(input, type);
  }
}
