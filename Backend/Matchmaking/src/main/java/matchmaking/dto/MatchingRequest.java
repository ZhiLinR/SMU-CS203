package matchmaking.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for matching users in a tournament.
 *
 * <p>This class encapsulates the data required to identify a tournament
 * and match users in the tournament.
 *
 * <p>Provides getter and setter methods to access and modify the ELO rating
 * and UUID values.
 */
@Data
public class MatchingRequest {
    private String tournamentID;
}
