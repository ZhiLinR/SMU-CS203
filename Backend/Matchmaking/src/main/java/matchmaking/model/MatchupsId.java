package matchmaking.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

/**
 * Represents the composite primary key for the {@code Matchups} entity.
 * This class is marked as {@code Embeddable}, meaning it can be used as a
 * composite key within an entity class. It contains the players and the
 * tournament ID as key components.
 */
@Data
@Embeddable
@Accessors(chain = true)
public class MatchupsId implements Serializable {

    /**
     * The UUID of the first player in the matchup.
     */
    private String player1;

    /**
     * The UUID of the second player in the matchup.
     */
    private String player2;

    /**
     * The ID of the tournament in which this matchup occurs.
     */
    private String tournamentId;
}
