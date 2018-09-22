package nl.hayovanloon.serializablecomparable;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


/**
 * This class generates a data file, which forms the input file for all
 * projects.
 * <p>
 * It is also used to deserialize that data again.
 */
public class Generator {

  /** Destination and/or source file */
  private final File file;

  /** Message type ({@link Simple} or {@link Nested}) */
  private final Class<? extends LocalMessage> type;

  public Generator(File file, Class<? extends LocalMessage> type) {
    this.file = file;
    this.type = type;
  }

  public static Generator of(String[] args) {
    return new Generator(
        new File(args.length > 2 ? args[2] : "data.out"),
        "simple".equalsIgnoreCase(args[2]) ? Simple.class : Nested.class);
  }

  /**
   * Factory method for a generator.
   *
   * @param type class to use
   * @param fileName  file name to store data set in or retrieve from
   *
   * @return a new Generator
   */
  public static <U extends LocalMessage> Generator of(Class<U> type,
                                                      String fileName) {
    final File file = new File(fileName == null ? "data.out" :fileName);
    return new Generator(file, type);
  }

  public Class<? extends LocalMessage> getType() {
    return type;
  }

  /**
   * Generates a data file
   *
   * @param n number of items to create and serialize
   */
  public void generate(int n) throws IOException {
    final Supplier<LocalMessage> supplier =
        type == Simple.class ? Simple::createRandom : Nested::createRandom;
    try (
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutput oos = new ObjectOutputStream(fos)
    ) {
      for (int i = 0; i < n; i += 1) {
        oos.writeObject(supplier.get());
      }
    }
  }

  /**
   * Deserializes the items created by {@link #generate}.
   *
   * @return a list of messages
   */
  public List<LocalMessage> retrieve()
      throws IOException, ClassNotFoundException {
    try (
        FileInputStream fos = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fos)
    ) {
      final List<LocalMessage> result = new ArrayList<>();
      try {
        Object o = ois.readObject();
        while (o != null) {
          if (type.isInstance(o)) {
            result.add(type.cast(o));
          } else {
            throw new IOException("invalid format");
          }
          o = ois.readObject();
        }
      } catch (EOFException e) {
        //
      }
      return result;
    }
  }
}
