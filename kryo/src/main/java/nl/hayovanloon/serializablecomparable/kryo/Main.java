package nl.hayovanloon.serializablecomparable.kryo;

import nl.hayovanloon.serializablecomparable.Generator;
import nl.hayovanloon.serializablecomparable.Runner;
import nl.hayovanloon.serializablecomparable.Serializer;

import java.io.IOException;


public class Main {

  private static final Serializer SERIALIZER = new KryoSerializer();

  public static void main(String[] args)
      throws IOException, ClassNotFoundException {

    if ("generate".equals(args[0])) {
      final int n = Integer.valueOf(args[3]);
      Generator.of(args).generate(n);
    } else if ("run".equals(args[0])) {
      final Runner runner = Runner.getInstance(SERIALIZER, args);

      runner.run();
    }
  }
}
