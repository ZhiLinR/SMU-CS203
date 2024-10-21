package user.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

/**
 * Represents a user entity in the system.
 * This entity is mapped to the "User" table in the database.
 *
 * <p>
 * The class contains fields corresponding to user-specific data
 * such as email, password, name, etc., which are persisted in
 * the database.
 * </p>
 */
@Entity
@Table(name = "User")
@Data
public class User {
    @Id
    @Column(name = "UUID")
    private String uuid;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "isAdmin")  // Ensure this matches your database column name
    private Byte isAdmin;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "elo")
    private String elo;
}
