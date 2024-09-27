package TournamentAdminService.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MatchupId implements Serializable {

    private String tournamentID;
    private Integer roundNum;

    public MatchupId() {}

    public MatchupId(String tournamentID, Integer roundNum) {
        this.tournamentID = tournamentID;
        this.roundNum = roundNum;
    }

    // Getters and setters

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchupId)) return false;
        MatchupId that = (MatchupId) o;
        return Objects.equals(tournamentID, that.tournamentID) &&
                Objects.equals(roundNum, that.roundNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tournamentID, roundNum);
    }
}
