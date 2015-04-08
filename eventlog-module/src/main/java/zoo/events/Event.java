package zoo.events;

import java.util.Date;

/**
 * Created by dueerkopra on 08.04.2015.
 */
public abstract class Event {

  protected String animalId;

  protected Date timestamp;

  public Event(String animalId, Date timestamp) {
    this.animalId = animalId;
    this.timestamp = timestamp;
  }

  public String getAnimalId() {
    return animalId;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Bought)) return false;

    Event event = (Event) o;

    if (animalId != null ? !animalId.equals(event.animalId) : event.animalId != null) return false;
    if (timestamp != null ? !timestamp.equals(event.timestamp) : event.timestamp != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = animalId != null ? animalId.hashCode() : 0;
    result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
    return result;
  }
}
