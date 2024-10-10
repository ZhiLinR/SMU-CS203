package middlewareapd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Async;

import io.jsonwebtoken.Claims;

import middlewareapd.dto.JWTRequest;
import middlewareapd.model.JWToken;
import middlewareapd.repository.*;
import middlewareapd.util.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

/**
 * MiddlewareService handles the business logic for validating JWT tokens and checking user roles.
 * It interacts with repositories to validate JWT sessions, user roles, and extract claims from the token.
 */
@Service
public class MiddlewareService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTokenRepository jwTokenRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final ReentrantReadWriteLock mReadWriteLock = new ReentrantReadWriteLock();
    private final Lock mReadLock = mReadWriteLock.readLock();

    /**
     * Validates the provided JWT by extracting claims, checking its validity in the database, 
     * and comparing user role information.
     *
     * <p>This method decrypts the JWT to extract claims such as user UUID and admin status.
     * It then validates the session by checking if the token exists in the database and matches
     * the extracted information.</p>
     *
     * @param jwtRequest the request object containing the JWT to be validated.
     * @return a Map containing the user's UUID and admin status ("isAdmin").
     * @throws UnauthorizedException if the JWT is invalid or session validation fails.
     * @throws UserNotFoundException if the user UUID associated with the JWT is not found.
     */
    @Async
    @Transactional
    public CompletableFuture<Map<String, String>> checkJwt(JWTRequest jwtRequest) {
        String jwt = jwtRequest.getJwt();
        Claims claims;

        // Decrypt JWT and extract claims
        claims = jwtUtil.decryptToken(jwt);

        String extractedUuid = claims.get("uuid", String.class);
        Byte isAdmin = claims.get("isAdmin", Byte.class);

                mReadLock.lock();
        try {
            // Critical section: reading from the database and performing validations
            JWToken dbJwt = jwTokenRepository.checkValidity(jwt);
            ValidationUtil.validateJwtSession(dbJwt);

            String dbUuid = dbJwt.getUuid();
            ValidationUtil.validateUuid(dbUuid, extractedUuid);

            Byte dbIsAdmin = userRepository.getRoleByUUID(dbUuid);
            ValidationUtil.validateUserRole(dbIsAdmin, isAdmin);

            // Prepare and return the result as a map
            return CompletableFuture.completedFuture(Map.of("uuid", dbUuid, "isAdmin", isAdmin.toString()));

        } finally {
            // Release the read lock to allow other threads to proceed
            mReadLock.unlock();
        }
    }
}