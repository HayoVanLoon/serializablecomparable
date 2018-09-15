package nl.hayovanloon.serializablecomparable;

import java.io.Serializable;


/**
 * A simple, flat POJO
 */
public class Simple implements Serializable, LocalMessage {
  public static final long serialVersionUID = -1L;

  private String stringValue;
  private long longValue;
  private int intValue;
  private double doubleValue;
  private float floatValue;
  private boolean boolValue;

  public Simple(String stringValue, long longValue, int intValue, double doubleValue, float floatValue, boolean boolValue) {
    this.stringValue = stringValue;
    this.longValue = longValue;
    this.intValue = intValue;
    this.doubleValue = doubleValue;
    this.floatValue = floatValue;
    this.boolValue = boolValue;
  }

  public Simple() {
  }

  /**
   * Generates a randomly (yet correctly) filled instance.
   *
   * @return a new Simple object
   */
  public static Simple createRandom() {
    final StringBuilder sb = new StringBuilder();
    for (int i =0; i < 128; i += 1) {
      int j = (int)(Math.random() * ('z' - 'a'));
      sb.append((char)('a' + j));
    }
    return new Simple(
        sb.toString(),
        (long)(Math.random() * Long.MAX_VALUE),
        (int)(Math.random() * Integer.MAX_VALUE),
        Math.random() * Double.MAX_VALUE,
        (float)(Math.random() * Float.MAX_VALUE),
        Math.random() > .5
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

  @Override
  public String toString() {
    return "Simple{" +
        "stringValue='" + stringValue + '\'' +
        ", longValue=" + longValue +
        ", intValue=" + intValue +
        ", doubleValue=" + doubleValue +
        ", floatValue=" + floatValue +
        ", boolValue=" + boolValue +
        '}';
  }
}
