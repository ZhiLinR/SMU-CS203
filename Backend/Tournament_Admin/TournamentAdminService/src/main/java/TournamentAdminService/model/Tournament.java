package TournamentAdminService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private Date startDate;
    @Column(name="endDate")
    private Date endDate;
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

    public String getStatus() {
        Date now = new Date();
        if (now.before(startDate)) {
            return "Upcoming";
        } else if (now.after(endDate)) {
            return "Completed";
        } else {
            return "Ongoing";
        }
    }
}