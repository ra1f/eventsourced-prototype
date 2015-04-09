package zoo.exceptions;

/**
 * Created by dueerkopra on 08.04.2015.
 */
public class AnimalNotThereException extends ZooException {

  public AnimalNotThereException() {
  }

  public AnimalNotThereException(String message) {
    super(message);
  }
}
