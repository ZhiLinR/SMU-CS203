package middlewareapd.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Claim;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import middlewareapd.repository.MockJWTRepository;
import middlewareapd.util.ValidationUtil;
import middlewareapd.util.JwtUtil;
import middlewareapd.model.JWToken;
import middlewareapd.exception.UnauthorizedException;

public class MiddlewareServiceTest {

    private MiddlewareService middlewareService;
    private MockJWTRepository mockJWTRepository;

    // Declare test variables
    private String validJwt;
    private String validUuid;
    private String invalidJwt;
    private JWToken mockToken;
    private Map<String, Object> decodedResult;

    @BeforeEach
    public void setUp() {
        // Mock the repository
        mockJWTRepository = Mockito.mock(MockJWTRepository.class);
        middlewareService = new MiddlewareService(mockJWTRepository);

        // Initialize test variables
        validJwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2lkIjoiMTIzZTQ1NjctZTg5Yi0xMmQzLWE0NTYtNDI2NjE0MTc0MDAwIiwiaWF0IjoxNTYyMjY3OTg5fQ.X8ucVjfx8qMlTw0e3KDILUyyBYE4MCGHOsUzQ9TQLlE"; // Example valid JWT
        validUuid = "123e4567-e89b-12d3-a456-426614174000";
        invalidJwt = "invalid.jwt.token";
        mockToken = new JWToken(validJwt, validUuid, 1, LocalDateTime.now(), null, null);

        // Prepare the successful validation result
        decodedResult = new HashMap<>();
        decodedResult.put("uuid", validUuid);
        decodedResult.put("isAdmin", 1);
    }

    @Test
    public void testInvalidToken() {
        // Mock repository to return null
        when(mockJWTRepository.getTokenByUuid(anyString())).thenReturn(null);

        // Then it should throw an UnauthorizedException
        assertThrows(UnauthorizedException.class, () -> {
            middlewareService.checkJwt(invalidJwt);
        });
    }

    @Test
    public void testSuccessfulJwtCheck() {
        // Create mocks for DecodedJWT and Claim
        DecodedJWT decodedJWTMock = Mockito.mock(DecodedJWT.class);
        Claim uuidClaimMock = Mockito.mock(Claim.class);
        Claim isAdminClaimMock = Mockito.mock(Claim.class);

        // Set up the claims to return the expected values
        when(uuidClaimMock.asString()).thenReturn(validUuid);
        when(isAdminClaimMock.asInt()).thenReturn(1);

        // Set up the DecodedJWT to return the mocked claims
        when(decodedJWTMock.getClaim("uuid")).thenReturn(uuidClaimMock);
        when(decodedJWTMock.getClaim("isAdmin")).thenReturn(isAdminClaimMock);

        // Mock the JwtUtil to return the mocked DecodedJWT
        when(JwtUtil.verifyToken(validJwt)).thenReturn(decodedJWTMock); // Ensure this line is present

        // Mocking the ValidationUtil to return a successful result
        when(ValidationUtil.validateJwt(validJwt, mockJWTRepository)).thenReturn(decodedResult);
        when(mockJWTRepository.getTokenByUuid(validUuid)).thenReturn(mockToken);

        // When checking the token
        Map<String, Object> result = middlewareService.checkJwt(validJwt);
        
        // Print the result for debugging
        System.out.println("Result from checkJwt: " + result);

        // Then it should return the expected UUID and isAdmin status
        assertEquals(validUuid, result.get("uuid"));
        assertEquals(1, result.get("isAdmin"));

        // Verify repository methods were called
        verify(mockJWTRepository, times(1)).getTokenByUuid(validUuid);
        verify(mockJWTRepository, times(1)).updateToken(mockToken);
        assertEquals(LocalDateTime.now().getHour(), mockToken.getLastAccess().getHour()); // Verify lastAccess is updated
    }
}
