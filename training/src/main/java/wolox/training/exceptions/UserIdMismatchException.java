package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserIdMismatchException extends ResponseStatusException {
    public UserIdMismatchException() {
        super(HttpStatus.BAD_REQUEST, "The given id does not match user id");
    }
}
