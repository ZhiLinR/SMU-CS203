package user.dto;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for updating user information.
 *
 * <p>
 * This class encapsulates the data required to update user details such as
 * UUID, email, password, name, admin status, and date of birth (DOB).
 *
 * <p>
 * It includes getter and setter methods to access and modify these fields.
 * The `dob` field uses the `LocalDate` class for representing the user's
 * date of birth in ISO format.
 */
@Data
public class UserUpdateRequest {

    private String uuid;
    private String email;
    private String password;
    private String name;
    private Byte isAdmin;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;
}
