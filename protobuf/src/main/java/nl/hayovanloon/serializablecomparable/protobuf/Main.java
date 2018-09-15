package nl.hayovanloon.serializablecomparable.protobuf;

import nl.hayovanloon.serializablecomparable.Generator;
import nl.hayovanloon.serializablecomparable.Runner;

import java.io.IOException;


public class Main {

  public static void main(String[] args)
      throws IOException, ClassNotFoundException {

    if ("generate".equals(args[0])) {
      final int n = Integer.valueOf(args[3]);
      Generator.of(args).generate(n);
    } else if ("run".equals(args[0])) {
      final Runner runner = PbRunner.getInstance(args);

      runner.run();
    }
  }
}
