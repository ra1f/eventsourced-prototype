package zoo.commands;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public abstract class Command {

  protected String animalId;

  public String getAnimalId() {
    return animalId;
  }

  public void setAnimalId(String animalId) {
    this.animalId = animalId;
  }
}
