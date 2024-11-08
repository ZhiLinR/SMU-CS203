package user.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for handling user profile information.
 *
 * <p>
 * This class encapsulates the data required for creating or updating a user
 * profile,
 * including the user's email, password, name, and admin status.
 *
 * <p>
 * Provides getter and setter methods to access and modify these fields.
 */
@Data
public class ProfileRequest {
    private String email;
    private String password;
    private String name;
    private Byte isAdmin;
}
