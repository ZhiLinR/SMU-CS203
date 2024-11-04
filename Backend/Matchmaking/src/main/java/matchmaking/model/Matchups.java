package matchmaking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Represents a matchups entity in the system.
 * This entity is mapped to the "Matchups" table in the database.
 *
 * <p>
 * The class contains fields corresponding to user-specific data
 * such as email, password, name, etc., which are persisted in
 * the database.
 * </p>
 */
@Entity
@Table(name = "Matchups")
@Data
@Accessors(chain = true)
public class Matchups {

    /**
     * The composite primary key for this matchup, containing the player UUIDs
     * and the tournament ID.
     */
    @EmbeddedId
    private MatchupsId id;

    /**
     * The UUID of the player who won the matchup.
     */
    @Column(name = "playerWon")
    private String playerWon;

    /**
     * The round number in which this matchup occurs.
     */
    @Column(name = "roundNum")
    private int roundNum;
}
