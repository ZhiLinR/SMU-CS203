package user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "JWToken")
public class JWToken {
    @Id
    private String jwt;
    private String UUID;
    private Date lastLogin;
    private Date logout;
    // Getters and Setters


    public String getJwt() {
        return this.jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getUUID() {
        return this.UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Date getLastLogin() {
        return this.lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getLogout() {
        return this.logout;
    }

    public void setLogout(Date logout) {
        this.logout = logout;
    }
}
