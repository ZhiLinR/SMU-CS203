package middlewareapd.repository;

import middlewareapd.util.JwtUtil; // Ensure you import your JwtUtil class
import middlewareapd.model.JWToken;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class MockJWTRepository {
    private List<JWToken> tokens;

    // Hardcoded email addresses
    private String[] EMAILS = {
        "user1@example.com",
        "user2@example.com",
        "user3@example.com",
        "user4@example.com",
        "user5@example.com",
        "user6@example.com",
        "user7@example.com",
        "user8@example.com",
        "user9@example.com",
        "user10@example.com",
        "user11@example.com",
        "user12@example.com",
        "user13@example.com",
        "user14@example.com",
        "user15@example.com",
        "user16@example.com",
        "user17@example.com",
        "user18@example.com",
        "user19@example.com",
        "user20@example.com"
    };

    public MockJWTRepository() {
        this.tokens = new ArrayList<>();
        createMockTokens();
    }

    // Method to create mock JWToken entries
    private void createMockTokens() {
        for (int i = 0; i < 20; i++) {
            String uuid = UUID.randomUUID().toString(); // Generate a unique UUID
            Byte isAdmin = (byte) (i % 2); // Alternate between 0 and 1 for isAdmin

            // Simulate last login time with more variation
            LocalDateTime lastLogin = LocalDateTime.now().minusDays(i).minusHours(ThreadLocalRandom.current().nextInt(0, 5)); // Randomize hours from 0 to 4

            // Simulate logout time
            LocalDateTime logout = (i % 3 == 0) ? LocalDateTime.now().minusDays(i).minusHours(ThreadLocalRandom.current().nextInt(0, 5)) : null;

            // Generate lastAccess within 10 minutes of lastLogin
            int randomMinutes = ThreadLocalRandom.current().nextInt(0, 11); // Randomize minutes from 0 to 10
            LocalDateTime lastAccess = lastLogin.plusMinutes(randomMinutes); // Ensure lastAccess is within 10 minutes of lastLogin

            // Use the hardcoded email based on the index
            String email = EMAILS[i];

            // Create a JWT using JwtUtil (assumed to have createToken method)
            String jwt = JwtUtil.generateToken(email, uuid, isAdmin); // Ensure this method exists in JwtUtil

            // Create a new JWToken instance
            JWToken token = new JWToken(jwt, uuid, isAdmin, lastLogin, logout, lastAccess);
            tokens.add(token);
        }
    }


    // Method to retrieve all tokens
    public List<JWToken> getAllTokens() {
        return tokens;
    }

    // Method to retrieve a specific token by its UUID
    public JWToken getTokenByUuid(String uuid) {
        return tokens.stream()
                .filter(token -> token.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }


    // Method to update a token in the repository
    public void updateToken(JWToken token) {
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getUuid().equals(token.getUuid())) {
                tokens.set(i, token); // Replace the existing token
                return; // Exit after updating
            }
        }
    }
}
