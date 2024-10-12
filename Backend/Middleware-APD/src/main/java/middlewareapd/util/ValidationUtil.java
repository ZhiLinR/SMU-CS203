package middlewareapd.util;

import java.util.HashMap;
import java.util.Map;

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
     * @param dbJwt the JWT object retrieved from the database.
     * @throws UnauthorizedException if the JWT is invalid, the session has expired, or the user is logged out.
     */
    public static void validateJwtSession(JWToken dbJwt) {
        if (dbJwt == null) {
            throw new UnauthorizedException("Invalid JWT or session expired.");
        }

        // Check if the user has logged out
        if (dbJwt.getLogout() != null) {
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
        // Check if the dbIsAdmin is null, indicating the user is not found
        if (dbIsAdmin == null) {
            throw new UserNotFoundException("User not found for the provided UUID.");
        }
        
        // Check if the roles do not match
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
    public static JWToken fetchDbJwt(String extractedUuid, MockJWTRepository jwtRepository) {
        JWToken dbJwt = jwtRepository.getTokenByUuid(extractedUuid); // Assuming this method exists in MockJWTRepository
        validateJwtSession(dbJwt); // Validate that the session exists
        return dbJwt;
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
        // Step 1: Verify and decode the JWT
        DecodedJWT decodedJWT = decodeJwt(token);

        // Step 2: Retrieve UUID from the JWT
        String extractedUuid = decodedJWT.getClaim("uuid").asString();
        if (extractedUuid == null) {
            throw new UnauthorizedException("Invalid JWT: UUID not found.");
        }

        // Step 3: Fetch the corresponding JWToken entry from the repository
        JWToken dbJwt = fetchDbJwt(extractedUuid, jwtRepository);

        // Step 4: Validate the UUID matches
        validateUuid(dbJwt.getUuid(), extractedUuid);

        // Step 5: Validate user role
        validateUserRole(dbJwt.getIsAdmin(), decodedJWT.getClaim("isAdmin").asInt());

        // Step 6: Return the UUID and isAdmin as a Map
        return createResponseMap(dbJwt);
    }

    /**
     * Creates a response map containing UUID and isAdmin status.
     *
     * @param dbJwt the JWToken object.
     * @return a map containing UUID and isAdmin status.
     */
    private static Map<String, Object> createResponseMap(JWToken dbJwt) {
        Map<String, Object> response = new HashMap<>();
        response.put("uuid", dbJwt.getUuid());
        response.put("isAdmin", dbJwt.getIsAdmin());
        return response;
    }
}
