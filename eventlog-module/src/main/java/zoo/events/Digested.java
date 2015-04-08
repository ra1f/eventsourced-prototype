package zoo.events;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dueerkopra on 08.04.2015.
 */
public class Digested extends Event {

  public Digested(String animalId, Date timestamp) {
    super(animalId, timestamp);
  }

  @Override
  public String toString() {
    return "Digested{" +
        "animalId='" + animalId + '\'' +
        ", timestamp=" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(timestamp) +
        '}';
  }
}
