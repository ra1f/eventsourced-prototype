package zoo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by dueerkopra on 08.04.2015.
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="No such animal")  // 400
public class NoSuchAnimalException extends ZooException {

  public NoSuchAnimalException() {
  }

  public NoSuchAnimalException(String message) {
    super(message);
  }
}
