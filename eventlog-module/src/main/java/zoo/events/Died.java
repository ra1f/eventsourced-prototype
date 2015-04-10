package zoo.events;

import java.util.Date;

/**
 * Created by dueerkopra on 10.04.2015.
 */
public class Died extends Event {

  public Died(String animalId, Date timestamp) {
    super(animalId, timestamp);
  }

  @Override
  public String toString() {
    return "Died" + super.toString();
  }
}
