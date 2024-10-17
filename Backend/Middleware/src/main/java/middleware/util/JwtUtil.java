package middleware.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException; // Import for JwtException
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    private String secretKey; // Default for testing

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
                    .setSigningKey(secretKey) // Use the secret key to validate the signature
                    .parseClaimsJws(token)
                    .getBody(); // Return the claims (UUID, username, isAdmin, etc.)
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("JWT token has expired", e);
        } catch (UnsupportedJwtException e) {
            throw new UnauthorizedException("JWT token is unsupported", e);
        } catch (SignatureException e) {
            throw new UnauthorizedException("Invalid JWT signature", e);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid JWT token", e);
        }
    }
}

