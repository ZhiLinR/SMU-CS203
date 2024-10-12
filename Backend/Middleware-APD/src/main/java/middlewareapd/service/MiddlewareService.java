package middlewareapd.service;

import java.time.LocalDateTime;
import java.util.Map;

import middlewareapd.exception.UnauthorizedException;
import middlewareapd.model.JWToken;
import middlewareapd.repository.MockJWTRepository;
import middlewareapd.util.ValidationUtil;

public class MiddlewareService {
    private final MockJWTRepository jwtRepository;

    public MiddlewareService(MockJWTRepository jwtRepository) {
        this.jwtRepository = jwtRepository; // Use the provided repository
    }

    public Map<String, Object> checkJwt(String token) {
        try {
            // Step 1: Validate the JWT and retrieve the result
            Map<String, Object> result = ValidationUtil.validateJwt(token, jwtRepository);
            
            // Step 2: Retrieve the UUID from the result
            String uuid = (String) result.get("uuid");

            // Step 3: Fetch the corresponding JWToken entry from the repository
            JWToken dbJwt = jwtRepository.getTokenByUuid(uuid);
            
            // Step 4: Update lastAccess to the current time
            dbJwt.setLastAccess(LocalDateTime.now());
            
            // Step 5: Save the updated JWToken back to the repository
            jwtRepository.updateToken(dbJwt); // Assuming this method exists in MockJWTRepository

            return result; // Return the validated result
        } catch (UnauthorizedException e) {
            // Handle UnauthorizedException as needed (logging, rethrowing, etc.)
            throw e; // Or wrap and throw a custom exception
        }
    }
}
