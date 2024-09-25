package user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import user.model.JWToken;

    @Repository
public interface JWTokenRepository extends JpaRepository<JWToken, String> {
    
    @Procedure(name = "UpdateJWTLastLogin")
    void updateJWTLastLogin(
        @Param("p_email") String email, 
        @Param("p_jwt") String jwt
    );

    @Procedure(name = "UpdateLogout")
    Integer updateLogout(@Param("p_uuid") String uuid);

    @Procedure(name = "CheckJWT")
    void checkJWT(@Param("p_uuid") String uuid);
}
