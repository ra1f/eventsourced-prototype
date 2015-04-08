package zoo.events;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Sold extends Event implements ExistenceEvent {

  public Sold(String animalId) {
    super(animalId);
  }
}
