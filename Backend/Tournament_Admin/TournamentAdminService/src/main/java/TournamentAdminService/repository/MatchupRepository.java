package TournamentAdminService.repository;

import TournamentAdminService.model.Matchup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchupRepository extends JpaRepository<Matchup, Long> {

    @Procedure(name = "UpdateGameResult")
    void updateGameResult(@Param("p_playerWon") String playerWon,
                          @Param("p_tournamentId") String tournamentId,
                          @Param("p_roundNum") Integer roundNum);

    @Procedure(name = "DeleteGameResult")
    void deleteGameResult(@Param("p_player") String player,
                          @Param("p_tournamentId") String tournamentId,
                          @Param("p_roundNum") Integer roundNum);

    @Procedure(name="GetGameResultsByTournamentId")
    List<Matchup> getGameResultsByTournamentId(@Param("p_tournamentId") String tournamentId);

    @Procedure(name="GetParticipantsByTournamentId")
    List<String> getParticipantsByTournamentId(@Param("p_tournamentId") String tournamentId);
}
