package zoo.events;

/**
 * Created by dueerkopra on 08.04.2015.
 */
public class Digested extends Event {

  public Digested(String animalId, Long sequenceId) {
    super(animalId, sequenceId);
  }

  @Override
  public String toString() {
    return "Digested" + super.toString();
  }
}
