package zoo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by dueerkopra on 08.04.2015.
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Animal already exists")  // 400
public class AnimalAlreadyThereException extends ZooException {

  public AnimalAlreadyThereException() {
  }

  public AnimalAlreadyThereException(String message) {
    super(message);
  }
}
