package zoo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by dueerkopra on 08.04.2015.
 */
@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Error loading aggregate")  // 500
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
