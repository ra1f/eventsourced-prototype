package zoo.exceptions;

/**
 * Created by dueerkopra on 08.04.2015.
 */
public class AnimalNotExistingException extends ZooException {

  public AnimalNotExistingException() {
  }

  public AnimalNotExistingException(String message) {
    super(message);
  }
}
