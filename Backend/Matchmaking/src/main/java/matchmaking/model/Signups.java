package matchmaking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Represents a signups entity in the tournament system.
 * This entity is mapped to the "Signups" table in the database.
 *
 * <p>
 * Each signup corresponds to a player participating in a tournament, with
 * fields representing the player's unique identifier, the associated tournament
 * ID, and the player's Elo rating. These attributes are persisted in the
 * database.
 * </p>
 */
@Entity
@Table(name = "Signups")
@Data
@Accessors(chain = true)
public class Signups {

    /**
     * The composite primary key for this Signup, containing the player UUID
     * and the tournament ID.
     */
    @EmbeddedId
    private PlayerTournamentId id;

    /**
     * The Elo rating of the player, representing their skill level.
     */
    @Column(name = "elo")
    private int elo;
}
