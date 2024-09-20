package user;

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

        if (additionalData.length > 0) {
            Object firstData = additionalData[0];
            if (firstData instanceof Map) {
                Map<?, ?> additionalMap = (Map<?, ?>) firstData;
                for (Map.Entry<?, ?> entry : additionalMap.entrySet()) {
                    // Type safety: Ensure key and value are both String and Object, respectively
                    if (entry.getKey() instanceof String) {
                        response.put((String) entry.getKey(), entry.getValue());
                    }
                }
            } else {
                response.put("data", firstData);
            }
        }
        return response;
    }
}


