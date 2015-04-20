package zoo.events;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Bought extends Event {

  public Bought(String animalId, Long sequenceId) {
    super(animalId, sequenceId);
  }

  @Override
  public String toString() {
    return "Bought" + super.toString();
  }
}
