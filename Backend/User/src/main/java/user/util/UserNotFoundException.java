package user.util;

public class UserNotFoundException extends RuntimeException {

    // Constructor that accepts a message
    public UserNotFoundException(String message) {
        super(message);
    }

    // Constructor that accepts a message and a cause
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
