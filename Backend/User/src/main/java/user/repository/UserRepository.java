package user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    @Procedure(procedureName = "InsertUser")
    Integer insertUser(
        @Param("p_email") String email, 
        @Param("p_password") String password, 
        @Param("p_name") String name, 
        @Param("p_isAdmin") Byte isAdmin
    );

    @Procedure(name = "GetHashedPassword")
    String getHashedPassword(@Param("p_email") String email);

    @Procedure(name = "CheckEmail")
    String checkEmail(@Param("p_email") String email);

    @Procedure(name = "GetName")
    String getName(@Param("p_uuid") String uuid);

    @Procedure(name = "GetProfile")
    User getProfile(@Param("p_uuid") String uuid);

    @Procedure(name = "getRoleByUUID")
    Byte getRoleByUUID(@Param("p_uuid") String uuid);   

    @Procedure(name = "UpdateUser")
    void updateUser(
        @Param("p_UUID") String uuid,
        @Param("p_email") String email,
        @Param("p_password") String password,
        @Param("p_name") String name,
        @Param("p_isAdmin") Byte isAdmin,
        @Param("p_dob") java.sql.Date dob
    );

    @Procedure(name = "UpdateElo")
    void updateElo(
        @Param("p_uuid") String uuid,
        @Param("p_elo") int elo
    );
}
