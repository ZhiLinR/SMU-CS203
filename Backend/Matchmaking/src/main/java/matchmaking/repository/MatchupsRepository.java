package matchmaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import matchmaking.dto.PlayerWins;
import matchmaking.model.Matchups;

/**
 * Repository interface for managing {@link Matchups} entities.
 * This interface extends {@link JpaRepository}, providing a set of built-in
 * methods for performing CRUD operations on {@link Matchups} objects in the
 * database.
 *
 * <p>
 * In addition to the standard repository methods, this interface includes
 * methods for executing stored procedures related to matchups management,
 * such as retrieving the current round, player wins, and matchups for a
 * specific tournament.
 * </p>
 * <p>
 * The methods in this repository use the {@link Procedure} annotation to
 * specify the corresponding stored procedures in the database.
 * </p>
 */
@Repository
public interface MatchupsRepository extends JpaRepository<Matchups, String> {

        /**
         * Retrieves the current round number for the specified tournament.
         *
         * This method calls the stored procedure "GetRoundNumByTournamentId" to
         * fetch the current round associated with the given tournament ID.
         *
         * @param tournamentId the ID of the tournament for which the current round is
         *                     requested.
         * @return the current round number for the tournament, or {@code null} if not
         *         found.
         */
        @Procedure(procedureName = "GetRoundNumByTournamentId")
        Integer getCurrentRoundByTournamentId(
                        @Param("p_tournamentId") String tournamentId);

        /**
         * Retrieves the list of player wins for a specified tournament.
         *
         * This method calls the stored procedure "GetPlayerWinsByTournamentId" to
         * obtain a list of players and their corresponding win counts in the specified
         * tournament.
         *
         * @param tournamentId the ID of the tournament for which player wins are
         *                     requested.
         * @return a list of {@link PlayerWins} objects containing player win
         *         information.
         */
        @Procedure(procedureName = "GetPlayerWinsByTournamentId")
        List<Object[]> getPlayerWinsByTournamentId(
                        @Param("p_tournamentId") String tournamentId);

        /**
         * Retrieves a list of matchups for a specified tournament.
         *
         * This method calls the stored procedure "GetMatchupsByTournamentId" to
         * fetch the matchups associated with the given tournament ID.
         *
         * @param tournamentId the ID of the tournament for which matchups are
         *                     requested.
         * @return a list of {@link Matchups} objects representing the matchups for the
         *         tournament.
         */
        @Procedure(procedureName = "GetMatchupsByTournamentId")
        List<Matchups> getMatchupsByTournamentId(
                        @Param("p_tournamentId") String tournamentId);

        /**
         * Inserts a new matchup into the database.
         *
         * This method calls the stored procedure "InsertMatchup" to create a new
         * matchup entry with the specified players, winner, tournament ID, and round
         * number.
         *
         * @param player1      the ID of the first player in the matchup.
         * @param player2      the ID of the second player in the matchup.
         * @param playerWon    the ID of the player who won the matchup.
         * @param tournamentId the ID of the tournament associated with the matchup.
         * @param roundNum     the round number in which the matchup occurs.
         */
        @Procedure(procedureName = "InsertMatchup")
        void insertMatchup(
                        @Param("player1") String player1,
                        @Param("player2") String player2,
                        @Param("playerWon") String playerWon,
                        @Param("tournamentId") String tournamentId,
                        @Param("roundNum") int roundNum);
}
