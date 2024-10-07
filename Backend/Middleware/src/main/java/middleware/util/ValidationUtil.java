package middleware.util;


import middleware.model.JWToken;

public class ValidationUtil {

    public static void validateJwtSession(JWToken dbJwt) {
        if (dbJwt == null) {
            throw new UnauthorizedException("Invalid JWT or session expired.");
        }
    }

    public static void validateUuid(String dbUuid, String extractedUuid) {
        if (!dbUuid.equals(extractedUuid)) {
            throw new UnauthorizedException("JWT UUID mismatch.");
        }
    }

    public static void validateUserRole(Byte dbIsAdmin, Byte isAdmin) {
        if (dbIsAdmin == null) {
            throw new UserNotFoundException("User not found for the provided UUID.");
        }
        if (!dbIsAdmin.equals(isAdmin)) {
            throw new UnauthorizedException("Role does not match admin status.");
        }
    }
}
