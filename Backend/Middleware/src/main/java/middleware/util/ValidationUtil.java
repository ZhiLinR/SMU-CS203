package middleware.util;

import middleware.exception.UserNotFoundException;
import middleware.exception.UnauthorizedException;
import middleware.model.JWToken;

/**
 * ValidationUtil provides utility methods for validating JWT session data,
 * UUIDs, and user roles. These methods are used to ensure that the JWT and its
 * claims are consistent with the data stored in the database.
 */
public class ValidationUtil {

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
    public static void validateUserRole(Byte dbIsAdmin, Byte isAdmin)
            throws UnauthorizedException, UserNotFoundException {
        if (dbIsAdmin == null) {
            // Throw an exception if user is not found
            throw new UserNotFoundException("User not found for the provided UUID.");
        }
        if (!dbIsAdmin.equals(isAdmin)) {
            // Throw an exception if roles do not match
            throw new UnauthorizedException("Unauthorized Transaction");
        }
    }
}
