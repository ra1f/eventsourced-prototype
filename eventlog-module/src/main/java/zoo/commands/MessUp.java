package zoo.commands;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class MessUp extends Command {

  public MessUp() {
  }

  public MessUp(String animalId, Long sequenceId) {
    super(animalId, sequenceId);
  }

  @Override
  public String toString() {
    return "MessUp" + super.toString();
  }
}
