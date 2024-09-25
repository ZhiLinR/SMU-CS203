package user.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

/**
 * Entity class representing a JSON Web Token (JWT) in the database.
 *
 * <p>This class maps to the "JWToken" table and encapsulates the details of a 
 * JWT, including the token itself, associated user UUID, last login time, 
 * and logout time. It serves as a model for managing JWT information 
 * within the application.
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

