package zoo.events;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Sold extends Event {

  public Sold(String animalId, Long sequenceId) {
    super(animalId, sequenceId);
  }

  @Override
  public String toString() {
    return "Sold" + super.toString();
  }
}
