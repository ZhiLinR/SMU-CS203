package middlewareapd.util;

import middlewareapd.model.JWToken;

/**
 * ValidationUtil provides utility methods for validating JWT session data, UUIDs, and user roles.
 * These methods are used to ensure that the JWT and its claims are consistent with the data stored in the database.
 */
public class ValidationUtil {

    /**
     * Validates the JWT session by checking if the JWT exists in the database.
     *
     * @param dbJwt the JWT object retrieved from the database.
     * @throws UnauthorizedException if the JWT is invalid or the session has expired.
     */
    public static void validateJwtSession(JWToken dbJwt) {
        if (dbJwt == null) {
            throw new UnauthorizedException("Invalid JWT or session expired.");
        }
    }

    /**
     * Validates the UUID by comparing the UUID extracted from the JWT with the one stored in the database.
     *
     * @param dbUuid the UUID stored in the database.
     * @param extractedUuid the UUID extracted from the JWT.
     * @throws UnauthorizedException if the UUIDs do not match.
     */
    public static void validateUuid(String dbUuid, String extractedUuid) {
        if (!dbUuid.equals(extractedUuid)) {
            throw new UnauthorizedException("JWT UUID mismatch.");
        }
    }

    /**
     * Validates the user's role by checking if the role from the database matches the role specified in the JWT.
     *
     * @param dbIsAdmin the admin status (role) stored in the database.
     * @param isAdmin the admin status (role) extracted from the JWT.
     * @throws UserNotFoundException if the user is not found for the provided UUID.
     * @throws UnauthorizedException if the roles do not match.
     */
    public static void validateUserRole(Byte dbIsAdmin, Byte isAdmin) {
        if (dbIsAdmin == null) {
            throw new UserNotFoundException("User not found for the provided UUID.");
        }
        if (!dbIsAdmin.equals(isAdmin)) {
            throw new UnauthorizedException("Role does not match admin status.");
        }
    }
}