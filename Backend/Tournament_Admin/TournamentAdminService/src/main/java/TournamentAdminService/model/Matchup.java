package TournamentAdminService.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a matchup between two players in a tournament.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Matchups")
public class Matchup {
    /**
     * The composite ID consisting of the tournament ID and round number.
     */
    @EmbeddedId
    @Column(name = "tournamentID", nullable = false)
    private MatchupId tournamentID;
    @Column(name = "player1")

    /**
     * The ID or name of the first and second player in the matchup.
     */
    private String player1;
    @Column(name = "player2")
    private String player2;
    @Column(name = "playerWon")
    private String playerWon;
    @Column(name = "roundNum", insertable=false, updatable=false)
    private Integer roundNum;
}
