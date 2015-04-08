package zoo.exceptions;

/**
 * Created by dueerkopra on 08.04.2015.
 */
public class AggregateLoadException extends Exception {

  public AggregateLoadException(String message) {
    super(message);
  }

  public AggregateLoadException() {
  }

  public AggregateLoadException(String message, Throwable cause) {
    super(message, cause);
  }

  public AggregateLoadException(Throwable cause) {
    super(cause);
  }
}
