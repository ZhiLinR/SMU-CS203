package middleware.util;

import middleware.exception.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;

import middleware.exception.UnauthorizedException;
import middleware.model.JWToken;

/**
 * ValidationUtil provides utility methods for validating JWT session data,
 * UUIDs, and user roles. These methods are used to ensure that the JWT and its
 * claims are consistent with the data stored in the database.
 */
public class ValidationUtil {

    @Autowired
    private TokenValidationService tokenValidationService;

    /**
     * Validates that a string is not null or empty.
     *
     * @param value     The string to validate.
     * @param fieldName The name of the field being validated (for error messages).
     * @throws IllegalArgumentException if the string is null or empty.
     */
    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be null or empty.");
        }
    }

    /**
     * Validates the JWT session by checking if the JWT exists in the database.
     *
     * @param retrievedJWT the JWT object retrieved from the database.
     * @throws UnauthorizedException if the JWT is invalid or session expired.
     */
    public static void validateJwtSession(JWToken retrievedJWT) throws UnauthorizedException {
        if (retrievedJWT == null) {
            // Throw an exception if the JWT is invalid or session expired
            throw new UnauthorizedException("Invalid JWT or session expired.");
        }
    }

    /**
     * Validates the UUID by comparing the UUID extracted from the JWT with the one
     * stored in the database.
     *
     * @param dbUuid        the UUID stored in the database.
     * @param extractedUuid the UUID extracted from the JWT.
     * @throws UnauthorizedException if UUIDs do not match.
     */
    public static void validateUuid(String dbUuid, String extractedUuid) throws UnauthorizedException {
        validateNotEmpty(dbUuid, "UUID");
        validateNotEmpty(extractedUuid, "UUID");

        if (!dbUuid.equals(extractedUuid)) {
            // Throw an exception if the UUIDs do not match
            throw new UnauthorizedException("JWT UUID mismatch.");
        }
    }

    /**
     * Validates the user's role by checking if the role from the database matches
     * the role specified in the JWT.
     * Validates the user's role by checking if the role from the database matches
     * the role specified in the JWT.
     *
     * @param dbIsAdmin the admin status (role) stored in the database.
     * @param isAdmin   the admin status (role) extracted from the JWT.
     * @throws UnauthorizedException if roles do not match.
     * @throws UserNotFoundException if user is not found.
     */
    public void validateUserRole(Byte dbIsAdmin, Byte isAdmin, String uuid)
            throws UnauthorizedException, UserNotFoundException {
        if (dbIsAdmin == null || !dbIsAdmin.equals(isAdmin)) {
            // Throw an exception if user is not found OR user roles do not match
            tokenValidationService.updateLogout(uuid);

            throw new UserNotFoundException("User not found for the provided UUID.");
        }
    }
}
