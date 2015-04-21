package zoo.events;

/**
 * Created by dueerkopra on 21.04.2015.
 */
public class Played extends Event {

  public Played(String animalId, Long sequenceId) {
    super(animalId, sequenceId);
  }

  @Override
  public String toString() {
    return "Played" + super.toString();
  }
}
