package user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import user.model.JWToken;

/**
 * Repository interface for performing CRUD operations on {@link JWToken} entities.
 * This interface extends {@link JpaRepository}, providing built-in methods for
 * managing {@link JWToken} objects in the database.
 *
 * Additionally, this repository includes methods for executing stored procedures
 * related to JSON Web Token (JWT) management, such as updating the last login,
 * logging out, and checking JWT validity.
 *
 * The methods in this repository use the {@link Procedure} annotation to specify
 * the corresponding stored procedures in the database.
 */
@Repository
public interface JWTokenRepository extends JpaRepository<JWToken, String> {

    /**
     * Updates the last login timestamp and JWT associated with the specified email.
     *
     * @param email the email address of the user
     * @param jwt the JSON Web Token associated with the user
     */
    @Procedure(name = "UpdateJWTLastLogin")
    void updateJWTLastLogin(
        @Param("p_email") String email,
        @Param("p_jwt") String jwt
    );

    /**
     * Updates the logout information for the user identified by the specified UUID.
     *
     * @param uuid the UUID of the user to log out
     * @return the number of rows affected by the update operation
     */
    @Procedure(name = "UpdateLogout")
    Integer updateLogout(@Param("p_uuid") String uuid);

    /**
     * Checks the if valid JWT exists for the user identified by the specified UUID.
     *
     * @param uuid the UUID of the user whose JWT is to be checked
     */
    @Procedure(name = "CheckJWT")
    void checkJWT(@Param("p_uuid") String uuid);
}
