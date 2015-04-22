package zoo.dto;

import zoo.events.Event;

import java.util.Collection;

/**
 * Created by dueerkopra on 20.04.2015.
 */
public class Events<E extends Event> {

  private String id;
  private Long sequenceId;
  private Collection<E> events;

  public Events(String id, Long sequenceId, Collection<E> events) {
    this.id = id;
    this.sequenceId = sequenceId;
    this.events = events;
  }

  public String getId() {
    return id;
  }

  public Collection<E> getEvents() {
    return events;
  }

  public Long getSequenceId() {
    return sequenceId;
  }
}
