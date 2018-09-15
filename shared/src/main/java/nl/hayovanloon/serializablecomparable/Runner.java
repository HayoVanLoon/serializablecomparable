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
  protected final List<Integer> sizes = new LinkedList<>();

  /** serialized item creation timestamps */
  protected final List<Long> timestamps = new LinkedList<>();

  /** number of iterations over data set */
  protected final int cycles;

  /** maximum simulation duration in seconds*/
  protected final int maxDuration;

  /** data generator/reader */
  protected final Generator generator;

  /** data serializer */
  private final Serializer serializer;

  /** counter for items created */
  protected long count = 0;

  /** start time */
  protected long startTimestamp = -1;

  /** end time */
  protected long endTimestamp = -1;

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
   * @param cycles
   * @param maxDuration
   * @param generator
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
   * @return  the serialization name
   */
  protected String getName() {
    return serializer.getName();
  }

  /**
   * Runs the simulation
   *
   */
  public void run() throws IOException, ClassNotFoundException {
    final List<LocalMessage> messages = generator.retrieve();

    startTimestamp = Instant.now().toEpochMilli();
    for (int i = 0; i < cycles; i += 1) {
      iterate(messages);
    }
    endTimestamp = Instant.now().toEpochMilli();

    Report.report(getName(), sizes, timestamps, startTimestamp,
        endTimestamp, count, maxDuration);
  }

  /**
   * Iterates over the data set, serializing the items and gathering
   * measurements on those
   *
   * @param messages messages to serialize
   */
  protected void iterate(Iterable<LocalMessage> messages) throws IOException {
    final Iterator<LocalMessage> iter = messages.iterator();
    while (iter.hasNext() &&
        Instant.now().toEpochMilli() - startTimestamp < maxDuration * 1000) {
      final LocalMessage x = iter.next();
      final byte[] serialized = serializer.serialize(x);
      sizes.add(serialized.length);
      timestamps.add(Instant.now().toEpochMilli());
      count += 1;
    }
  }
}
