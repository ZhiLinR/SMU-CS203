package user.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entity class representing a JSON Web Token (JWT) in the database.
 *
 * <p>
 * This class maps to the "JWToken" table and encapsulates the details of a
 * JWT, including the token itself, associated user UUID, last login time,
 * and logout time. It serves as a model for managing JWT information
 * within the application.
 */
@Entity
@Table(name = "JWToken")
@Data
public class JWToken {
    @Id
    @Column(name = "jwt")
    private String jwt;

    @Column(name = "UUID")
    private String uuid;

    @Column(name = "lastLogin")
    private LocalDateTime lastLogin;

    @Column(name = "logout")
    private LocalDateTime logout;
}
