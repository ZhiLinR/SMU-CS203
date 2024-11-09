package TournamentAdminService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Tournament")
public class Tournament {
    @Id
    @Column(name="tournamentID")
    private String tournamentID;

    @Column(name="name")
    private String name;

    @Column(name="startDate")
    private Date startDate;

    @Column(name="endDate")
    private Date endDate;

    @Column(name="location")
    private String location;

    @Column(name="playerLimit")
    private Integer playerLimit;

    @Column(name="status")
    private String status;

    @Column(name="descOID")
    private String descOID;

    /**
     * Determines the current status of the tournament based on the current date.
     * 
     * @return "Upcoming" if the tournament is in the future, "Ongoing" if it is currently active, or "Completed" if it has ended.
     */
    public String updateStatus() {
        Date now = new Date(System.currentTimeMillis());
        if (startDate == null || endDate == null) {
            return "Unknown";
        }
        if (now.before(startDate)) {
            return "Upcoming";
        } else if (now.after(endDate)) {
            return "Completed";
        } else {
            return "Ongoing";
        }
    }

    /**
     * Sets the start date of the tournament and updates the status accordingly.
     *
     * @param startDate the start date to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate != null ? startDate : new Date(System.currentTimeMillis());
        this.status = getStatus();
    }
    /**
     * Sets the end date of the tournament and updates the status accordingly.
     *
     * @param endDate the end date to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate != null ? endDate : new Date(System.currentTimeMillis() + 86400000);
        this.status = getStatus();
    }
}