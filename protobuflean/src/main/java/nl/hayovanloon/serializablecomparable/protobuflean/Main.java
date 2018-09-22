package nl.hayovanloon.serializablecomparable.protobuflean;

import nl.hayovanloon.serializablecomparable.Generator;

import java.io.IOException;


public class Main {

  public static void main(String[] args)
      throws IOException, ClassNotFoundException {

    if ("generate".equals(args[0])) {
      final int n = Integer.valueOf(args[3]);
      Generator.of(args).generate(n);
    } else if ("run".equals(args[0])) {
      final ProtobufLeanSerializer serializer;
      if (args.length > 1 && "simple".equals(args[1])) {
        serializer = new ProtobufLeanSerializer();
      } else {
        serializer = new ProtobufLeanSerializer();
      }
      final Runner runner = Runner.getInstance(serializer, args);

      runner.run();
    }
  }
}
