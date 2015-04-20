package zoo.commands;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Sell extends Command {

  public Sell() {
  }

  public Sell(String animalId, Long sequenceId) {
    super(animalId, sequenceId);
  }

  @Override
  public String toString() {
    return "Sell" + super.toString();
  }
}
