package zoo.events;

/**
 * Created by dueerkopra on 08.04.2015.
 */
public abstract class Event {

  protected String animalId;

  protected Long sequenceId;

  public Event(String animalId, Long sequenceId) {
    this.animalId = animalId;
    this.sequenceId = sequenceId;
  }

  public String getAnimalId() {
    return animalId;
  }

  public Long getSequenceId() {
    return sequenceId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Event)) return false;

    Event event = (Event) o;

    if (!animalId.equals(event.animalId)) return false;
    if (!sequenceId.equals(event.sequenceId)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = animalId.hashCode();
    result = 31 * result + sequenceId.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "{" +
        "animalId='" + animalId + '\'' +
        ", sequenceId=" + sequenceId +
        '}';
  }
}
