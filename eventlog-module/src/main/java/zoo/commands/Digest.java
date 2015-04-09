package zoo.commands;

import java.util.Date;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Digest extends Command {

  public Digest() {
  }

  public Digest(String animalId, Date timestamp) {
    super(animalId, timestamp);
  }

  @Override
  public String toString() {
    return "Digest" + super.toString();
  }
}
