package nl.hayovanloon.serializablecomparable.protobuf;

import nl.hayovanloon.serializablecomparable.Generator;
import nl.hayovanloon.serializablecomparable.Runner;
import nl.hayovanloon.serializablecomparable.Serializer;

import java.io.IOException;


public class Main {

  public static void main(String[] args)
      throws IOException, ClassNotFoundException {

    if ("generate".equals(args[0])) {
      final int n = Integer.valueOf(args[3]);
      Generator.of(args).generate(n);
    } else if ("run".equals(args[0])) {
      final Serializer serializer;
      if (args.length > 1 && "simple".equals(args[1])) {
        serializer = new ProtobufSerializer(SimplePbOps::from, SimplePbOps::toLocal);
      } else {
        serializer = new ProtobufSerializer(NestedPbOps::from, NestedPbOps::toLocal);
      }
      final Runner runner = Runner.getInstance(serializer, args);

      runner.run();
    }
  }
}
