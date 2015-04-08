package zoo.exceptions;

/**
 * Created by dueerkopra on 08.04.2015.
 */
public class AnimalAlreadyBoughtException extends ZooException {

  public AnimalAlreadyBoughtException() {
  }

  public AnimalAlreadyBoughtException(String message) {
    super(message);
  }
}
