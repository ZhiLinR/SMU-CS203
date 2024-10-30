package matchmaking.exception;

/**
 * Exception thrown when there are any issues with matchmaking logic.
 * This is a runtime exception that extends {@link RuntimeException}.
 */
public class InvalidRoundException extends RuntimeException {

    /**
     * Constructs a new {@code InvalidRoundException} with the specified detail
     * message.
     *
     * @param message the detail message, which is saved for later retrieval by the
     *                {@link Throwable#getMessage()} method
     */
    public InvalidRoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code InvalidRoundException} with the specified detail
     * message and cause.
     *
     * @param message the detail message, which is saved for later retrieval by the
     *                {@link Throwable#getMessage()} method
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link Throwable#getCause()} method)
     */
    public InvalidRoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
