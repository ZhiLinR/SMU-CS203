package user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import user.model.User;

/**
 * Repository interface for managing {@link User} entities. This interface
 * extends {@link JpaRepository}, providing a set of built-in methods for
 * performing CRUD operations on {@link User} objects in the database.
 *
 * In addition to the standard repository methods, this interface includes
 * methods for executing stored procedures related to user management, such as
 * inserting new users, retrieving user profiles, and updating user details.
 *
 * The methods in this repository use the {@link Procedure} annotation to
 * specify the corresponding stored procedures in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Inserts a new user into the database with the provided details.
     *
     * @param email    the email address of the new user
     * @param password the password of the new user
     * @param name     the name of the new user
     * @param isAdmin  a byte indicating whether the user is an administrator (1) or
     *                 not (0)
     * @return {@code 1} if the email format is valid; {@code 0} otherwise
     */
    @Procedure(procedureName = "InsertUser")
    Integer insertUser(
            @Param("p_email") String email,
            @Param("p_password") String password,
            @Param("p_name") String name,
            @Param("p_isAdmin") Byte isAdmin);

    /**
     * Retrieves the hashed password for the user associated with the specified
     * email.
     *
     * @param email the email address of the user whose hashed password is to be
     *              retrieved
     * @return the hashed password of the user, or null if the user does not exist
     */
    @Procedure(name = "GetHashedPassword")
    String getHashedPassword(@Param("p_email") String email);

    /**
     * Checks if the specified email exists in the database.
     *
     * @param email the email address to check
     * @return uuid of the user, or null if user does not exist
     */
    @Procedure(name = "CheckEmail")
    String checkEmail(@Param("p_email") String email);

    /**
     * Retrieves the name of the user associated with the specified UUID.
     *
     * @param uuid the UUID of the user whose name is to be retrieved
     * @return the name of the user, or null if the user does not exist
     */
    @Procedure(name = "GetName")
    String getName(@Param("p_uuid") String uuid);

    /**
     * Retrieves the profile of the user associated with the specified UUID.
     *
     * @param uuid the UUID of the user whose profile is to be retrieved
     * @return the {@link User} object representing the user's profile, or null if
     *         the user does not exist
     */
    @Procedure(name = "GetProfile")
    User getProfile(@Param("p_uuid") String uuid);

    /**
     * Retrieves the role of the user associated with the specified UUID.
     *
     * @param uuid the UUID of the user whose role is to be retrieved
     * @return a byte representing the user's role, or null if the user does not
     *         exist
     */
    @Procedure(name = "GetRoleByUUID")
    Byte getRoleByUUID(@Param("p_uuid") String uuid);

    /**
     * Updates the details of the user identified by the specified UUID.
     *
     * @param uuid     the UUID of the user to update
     * @param email    the new email address of the user
     * @param password the new password of the user
     * @param name     the new name of the user
     * @param isAdmin  a byte indicating whether the user should be an administrator
     *                 (1) or not (0)
     * @param dob      the date of birth of the user
     */
    @Procedure(name = "UpdateUser")
    void updateUser(
            @Param("p_UUID") String uuid,
            @Param("p_email") String email,
            @Param("p_password") String password,
            @Param("p_name") String name,
            @Param("p_isAdmin") Byte isAdmin,
            @Param("p_dob") java.sql.Date dob);

    /**
     * Updates the Elo rating for the user identified by the specified UUID.
     *
     * @param uuid the UUID of the user whose Elo rating is to be updated
     * @param elo  the new Elo rating for the user
     */
    @Procedure(name = "UpdateElo")
    void updateElo(
            @Param("p_uuid") String uuid,
            @Param("p_elo") int elo);
}
