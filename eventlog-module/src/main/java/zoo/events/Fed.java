package zoo.events;

import java.util.Date;

/**
 * Created by dueerkopra on 08.04.2015.
 */
public class Fed extends Event {

  public Fed(String animalId, Date timestamp) {
    super(animalId, timestamp);
  }

  @Override
  public String toString() {
    return "Fed" + super.toString();
  }
}
