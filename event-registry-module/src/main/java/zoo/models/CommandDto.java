package zoo.models;

import zoo.common.Command;
import zoo.common.AnimalId;

import java.util.Date;

/**
 * Created by dueerkopra on 30.03.2015.
 */
public class CommandDto {

  private Command command;
  private AnimalId animalId;
  private Date timestamp;

  public Command getCommand() {
    return command;
  }

  public void setCommand(Command command) {
    this.command = command;
  }

  public AnimalId getAnimalId() {
    return animalId;
  }

  public void setAnimalId(AnimalId animalId) {
    this.animalId = animalId;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CommandDto that = (CommandDto) o;

    if (animalId != that.animalId) return false;
    if (command != that.command) return false;
    if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = command != null ? command.hashCode() : 0;
    result = 31 * result + (animalId != null ? animalId.hashCode() : 0);
    result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
    return result;
  }
}
