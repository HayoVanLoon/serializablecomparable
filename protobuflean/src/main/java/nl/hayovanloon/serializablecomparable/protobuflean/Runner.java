package nl.hayovanloon.serializablecomparable.protobuflean;

import com.google.protobuf.Message;
import nl.hayovanloon.serializablecomparable.Generator;
import nl.hayovanloon.serializablecomparable.LocalMessage;
import nl.hayovanloon.serializablecomparable.Nested;
import nl.hayovanloon.serializablecomparable.Report;
import nl.hayovanloon.serializablecomparable.Simple;
import nl.hayovanloon.serializablecomparable.protobuf.pb.NestedPb;
import nl.hayovanloon.serializablecomparable.protobuf.pb.SimplePb;

import java.io.IOException;
import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Class for running the simulation(s)
 */
public class Runner extends nl.hayovanloon.serializablecomparable.Runner{

  private static Runner instance = null;
  /** data serializer/deserializer */
  protected final ProtobufLeanSerializer serializer;


  private Runner(int cycles, int maxDuration, Generator generator,
                 ProtobufLeanSerializer serializer) {
    super(cycles, maxDuration, generator);
    this.serializer = serializer;
  }

  public static Runner getInstance(ProtobufLeanSerializer serializer, String... args) {
    final Generator generator = Generator.of(args);
    if (args.length < 4) {
      System.out.println("missing cycles parameter at position 4");
      System.exit(1);
    }
    int n = Integer.valueOf(args[3]);
    int maxDuration = args.length > 4 ? Integer.valueOf(args[4]) : 60;

    if (instance == null) {
      instance = new Runner(n, maxDuration, generator, serializer);
    }
    return instance;
  }

  /**
   * Human-readable serialization name
   *
   * @return the serialization name
   */
  private String getName() {
    return serializer.getName();
  }

  private List<Message> getData() throws IOException, ClassNotFoundException {
    final LinkedList<Message> messages = new LinkedList<>();
    for (LocalMessage localMessage : getGenerator().retrieve()) {
      if (getGenerator().getType() == Simple.class) {
        messages.add(SimplePbOps.from(localMessage));
      } else {
        messages.add(NestedPbOps.from(localMessage));
      }
    }
    return messages;
  }

  /**
   * Runs the simulation
   */
  public final void run() throws IOException, ClassNotFoundException {
    List<Message> messages = getData();

    int count = 0;
    start = Instant.now().toEpochMilli();
    for (int i = 0; i < getCycles(); i += 1) {
      count += iteratePb(messages);
    }
    long end = Instant.now().toEpochMilli();

    Report.reportSerialization(getName(), sizes, timestamps, start,
        end, count, getMaxDuration());

    final List<byte[]> serialized = getSerialized(messages);

    int diffs = isEquivalent(messages, serialized);
    if (diffs > 0) {
      System.out.println("inequality detected " + diffs);
    }

    messages = null;
    System.gc();

    long deserializationCount = 0;
    deserializationStart = Instant.now().toEpochMilli();
    for (int i = 0; i < getCycles(); i += 1) {
      deserializationCount += iterateDeserializer(serialized);
    }
    long deserializationEnd = Instant.now().toEpochMilli();

    Report.reportDeserialization(getMaxDuration(), deserializationStart,
        deserializationEnd, deserializationCount);
  }

  /**
   * Iterates over the data set, serializing the items and gathering
   * measurements on those. Returns the number of items serialized.
   *
   * @param messages messages to serialize
   * @return number of items serialized
   */
  protected long iteratePb(Iterable<Message> messages) {
    long count = 0;

    final long endAt = getSerializationPhaseLimit();
    for (Message message : messages) {
      if (Instant.now().toEpochMilli() >= endAt) {
        return count;
      }
      final byte[] serialized = serializer.serialize(message);
      sizes.add(serialized.length);
      timestamps.add(Instant.now().toEpochMilli());
      count += 1;
    }

    return count;
  }

  /**
   * Creates a serialized data set
   *
   * @param messages messages to serialize
   */
  private List<byte[]> getSerialized(List<Message> messages) {

    final LinkedList<byte[]> result = new LinkedList<>();
    for (Message message : messages) {
      result.add(serializer.serialize(message));
    }
    return result;
  }

  private Class<? extends Message> getMessageType() {
    return getGenerator().getType() == Simple.class
        ? SimplePb.class : NestedPb.class;
  }

  private int isEquivalent(List<Message> messages,
                           List<byte[]> bytes) throws IOException {
    final Iterator<Message> iterMessage = messages.iterator();
    final Iterator<byte[]> iterBytes = bytes.iterator();

    int diffs = 0;
    while (iterMessage.hasNext() && iterBytes.hasNext()) {
      final Message deserialized =
          serializer.deserialize(iterBytes.next(), getMessageType());
      if (!deserialized.equals(iterMessage.next())) {
        diffs += 1;
      }
    }

    return diffs;
  }

  /**
   * Iterates over the serialized data set, deserializing the items. Returns the
   * number of items deserialized.
   *
   * @param serialized byte arrays to deserialize
   * @return number of items deserialized
   */
  private long iterateDeserializer(List<byte[]> serialized)
      throws IOException {

    long count = 0;

    final long endAt = getDeserializationPhaseLimit();
    for (byte[] bytes : serialized) {
      if (Instant.now().toEpochMilli() >= endAt) {
        return count;
      }
      serializer.deserialize(bytes, getMessageType());
      count += 1;
    }

    return count;
  }
}
