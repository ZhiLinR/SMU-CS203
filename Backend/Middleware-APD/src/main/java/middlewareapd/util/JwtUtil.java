package middlewareapd.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import middlewareapd.exception.UnauthorizedException;

import java.util.Date;

/**
 * JwtUtil is a utility class that provides methods for creating and verifying JSON Web Tokens (JWTs).
 * It handles the generation of tokens and their validation using a secret key.
 */
public class JwtUtil {

    // Secret key for signing the JWT
    private static final String SECRET_KEY = "your_secret_key_here";

    // Expiration time in minutes
    private static final int EXPIRATION_MINUTES = 10;

    // Expiration time for the token in milliseconds
    private static final long EXPIRATION_TIME = EXPIRATION_MINUTES * 60 * 1000;

    /**
     * Generates a JWT token for the given email, UUID, and admin status.
     *
     * @param email   the email address to be used as the subject of the token
     * @param uuid    a unique identifier for the user
     * @param isAdmin an integer indicating whether the user has admin privileges (0 or 1)
     * @return the generated JWT token as a String
     */
    public static String generateToken(String email, String uuid, int isAdmin) {
        // Define the signing algorithm using the secret key
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        // Create the token
        String token = JWT.create()
                .withSubject(email) // Set the email as the subject (username)
                .withClaim("uuid", uuid) // Add UUID to the token
                .withClaim("isAdmin", isAdmin) // Add isAdmin to the token
                .withIssuedAt(new Date()) // Set issued time (now)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Set expiration time
                .sign(algorithm); // Sign the token with the secret key

        return token;
    }

    /**
     * Verifies and decodes the given JWT token.
     *
     * @param token the JWT token to be verified
     * @return a DecodedJWT instance containing the decoded token information
     * @throws UnauthorizedException if the token is invalid or expired
     */
    public static DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        try {
            // Verify the token
            return JWT.require(algorithm).build().verify(token);
        } catch (JWTVerificationException e) {
            // Handle signature verification failure, token expiration, etc.
            throw new UnauthorizedException("Invalid JWT or session expired: " + e.getMessage());
        }
    }
}
