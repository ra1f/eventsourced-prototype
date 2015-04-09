package zoo.commands;

import java.util.Date;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Sell extends Command {

  public Sell() {
  }

  public Sell(String animalId, Date timestamp) {
    super(animalId, timestamp);
  }

  @Override
  public String toString() {
    return "Sell" + super.toString();
  }
}
