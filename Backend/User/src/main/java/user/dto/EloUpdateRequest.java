package user.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for updating a user's ELO rating.
 *
 * <p>
 * This class encapsulates the data required to update a user's ELO score,
 * including the user's unique identifier (UUID) and the new ELO rating.
 *
 * <p>
 * Provides getter and setter methods to access and modify the ELO rating
 * and UUID values.
 */
@Data
public class EloUpdateRequest {
    private Integer elo;
    private String uuid;
}
