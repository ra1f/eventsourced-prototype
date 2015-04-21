package zoo.commands;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Buy extends Command {

  public Buy() {
  }

  public Buy(String animalId, Long sequenceId) {
    super(animalId, sequenceId);
  }

  public Buy(String animalId) {
    super(animalId, 0L);
  }

  @Override
  public String toString() {
    return "Buy" + super.toString();
  }

}
