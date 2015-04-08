package zoo.commands;

import java.util.Date;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public abstract class Command {

  protected String animalId;

  protected Date timestamp;

  public Command() {
  }

  public Command(String animalId, Date timestamp) {
    this.animalId = animalId;
    this.timestamp = timestamp;
  }

  public String getAnimalId() {
    return animalId;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Buy)) return false;

    Command command = (Command) o;

    if (animalId != null ? !animalId.equals(command.animalId) : command.animalId != null) return false;
    if (timestamp != null ? !timestamp.equals(command.timestamp) : command.timestamp != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = animalId != null ? animalId.hashCode() : 0;
    result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
    return result;
  }
}
