package middleware.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import middleware.model.JWToken;

/**
 * Repository interface for performing CRUD operations on {@link JWToken}
 * entities. This interface extends {@link JpaRepository}, providing built-in
 * methods for managing {@link JWToken} objects in the database.
 *
 * <p>
 * Additionally, this repository includes methods for executing stored
 * procedures related to JSON Web Token (JWT) management, such as updating the
 * last login, logging out, and checking JWT validity.
 *
 * <p>
 * The methods in this repository use the {@link Procedure} annotation to
 * specify the corresponding stored procedures in the database.
 */
@Repository
public interface JWTokenRepository extends JpaRepository<JWToken, String> {

    /**
     * Checks the if JWT exists in the database.
     *
     * @param jwt the JWToken given to check
     */
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Procedure(name = "CheckValidity")
    JWToken checkValidity(@Param("jwtValue") String jwt);
}
