package user.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for representing a UUID request.
 *
 * <p>This class encapsulates a single UUID, which is used to identify
 * a user or an entity in the system. It provides getter and setter methods to
 * access and modify the UUID value.
 */
@Data
public class UUIDRequest {
    private String uuid;
}
