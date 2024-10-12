package middlewareapd.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import middlewareapd.repository.MockJWTRepository;
import middlewareapd.util.JwtUtil;
import middlewareapd.model.JWToken;

/**
 * Unit tests for the {@link MiddlewareService} class.
 * <p>
 * This test class ensures that the JWT validation and token management logic within
 * {@link MiddlewareService} works as expected. The {@link MockJWTRepository} is mocked to simulate
 * the behavior of the repository without interacting with the actual data source.
 * </p>
 *
 * <h3>Test Methods:</h3>
 * <ul>
 *     <li>{@link #testCheckJwt_ValidToken()} - Verifies the correct handling of a valid JWT token,
 *     including the validation logic and updating the repository.</li>
 * </ul>
 */
public class MiddlewareServiceTest {

    /** Mocked instance of the JWT repository. */
    @Mock
    private MockJWTRepository jwtRepository;

    /** Service under test with dependencies injected. */
    @InjectMocks
    private MiddlewareService middlewareService;

    /** A valid JWT token string used for testing. */
    private String validToken;

    /** Mock {@link JWToken} object representing a token retrieved from the repository. */
    private JWToken dbJwt;

    /**
     * Sets up the test environment before each test method.
     * <p>
     * Initializes mock objects using {@link MockitoAnnotations} and stubs the
     * {@link MockJWTRepository#getTokenByUuid(String)} method to return a mock {@link JWToken}.
     * Additionally, a valid JWT token is generated for testing.
     * </p>
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validToken = JwtUtil.generateToken("email@gmail.com", "uuid-1234", 1);
        dbJwt = new JWToken(validToken, "uuid-1234", 1, LocalDateTime.now(), null, LocalDateTime.now());
        when(jwtRepository.getTokenByUuid("uuid-1234")).thenReturn(dbJwt);
    }

    /**
     * Tests the {@link MiddlewareService#checkJwt(String)} method with a valid JWT token.
     * <p>
     * This test verifies that the middleware correctly validates the provided JWT token
     * and updates the repository with the token details. It mocks the expected behavior
     * of the validation logic and ensures that the returned data matches the validated JWT.
     * </p>
     * <ul>
     *     <li>Given: A valid JWT token and a mocked repository.</li>
     *     <li>When: {@link MiddlewareService#checkJwt(String)} is called with the valid token.</li>
     *     <li>Then: The result should match the expected JWT data, and the token in the repository should be updated.</li>
     * </ul>
     */
    @Test
    public void testCheckJwt_ValidToken() {
        Map<String, Object> validJwtMap = new HashMap<>();
        validJwtMap.put("uuid", "uuid-1234");
        validJwtMap.put("isAdmin", 1);

        Map<String, Object> result = middlewareService.checkJwt(validToken);

        assertEquals(validJwtMap, result, "The returned map should match the validated JWT data.");
        verify(jwtRepository).updateToken(any(JWToken.class));
    }
}
