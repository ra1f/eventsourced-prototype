package zoo.persistence;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dueerkopra on 30.03.2015.
 */
@Entity
@Table(name="eventlog")
public class EventLog {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;

  @Column(name = "event")
  private String event;

  @Column(name = "animal_id")
  private String animalId;

  @Column(name = "occurence")
  @Temporal(TemporalType.TIMESTAMP)
  private Date occurence;

  protected EventLog() {}

  public EventLog(String event, String animalId, Date occurence) {
    this.event = event;
    this.animalId = animalId;
    this.occurence = occurence;
  }

  public Long getId() {
    return id;
  }

  public String getEvent() {
    return event;
  }

  public String getAnimalId() {
    return animalId;
  }

  public Date getOccurence() {
    return occurence;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EventLog)) return false;

    EventLog eventLog = (EventLog) o;

    if (animalId != eventLog.animalId) return false;
    if (event != eventLog.event) return false;
    if (occurence != null ? !occurence.equals(eventLog.occurence) : eventLog.occurence != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = event != null ? event.hashCode() : 0;
    result = 31 * result + (animalId != null ? animalId.hashCode() : 0);
    result = 31 * result + (occurence != null ? occurence.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return String.format(
        "EventLog[id='%d', event=%s, animalId='%s', occurence='%s']",
        id, event, animalId, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(occurence));
  }
}
