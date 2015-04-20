package zoo.aggregates;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public abstract class Aggregate {

  protected final String id;

  protected final Long sequenceId;

  public Aggregate(String id, Long sequenceId) {
    this.id = id;
    this.sequenceId = sequenceId;
  }

  public Aggregate() {
    id = null;
    sequenceId = null;
  }

  public String getId() {
    return id;
  }

  public Long getSequenceId() {
    return sequenceId;
  }
}
