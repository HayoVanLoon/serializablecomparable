package nl.hayovanloon.serializablecomparable;

import java.math.BigDecimal;
import java.util.Iterator;


/**
 * Simple static report printer.
 */
public final class Report {

  /** Number of number columns per table row */
  private static final int PER_LINE = 5;

  private Report() {
    throw new AssertionError();
  }

  /**
   * Prints a simple report to the standard output.
   * @param name            name of the serializer
   * @param sizes           sizes of the items
   * @param timestamps      item creation timestamps
   * @param startTimestamp  start time
   * @param endTimestamp    end time
   * @param count           number of items created
   * @param maxDuration     maximum duration
   */
  public static void report(String name,
                            Iterable<Integer> sizes,
                            Iterable<Long> timestamps,
                            long startTimestamp,
                            long endTimestamp,
                            long count,
                            int maxDuration) {
    final Iterator<Integer> sizeIter = sizes.iterator();
    final Iterator<Long> tsIter = timestamps.iterator();

    final int duration = (int)(endTimestamp - startTimestamp) / 1000;
    if (duration == 0) {
      throw new IllegalStateException("nothing to report");
    }

    // count items serialized per seconds
    int[] hits = new int[duration + 1];
    long acc = 0;
    for (int i = 0; i < count; i += 1) {
      final int size = sizeIter.next();
      final long timestamp = tsIter.next();
      hits[(int)Math.floorDiv(timestamp - startTimestamp, 1000)] += 1;
      acc += size;
    }

    // create a somewhat decent table
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < hits.length;  i += 1) {
      if (i % PER_LINE == 0) {
        if (i == 0) {
          sb.append("  0 to ");
        } else {
          sb.append("\n").append(fill(Integer.toString(i), 3)).append(" to ");
        }
        sb.append(fill(Integer.toString(i + PER_LINE - 1), 3)).append("s: ");
      } else {
        sb.append(",\t");
      }
      sb.append(hits[i]);
    }

    final String message = String.format(
        "\n======== " + name + " ========" +
            "\n" +
            "\nSerialised: %sk" +
            "\nAverage size: %s" +
            "\n" +
            "\nTotal time: %ss (max %ss)" +
            "\nAverage time/item: %s microseconds" +
            "\n" +
            "\n%s\n",
        count / 1000, BigDecimal.valueOf(acc / (double)count).intValue(),
        duration, maxDuration,
        BigDecimal.valueOf(duration / (double)count * 1000000)
            .setScale(2, BigDecimal.ROUND_HALF_DOWN).toPlainString(),
        sb.toString()
    );

    System.out.println(message);
  }

  private static String fill(String s, int length) {
    return length - s.length() <= 0 ? s : fill(' ' + s, length);
  }
}
