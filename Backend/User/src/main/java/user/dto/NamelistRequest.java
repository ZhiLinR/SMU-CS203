package user.dto;

import java.util.List;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for handling a list of UUIDs.
 *
 * <p>This class encapsulates the data required for operations involving
 * multiple user UUIDs, typically used for retrieving or processing user information
 * based on a list of identifiers.
 *
 * <p>Provides getter and setter methods to access and modify the list of UUIDs.
 */
@Data
public class NamelistRequest {
    private List<String> data;
}
