package zoo.exceptions;

/**
 * Created by dueerkopra on 08.04.2015.
 */
public class NoSuchAnimalException extends ZooException {

  public NoSuchAnimalException() {
  }

  public NoSuchAnimalException(String message) {
    super(message);
  }
}
