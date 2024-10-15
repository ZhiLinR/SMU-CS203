package user.util;

import java.util.regex.Pattern;

/**
 * Utility class for common validation operations.
 * Provides methods to validate fields such as email, UUID, role, and required fields.
 */
public class ValidationUtil {

    // Maximum length for an email address
    private static final int MAX_EMAIL_LENGTH = 254;

    /**
     * Validates if the provided string is not null or empty.
     *
     * @param value the string to validate
     * @param errorMessage the error message to throw if the validation fails
     * @throws IllegalArgumentException if the string is null or empty
     */
    public static void validateRequiredFields(String value, String errorMessage) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Validates the admin role to ensure it is either 0 (non-admin) or 1 (admin).
     *
     * @param isAdmin the role value to validate
     * @throws IllegalArgumentException if the role value is invalid (not 0 or 1)
     */
    public static void validateRole(Byte isAdmin) {
        if (isAdmin == null || (isAdmin != 0 && isAdmin != 1)) {
            throw new IllegalArgumentException("Invalid value for Role.");
        }
    }

    /**
     * Validates if the provided email has a valid format using a regular expression.
     *
     * @param email the email to validate
     * @return {@code true} if the email is valid, {@code false} otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email.length() > MAX_EMAIL_LENGTH) {
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    /**
     * Validates the UUID to ensure it is not null or empty.
     *
     * @param uuid the UUID to validate
     * @throws IllegalArgumentException if the UUID is null or empty
     */
    public static void validateUUID(String uuid) {
        validateRequiredFields(uuid, "UUID is required");
    }
}
