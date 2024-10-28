package matchmaking.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents a matchups entity in the system.
 * This entity is mapped to the "Matchups" table in the database.
 *
 * <p>
 * The class contains fields corresponding to user-specific data
 * such as email, password, name, etc., which are persisted in
 * the database.
 * </p>
 */
@Entity
@Table(name = "Matchups")
@Data
public class Matchups {
    @Id
    @Column(name = "player1")
    private String player1;

    @Column(name = "player2")
    private String player2;

    @Column(name = "playerWon")
    private String playerWon;

    @Column(name = "tournamentID")
    private String tournamentId;

    @Column(name = "roundNum")
    private int roundNum;
}
