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

public class MiddlewareServiceTest {

    @Mock
    private MockJWTRepository jwtRepository; // Mocking the JWT repository

    @InjectMocks
    private MiddlewareService middlewareService; // Injecting the service to be tested

    private String validToken;
    private JWToken dbJwt;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup a valid token (we assume that JwtUtil generates this correctly)
        validToken = JwtUtil.generateToken("email@gmail.com", "uuid-1234", 1);

        // Mocking a JWToken object retrieved from the repository
        dbJwt = new JWToken(validToken, "uuid-1234", 1, LocalDateTime.now(), null, LocalDateTime.now());

        // Stubbing the repository call to return the mock JWToken
        when(jwtRepository.getTokenByUuid("uuid-1234")).thenReturn(dbJwt);
    }

    @Test
    public void testCheckJwt_ValidToken() {
        // Mock the ValidationUtil static method to return a valid map of data
        Map<String, Object> validJwtMap = new HashMap<>();
        validJwtMap.put("uuid", "uuid-1234");
        validJwtMap.put("isAdmin", 1);

        // Call the method under test
        Map<String, Object> result = middlewareService.checkJwt(validToken);

        // Assertions
        assertEquals(validJwtMap, result, "The returned map should match the validated JWT data.");

        // Verify that the token in the repo was updated
        verify(jwtRepository).updateToken(any(JWToken.class));
    }
}