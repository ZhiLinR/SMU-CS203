package middleware.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitpioneer.jupiter.SetEnvironmentVariable;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SetEnvironmentVariable(key = "security.jwt.secret-key", value = "jwt-secret-key")
public class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @Value("${security.jwt.secret-key}")
    private String secretKey; // Default for testing

    private String validToken;
    private String expiredToken;
    private String invalidSignatureToken;

    @BeforeEach
    public void setUp() {
        System.out.println(secretKey);

        // Create a valid token
        validToken = Jwts.builder()
                .setSubject("test-user")
                .claim("uuid", "12345")
                .claim("isAdmin", true)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        // Create an expired token
        expiredToken = Jwts.builder()
                .setSubject("test-user")
                .claim("uuid", "12345")
                .claim("isAdmin", true)
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60)) // 1 hour ago
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60)) // Expired 1 minute ago
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        // Create a token with an invalid signature
        invalidSignatureToken = Jwts.builder()
                .setSubject("test-user")
                .claim("uuid", "12345")
                .claim("isAdmin", true)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
                .signWith(SignatureAlgorithm.HS256, "wrong-secret-key") // Different secret
                .compact();
    }

    @Test
    public void testDecryptToken_ValidToken() {
        Claims claims = jwtUtil.decryptToken(validToken);
        assertEquals("12345", claims.get("uuid"));
        assertEquals(true, claims.get("isAdmin"));
    }

    @Test
    public void testDecryptToken_ExpiredToken() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            jwtUtil.decryptToken(expiredToken);
        });
        assertEquals("JWT token has expired", thrown.getMessage());
    }

    @Test
    public void testDecryptToken_InvalidSignature() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            jwtUtil.decryptToken(invalidSignatureToken);
        });
        assertEquals("Invalid JWT signature", thrown.getMessage());
    }

    @Test
    public void testDecryptToken_InvalidToken() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            jwtUtil.decryptToken("invalid.token.string");
        });
        assertEquals("Invalid JWT token", thrown.getMessage());
    }
}

