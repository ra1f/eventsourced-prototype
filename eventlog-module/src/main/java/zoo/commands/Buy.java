package zoo.commands;

import java.util.Date;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Buy extends Command {

  public Buy() {
  }

  public Buy(String animalId, Date timestamp) {
    super(animalId, timestamp);
  }

  @Override
  public String toString() {
    return "Buy" + super.toString();
  }

}
