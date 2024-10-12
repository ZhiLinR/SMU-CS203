package middlewareapd.model; // Ensure this matches your directory structure

import java.time.LocalDateTime;

public class JWToken {
    private String jwt;
    private String uuid;
    private int isAdmin; // Use Byte for isAdmin as per your request
    private LocalDateTime lastLogin;
    private LocalDateTime logout;
    private LocalDateTime lastAccess;

    // Constructor to initialize all fields
    public JWToken(String jwt, String uuid, int isAdmin, LocalDateTime lastLogin, LocalDateTime logout, LocalDateTime lastAccess) {
        this.jwt = jwt;
        this.uuid = uuid;
        this.isAdmin = isAdmin;
        this.lastLogin = lastLogin;
        this.logout = logout;
        this.lastAccess = lastAccess;
    }

    // Getters and Setters for all fields
    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDateTime getLogout() {
        return logout;
    }

    public void setLogout(LocalDateTime logout) {
        this.logout = logout;
    }

    public LocalDateTime getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(LocalDateTime lastAccess) {
        this.lastAccess = lastAccess;
    }

    @Override
    public String toString() {
        return "JWToken{" +
                "jwt='" + jwt + '\'' +
                ", uuid='" + uuid + '\'' +
                ", isAdmin=" + isAdmin +
                ", lastLogin=" + lastLogin +
                ", logout=" + logout +
                ", lastAccess=" + lastAccess +
                '}';
    }
}
