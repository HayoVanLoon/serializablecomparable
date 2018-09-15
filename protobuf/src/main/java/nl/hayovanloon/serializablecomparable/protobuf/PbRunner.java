package nl.hayovanloon.serializablecomparable.protobuf;

import com.google.protobuf.Message;
import nl.hayovanloon.serializablecomparable.Generator;
import nl.hayovanloon.serializablecomparable.LocalMessage;
import nl.hayovanloon.serializablecomparable.Nested;
import nl.hayovanloon.serializablecomparable.Runner;
import nl.hayovanloon.serializablecomparable.Simple;

import java.time.Instant;
import java.util.Iterator;


public class PbRunner extends Runner {

  private static final ProtobufSerializer SERIALIZER = new ProtobufSerializer();

  private static PbRunner instance = null;

  public PbRunner(int cycles, int maxDuration, Generator generator) {
    super(cycles, maxDuration, generator);
  }

  public static Runner getInstance(String... args) {
    final Generator generator = Generator.of(args);
    int n = Integer.valueOf(args[3]);
    int maxDuration = args.length > 4 ? Integer.valueOf(args[4]) : 60;

    if (instance == null) {
      instance = new PbRunner(n, maxDuration, generator);
    }
    return instance;
  }

  public String getName() {
    return "Protobuf";
  }

  /**
   * Overrides base method as each POJO has to be converted to a protobuf
   * Message object.
   *
   * @param messages messages tol serialize
   */
  @Override
  protected void iterate(Iterable<LocalMessage> messages) {
    final Iterator<LocalMessage> iter = messages.iterator();
    while (iter.hasNext() &&
        Instant.now().toEpochMilli() - startTimestamp < maxDuration * 1000) {
      final LocalMessage message = iter.next();

      final byte[] serialized;
      if (generator.getType() == Simple.class) {
        final Message m = SimplePbOps.from((Simple) message);
        serialized = SERIALIZER.serialize(m);
      } else {
        final Message m = NestedPbOps.from((Nested) message);
        serialized = SERIALIZER.serialize(m);
      }

      sizes.add(serialized.length);
      timestamps.add(Instant.now().toEpochMilli());
      count += 1;
    }
  }

}
