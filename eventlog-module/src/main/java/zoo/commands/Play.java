package zoo.commands;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Play extends Command {

  /**
   * Default constructor needed for Jackson
   */
  public Play() {
  }

  public Play(String animalId, Long sequenceId) {
    super(animalId, sequenceId);
  }

  @Override
  public String toString() {
    return "Play" + super.toString();
  }
}
