package zoo.persistence;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dueerkopra on 30.03.2015.
 */
@Entity
@IdClass(EventLogEntry.PrimaryKey.class)
@Table(name="eventlog")
public class EventLogEntry implements Serializable {

  private static final long serialVersionUID = 1L;

  public static class PrimaryKey implements Serializable {

    private String id;
    private Long sequenceId;

    public PrimaryKey(String id, Long sequenceId) {
      this.id = id;
      this.sequenceId = sequenceId;
    }

    public PrimaryKey() {
    }
  }

  @Id
  @Column(name = "agg_id")
  private String id;

  @Id
  @Column(name = "seq_id")
  private Long sequenceId;

  @Column(name = "event")
  private String event;

  @Column(name = "occurence")
  @Temporal(TemporalType.TIMESTAMP)
  private Date occurence;

  protected EventLogEntry() {}

  public EventLogEntry(String id, String event, Long sequenceId, Date occurence) {
    this.id = id;
    this.sequenceId = sequenceId;
    this.event = event;
    this.occurence = occurence;
  }

  public String getId() {
    return id;
  }

  public Long getSequenceId() {
    return sequenceId;
  }

  public String getEvent() {
    return event;
  }

  public Date getOccurence() {
    return occurence;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EventLogEntry)) return false;

    EventLogEntry that = (EventLogEntry) o;

    if (!id.equals(that.id)) return false;
    if (!sequenceId.equals(that.sequenceId)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + sequenceId.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format(
        "EventLog[id='%s', sequenceId='%d', event=%s, occurence='%s']",
        id, sequenceId, event, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(occurence));
  }
}
