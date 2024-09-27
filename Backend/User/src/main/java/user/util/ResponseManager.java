package user.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for managing responses in a RESTful API.
 * Provides static methods to create standardized success and error responses.
 */
public class ResponseManager {

    /**
     * Creates a success response with a message and additional data.
     *
     * @param message       the success message to be included in the response
     * @param additionalData optional additional data to be included in the response
     * @return a {@link ResponseEntity} containing a success response with the specified message and additional data
     */
    public static ResponseEntity<Map<String, Object>> success(String message, Object... additionalData) {
        return ResponseEntity.ok(createResponse(message, additionalData));
    }

    /**
     * Creates an error response with a specified HTTP status and message.
     *
     * @param status  the HTTP status to be set for the response
     * @param message the error message to be included in the response
     * @return a {@link ResponseEntity} containing an error response with the specified status and message
     */
    public static ResponseEntity<Map<String, Object>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(createResponse(message));
    }

    /**
     * Creates a response map with a message and optional additional data.
     *
     * @param message       the message to be included in the response
     * @param additionalData optional additional data to be included in the response
     * @return a map containing the response data
     */
    private static Map<String, Object> createResponse(String message, Object... additionalData) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
    
        // Check if there are additional data to include
        if (additionalData.length > 0) {
            Object firstData = additionalData[0];
    
            // Always wrap the first data in "data" key
            if (firstData instanceof Map) {
                response.put("data", firstData);
            } else {
                response.put("data", firstData); // Ensure all types of additional data are wrapped in "data"
            }
        }
    
        return response;
    }
}


