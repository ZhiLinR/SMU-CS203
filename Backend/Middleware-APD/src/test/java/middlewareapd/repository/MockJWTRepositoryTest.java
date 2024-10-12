package middlewareapd.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import middlewareapd.model.JWToken;

/**
 * Unit tests for the {@link MockJWTRepository} class.
 * <p>
 * This test class ensures the correct behavior of token retrieval methods
 * within the {@link MockJWTRepository}. It includes tests for fetching tokens
 * by UUID, both with valid and invalid UUIDs.
 * </p>
 * 
 * <h3>Test Methods:</h3>
 * <ul>
 *     <li>{@link #testGetTokenByUuid()} - Verifies that a valid token is returned
 *     when a valid UUID is provided.</li>
 *     <li>{@link #testGetTokenByInvalidUuid()} - Ensures that {@code null} is returned
 *     when an invalid or non-existent UUID is queried.</li>
 * </ul>
 */
public class MockJWTRepositoryTest {

    /** Repository instance used for testing. */
    private MockJWTRepository repository;

    /**
     * Sets up the test environment before each test method is executed.
     * <p>
     * Initializes a new {@link MockJWTRepository} instance.
     * </p>
     */
    @BeforeEach
    public void setUp() {
        repository = new MockJWTRepository();
    }

    /**
     * Tests the {@link MockJWTRepository#getTokenByUuid(String)} method with a valid UUID.
     * <p>
     * This test ensures that a token with the given UUID is successfully retrieved
     * from the repository.
     * </p>
     * <ul>
     *     <li>Given: A valid UUID from the mock repository.</li>
     *     <li>When: The token is fetched using {@link MockJWTRepository#getTokenByUuid(String)}.</li>
     *     <li>Then: The token should not be {@code null}, and its UUID should match the provided UUID.</li>
     * </ul>
     */
    @Test
    public void testGetTokenByUuid() {
        String validUuid = repository.getAllTokens().get(0).getUuid();
        JWToken token = repository.getTokenByUuid(validUuid);
        assertNotNull(token);
        assertEquals(validUuid, token.getUuid());
    }

    /**
     * Tests the {@link MockJWTRepository#getTokenByUuid(String)} method with an invalid UUID.
     * <p>
     * This test ensures that querying a non-existent UUID returns {@code null}.
     * </p>
     * <ul>
     *     <li>Given: An invalid or non-existent UUID.</li>
     *     <li>When: The token is fetched using {@link MockJWTRepository#getTokenByUuid(String)}.</li>
     *     <li>Then: The result should be {@code null}.</li>
     * </ul>
     */
    @Test
    public void testGetTokenByInvalidUuid() {
        String invalidUuid = "non-existent-uuid";
        JWToken token = repository.getTokenByUuid(invalidUuid);
        assertNull(token);
    }
}

