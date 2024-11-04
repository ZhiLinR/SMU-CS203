package matchmaking.exception;

/**
 * Exception thrown when a tournamnent results not passed.
 * This is a runtime exception that extends {@link RuntimeException}.
 */
public class ResultsNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code ResultsNotFoundException} with the specified
     * detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the
     *                {@link Throwable#getMessage()} method
     */
    public ResultsNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ResultsNotFoundException} with the specified
     * detail message and cause.
     *
     * @param message the detail message, which is saved for later retrieval by the
     *                {@link Throwable#getMessage()} method
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link Throwable#getCause()} method)
     */
    public ResultsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
