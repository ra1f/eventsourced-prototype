package zoo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by dueerkopra on 14.04.2015.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such animal")  // 404
public class AnimalNotFoundException extends RuntimeException {

}
