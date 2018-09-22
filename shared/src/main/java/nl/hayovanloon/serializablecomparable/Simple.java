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
  public static LocalMessage createRandom() {
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < Math.random() * 96 + 32; i += 1) {
      int j = (int) (Math.random() * ('z' - 'a'));
      sb.append((char) ('a' + j));
    }
    return new Simple(
        Math.random() >.8 ? null : sb.toString(),
        Math.random() >.8 ? 0 : (long) (Math.random() * Long.MAX_VALUE),
        Math.random() >.8 ? 0 : (int) (Math.random() * Integer.MAX_VALUE),
        Math.random() >.8 ? 0 : Math.random() * Double.MAX_VALUE,
        Math.random() >.8 ? 0 :(float) (Math.random() * Float.MAX_VALUE),
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Simple)) return false;

    Simple simple = (Simple) o;

    if (longValue != simple.longValue) return false;
    if (intValue != simple.intValue) return false;
    if (Double.compare(simple.doubleValue, doubleValue) != 0) return false;
    if (Float.compare(simple.floatValue, floatValue) != 0) return false;
    if (boolValue != simple.boolValue) return false;
    return stringValue != null
        ? stringValue.equals(simple.stringValue) : simple.stringValue == null;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = stringValue != null ? stringValue.hashCode() : 0;
    result = 31 * result + (int) (longValue ^ (longValue >>> 32));
    result = 31 * result + intValue;
    temp = Double.doubleToLongBits(doubleValue);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result +
        (floatValue != +0.0f ? Float.floatToIntBits(floatValue) : 0);
    result = 31 * result + (boolValue ? 1 : 0);
    return result;
  }
}
