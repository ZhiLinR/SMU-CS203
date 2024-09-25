package user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "JWToken")
public class JWToken {
    @Id
    @Column(name = "jwt")
    private String jwt;

    @Column(name = "UUID")
    private String uuid;

    @Column(name = "lastLogin")
    private LocalDateTime lastLogin;

    @Column(name = "logout")
    private LocalDateTime logout;
    // Getters and Setters


    public String getJwt() {
        return this.jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getLastLogin() {
        return this.lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDateTime getLogout() {
        return this.logout;
    }

    public void setLogout(LocalDateTime logout) {
        this.logout = logout;
    }
    
}
