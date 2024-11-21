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
     * @param message        the success message to be included in the response
     * @param additionalData optional additional data to be included in the response
     * @return a {@link ResponseEntity} containing a success response with the
     *         specified message and additional data
     */
    public static ResponseEntity<Map<String, Object>> success(String message, Object... additionalData) {
        return ResponseEntity.ok(createResponse(message, true, additionalData));
    }

    /**
     * Creates an error response with a specified HTTP status and message.
     *
     * @param status  the HTTP status to be set for the response
     * @param message the error message to be included in the response
     * @return a {@link ResponseEntity} containing an error response with the
     *         specified status and message
     */
    public static ResponseEntity<Map<String, Object>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(createResponse(message, false));
    }

    /**
     * Creates a response map with a message, success flag, and optional additional
     * data.
     *
     * @param message        the message to be included in the response
     * @param success        whether the response is successful or not
     * @param additionalData optional additional data to be included in the response
     * @return a map containing the response data
     */
    private static Map<String, Object> createResponse(String message, boolean success, Object... additionalData) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("success", success);

        // Check if there is additional data to include in the response
        if (additionalData.length > 0) {
            response.put("content", additionalData[0]); // Directly put additional data into the "content" key
        } else {
            // Set "content" to null when there's no additional data
            response.put("content", null);
        }
        return response;
    }
}
