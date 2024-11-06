package middleware.util;

import middleware.exception.UnauthorizedException;
import middleware.model.*;
import middleware.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * TokenValidationService provides services for validating JWT tokens and
 * retrieving user roles.
 * It interacts with the {@link JWTokenRepository} and {@link UserRepository} to
 * verify
 * the presence and validity of JWTs and to check user roles.
 *
 * <p>
 * This class is designed to handle database transactions through methods
 * annotated with {@code @Transactional} to ensure proper transaction
 * management.
 * Each method is configured to start a new transaction whenever invoked.
 * </p>
 */
@Component
public class TokenValidationService {

    @Autowired
    private JWTokenRepository jwTokenRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Synchronously checks the validity of the JWT by querying the database.
     * This method is annotated with {@code @Transactional} with propagation set to
     * {@code REQUIRES_NEW} to ensure a new transaction is started each time this
     * method is called.
     *
     * <p>
     * It retrieves the JWT from the database, validates its session state, and
     * returns
     * the {@link JWToken} object if the token is valid.
     * </p>
     *
     * @param jwt the JWT token to validate.
     * @return the {@link JWToken} object if the token is valid.
     * @throws UnauthorizedException if the JWT is invalid.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public JWToken checkValiditySync(String jwt) {
        // Validate the JWT by checking its presence and validity in the database
        JWToken dbJwt = jwTokenRepository.checkValidity(jwt); // Apply necessary locking if required
        ValidationUtil.validateJwtSession(dbJwt);
        return dbJwt;
    }

    /**
     * Synchronously retrieves the user's role based on their UUID.
     * This method is annotated with {@code @Transactional} with propagation set to
     * {@code REQUIRES_NEW} to ensure a new transaction is started for each
     * invocation.
     *
     * <p>
     * It queries the database to retrieve the user's role by their UUID and returns
     * the role as a byte value.
     * </p>
     *
     * @param uuid the user's UUID.
     * @return the user's role as a {@link Byte} value.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Byte getUserRoleSync(String uuid) {
        // Retrieve and check the user's role from the database
        return userRepository.getRoleByUUID(uuid); // Apply necessary locking if required
    }
}
