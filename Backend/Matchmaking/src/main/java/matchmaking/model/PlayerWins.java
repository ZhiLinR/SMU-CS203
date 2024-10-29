package matchmaking.model;

import lombok.Data;


/**
 * Represents a player's performance in a tournament, 
 * storing their UUID and the number of rounds they have won and drawed.
 */
@Data
public class PlayerWins {
    private String uuid;
    private int wins;
    private int draws;

    /**
     * Calculates the total points for the player based on wins and draws.
     *
     * @return the total points, calculated as wins + (draws * 0.5)
     */
    public double getPoints() {
        return wins + (draws * 0.5);
    }
}
