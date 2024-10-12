package middlewareapd.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import middlewareapd.exception.UnauthorizedException;

import java.util.Date;

public class JwtUtil {

    // Secret key for signing the JWT
    private static final String SECRET_KEY = "your_secret_key_here";
    
    // Expiration time for the token (e.g., 10 minutes in milliseconds)
    private static final long EXPIRATION_TIME = 600_000;

    // Method to create a JWT token
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

    // Method to verify and decode a JWT token
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
