package middlewareapd.model; // Ensure this matches your directory structure

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Represents a JWT (JSON Web Token) with associated metadata.
 * This class contains information about the token, user session details, and access times.
 */
@Data
public class JWToken {
    private String jwt;               // The JWT string
    private String uuid;              // Unique identifier for the user
    private int isAdmin;             // User's admin status (0 for false, 1 for true)
    private LocalDateTime lastLogin;  // Timestamp of the last login
    private LocalDateTime logout;     // Timestamp of the logout
    private LocalDateTime lastAccess; // Timestamp of the last access

    /**
     * Constructor to initialize a JWToken object with all fields.
     *
     * @param jwt        the JWT string.
     * @param uuid       the unique identifier for the user.
     * @param isAdmin    the user's admin status (0 for false, 1 for true).
     * @param lastLogin  the timestamp of the last login.
     * @param logout     the timestamp of the logout.
     * @param lastAccess the timestamp of the last access.
     */
    public JWToken(String jwt, String uuid, int isAdmin, LocalDateTime lastLogin, LocalDateTime logout, LocalDateTime lastAccess) {
        this.jwt = jwt;
        this.uuid = uuid;
        this.isAdmin = isAdmin;
        this.lastLogin = lastLogin;
        this.logout = logout;
        this.lastAccess = lastAccess;
    }
}
