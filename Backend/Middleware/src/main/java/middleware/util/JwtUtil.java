package middleware.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException; // Import for JwtException
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utility class for handling JSON Web Tokens (JWT).
 *
 * <p>This class provides methods for extracting claims, 
 * and validating JWTs. The tokens are signed using a secret key and 
 * contain information about the user, such as username, UUID, and admin status.</p>
 *
 * <p>Tokens are set to expire after a specified duration (24 hours in this case).</p>
 */
@Component
public class JwtUtil {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

        /**
     * Decrypts and validates a JWT token and extracts the claims.
     *
     * @param token the JWT token to decrypt and validate
     * @return the claims extracted from the JWT token
     * @throws JwtException if the token is invalid or expired
     */
    public Claims decryptToken(String token) {
        try {
            // Use the parser to validate and parse the JWT
            return Jwts.parser()
                    .setSigningKey(secretKey.getBytes()) // Use the secret key to validate the signature
                    .parseClaimsJws(token)
                    .getBody(); // Return the claims (UUID, username, isAdmin, etc.)
        } catch (SignatureException e) {
            // Throw an exception if the JWT is invalid, expired, or can't be decrypted
            throw new RuntimeException("Invalid or expired JWT token", e);
        }
    }
}

