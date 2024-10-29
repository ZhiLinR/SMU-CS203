package matchmaking.util;

/**
 * Utility class for common validation operations.
 * Provides methods to validate fields such as email, UUID, role, and required fields.
 */
public class ValidationUtil {

    /**
     * Validates that a string is not null or empty.
     *
     * @param value The string to validate.
     * @param fieldName The name of the field being validated (for error messages).
     * @throws IllegalArgumentException if the string is null or empty.
     */
    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be null or empty.");
        }
    }
}
