package zoo.commands;

import java.util.Date;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Feed extends Command {

  public Feed() {
  }

  public Feed(String animalId, Date timestamp) {
    super(animalId, timestamp);
  }

  @Override
  public String toString() {
    return "Feed" + super.toString();
  }
}
