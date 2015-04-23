package zoo.events;

/**
 * Created by dueerkopra on 10.04.2015.
 */
public class Died extends Event {

  public Died(String animalId, Long sequenceId) {
    super(animalId, sequenceId);
  }
}
