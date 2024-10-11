package middlewareapd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Async;

import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;
import middlewareapd.dto.JWTRequest;
import middlewareapd.model.JWToken;
import middlewareapd.util.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * MiddlewareService handles the business logic for validating JWT tokens and checking user roles.
 * It interacts with repositories to validate JWT sessions, user roles, and extract claims from the token.
 */

@Service
public class MiddlewareService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EntityManager entityManager;

    /**
     * Validates the provided JWT by extracting claims, checking its validity in the database,
     * and comparing user role information.
     *
     * @param jwtRequest the request object containing the JWT to be validated.
     * @return a CompletableFuture containing a Map with the user's UUID and admin status ("isAdmin").
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

        // Validate JWT session data using stored procedure
        JWToken dbJwt = checkJwtValidity(jwt);
        ValidationUtil.validateJwtSession(dbJwt);  // Validate JWT session

        String dbUuid = dbJwt.getUuid();
        ValidationUtil.validateUuid(dbUuid, extractedUuid);  // Validate UUID

        // Retrieve and check role for the user using stored procedure
        Byte dbIsAdmin = getUserRoleByUUID(dbUuid);
        ValidationUtil.validateUserRole(dbIsAdmin, isAdmin);  // Validate user role

        // Prepare and return the result as a map
        return CompletableFuture.completedFuture(Map.of("uuid", dbUuid, "isAdmin", isAdmin.toString()));
    }

    /**
     * Calls the stored procedure to check the validity of the JWT using EntityManager.
     *
     * @param jwt the JWT token to validate.
     * @return the JWToken entity from the database.
     */
    @Transactional
    public JWToken checkJwtValidity(String jwt) {
        // Create the stored procedure query
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("CheckValidity", JWToken.class);
        
        // Register the JWT parameter
        query.registerStoredProcedureParameter("jwtValue", String.class, jakarta.persistence.ParameterMode.IN); // Use jakarta.persistence
    
        // Set the JWT parameter
        query.setParameter("jwtValue", jwt); 
    
        // Execute the stored procedure
        JWToken dbJwt = (JWToken) query.getSingleResult(); // Assuming it returns a single result
        if (dbJwt == null) {
            throw new UnauthorizedException("Invalid JWT or session expired.");
        }
        return dbJwt;
    }

    /**
     * Calls the stored procedure to retrieve the user role by UUID using EntityManager.
     *
     * @param uuid the UUID of the user.
     * @return the admin status of the user.
     */
    @Transactional
    public Byte getUserRoleByUUID(String uuid) {
        // Create the stored procedure query
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("getRoleByUUID");
    
        // Register the UUID parameter
        query.registerStoredProcedureParameter("p_uuid", String.class, jakarta.persistence.ParameterMode.IN); // Use jakarta.persistence
    
        // Set the UUID parameter
        query.setParameter("p_uuid", uuid); 
    
        // Execute the stored procedure
        Byte dbIsAdmin = (Byte) query.getSingleResult(); // Assuming it returns a single result
        if (dbIsAdmin == null) {
            throw new UserNotFoundException("User not found for the provided UUID.");
        }
        return dbIsAdmin;
    }
    
}
