package zoo.aggregates;

import java.util.Date;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public abstract class Aggregate {

  protected String id;

  protected Date timestamp;

  public Aggregate(String id, Date timestamp) {
    this.id = id;
    this.timestamp = timestamp;
  }

  public Aggregate() {
  }

  public String getId() {
    return id;
  }

  public Date getTimestamp() {
    return timestamp;
  }
}
