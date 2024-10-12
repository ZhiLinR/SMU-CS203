package middlewareapd.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import middlewareapd.exception.*;

public class ValidationUtilTest {

    @Test
    public void testValidUuid() {
        String dbUuid = "123e4567-e89b-12d3-a456-426614174000";
        String extractedUuid = "123e4567-e89b-12d3-a456-426614174000";

        // Should pass without exception
        ValidationUtil.validateUuid(dbUuid, extractedUuid);
    }

    @Test
    public void testInvalidUuid() {
        String dbUuid = "123e4567-e89b-12d3-a456-426614174000";
        String extractedUuid = "invalid-uuid";

        // Should throw UnauthorizedException
        assertThrows(UnauthorizedException.class, () -> {
            ValidationUtil.validateUuid(dbUuid, extractedUuid);
        });
    }

    @Test
    public void testValidUserRole() {
        Integer dbIsAdmin = 1;
        int isAdmin = 1;

        // Should pass without exception
        ValidationUtil.validateUserRole(dbIsAdmin, isAdmin);
    }

    @Test
    public void testInvalidUserRole() {
        Integer dbIsAdmin = 0;
        int isAdmin = 1;

        // Should throw UnauthorizedException
        assertThrows(UnauthorizedException.class, () -> {
            ValidationUtil.validateUserRole(dbIsAdmin, isAdmin);
        });
    }

    // More tests for other validations like session, logout checks, etc.
}

