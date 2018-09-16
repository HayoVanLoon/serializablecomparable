package nl.hayovanloon.serializablecomparable;

import java.io.IOException;
import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Class for running the simulation(s)
 */
public class Runner {

  private static Runner instance = null;

  /** serialized item sizes */
  private final List<Integer> sizes = new LinkedList<>();
  /** serialized item creation timestamps */
  private final List<Long> timestamps = new LinkedList<>();

  /** start time */
  private long start = -1;
  /** start time */
  private long deserializationStart = -1;

  /** maximum simulation duration in seconds */
  private final int maxDuration;
  /** number of iterations over data set */
  private final int cycles;
  /** data generator/reader */
  private final Generator generator;

  /** data serializer/deserializer */
  protected final Serializer serializer;

  private Runner(int cycles, int maxDuration, Generator generator,
                 Serializer serializer) {
    this.cycles = cycles;
    this.maxDuration = maxDuration;
    this.generator = generator;
    this.serializer = serializer;
  }

  /**
   * Used by Protobuf simulation (which' serializer could not comply to the
   * interface)
   *
   * @param cycles       number of times to iterate over the data set
   * @param maxDuration  maximum phase duration in seconds
   * @param generator    data generator
   */
  protected Runner(int cycles, int maxDuration, Generator generator) {
    this(cycles, maxDuration, generator, null);
  }

  public static Runner getInstance(Serializer serializer, String... args) {
    final Generator generator = Generator.of(args);
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

  /**
   * Returns the class of the messages
   *
   * @return LocalMessage class
   */
  public Class<? extends LocalMessage> getType() {
    return generator.getType();
  }

  private long getSerializationPhaseLimit() {
    return start + maxDuration * 1000;
  }

  private long getDeserializationPhaseLimit() {
    return deserializationStart + maxDuration * 1000;
  }

  /**
   * Runs the simulation
   */
  public final void run() throws IOException, ClassNotFoundException {
    List<LocalMessage> messages = generator.retrieve();

    int count = 0;
    start = Instant.now().toEpochMilli();
    for (int i = 0; i < cycles; i += 1) {
      count += iterate(messages);
    }
    long end = Instant.now().toEpochMilli();

    Report.reportSerialization(getName(), sizes, timestamps, start,
        end, count, maxDuration);

    final List<byte[]> serialized = getSerialized(messages);

    int diffs = isEquivalent(messages, serialized);
    if (diffs > 0) {
      System.out.println("inequality detected " + diffs);
    }

    messages = null;
    System.gc();

    long deserializationCount = 0;
    deserializationStart = Instant.now().toEpochMilli();
    for (int i = 0; i < cycles; i += 1) {
      deserializationCount += iterateDeserializer(serialized);
    }
    long deserializationEnd = Instant.now().toEpochMilli();

    Report.reportDeserialization(maxDuration, deserializationStart,
        deserializationEnd, deserializationCount);
  }

  /**
   * Iterates over the data set, serializing the items and gathering
   * measurements on those. Returns the number of items serialized.
   *
   * @param messages messages to serialize
   * @return number of items serialized
   */
  protected long iterate(Iterable<LocalMessage> messages) throws IOException {
    long count = 0;

    final long endAt =  getSerializationPhaseLimit();
    for (LocalMessage x : messages) {
      if (Instant.now().toEpochMilli() >= endAt) {
        return count;
      }
      final byte[] serialized = serializer.serialize(x);
      sizes.add(serialized.length);
      timestamps.add(Instant.now().toEpochMilli());
      count += 1;
    }

    return count;
  }

  /**
   * Creates a serialized
   *
   * @param messages messages to serialize
   */
  private List<byte[]> getSerialized(List<LocalMessage> messages)
      throws IOException {

    final LinkedList<byte[]> result = new LinkedList<>();
    for (LocalMessage message : messages) {
      result.add(serializer.serialize(message));
    }
    return result;
  }

  private int isEquivalent(List<LocalMessage> messages,
                               List<byte[]> bytes) throws IOException {
    final Iterator<LocalMessage> iterMessage = messages.iterator();
    final Iterator<byte[]> iterBytes = bytes.iterator();

    int diffs = 0;
    while (iterMessage.hasNext() && iterBytes.hasNext()) {
      final LocalMessage deserialized =
          serializer.deserialize(iterBytes.next(), getType());
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
      serializer.deserialize(bytes, getType());
      count += 1;
    }

    return count;
  }
}
