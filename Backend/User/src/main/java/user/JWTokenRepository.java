package user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

    @Repository
public interface JWTokenRepository extends JpaRepository<JWToken, String> {
    
    @Procedure(name = "UpdateJWTLastLogin")
    void updateJWTLastLogin(
        @Param("p_email") String email, 
        @Param("p_jwt") String jwt
    );

    @Procedure(name = "UpdateLogout")
    void updateLogout(@Param("p_email") String email);
}
