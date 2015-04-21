package zoo.events;

/**
 * Created by dueerkopra on 21.04.2015.
 */
public class Saddened extends Event {

  public Saddened(String animalId, Long sequenceId) {
    super(animalId, sequenceId);
  }

  @Override
  public String toString() {
    return "Saddened" + super.toString();
  }
}
