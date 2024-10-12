package middlewareapd.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

import middlewareapd.exception.UnauthorizedException;
import middlewareapd.exception.UserNotFoundException;
import middlewareapd.model.JWToken;
import middlewareapd.repository.MockJWTRepository;
import middlewareapd.util.LockFactory;
import middlewareapd.util.ValidationUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MiddlewareService handles operations related to JWT validation and management.
 * It provides methods to validate JWT tokens, update access times, and interact with the JWT repository.
 */
public class MiddlewareService {
    private final MockJWTRepository jwtRepository;
    private static final Logger logger = LoggerFactory.getLogger(MiddlewareService.class);

    /**
     * Constructs a MiddlewareService with the specified JWT repository.
     *
     * @param jwtRepository the JWT repository to interact with.
     */
    public MiddlewareService(MockJWTRepository jwtRepository) {
        this.jwtRepository = jwtRepository;
    }

    /**
     * Validates the given JWT token and updates the last access time for the associated user.
     *
     * @param token the JWT token to validate.
     * @return a map containing the UUID and other information if validation is successful.
     */
    public Map<String, Object> checkJwt(String token) {
        ReadWriteLock jwtLock = LockFactory.getRWLock("JWTValidationLock");
        String uuid;
        Map<String, Object> result;

        try {
            // Validate the JWT and retrieve the result outside of the lock
            result = ValidationUtil.validateJwt(token, jwtRepository);
            uuid = (String) result.get("uuid");

            // Update last access time within a write lock
            @SuppressWarnings("unused")
            JWToken dbJwt = updateLastAccess(uuid, jwtLock);
            logger.info("Successfully validated JWT for UUID: {}", uuid);
            return result;
        } catch (UnauthorizedException e) {
            handleUnauthorizedException(e);
            throw e; // Return an empty map or handle accordingly
        } catch (UserNotFoundException e) {
            handleUserNotFoundException(e);
            throw e; // Return an empty map or handle accordingly
        } catch (Exception e) {
            handleUnexpectedException(e);
            throw e; // Return an empty map or handle accordingly
        }
    }

    /**
     * Updates the last access time for the given UUID and saves the updated token.
     *
     * @param uuid the UUID of the user whose access time is to be updated.
     * @return the updated JWToken, or null if the token was not found.
     */
    private JWToken updateLastAccess(String uuid, ReadWriteLock jwtLock) {
        JWToken dbJwt = jwtRepository.getTokenByUuid(uuid); // Read without locking
        if (dbJwt != null) {
            LocalDateTime now = LocalDateTime.now();
            dbJwt.setLastAccess(now);
            logger.info("Updated lastAccess for UUID {} to {}", uuid, now);
    
            jwtLock.writeLock().lock(); // Acquire the write lock only when updating the repository
            try {
                jwtRepository.updateToken(dbJwt); // Critical section
            } finally {
                jwtLock.writeLock().unlock();
            }
        }
        return dbJwt;
    }

    /**
     * Handles an UnauthorizedException by logging the error details.
     *
     * @param e the UnauthorizedException to handle.
     */
    private void handleUnauthorizedException(UnauthorizedException e) {
        logger.error("Unauthorized access attempt detected: {}", e.getMessage());
    }

    /**
     * Handles a UserNotFoundException by logging the error details.
     *
     * @param e the UserNotFoundException to handle.
     */
    private void handleUserNotFoundException(UserNotFoundException e) {
        logger.error("User not found: {}", e.getMessage());
    }

    /**
     * Handles unexpected exceptions by logging the error details.
     *
     * @param e the Exception to handle.
     */
    private void handleUnexpectedException(Exception e) {
        logger.error("Unexpected error during JWT validation: {}", e.getMessage(), e);
    }
}
