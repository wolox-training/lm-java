package wolox.training.exceptions;

public class BookNotOwnedByUserException extends Exception {
    public BookNotOwnedByUserException(String errorMessage) {
        super(errorMessage);
    }
}
