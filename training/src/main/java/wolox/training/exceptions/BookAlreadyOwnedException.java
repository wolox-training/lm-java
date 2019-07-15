package wolox.training.exceptions;

public class BookAlreadyOwnedException extends Exception {
    public BookAlreadyOwnedException(String errorMessage) {
        super(errorMessage);
    }
}
