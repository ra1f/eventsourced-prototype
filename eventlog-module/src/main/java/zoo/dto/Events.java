package zoo.dto;

import zoo.events.Event;

import java.util.Collection;

/**
 * Created by dueerkopra on 20.04.2015.
 */
public class Events<E extends Event> {

  private String id;
  private Collection<E> events;
  private Long sequenceId;

  public Events(String id, Collection<E> events, Long sequenceId) {
    this.id = id;
    this.events = events;
    this.sequenceId = sequenceId;
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
