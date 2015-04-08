package zoo.events;

import java.text.SimpleDateFormat;
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
    return "Bought{" +
        "animalId='" + animalId + '\'' +
        ", timestamp=" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(timestamp) +
        '}';
  }
}
