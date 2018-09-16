package nl.hayovanloon.serializablecomparable.nativejava;

import nl.hayovanloon.serializablecomparable.LocalMessage;
import nl.hayovanloon.serializablecomparable.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;


public class NativeSerializer implements Serializer {

  public String getName() {
    return "Native";
  }

  public byte[] serialize(LocalMessage o) throws IOException {
    try (
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput oos = new ObjectOutputStream(bos)
    ) {
      oos.writeObject(o);
      return bos.toByteArray();
    }
  }

  public <T extends LocalMessage> T deserialize(byte[] serialized,
                                                Class<T> type)
      throws IOException {
    try (
        ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
        ObjectInputStream ois = new ObjectInputStream(bis)
    ) {
      final Object o = ois.readObject();
      return type.cast(o);
    } catch (ClassNotFoundException e) {
      throw new IOException(e);
    }
  }
}
