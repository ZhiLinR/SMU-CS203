package matchmaking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

/**
 * Represents a tournament entity in the system.
 * This entity is mapped to the "Tournament" table in the database.
 *
 * <p>
 * The class contains fields corresponding to tournament-specific data
 * such as tournamentID, start and end date, location, etc., which are persisted
 * in the database.
 * </p>
 */
@Entity
@Table(name = "Tournament")
@Data
public class Tournament {
    @Id
    @Column(name = "tournamentID")
    private String tournamentId;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    @Column(name = "location")
    private String location;

    @Column(name = "playerLimit")
    private int playerLimit;

    @Column(name = "status")
    private String status;

    @Column(name = "descOID")
    private String descOID;

    @Column(name = "name")
    private String name;
}
