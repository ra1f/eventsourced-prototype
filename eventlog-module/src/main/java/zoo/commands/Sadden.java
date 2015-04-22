package zoo.commands;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Sadden extends Command {

  /**
   * Default constructor needed for Jackson
   */
  public Sadden() {
  }

  public Sadden(String animalId, Long sequenceId) {
    super(animalId, sequenceId);
  }

  @Override
  public String toString() {
    return "Sadden" + super.toString();
  }
}
