package matchmaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import matchmaking.model.PlayerWins;
import matchmaking.model.Matchups;

/**
 * Repository interface for managing {@link Matchups} entities. This interface extends
 * {@link JpaRepository}, providing a set of built-in methods for performing
 * CRUD operations on {@link Matchups} objects in the database.
 *
 * In addition to the standard repository methods, this interface includes methods
 * for executing stored procedures related to user management, such as inserting
 * new users, retrieving user profiles, and updating user details.
 *
 * The methods in this repository use the {@link Procedure} annotation to specify
 * the corresponding stored procedures in the database.
 */
@Repository
public interface MatchupsRepository extends JpaRepository<Matchups, String> {

    /**
     * Inserts a new user into the database with the provided details.
     *
     * @param email the email address of the new user
     * @param password the password of the new user
     * @param name the name of the new user
     * @param isAdmin a byte indicating whether the user is an administrator (1) or not (0)
     * @return {@code 1} if the email format is valid; {@code 0} otherwise
     */
    @Procedure(procedureName = "GetCurrentRoundByTournamentId")
    Integer getCurrentRoundByTournamentId(
        @Param("p_tournamentId") String tournamentId
    );

    @Procedure(procedureName = "GetPlayerWinsByTournamentId")
    List<PlayerWins> getPlayerWinsByTournamentId(
        @Param("p_tournamentId") String tournamentId
    );

    // TODO: create and add to server
    @Procedure(procedureName = "GetMatchupsByTournamentId")
    List<Matchups> getMatchupsByTournamentId(
        @Param("p_tournamentId") String tournamentId
    );

    @Procedure(procedureName = "InsertMatchup")
    void insertMatchup(
        @Param("player1") String player1, 
        @Param("player2") String player2, 
        @Param("playerWon") String playerWon, 
        @Param("tournamentId") String tournamentId, 
        @Param("roundNum") int roundNum
    );
}
