package user.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseManager {

    public static ResponseEntity<Map<String, Object>> success(String message, Object... additionalData) {
        return ResponseEntity.ok(createResponse(message, additionalData));
    }

    public static ResponseEntity<Map<String, Object>> successWithData(String message, Map<String, Object> data) {
        return ResponseEntity.ok(createResponse(message, data));
    }

    public static ResponseEntity<Map<String, Object>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(createResponse(message));
    }

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


