package matchmaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import matchmaking.model.Signups;

/**
 * Repository interface for managing {@link Signups} entities.
 * This interface extends {@link JpaRepository}, providing a set of built-in
 * methods for performing CRUD operations on {@link Signups} objects in the
 * database.
 *
 * <p>
 * In addition to the standard repository methods, this interface includes
 * methods for executing stored procedures related to signups management,
 * such as retrieving signups for a specific tournament.
 * </p>
 * <p>
 * The methods in this repository use the {@link Procedure} annotation to
 * specify the corresponding stored procedures in the database.
 * </p>
 */
@Repository
public interface SignupsRepository extends JpaRepository<Signups, String> {

        /**
         * Retrieves a list of signups for a specified tournament.
         *
         * This method calls the stored procedure "GetSignupsByTournamentId" to
         * fetch all signups associated with the given tournament ID.
         *
         * @param tournamentId the ID of the tournament for which signups are requested.
         * @return a list of {@link Signups} objects representing the users who signed
         *         up for the tournament.
         */
        @Procedure(procedureName = "GetSignupsByTournamentId")
        List<Signups> getSignupsByTournamentId(
                        @Param("p_tournamentId") String tournamentId);

        /**
         * Calls the stored procedure `UpdateElo` to update the Elo rating of a player.
         *
         * <p>
         * This method executes a stored procedure in the database that updates the
         * Elo rating for a player identified by their unique UUID. The procedure
         * expects the player's UUID and the new Elo rating as parameters.
         * </p>
         *
         * @param uuid the unique identifier (UUID) of the player whose Elo rating is to
         *             be updated.
         * @param elo  the new Elo rating to be set for the player.
         */
        @Procedure(procedureName = "UpdateElo")
        void updateElo(
                        @Param("p_uuid") String uuid,
                        @Param("p_elo") Integer elo);
}
