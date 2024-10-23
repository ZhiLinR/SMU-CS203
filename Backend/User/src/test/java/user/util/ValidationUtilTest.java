package user.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link ValidationUtil} class, focusing on ensuring the proper
 * validation of various fields, roles, email formats, and UUIDs.
 * <p>
 * This test class verifies that the validation methods in {@link ValidationUtil} function 
 * correctly under different scenarios, including required field validation, role validation, 
 * email format checks, and UUID format validation.
 * </p>
 */
public class ValidationUtilTest {

    /**
     * Tests the {@link ValidationUtil#validateRequiredFields(String, String)} method with a valid input.
     * <p>
     * This test ensures that validating a required field with a non-empty string does not throw an exception.
     * </p>
     * <ul>
     *     <li>Given: A non-empty string as input.</li>
     *     <li>When: The method {@link ValidationUtil#validateRequiredFields(String, String)} is called.</li>
     *     <li>Then: No exception should be thrown.</li>
     * </ul>
     */
    @Test
    public void testValidateRequiredFields_Success() {
        assertDoesNotThrow(() -> ValidationUtil.validateRequiredFields("Test", "Field is required"));
    }

    /**
     * Tests the {@link ValidationUtil#validateRequiredFields(String, String)} method with an empty string.
     * <p>
     * This test ensures that validating a required field with an empty string throws an 
     * {@link IllegalArgumentException} with the expected message.
     * </p>
     * <ul>
     *     <li>Given: An empty string as input.</li>
     *     <li>When: The method {@link ValidationUtil#validateRequiredFields(String, String)} is called.</li>
     *     <li>Then: An {@link IllegalArgumentException} should be thrown with the message "Field is required."</li>
     * </ul>
     */
    @Test
    public void testValidateRequiredFields_EmptyString() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtil.validateRequiredFields("", "Field is required");
        });
        assertEquals("Field is required", thrown.getMessage());
    }

    /**
     * Tests the {@link ValidationUtil#validateRequiredFields(String, String)} method with a null input.
     * <p>
     * This test ensures that validating a required field with a null input throws an 
     * {@link IllegalArgumentException} with the expected message.
     * </p>
     * <ul>
     *     <li>Given: A null input string.</li>
     *     <li>When: The method {@link ValidationUtil#validateRequiredFields(String, String)} is called.</li>
     *     <li>Then: An {@link IllegalArgumentException} should be thrown with the message "Field is required."</li>
     * </ul>
     */
    @Test
    public void testValidateRequiredFields_NullString() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtil.validateRequiredFields(null, "Field is required");
        });
        assertEquals("Field is required", thrown.getMessage());
    }

    /**
     * Tests the {@link ValidationUtil#validateRole(Byte)} method with a valid admin role value (0).
     * <p>
     * This test ensures that validating a valid admin role does not throw an exception.
     * </p>
     * <ul>
     *     <li>Given: A valid admin role value (0).</li>
     *     <li>When: The method {@link ValidationUtil#validateRole(Byte)} is called.</li>
     *     <li>Then: No exception should be thrown.</li>
     * </ul>
     */
    @Test
    public void testValidateRole_ValidRole0() {
        assertDoesNotThrow(() -> ValidationUtil.validateRole((byte) 0));
    }

    /**
     * Tests the {@link ValidationUtil#validateRole(Byte)} method with a valid admin role value (1).
     * <p>
     * This test ensures that validating a valid admin role does not throw an exception.
     * </p>
     * <ul>
     *     <li>Given: A valid admin role value (1).</li>
     *     <li>When: The method {@link ValidationUtil#validateRole(Byte)} is called.</li>
     *     <li>Then: No exception should be thrown.</li>
     * </ul>
     */
    @Test
    public void testValidateRole_ValidRole1() {
        assertDoesNotThrow(() -> ValidationUtil.validateRole((byte) 1));
    }

    /**
     * Tests the {@link ValidationUtil#validateRole(Byte)} method with an invalid role value (2).
     * <p>
     * This test ensures that validating an invalid role value throws an 
     * {@link IllegalArgumentException} with the expected message.
     * </p>
     * <ul>
     *     <li>Given: An invalid role value (2).</li>
     *     <li>When: The method {@link ValidationUtil#validateRole(Byte)} is called.</li>
     *     <li>Then: An {@link IllegalArgumentException} should be thrown with the message "Invalid value for Role."</li>
     * </ul>
     */
    @Test
    public void testValidateRole_InvalidRole() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtil.validateRole((byte) 2);
        });
        assertEquals("Invalid value for Role.", thrown.getMessage());
    }

    /**
     * Tests the {@link ValidationUtil#validateRole(Byte)} method with a null role.
     * <p>
     * This test ensures that validating a null role value throws an 
     * {@link IllegalArgumentException} with the expected message.
     * </p>
     * <ul>
     *     <li>Given: A null role value.</li>
     *     <li>When: The method {@link ValidationUtil#validateRole(Byte)} is called.</li>
     *     <li>Then: An {@link IllegalArgumentException} should be thrown with the message "Invalid value for Role."</li>
     * </ul>
     */
    @Test
    public void testValidateRole_NullRole() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtil.validateRole(null);
        });
        assertEquals("Invalid value for Role.", thrown.getMessage());
    }

    /**
     * Tests the {@link ValidationUtil#isValidEmail(String)} method with a valid email format.
     * <p>
     * This test ensures that validating a correctly formatted email returns {@code true}.
     * </p>
     * <ul>
     *     <li>Given: A valid email address.</li>
     *     <li>When: The method {@link ValidationUtil#isValidEmail(String)} is called.</li>
     *     <li>Then: The result should be {@code true}.</li>
     * </ul>
     */
    @Test
    public void testIsValidEmail_ValidEmail() {
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
    }

    /**
     * Tests the {@link ValidationUtil#isValidEmail(String)} method with a long email address.
     * <p>
     * This test ensures that validating an email address longer than the allowed length returns {@code false}.
     * </p>
     * <ul>
     *     <li>Given: An email address that exceeds the maximum length.</li>
     *     <li>When: The method {@link ValidationUtil#isValidEmail(String)} is called.</li>
     *     <li>Then: The result should be {@code false}.</li>
     * </ul>
     */
    @Test
    public void testIsValidEmail_LongEmail() {
        String longEmail = "a".repeat(255) + "@example.com"; // 255 characters before '@'
        assertFalse(ValidationUtil.isValidEmail(longEmail));
    }

    /**
     * Tests the {@link ValidationUtil#isValidEmail(String)} method with an invalid email format.
     * <p>
     * This test ensures that validating an improperly formatted email returns {@code false}.
     * </p>
     * <ul>
     *     <li>Given: An invalid email address format.</li>
     *     <li>When: The method {@link ValidationUtil#isValidEmail(String)} is called.</li>
     *     <li>Then: The result should be {@code false}.</li>
     * </ul>
     */
    @Test
    public void testIsValidEmail_InvalidEmailFormat() {
        assertFalse(ValidationUtil.isValidEmail("invalid-email"));
    }

    /**
     * Tests the {@link ValidationUtil#validateUUID(String)} method with a valid UUID.
     * <p>
     * This test ensures that validating a correctly formatted UUID does not throw an exception.
     * </p>
     * <ul>
     *     <li>Given: A valid UUID string.</li>
     *     <li>When: The method {@link ValidationUtil#validateUUID(String)} is called.</li>
     *     <li>Then: No exception should be thrown.</li>
     * </ul>
     */
    @Test
    public void testValidateUUID_ValidUUID() {
        assertDoesNotThrow(() -> ValidationUtil.validateUUID("123e4567-e89b-12d3-a456-426614174000"));
    }

    /**
     * Tests the {@link ValidationUtil#validateUUID(String)} method with an empty string.
     * <p>
     * This test ensures that validating an empty UUID string throws an 
     * {@link IllegalArgumentException} with the expected message.
     * </p>
     * <ul>
     *     <li>Given: An empty UUID string.</li>
     *     <li>When: The method {@link ValidationUtil#validateUUID(String)} is called.</li>
     *     <li>Then: An {@link IllegalArgumentException} should be thrown with the message "UUID is required."</li>
     * </ul>
     */
    @Test
    public void testValidateUUID_EmptyUUID() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtil.validateUUID("");
        });
        assertEquals("UUID is required", thrown.getMessage());
    }

    /**
     * Tests the {@link ValidationUtil#validateUUID(String)} method with a null input.
     * <p>
     * This test ensures that validating a null UUID string throws an 
     * {@link IllegalArgumentException} with the expected message.
     * </p>
     * <ul>
     *     <li>Given: A null UUID string.</li>
     *     <li>When: The method {@link ValidationUtil#validateUUID(String)} is called.</li>
     *     <li>Then: An {@link IllegalArgumentException} should be thrown with the message "UUID is required."</li>
     * </ul>
     */
    @Test
    public void testValidateUUID_NullUUID() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtil.validateUUID(null);
        });
        assertEquals("UUID is required", thrown.getMessage());
    }
}