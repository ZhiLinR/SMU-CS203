package matchmaking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Represents a matchups entity in the system.
 * This entity is mapped to the "Matchups" table in the database.
 *
 * <p>
 * The class contains fields corresponding to results data
 * such as uuid, tournamentId and ranking, which are persisted in
 * the database.
 * </p>
 */
@Entity
@Table(name = "Results")
@Data
@Accessors(chain = true)
public class Results {

    /**
     * The composite primary key for this Result, containing the player UUID
     * and the tournament ID.
     */
    @EmbeddedId
    private PlayerTournamentId id;

    /**
     * The rank of the player in the tournament.
     */
    @Column(name = "ranking")
    private Integer ranking;
}