package middlewareapd.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import middlewareapd.model.JWToken;

public class MockJWTRepositoryTest {

    private MockJWTRepository repository;

    @BeforeEach
    public void setUp() {
        // Initialize repository and mock tokens
        repository = new MockJWTRepository();
    }

    @Test
    public void testGetAllTokens() {
        // Should return exactly 20 mock tokens
        assertEquals(20, repository.getAllTokens().size());
    }

    @Test
    public void testGetTokenByUuid() {
        // Given a known UUID from the mock repository
        String validUuid = repository.getAllTokens().get(0).getUuid();

        // Fetch the token by UUID
        JWToken token = repository.getTokenByUuid(validUuid);

        // Should return a valid token with matching UUID
        assertNotNull(token);
        assertEquals(validUuid, token.getUuid());
    }

    @Test
    public void testGetTokenByInvalidUuid() {
        // Given an invalid UUID
        String invalidUuid = "non-existent-uuid";

        // Fetch the token by UUID
        JWToken token = repository.getTokenByUuid(invalidUuid);

        // Should return null for non-existent UUID
        assertNull(token);
    }

    // Add more repository-specific tests as needed.
}
