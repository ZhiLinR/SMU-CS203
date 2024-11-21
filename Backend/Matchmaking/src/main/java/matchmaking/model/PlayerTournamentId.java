package matchmaking.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

/**
 * Represents the composite primary key for the {@code Signups} and
 * {@code Results} entity. This class is marked as {@code Embeddable}, meaning
 * it can be used as a composite key within an entity class. It contains the
 * players and the tournament ID as key components.
 */
@Data
@Embeddable
@Accessors(chain = true)
public class PlayerTournamentId implements Serializable {
    /**
     * The UUID of user.
     */
    private String uuid;

    /**
     * The ID of the tournament user is in.
     */
    private String tournamentId;
}
