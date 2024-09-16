import java.time.LocalDate;

public class ProfileResponse {

    private String UUID;
    private String email;
    private String name;
    private LocalDate dob;
    private String elo;

    // Constructor to initialize ProfileResponse from a User entity
    public ProfileResponse(User user) {
        this.UUID = user.getUUID();
        this.email = user.getEmail();
        this.name = user.getName();
        this.dob = user.getDob();
        this.elo = user.getElo();
    }

    // Default constructor
    public ProfileResponse() {
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

