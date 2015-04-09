package zoo.events;

import java.util.Date;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Bought extends Event {

  public Bought(String animalId, Date timestamp) {
    super(animalId, timestamp);
  }

  @Override
  public String toString() {
    return "Bought" + super.toString();
  }
}
