package user.dto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import lombok.Data;
import user.model.User;

/**
 * Data Transfer Object (DTO) for sending user profile information in responses.
 *
 * <p>This class encapsulates the user's profile data, including email, name,
 * date of birth (as {@link LocalDate}), and ELO rating.
 *
 * <p>It provides a constructor to initialize the profile response using a {@link User} entity
 * and includes getter and setter methods for each field.
 */
@Data
public class ProfileResponse {

    private String email;
    private String name;
    private LocalDate dob;
    private String elo;


    /**
     * Constructor to initialize ProfileResponse from a User entity.
     *
     * @param user the {@link User} entity from which the profile data is extracted
     */
    public ProfileResponse(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.dob = convertToLocalDate(user.getDob()); // Conversion from Date to LocalDate
        this.elo = user.getElo();
    }

    /**
     * Converts a {@link java.util.Date} to {@link java.time.LocalDate}.
     *
     * @param dateToConvert the {@link Date} object to be converted
     * @return the equivalent {@link LocalDate} object, or null if the input date is null
     */
    private LocalDate convertToLocalDate(Date dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
