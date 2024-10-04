package user.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ValidationUtilTest {

    /**
     * Tests that a required field validation succeeds for a non-empty string.
     */
    @Test
    public void testValidateRequiredFields_Success() {
        assertDoesNotThrow(() -> ValidationUtil.validateRequiredFields("Test", "Field is required"));
    }

    /**
     * Tests that a required field validation fails for an empty string.
     */
    @Test
    public void testValidateRequiredFields_EmptyString() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtil.validateRequiredFields("", "Field is required");
        });
        assertEquals("Field is required", thrown.getMessage());
    }

    /**
     * Tests that a required field validation fails for a null string.
     */
    @Test
    public void testValidateRequiredFields_NullString() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtil.validateRequiredFields(null, "Field is required");
        });
        assertEquals("Field is required", thrown.getMessage());
    }

    /**
     * Tests valid admin role value (0).
     */
    @Test
    public void testValidateRole_ValidRole0() {
        assertDoesNotThrow(() -> ValidationUtil.validateRole((byte) 0));
    }

    /**
     * Tests valid admin role value (1).
     */
    @Test
    public void testValidateRole_ValidRole1() {
        assertDoesNotThrow(() -> ValidationUtil.validateRole((byte) 1));
    }

    /**
     * Tests invalid admin role value (2).
     */
    @Test
    public void testValidateRole_InvalidRole() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtil.validateRole((byte) 2);
        });
        assertEquals("Invalid value for Role.", thrown.getMessage());
    }

    /**
     * Tests that validation throws an exception for null admin role.
     */
    @Test
    public void testValidateRole_NullRole() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtil.validateRole(null);
        });
        assertEquals("Invalid value for Role.", thrown.getMessage());
    }

    /**
     * Tests valid email format.
     */
    @Test
    public void testIsValidEmail_ValidEmail() {
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
    }

    /**
     * Tests that an email address longer than the maximum length is invalid.
     */
    @Test
    public void testIsValidEmail_LongEmail() {
        String longEmail = "a".repeat(255) + "@example.com"; // 255 characters before '@'
        assertFalse(ValidationUtil.isValidEmail(longEmail));
    }

    /**
     * Tests invalid email format.
     */
    @Test
    public void testIsValidEmail_InvalidEmailFormat() {
        assertFalse(ValidationUtil.isValidEmail("invalid-email"));
    }

    /**
     * Tests that UUID validation succeeds for a valid UUID.
     */
    @Test
    public void testValidateUUID_ValidUUID() {
        assertDoesNotThrow(() -> ValidationUtil.validateUUID("123e4567-e89b-12d3-a456-426614174000"));
    }

    /**
     * Tests that UUID validation fails for an empty UUID.
     */
    @Test
    public void testValidateUUID_EmptyUUID() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtil.validateUUID("");
        });
        assertEquals("UUID is required", thrown.getMessage());
    }

    /**
     * Tests that UUID validation fails for a null UUID.
     */
    @Test
    public void testValidateUUID_NullUUID() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtil.validateUUID(null);
        });
        assertEquals("UUID is required", thrown.getMessage());
    }
}
