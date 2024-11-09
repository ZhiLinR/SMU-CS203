package middleware.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

import middleware.exception.UnauthorizedException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class for handling JSON Web Tokens (JWT).
 *
 * <p>
 * This class provides methods for extracting claims,
 * and validating JWTs. The tokens are signed using a secret key and
 * contain information about the user, such as username, UUID, and admin status.
 * </p>
 *
 * <p>
 * Tokens are set to expire after a specified duration (24 hours in this case).
 * </p>
 */
@Component
public class JwtUtil {

    @Value("${security.jwt.secret-key}")
    private String secretKey; // Default for testing

    /**
     * Decrypts and validates a JWT token and extracts the claims.
     *
     * @param token the JWT token to decrypt and validate.
     * @return the claims extracted from the JWT token.
     * @throws UnauthorizedException if the token is invalid, expired, or has an
     *                               invalid signature.
     */
    public Claims decryptToken(String token) throws UnauthorizedException {
        ValidationUtil.validateNotEmpty(token, "JWT");
        try {
            // Use the parser to validate and parse the JWT
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey) // Use the secret key to validate the signature
                    .parseClaimsJws(token)
                    .getBody(); // Return the claims (UUID, username, isAdmin, etc.)

            return claims;
        } catch (ExpiredJwtException e) {
            // Print the error message for debugging
            throw new UnauthorizedException("JWT token has expired", e);
        } catch (SignatureException e) {
            // Print the error message for debugging
            throw new UnauthorizedException("Invalid JWT signature", e);
        } catch (Exception e) {
            // Print the error message for debugging
            e.printStackTrace(); // Print stack trace for additional context
            throw new UnauthorizedException("Invalid JWT token", e);
        }
    }
}
