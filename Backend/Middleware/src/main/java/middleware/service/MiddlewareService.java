package middleware.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.Claims;
import middleware.dto.JWTRequest;
import middleware.exception.UserNotFoundException;
import middleware.exception.UnauthorizedException;
import middleware.model.JWToken;
import middleware.util.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * MiddlewareService handles the business logic for validating JWT tokens and
 * checking user roles. It interacts with the {@link TokenValidationService} to
 * validate JWT sessions, verify user roles, and extract claims from the token.
 * This service provides an asynchronous method for validating JWTs and
 * performing associated checks, such as user verification and role validation.
 *
 * <p>
 * All database-related operations are handled in transactional methods
 * to ensure consistency and integrity.
 * </p>
 */
@Service
@Transactional
public class MiddlewareService {

    @Autowired
    private TokenValidationService tokenValidationService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Asynchronously validates a JWT by extracting claims, verifying its presence
     * in the database, and checking the user's role.
     *
     * <p>
     * This method decrypts the JWT to extract the user UUID and admin status. It
     * then calls synchronous methods to validate the JWT and verify the user's role
     * against the stored values in the database.
     * </p>
     *
     * <p>
     * The method returns a {@link CompletableFuture} containing the user UUID and
     * admin status as key-value pairs. The operation is performed asynchronously to
     * prevent blocking the main thread.
     * </p>
     *
     * @param jwtRequest the request containing the JWT to validate.
     * @return a {@link CompletableFuture} with a map containing the user's UUID and
     *         admin status.
     * @throws UnauthorizedException if the JWT is invalid or expired.
     * @throws UserNotFoundException if the associated user UUID is not found in the
     *                               database.
     * @throws RuntimeException      if any unexpected error occurs during the JWT
     *                               validation process.
     */
    @Async
    public CompletableFuture<Map<String, String>> checkJwt(JWTRequest jwtRequest) {
        System.out.println("Executing in thread: " + Thread.currentThread().getName()); // For diagnostic purposes

        return CompletableFuture.supplyAsync(() -> {
            try {
                String jwt = jwtRequest.getJwt();
                Claims claims = jwtUtil.decryptToken(jwt);

                String extractedUuid = claims.get("uuid", String.class);
                Byte isAdmin = claims.get("isAdmin", Byte.class);

                // Validate JWT session and user role
                JWToken dbJwt = tokenValidationService.checkValiditySync(jwt);
                String dbUuid = dbJwt.getUuid();
                ValidationUtil.validateUuid(dbUuid, extractedUuid);

                Byte dbIsAdmin = tokenValidationService.getUserRoleSync(dbUuid);
                ValidationUtil.validateUserRole(dbIsAdmin, isAdmin);

                // Return validated user data
                return Map.of("uuid", dbUuid, "isAdmin", isAdmin.toString());

            } catch (UnauthorizedException | UserNotFoundException e) {
                tokenValidationService.invalidateJwt(jwtRequest.getJwt());
                throw e; // Propagate known exceptions
            } catch (Exception e) {
                throw new RuntimeException("Unexpected error during JWT validation: " + e.getMessage());
            }
        });
    }
}
