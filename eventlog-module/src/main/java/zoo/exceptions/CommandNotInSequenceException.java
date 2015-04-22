package zoo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by dueerkopra on 20.04.2015.
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Command not in sequence")  // 400
public class CommandNotInSequenceException extends ZooException {

  public CommandNotInSequenceException() {
  }

  public CommandNotInSequenceException(String message) {
    super(message);
  }
}
