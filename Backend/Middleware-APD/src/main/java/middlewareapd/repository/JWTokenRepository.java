package middlewareapd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import middlewareapd.model.JWToken;

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
     * Checks the if JWT exists and is valid.
     *
     * @param jwt the JWToken given to check
     */
    @Procedure(name = "CheckValidity")
    JWToken checkValidity(@Param("jwtValue") String jwt);
}
