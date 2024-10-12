package middlewareapd;

import java.util.List;
import java.util.Map;

import middlewareapd.model.JWToken;
import middlewareapd.service.MiddlewareService;
import middlewareapd.repository.MockJWTRepository;
import middlewareapd.exception.UnauthorizedException;

public class Main {
    public static void main(String[] args) {
        // Initialize MockJWTRepository
        MockJWTRepository mockRepository = new MockJWTRepository();
        
        // Initialize MiddlewareService with the mock repository
        MiddlewareService middlewareService = new MiddlewareService(mockRepository);
        
        // Get all mock tokens from the repository
        List<JWToken> mockTokens = mockRepository.getAllTokens();

        // Print and validate all mock tokens
        System.out.println("Validating Mock Tokens:");
        for (JWToken token : mockTokens) {
            try {
                // Attempt to validate each token's JWT
                System.out.println("Checking token with JWT: " + token.getJwt());
                Map<String, Object> result = middlewareService.checkJwt(token.getJwt());
                
                // If successful, print the result
                System.out.println("Token valid. UUID: " + result.get("uuid") + ", isAdmin: " + result.get("isAdmin"));
            } catch (UnauthorizedException e) {
                // Handle invalid tokens (e.g., expired or tampered)
                System.out.println("Invalid token: " + token.getJwt() + ". Reason: " + e.getMessage());
            }
            System.out.println("-------------------------");
        }
    }
}
