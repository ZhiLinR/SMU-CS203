package TournamentAdminService.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Matchups")
public class Matchup {
    @EmbeddedId
    @Column(name = "tournamentID", nullable = false)
    private MatchupId tournamentID;
    @Column(name = "player1")
    private String player1;
    @Column(name = "player2")
    private String player2;
    @Column(name = "playerWon")
    private String playerWon;
    @Column(name = "roundNum", insertable=false, updatable=false)
    private Integer roundNum;
}
