package middlewareapd.repository;

import middlewareapd.util.JwtUtil; // Ensure you import your JwtUtil class
import middlewareapd.model.JWToken;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * MockJWTRepository is a repository class that simulates a data store for JSON Web Tokens (JWTs).
 * It provides methods to create, retrieve, and update mock JWT tokens for testing purposes.
 */
public class MockJWTRepository {
    private List<JWToken> tokens;
    private static final int EMAIL_NUM = 10000; // Number of hardcoded email addresses
    private final String[] EMAILS = generateEmails(EMAIL_NUM); // Hardcoded email addresses

    /**
     * Constructs a MockJWTRepository and initializes mock JWT tokens.
     */
    public MockJWTRepository() {
        this.tokens = new ArrayList<>();
        createMockTokens();
    }

    /**
     * Creates mock JWToken entries and adds them to the repository.
     */
    private void createMockTokens() {
        for (int i = 0; i < EMAIL_NUM; i++) {
            String uuid = UUID.randomUUID().toString(); // Generate a unique UUID
            Byte isAdmin = (byte) (i % 2); // Alternate between 0 and 1 for isAdmin

            // Simulate last login time with more variation
            LocalDateTime lastLogin = LocalDateTime.now().minusDays(i)
                    .minusHours(ThreadLocalRandom.current().nextInt(0, 5)); // Randomize hours from 0 to 4

            // Simulate logout time
            LocalDateTime logout = (i % 3 == 0) 
                ? LocalDateTime.now().minusDays(i).minusHours(ThreadLocalRandom.current().nextInt(0, 5)) 
                : null;

            // Generate lastAccess within 10 minutes of lastLogin
            int randomMinutes = ThreadLocalRandom.current().nextInt(0, 11); // Randomize minutes from 0 to 10
            LocalDateTime lastAccess = lastLogin.plusMinutes(randomMinutes); // Ensure lastAccess is within 10 minutes of lastLogin

            // Use the hardcoded email based on the index
            String email = EMAILS[i];

            // Create a JWT using JwtUtil (assumed to have createToken method)
            String jwt = JwtUtil.generateToken(email, uuid, isAdmin); // Ensure this method exists in JwtUtil

            // Create a new JWToken instance and add it to the tokens list
            JWToken token = new JWToken(jwt, uuid, isAdmin, lastLogin, logout, lastAccess);
            tokens.add(token);
        }
    }

    /**
     * Retrieves all tokens in the repository.
     *
     * @return a list of all JWToken instances
     */
    public List<JWToken> getAllTokens() {
        return tokens;
    }

    /**
     * Retrieves a specific token by its UUID.
     *
     * @param uuid the UUID of the token to retrieve
     * @return the JWToken instance if found, or null if not
     */
    public JWToken getTokenByUuid(String uuid) {
        return tokens.stream()
                .filter(token -> token.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates an existing token in the repository.
     *
     * @param token the JWToken instance to update
     */
    public void updateToken(JWToken token) {
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getUuid().equals(token.getUuid())) {
                tokens.set(i, token); // Replace the existing token
                return; // Exit after updating
            }
        }
    }

    /**
     * Generates an array of email addresses in the format user[number]@example.com.
     *
     * @param count the number of email addresses to generate
     * @return an array of generated email addresses
     */
    public String[] generateEmails(int count) {
        String[] emails = new String[count];

        for (int i = 0; i < count; i++) {
            emails[i] = "user" + (i + 1) + "@example.com"; // i + 1 to start from user1
        }

        return emails;
    }
}
