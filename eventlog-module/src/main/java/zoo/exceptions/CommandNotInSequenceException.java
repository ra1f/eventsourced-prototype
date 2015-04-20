package zoo.exceptions;

/**
 * Created by dueerkopra on 20.04.2015.
 */
public class CommandNotInSequenceException extends ZooException {

  public CommandNotInSequenceException() {
  }

  public CommandNotInSequenceException(String message) {
    super(message);
  }
}
