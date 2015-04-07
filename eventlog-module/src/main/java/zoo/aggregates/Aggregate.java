package zoo.aggregates;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public abstract class Aggregate {

  protected String id;

  public Aggregate(String id) {
    this.id = id;
  }

  public Aggregate() {
  }

  public String getId() {
    return id;
  }
}
