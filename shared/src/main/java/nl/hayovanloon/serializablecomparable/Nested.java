package nl.hayovanloon.serializablecomparable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple POJO with some nested objects
 */
public class Nested implements Serializable, LocalMessage {
  public static final long serialVersionUID = -1L;

  private String stringValue;
  private long longValue;
  private int intValue;
  private double doubleValue;
  private float floatValue;
  private boolean boolValue;
  private List<Long> longList;
  private Simple simple;
  private List<Simple> simples;

  public Nested(String stringValue, long longValue, int intValue,
                double doubleValue, float floatValue, boolean boolValue,
                List<Long> longList, Simple simple, List<Simple> simples) {
    this.stringValue = stringValue;
    this.longValue = longValue;
    this.intValue = intValue;
    this.doubleValue = doubleValue;
    this.floatValue = floatValue;
    this.boolValue = boolValue;
    this.longList = longList;
    this.simple = simple;
    this.simples = simples;
  }

  public Nested() {
  }

  /**
   * Generates a randomly (yet correctly) filled instance.
   *
   * @return a new Nested object
   */
  public static Nested createRandom() {
    final Simple filler = Simple.createRandom();
    final Simple simple = Simple.createRandom();
    final List<Long> longList = new ArrayList<>();
    for (int i = 0; i < 16; i += 1) {
      longList.add((long) (Math.random() * Long.MAX_VALUE));
    }
    final List<Simple> simpleList = new ArrayList<>();
    for (int i = 0; i < 16; i += 1) {
      simpleList.add(Simple.createRandom());
    }
    return new Nested(
        filler.getStringValue(),
        filler.getLongValue(),
        filler.getIntValue(),
        filler.getDoubleValue(),
        filler.getFloatValue(),
        filler.isBoolValue(),
        longList,
        simple,
        simpleList
    );
  }

  public String getStringValue() {
    return stringValue;
  }

  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  public long getLongValue() {
    return longValue;
  }

  public void setLongValue(long longValue) {
    this.longValue = longValue;
  }

  public int getIntValue() {
    return intValue;
  }

  public void setIntValue(int intValue) {
    this.intValue = intValue;
  }

  public double getDoubleValue() {
    return doubleValue;
  }

  public void setDoubleValue(double doubleValue) {
    this.doubleValue = doubleValue;
  }

  public float getFloatValue() {
    return floatValue;
  }

  public void setFloatValue(float floatValue) {
    this.floatValue = floatValue;
  }

  public boolean isBoolValue() {
    return boolValue;
  }

  public void setBoolValue(boolean boolValue) {
    this.boolValue = boolValue;
  }

  public List<Long> getLongList() {
    return longList;
  }

  public void setLongList(List<Long> longList) {
    this.longList = longList;
  }

  public Simple getSimple() {
    return simple;
  }

  public void setSimple(Simple simple) {
    this.simple = simple;
  }

  public List<Simple> getSimples() {
    return simples;
  }

  public void setSimples(List<Simple> simples) {
    this.simples = simples;
  }

  @Override
  public String toString() {
    return "Nested{" +
        "stringValue='" + stringValue + '\'' +
        ", longValue=" + longValue +
        ", intValue=" + intValue +
        ", doubleValue=" + doubleValue +
        ", floatValue=" + floatValue +
        ", boolValue=" + boolValue +
        ", longList=" + longList +
        ", simple=" + simple +
        ", simples=" + simples +
        '}';
  }
}
