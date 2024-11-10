package user.util;

import java.util.regex.Pattern;

import user.dto.ProfileRequest;

/**
 * Utility class for common validation operations.
 * Provides methods to validate fields such as email, UUID, role, and required
 * fields.
 */
public class ValidationUtil {

    // Maximum length for an email address
    private static final int MAX_EMAIL_LENGTH = 254;

    /**
     * Validates if the provided string is not null or empty.
     *
     * @param value        the string to validate
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
     * Validates if the provided email has a valid format using a regular
     * expression.
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

    /**
     * Validates the provided profile request for missing fields and invalid data.
     *
     * @param profileRequest the profile request to validate
     * @throws IllegalArgumentException if any required fields are missing or
     *                                  contain invalid data
     */
    public static void validateProfileRequest(ProfileRequest profileRequest) {
        ValidationUtil.validateRequiredFields(profileRequest.getEmail(), "Email is required");
        ValidationUtil.validateRequiredFields(profileRequest.getPassword(), "Password is required");
        ValidationUtil.validateRequiredFields(profileRequest.getName(), "Name is required");
        ValidationUtil.validateRole(profileRequest.getIsAdmin());

        if (!ValidationUtil.isValidEmail(profileRequest.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    /**
     * Validates the provided login request for missing fields and invalid data.
     *
     * @param loginRequest the login request to validate
     * @throws IllegalArgumentException if the email or password is missing or
     *                                  invalid
     */
    public static void validateLoginRequest(ProfileRequest loginRequest) {
        ValidationUtil.validateRequiredFields(loginRequest.getEmail(), "Email is required");
        ValidationUtil.validateRequiredFields(loginRequest.getPassword(), "Password is required");

        if (!ValidationUtil.isValidEmail(loginRequest.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    /**
     * Validates the provided user update parameters for missing or invalid data.
     *
     * @param uuid     the UUID of the user
     * @param email    the new email to update
     * @param password the new password to update
     * @param name     the new name to update
     * @param isAdmin  the new admin status (0 or 1)
     * @throws IllegalArgumentException if any provided parameters are invalid or
     *                                  missing
     */
    public static void validateUserUpdate(String uuid, String email, String password, String name, Byte isAdmin) {
        ValidationUtil.validateUUID(uuid);
        ValidationUtil.validateRequiredFields(email, "Email is required");
        ValidationUtil.validateRequiredFields(password, "Password is required");
        ValidationUtil.validateRequiredFields(name, "Name is required");
        ValidationUtil.validateRole(isAdmin);
    }
}
