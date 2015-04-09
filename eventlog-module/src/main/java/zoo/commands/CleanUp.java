package zoo.commands;

import java.util.Date;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class CleanUp extends Command {

  public CleanUp() {
  }

  public CleanUp(String animalId, Date timestamp) {
    super(animalId, timestamp);
  }

  @Override
  public String toString() {
    return "CleanUp" + super.toString();
  }
}
