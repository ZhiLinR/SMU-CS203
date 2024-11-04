package matchmaking.dto;

import lombok.Data;

/**
 * Represents the results of a player in a tournament.
 * This class holds the Elo rating, Buchholz score and ranking for the player.
 */
@Data
public class PlayerResults {
    private String uuid;
    private Integer elo;
    private Integer buchholz;
    private Integer rank;

    public PlayerResults setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public PlayerResults setElo(Integer elo) {
        this.elo = elo;
        return this;
    }

    public PlayerResults setBuchholz(Integer buchholz) {
        this.buchholz = buchholz;
        return this;
    }

    public PlayerResults setRank(Integer rank) {
        this.rank = rank;
        return this;
    }
}
