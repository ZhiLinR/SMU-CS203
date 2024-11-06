package middleware.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import middleware.model.User;

/**
 * Repository interface for managing {@link User} entities. This interface
 * extends
 * {@link JpaRepository}, providing a set of built-in methods for performing
 * CRUD operations on {@link User} objects in the database.
 *
 * In addition to the standard repository methods, this interface includes
 * methods
 * for executing stored procedures related to user management, such as inserting
 * new users, retrieving user profiles, and updating user details.
 *
 * The methods in this repository use the {@link Procedure} annotation to
 * specify
 * the corresponding stored procedures in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Retrieves the role of the user associated with the specified UUID.
     *
     * @param uuid the UUID of the user whose role is to be retrieved
     * @return a byte representing the user's role, or null if the user does not
     *         exist
     */
    @Procedure(name = "getRoleByUUID")
    Byte getRoleByUUID(@Param("p_uuid") String uuid);
}
