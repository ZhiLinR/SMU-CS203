package matchmaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import matchmaking.model.Signups;

/**
 * Repository interface for managing {@link Signups} entities. This interface extends
 * {@link JpaRepository}, providing a set of built-in methods for performing
 * CRUD operations on {@link Signups} objects in the database.
 *
 * In addition to the standard repository methods, this interface includes methods
 * for executing stored procedures related to user management, such as inserting
 * new users, retrieving user profiles, and updating user details.
 *
 * The methods in this repository use the {@link Procedure} annotation to specify
 * the corresponding stored procedures in the database.
 */
@Repository
public interface SignupsRepository extends JpaRepository<Signups, String> {

    /**
     * Inserts a new user into the database with the provided details.
     *
     * @param email the email address of the new user
     * @param password the password of the new user
     * @param name the name of the new user
     * @param isAdmin a byte indicating whether the user is an administrator (1) or not (0)
     * @return {@code 1} if the email format is valid; {@code 0} otherwise
     */
    @Procedure(procedureName = "GetSignupsByTournamentId")
    List<Signups> getSignupsByTournamentId(
        @Param("p_tournamentId") String tournamentId
    );
}
