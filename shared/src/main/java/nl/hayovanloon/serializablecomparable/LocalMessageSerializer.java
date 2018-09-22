package nl.hayovanloon.serializablecomparable;

import java.util.List;


public abstract class LocalMessageSerializer
    implements Serializer<LocalMessage> {

  @Override
  public List<LocalMessage> prepareInput(List<LocalMessage> retrieved) {
    return retrieved;
  }
}
