package zoo.commands;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class CleanUp extends Command {

  /**
   * Default constructor needed for Jackson
   */
  public CleanUp() {
  }

  public CleanUp(String animalId, Long sequenceId) {
    super(animalId, sequenceId);
  }

  @Override
  public String toString() {
    return "CleanUp" + super.toString();
  }
}
