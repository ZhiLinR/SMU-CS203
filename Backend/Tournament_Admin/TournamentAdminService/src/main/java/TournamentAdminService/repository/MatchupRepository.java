package TournamentAdminService.repository;

import TournamentAdminService.model.Matchup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Repository interface for performing database operations related to matchups.
 */
public interface MatchupRepository extends JpaRepository<Matchup, Long> {

    /**
     * Updates the game result for a specific player in a tournament.
     * 
     * @param playerWon   the player who won the game
     * @param tournamentId the ID of the tournament
     * @param roundNum    the round number of the matchup
     */
    @Procedure(name = "UpdateGameResult")
    void updateGameResult(@Param("p_playerWon") String playerWon,
                          @Param("p_tournamentId") String tournamentId,
                          @Param("p_roundNum") Integer roundNum);
    
    /**
     * Deletes a game result for a specific player in a tournament.
     * 
     * @param player the player whose result is being deleted
     * @param tournamentId the ID of the tournament
     * @param roundNum    the round number of the matchup
     */
    @Procedure(name = "DeleteGameResult")
    void deleteGameResult(@Param("p_player") String player,
                          @Param("p_tournamentId") String tournamentId,
                          @Param("p_roundNum") Integer roundNum);


    /**
     * Retrieves all game results for a specific tournament.
     * 
     * @param tournamentId the ID of the tournament
     * @return a list of matchups containing the game results
     */
    @Procedure(name="GetGameResultsByTournamentId")
    List<Matchup> getGameResultsByTournamentId(@Param("p_tournamentId") String tournamentId);


    /**
     * Retrieves all participants for a specific tournament.
     * 
     * @param tournamentId the ID of the tournament
     * @return a list of participant names
     */
    @Procedure(name="GetParticipantsByTournamentId")
    List<String> getParticipantsByTournamentId(@Param("p_tournamentId") String tournamentId);
}
