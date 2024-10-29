package matchmaking.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents a signups entity in the system.
 * This entity is mapped to the "Signups" table in the database.
 *
 * <p>
 * The class contains fields corresponding to user-specific data
 * such as email, password, name, etc., which are persisted in
 * the database.
 * </p>
 */
@Entity
@Table(name = "Signups")
@Data
public class Signups {
    @Id
    @Column(name = "UUID")
    private String uuid;

    @Column(name = "tournamentID")
    private String tournamentId;

    @Column(name = "elo")
    private int elo;
}
