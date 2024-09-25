package user.dto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import user.model.User;

public class ProfileResponse {

    private String UUID;
    private String email;
    private String name;
    private LocalDate dob; // Use LocalDate for date of birth
    private String elo;

    // Constructor to initialize ProfileResponse from a User entity
    public ProfileResponse(User user) {
        this.UUID = user.getUUID();
        this.email = user.getEmail();
        this.name = user.getName();
        this.dob = convertToLocalDate(user.getDob()); // Conversion from Date to LocalDate
        this.elo = user.getElo();
    }

    // Method to convert java.util.Date to java.time.LocalDate
    private LocalDate convertToLocalDate(Date dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }


    // Getters and Setters
    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getElo() {
        return elo;
    }

    public void setElo(String elo) {
        this.elo = elo;
    }

    @Override
    public String toString() {
        return "ProfileResponse{" +
                "UUID='" + UUID + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", elo='" + elo + '\'' +
                '}';
    }
}

