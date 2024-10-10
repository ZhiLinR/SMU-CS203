package middlewareapd.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for representing a JWT request.
 *
 * <p>This class encapsulates a single JWT, which is used to identify 
 * a user or an entity in the system. It provides getter and setter methods to 
 * access and modify the JWT value.
 */
@Data
public class JWTRequest {
    private String jwt;
}
