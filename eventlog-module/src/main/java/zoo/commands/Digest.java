package zoo.commands;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Digest extends Command {

  public Digest() {
  }

  public Digest(String animalId, Long sequenceId) {
    super(animalId, sequenceId);
  }

  @Override
  public String toString() {
    return "Digest" + super.toString();
  }
}
