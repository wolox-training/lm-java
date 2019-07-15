package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BookIdMismatchException extends ResponseStatusException {
    public BookIdMismatchException() {
        super(HttpStatus.BAD_REQUEST, "The given id does not match book id");
    }
}
