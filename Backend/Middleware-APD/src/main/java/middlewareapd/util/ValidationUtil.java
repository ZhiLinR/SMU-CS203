package middlewareapd.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

import com.auth0.jwt.interfaces.DecodedJWT;

import middlewareapd.exception.UnauthorizedException;
import middlewareapd.exception.UserNotFoundException;
import middlewareapd.model.JWToken;
import middlewareapd.repository.MockJWTRepository;

/**
 * ValidationUtil provides utility methods for validating JWT session data, UUIDs, and user roles.
 * These methods are used to ensure that the JWT and its claims are consistent with the data stored in the database.
 */
public class ValidationUtil {

    /**
     * Validates the JWT session by checking if the JWT exists in the database
     * and ensuring that the user has not logged out.
     *
     * @param retrievedJWT the JWT object retrieved from the database.
     * @throws UnauthorizedException if the JWT is invalid, the session has expired, or the user is logged out.
     */
    public static void validateJwtSession(JWToken retrievedJWT) {
        if (retrievedJWT == null) {
            throw new UnauthorizedException("Invalid JWT or session expired.");
        }

        if (retrievedJWT.getLogout() != null) {
            throw new UnauthorizedException("User has logged out.");
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
    public static void validateUserRole(Integer dbIsAdmin, int isAdmin) {
        if (dbIsAdmin == null) {
            throw new UserNotFoundException("User not found for the provided UUID.");
        }

        if (!dbIsAdmin.equals(isAdmin)) {
            throw new UnauthorizedException("Role does not match admin status.");
        }
    }

    /**
     * Validates the JWT by checking its signature and retrieving the claims.
     *
     * @param token the JWT to validate.
     * @return the decoded JWT if validation is successful.
     * @throws UnauthorizedException if the JWT is invalid or expired.
     */
    public static DecodedJWT decodeJwt(String token) {
        return JwtUtil.verifyToken(token);
    }

    /**
     * Fetches the JWToken entry from the repository using the extracted UUID.
     *
     * @param extractedUuid the UUID extracted from the JWT.
     * @param jwtRepository the repository to fetch the JWToken entry.
     * @return the JWToken corresponding to the extracted UUID.
     * @throws UnauthorizedException if the JWToken does not exist.
     */
    public static JWToken fetchDatabaseJWT(String extractedUuid, MockJWTRepository jwtRepository) {
        JWToken retrievedJWT = jwtRepository.getTokenByUuid(extractedUuid);
        validateJwtSession(retrievedJWT); // Validate that the session exists
        return retrievedJWT;
    }

    /**
     * Validates the JWT by checking its signature, UUID claim, and user role.
     *
     * @param token the JWT to validate.
     * @param jwtRepository the repository to fetch the JWToken entry.
     * @return a map containing UUID and isAdmin status if validation is successful.
     * @throws UnauthorizedException if the JWT is invalid or does not match stored data.
     */
    public static Map<String, Object> validateJwt(String token, MockJWTRepository jwtRepository) {
        ReadWriteLock lock = LockFactory.getRWLock(token);
        DecodedJWT decodedJWT = decodeJwt(token);
        String extractedUuid = getExtractedUuid(decodedJWT);

        JWToken retrievedJWT = fetchDbJWTWithLock(extractedUuid, jwtRepository, lock);
        validateUuid(retrievedJWT.getUuid(), extractedUuid);
        validateUserRole(retrievedJWT.getIsAdmin(), decodedJWT.getClaim("isAdmin").asInt());

        return createResponseMap(retrievedJWT);
    }

    /**
     * Retrieves the UUID from the decoded JWT.
     *
     * @param decodedJWT the decoded JWT.
     * @return the extracted UUID.
     * @throws UnauthorizedException if the UUID is not found.
     */
    private static String getExtractedUuid(DecodedJWT decodedJWT) {
        String extractedUuid = decodedJWT.getClaim("uuid").asString();
        if (extractedUuid == null) {
            throw new UnauthorizedException("Invalid JWT: UUID not found.");
        }
        return extractedUuid;
    }

    /**
     * Fetches the JWToken entry from the repository within a read lock.
     *
     * @param extractedUuid the UUID extracted from the JWT.
     * @param jwtRepository the repository to fetch the JWToken entry.
     * @return the JWToken corresponding to the extracted UUID.
     */
    private static JWToken fetchDbJWTWithLock(String extractedUuid, MockJWTRepository jwtRepository, ReadWriteLock lock) {
        lock.readLock().lock();
        try {
            return fetchDatabaseJWT(extractedUuid, jwtRepository);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Creates a response map containing UUID and isAdmin status.
     *
     * @param retrievedJWT the JWToken object.
     * @return a map containing UUID and isAdmin status.
     */
    private static Map<String, Object> createResponseMap(JWToken retrievedJWT) {
        Map<String, Object> response = new HashMap<>();
        response.put("uuid", retrievedJWT.getUuid());
        response.put("isAdmin", retrievedJWT.getIsAdmin());
        return response;
    }
}

