package zoo.commands;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public abstract class Command {

  protected String animalId;

  protected Long sequenceId;

  public Command() {
  }

  public Command(String animalId, Long sequenceId) {
    this.animalId = animalId;
    this.sequenceId = sequenceId;
  }

  public String getAnimalId() {
    return animalId;
  }

  public Long getSequenceId() {
    return sequenceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Command)) return false;

    Command command = (Command) o;

    if (!animalId.equals(command.animalId)) return false;
    if (!sequenceId.equals(command.sequenceId)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = animalId.hashCode();
    result = 31 * result + sequenceId.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "{" +
        "animalId='" + animalId + '\'' +
        ", sequenceId=" + sequenceId +
        '}';
  }
}
