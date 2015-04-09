package zoo.exceptions;

/**
 * Created by dueerkopra on 08.04.2015.
 */
public class AnimalAlreadyThereException extends ZooException {

  public AnimalAlreadyThereException() {
  }

  public AnimalAlreadyThereException(String message) {
    super(message);
  }
}
