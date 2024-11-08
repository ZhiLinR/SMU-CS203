package matchmaking.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

/**
 * Represents the composite primary key for the {@code Matchups} entity.
 * This class is marked as {@code Embeddable}, meaning it can be used as a
 * composite key
 * within an entity class. It contains the players and the tournament ID as key
 * components.
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

    /**
     * Compares this {@code MatchupsId} object to another for equality.
     * Two {@code MatchupsId} objects are considered equal if their player UUIDs and
     * tournament ID are identical.
     *
     * @param o the object to compare with this {@code MatchupsId}.
     * @return {@code true} if the given object is equal to this one; {@code false}
     *         otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatchupsId)) {
            return false;
        }
        MatchupsId that = (MatchupsId) o;
        return player1.equals(that.player1) &&
                player2.equals(that.player2) &&
                tournamentId.equals(that.tournamentId);
    }

    /**
     * Generates a hash code for this {@code MatchupsId} object.
     * The hash code is computed based on the UUIDs of both players and the
     * tournament ID.
     *
     * @return the hash code for this {@code MatchupsId}.
     */
    @Override
    public int hashCode() {
        return Objects.hash(player1, player2, tournamentId);
    }
}
