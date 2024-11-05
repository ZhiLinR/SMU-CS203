package TournamentAdminService.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the composite key for the {@link Matchup} entity.
 * Consists of the tournament ID and the round number.
 */
@Getter
@Setter
@Embeddable
public class MatchupId implements Serializable {

    private String tournamentID;
    private Integer roundNum;

    public MatchupId() {}

    public MatchupId(String tournamentID, Integer roundNum) {
        this.tournamentID = tournamentID;
        this.roundNum = roundNum;
    }

    /**
     * Compares this {@link MatchupId} with another object for equality.
     * Two {@link MatchupId} objects are equal if their tournament ID and round number are the same.
     *
     * @param o the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchupId)) return false;
        MatchupId that = (MatchupId) o;
        return Objects.equals(tournamentID, that.tournamentID) &&
                Objects.equals(roundNum, that.roundNum);
    }

     /**
     * Returns a hash code for this {@link MatchupId}.
     *
     * @return a hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(tournamentID, roundNum);
    }
}
