package matchmaking.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Unit tests for the ResponseManager class, which provides utility methods
 * for constructing standardized success and error responses in a REST API.
 */
public class ResponseManagerTest {

    /**
     * Tests that a success response without additional data is returned with
     * HTTP status 200 (OK), a success message, and no content field in the response
     * body.
     */
    @Test
    public void testSuccessResponseWithoutAdditionalData() {
        ResponseEntity<Map<String, Object>> response = ResponseManager.success("Operation successful");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Operation successful", body.get("message"));
        assertEquals(true, body.get("success"));
        assertNull(body.get("content")); // No additional data should set "content" to null
    }

    /**
     * Tests that a success response with additional data is returned with
     * HTTP status 200 (OK), a success message, and the provided data in the
     * "content" field.
     */
    @Test
    public void testSuccessResponseWithAdditionalData() {
        Map<String, String> data = Map.of("key", "value");
        ResponseEntity<Map<String, Object>> response = ResponseManager.success("Operation successful", data);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Operation successful", body.get("message"));
        assertEquals(true, body.get("success"));
        assertEquals(data, body.get("content")); // Additional data should be in "content"
    }

    /**
     * Tests that an error response is returned with a specified HTTP status (400
     * Bad Request),
     * an error message, and no content field in the response body.
     */
    @Test
    public void testErrorResponseWithStatusAndMessage() {
        ResponseEntity<Map<String, Object>> response = ResponseManager.error(HttpStatus.BAD_REQUEST,
                "An error occurred");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("An error occurred", body.get("message"));
        assertEquals(false, body.get("success"));
        assertNull(body.get("content")); // No additional data should set "content" to null
    }

    /**
     * Tests that an error response is returned with a different HTTP status (404
     * Not Found),
     * an error message, and no content field in the response body.
     */
    @Test
    public void testErrorResponseWithDifferentHttpStatus() {
        ResponseEntity<Map<String, Object>> response = ResponseManager.error(HttpStatus.NOT_FOUND,
                "Resource not found");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Resource not found", body.get("message"));
        assertEquals(false, body.get("success"));
        assertNull(body.get("content"));
    }
}
