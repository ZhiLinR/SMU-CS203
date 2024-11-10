package matchmaking.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Temporary class for managing player rankings during calculation.
 */
@Data
@Accessors(chain = true)
public class PlayerRanking {
    private String uuid; // Player's UUID
    private Double points; // Total points earned in the tournament
    private Integer buchholz; // Buchholz score for sorting
    private PlayerResults playerResult;
}
