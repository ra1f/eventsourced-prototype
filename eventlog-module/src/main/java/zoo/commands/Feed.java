package zoo.commands;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Feed extends Command {

  public Feed() {
  }

  public Feed(String animalId, Long sequenceId) {
    super(animalId, sequenceId);
  }

  @Override
  public String toString() {
    return "Feed" + super.toString();
  }
}
