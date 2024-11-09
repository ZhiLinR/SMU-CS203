package matchmaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import matchmaking.model.Results;

/**
 * Repository interface for managing {@link Results} entities.
 * This interface extends {@link JpaRepository}, providing a set of built-in
 * methods for performing CRUD operations on {@link Results} objects in the
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
public interface ResultsRepository extends JpaRepository<Results, String> {

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
    @Procedure(procedureName = "GetTournamentResult")
    List<Results> getTournamentResults(
            @Param("p_tournamentId") String tournamentId);

    @Procedure(procedureName = "InsertTournamentResult")
    void insertTournamentResult(
            @Param("p_uuid") String uuid,
            @Param("p_tournamentId") String tournamentId,
            @Param("p_ranking") Integer ranking);
}
