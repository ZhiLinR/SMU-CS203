package middleware.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entity class representing a JSON Web Token (JWT) in the database.
 *
 * <h3>Jakarta Bean ORM Mapping:</h3>
 * <ul>
 *   <li><strong>@Entity</strong>: Declares this class as a JPA entity.</li>
 *   <li><strong>@Table(name = "JWToken")</strong>: Maps this class to the "JWToken" database table.</li>
 *   <li><strong>@Id</strong>: Marks the primary key of the entity.</li>
 *   <li><strong>@Column</strong>: Maps each field to a corresponding column in the database.</li>
 * </ul>
 *
 * <p>
 * The {@code @Data} annotation from
 * <a href="https://projectlombok.org/features/Data" target="_blank">Lombok</a>
 * generates necessary boilerplate code such as getters, setters, and
 * {@code toString()} methods, simplifying development.
 * </p>
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
