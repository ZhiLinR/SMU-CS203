package TournamentAdminService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Tournament")
public class Tournament {
    @Id
    @Column(name="tournamentID")
    private String tournamentID;
    @Column(name="startDate")
    private LocalDate startDate;
    @Column(name="endDate")
    private LocalDate endDate;
    @Column(name="location")
    private String location;
    @Column(name="playerLimit")
    private Integer playerLimit;
    @Column(name="isActive")
    private Boolean isActive;
    @Column(name="descOID")
    private String descOID;
    @Column(name="name")
    private String name;

    /**
     * Determines the current status of the tournament based on the current date.
     * 
     * @return "Upcoming" if the tournament is in the future, "Ongoing" if it is currently active, or "Completed" if it has ended.
     */
    public String getStatus() {
            LocalDate now = LocalDate.now();
        if (now.isBefore(startDate)) {
            return "Upcoming";
        } else if (now.isAfter(endDate)) {
            return "Completed";
        } else {
            return "Ongoing";
        }
    }
}