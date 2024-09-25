package user.dto;

public class EloUpdateRequest {
    private Integer elo;
    private String uuid;

    public Integer getElo() {
        return this.elo;
    }

    public void setElo(Integer elo) {
        this.elo = elo;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
