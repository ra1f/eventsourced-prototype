package zoo.events;

import java.util.Date;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Sold extends Event {

  public Sold(String animalId, Date timestamp) {
    super(animalId, timestamp);
  }

  @Override
  public String toString() {
    return "Sold" + super.toString();
  }
}
