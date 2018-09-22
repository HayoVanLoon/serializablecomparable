package nl.hayovanloon.serializablecomparable.protobuflean;

import com.google.protobuf.Message;
import nl.hayovanloon.serializablecomparable.Generator;
import nl.hayovanloon.serializablecomparable.Runner;
import nl.hayovanloon.serializablecomparable.Serializer;
import nl.hayovanloon.serializablecomparable.protobuflean.pb.NestedPb;
import nl.hayovanloon.serializablecomparable.protobuflean.pb.SimplePb;

import java.io.IOException;


public class Main {

  public static void main(String[] args)
      throws IOException, ClassNotFoundException {

    if ("generate".equals(args[0])) {
      final int n = Integer.valueOf(args[3]);
      Generator.of(args).generate(n);
    } else if ("run".equals(args[0])) {
      final Serializer<Message> serializer;
      if (args.length > 1 && "simple".equals(args[1])) {
        serializer = new ProtobufLeanSerializer<>(SimplePb.class);
      } else {
        serializer = new ProtobufLeanSerializer<>(NestedPb.class);
      }
      final Runner runner = Runner.getInstance(serializer, args);

      runner.run();
    }
  }
}
