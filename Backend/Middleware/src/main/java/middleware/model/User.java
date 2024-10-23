package middleware.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

/**
 * Represents a user entity in the system, mapped to the "User" table in the database.
 * 
 * <h3>Jakarta Bean ORM Mapping:</h3>
 * <ul>
 *   <li><strong>@Entity</strong>: Marks this class as a JPA entity.</li>
 *   <li><strong>@Table(name = "User")</strong>: Specifies the corresponding database table.</li>
 *   <li><strong>@Id</strong>: Defines the primary key for this entity.</li>
 *   <li><strong>@Column</strong>: Maps class fields to database columns.</li>
 * </ul>
 * 
 * <p>
 * This entity stores user-specific data such as UUID, email, password, name, role, and 
 * other relevant information. The {@code @Data} annotation from 
 * <a href="https://projectlombok.org/features/Data" target="_blank">Lombok</a> generates 
 * boilerplate code like getters, setters, and {@code toString()} methods.
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

