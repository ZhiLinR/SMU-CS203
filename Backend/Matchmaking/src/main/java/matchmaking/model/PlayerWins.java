package matchmaking.model;

import lombok.Data;

@Data

/**
 * Represents a player's performance in a tournament, 
 * storing their UUID and the number of rounds they have won and drawed.
 */
public class PlayerWins {
    private String uuid;
    private int wins;
    private int draws;
}
