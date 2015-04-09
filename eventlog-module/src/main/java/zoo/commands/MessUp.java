package zoo.commands;

import java.util.Date;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class MessUp extends Command {

  public MessUp() {
  }

  public MessUp(String animalId, Date timestamp) {
    super(animalId, timestamp);
  }

  @Override
  public String toString() {
    return "MessUp" + super.toString();
  }
}
