package zoo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by dueerkopra on 22.04.2015.
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Request not idempotent")  // 400
public class NotIdempotentException extends Exception {

  public NotIdempotentException() {
  }

  public NotIdempotentException(String message) {
    super(message);
  }

}
