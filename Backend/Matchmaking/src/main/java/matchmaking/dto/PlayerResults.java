package matchmaking.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Represents the results of a player in a tournament.
 * This class holds the Elo rating, Buchholz score and ranking for the player.
 */
@Data
@Accessors(chain = true)
public class PlayerResults {
    private String uuid;
    private Integer elo;
    private Integer buchholz;
    private Integer rank;
}
