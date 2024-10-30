package matchmaking.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Data
@Embeddable
public class MatchupsId implements Serializable {
    private String player1;
    private String player2;
    private String tournamentId;

    // Implement equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MatchupsId))
            return false;
        MatchupsId that = (MatchupsId) o;
        return player1.equals(that.player1) &&
                player2.equals(that.player2) &&
                tournamentId.equals(that.tournamentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player1, player2, tournamentId);
    }
}
