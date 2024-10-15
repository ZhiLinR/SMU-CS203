package user.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utility class for handling JSON Web Tokens (JWT).
 *
 * <p>This class provides methods for generating, extracting claims, 
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
     * Generates a JWT token for the specified username, UUID, and admin status.
     *
     * @param username the username to include in the token (email)
     * @param uuid the UUID to include as a claim in the token
     * @param isAdmin the admin status to include as a claim in the token
     * @return a JWT token as a String
     */
    public String generateToken(String username, String uuid, Byte isAdmin) {
        return Jwts.builder()
                .setSubject(username)
                .claim("uuid", uuid)
                .claim("isAdmin", isAdmin)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Token valid for 24 hours
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}

