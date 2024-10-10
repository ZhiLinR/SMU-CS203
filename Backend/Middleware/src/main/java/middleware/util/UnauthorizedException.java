package middleware.util;

/**
 * Exception thrown when User inputs the wrong email or password.
 * This is a runtime exception that extends {@link RuntimeException}.
 */
public class UnauthorizedException extends RuntimeException {

    /**
     * Constructs a new {@code UnauthorizedException} with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the {@link Throwable#getMessage()} method
     */
    public UnauthorizedException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code UnauthorizedException} with the specified detail message and cause.
     *
     * @param message the detail message, which is saved for later retrieval by the {@link Throwable#getMessage()} method
     * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method)
     */
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}

