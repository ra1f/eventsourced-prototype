package zoo.events;

/**
 * Created by dueerkopra on 08.04.2015.
 */
public class Fed extends Event {

  public Fed(String animalId, Long sequenceId) {
    super(animalId, sequenceId);
  }

  @Override
  public String toString() {
    return "Fed" + super.toString();
  }
}
