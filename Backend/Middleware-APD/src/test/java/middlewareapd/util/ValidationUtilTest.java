package middlewareapd.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import middlewareapd.exception.*;

/**
 * Unit tests for the {@link ValidationUtil} class.
 * <p>
 * This class tests the utility methods used for validating UUIDs and user roles
 * to ensure that they function correctly and throw appropriate exceptions
 * when the validation fails.
 * </p>
 *
 * <h3>Test Methods:</h3>
 * <ul>
 *     <li>{@link #testValidUuid()} - Verifies that valid UUIDs pass the validation without exceptions.</li>
 *     <li>{@link #testInvalidUuid()} - Ensures that invalid UUIDs throw an {@link UnauthorizedException}.</li>
 *     <li>{@link #testValidUserRole()} - Checks that valid user roles pass the validation without exceptions.</li>
 *     <li>{@link #testInvalidUserRole()} - Verifies that mismatched user roles throw an {@link UnauthorizedException}.</li>
 * </ul>
 */
public class ValidationUtilTest {

    /**
     * Tests the {@link ValidationUtil#validateUuid(String, String)} method with matching UUIDs.
     * <p>
     * This test ensures that no exception is thrown when the database UUID and the extracted UUID are identical.
     * </p>
     */
    @Test
    public void testValidUuid() {
        String dbUuid = "123e4567-e89b-12d3-a456-426614174000";
        String extractedUuid = "123e4567-e89b-12d3-a456-426614174000";

        // Should pass without exception
        ValidationUtil.validateUuid(dbUuid, extractedUuid);
    }

    /**
     * Tests the {@link ValidationUtil#validateUuid(String, String)} method with non-matching UUIDs.
     * <p>
     * This test verifies that an {@link UnauthorizedException} is thrown when the extracted UUID
     * does not match the database UUID.
     * </p>
     */
    @Test
    public void testInvalidUuid() {
        String dbUuid = "123e4567-e89b-12d3-a456-426614174000";
        String extractedUuid = "invalid-uuid";

        // Should throw UnauthorizedException
        assertThrows(UnauthorizedException.class, () -> {
            ValidationUtil.validateUuid(dbUuid, extractedUuid);
        });
    }

    /**
     * Tests the {@link ValidationUtil#validateUserRole(Integer, int)} method with matching user roles.
     * <p>
     * This test ensures that no exception is thrown when the stored user role matches the expected role.
     * </p>
     */
    @Test
    public void testValidUserRole() {
        Integer dbIsAdmin = 1;
        int isAdmin = 1;

        // Should pass without exception
        ValidationUtil.validateUserRole(dbIsAdmin, isAdmin);
    }

    /**
     * Tests the {@link ValidationUtil#validateUserRole(Integer, int)} method with non-matching user roles.
     * <p>
     * This test verifies that an {@link UnauthorizedException} is thrown when the stored user role
     * does not match the expected role.
     * </p>
     */
    @Test
    public void testInvalidUserRole() {
        Integer dbIsAdmin = 0;
        int isAdmin = 1;

        // Should throw UnauthorizedException
        assertThrows(UnauthorizedException.class, () -> {
            ValidationUtil.validateUserRole(dbIsAdmin, isAdmin);
        });
    }
}


