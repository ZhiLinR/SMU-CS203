package user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import user.dto.ProfileRequest;
import user.util.UnauthorizedException;
import user.util.UserNotFoundException;
import user.model.User;
import user.repository.JWTokenRepository;
import user.repository.UserRepository;
import user.util.JwtUtil;

import java.time.LocalDate;
import java.util.*;

/**
 * Unit tests for the {@link ProfileService} class, focusing on various operations related 
 * to profile management, including profile creation, user authentication, logout functionality, 
 * retrieval of user profiles, and updating user details.
 * <p>
 * This test class ensures that the {@link ProfileService} behaves correctly under various scenarios. 
 * It includes tests for successfully creating profiles, handling authentication errors, 
 * and managing user updates, all while ensuring the service interacts properly with 
 * mocked repositories.
 * </p>
 */
public class ProfileServiceTest {

    @InjectMocks
    private ProfileService profileService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTokenRepository jwTokenRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

        /**
     * Initializes the mocks before each test method is executed.
     * <p>
     * This method uses {@link MockitoAnnotations#openMocks(Object)} to set up the mock objects 
     * annotated with {@link Mock} and inject them into the {@link ProfileServiceTest} instance.
     * </p>
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the {@link ProfileService#createProfile(ProfileRequest)} method for successful profile creation.
     * <p>
     * This test verifies that a profile is successfully created and the user is inserted into the repository.
     * </p>
     * <ul>
     *     <li>Given: A valid {@link ProfileRequest} with email, password, name, and isAdmin.</li>
     *     <li>When: The method {@link ProfileService#createProfile(ProfileRequest)} is called.</li>
     *     <li>Then: The return value should be {@code true}, and the user repository's insertUser method should be called with the correct parameters.</li>
     * </ul>
     */
    @Test
    public void testCreateProfile_Success() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setEmail("test@example.com");
        profileRequest.setPassword("password123");
        profileRequest.setName("Test User");
        profileRequest.setIsAdmin((byte) 0);

        when(passwordEncoder.encode(profileRequest.getPassword())).thenReturn("encryptedPassword");
        when(userRepository.insertUser(anyString(), anyString(), anyString(), anyByte())).thenReturn(1);

        boolean result = profileService.createProfile(profileRequest);

        assertTrue(result);
        verify(userRepository).insertUser("test@example.com", "encryptedPassword", "Test User", (byte) 0);
    }

    /**
     * Tests the {@link ProfileService#createProfile(ProfileRequest)} method with an empty email.
     * <p>
     * This test ensures that an {@code IllegalArgumentException} is thrown when an empty email is provided.
     * </p>
     * <ul>
     *     <li>Given: A {@link ProfileRequest} with an empty email.</li>
     *     <li>When: The method {@link ProfileService#createProfile(ProfileRequest)} is called.</li>
     *     <li>Then: An {@code IllegalArgumentException} should be thrown.</li>
     * </ul>
     */
    @Test
    public void testCreateProfile_EmptyEmail() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setEmail("");
        profileRequest.setPassword("password123");
        profileRequest.setName("Test User");
        profileRequest.setIsAdmin((byte) 0);

        assertThrows(IllegalArgumentException.class, () -> profileService.createProfile(profileRequest));
    }

    /**
     * Tests the {@link ProfileService#createProfile(ProfileRequest)} method with a null password.
     * <p>
     * This test confirms that an {@code IllegalArgumentException} is thrown when attempting to create a profile with a null password.
     * </p>
     * <ul>
     *     <li>Given: A {@link ProfileRequest} with a null password.</li>
     *     <li>When: The method {@link ProfileService#createProfile(ProfileRequest)} is called.</li>
     *     <li>Then: An {@code IllegalArgumentException} should be thrown.</li>
     * </ul>
     */
    @Test
    public void testCreateProfile_NullPassword() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setEmail("test@example.com");
        profileRequest.setPassword(null);
        profileRequest.setName("Test User");
        profileRequest.setIsAdmin((byte) 0);

        assertThrows(IllegalArgumentException.class, () -> profileService.createProfile(profileRequest));
    }

    /**
     * Tests the {@link ProfileService#createProfile(ProfileRequest)} method with an excessively long email.
     * <p>
     * This test verifies that an {@code IllegalArgumentException} is thrown when the email exceeds the maximum length.
     * </p>
     * <ul>
     *     <li>Given: A {@link ProfileRequest} with a long email.</li>
     *     <li>When: The method {@link ProfileService#createProfile(ProfileRequest)} is called.</li>
     *     <li>Then: An {@code IllegalArgumentException} should be thrown.</li>
     * </ul>
     */
    @Test
    public void testCreateProfile_TooLongEmail() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setEmail("a".repeat(256) + "@example.com");
        profileRequest.setPassword("password123");
        profileRequest.setName("Test User");
        profileRequest.setIsAdmin((byte) 0);

        assertThrows(IllegalArgumentException.class, () -> profileService.createProfile(profileRequest));
    }

    /**
     * Tests the {@link ProfileService#authenticateUser(ProfileRequest)} method with an empty email.
     * <p>
     * This test verifies that an {@code IllegalArgumentException} is thrown when an empty email is provided.
     * </p>
     * <ul>
     *     <li>Given: A {@link ProfileRequest} with an empty email.</li>
     *     <li>When: The method {@link ProfileService#authenticateUser(ProfileRequest)} is called.</li>
     *     <li>Then: An {@code IllegalArgumentException} should be thrown.</li>
     * </ul>
     */
    @Test
    public void testAuthenticateUser_EmptyEmail() {
        ProfileRequest loginRequest = new ProfileRequest();
        loginRequest.setEmail("");
        loginRequest.setPassword("password123");

        assertThrows(IllegalArgumentException.class, () -> profileService.authenticateUser(loginRequest));
    }

    /**
     * Tests the {@link ProfileService#authenticateUser(ProfileRequest)} method for a non-existent user.
     * <p>
     * This test verifies that an {@code UnauthorizedException} is thrown when attempting to authenticate a non-existent user.
     * </p>
     * <ul>
     *     <li>Given: A {@link ProfileRequest} for a non-existent user.</li>
     *     <li>When: The method {@link ProfileService#authenticateUser(ProfileRequest)} is called.</li>
     *     <li>Then: An {@code UnauthorizedException} should be thrown.</li>
     * </ul>
     */
    @Test
    public void testAuthenticateUser_UserNotFound() {
        ProfileRequest loginRequest = new ProfileRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongPassword");

        when(userRepository.getHashedPassword(loginRequest.getEmail())).thenReturn(null);

        assertThrows(UnauthorizedException.class, () -> profileService.authenticateUser(loginRequest));
    }

    /**
     * Tests the {@link ProfileService#logoutUser(String)} method for a non-existent user.
     * <p>
     * This test verifies that a {@code UserNotFoundException} is thrown when attempting to log out a non-existent user.
     * </p>
     * <ul>
     *     <li>Given: A UUID for a non-existent user.</li>
     *     <li>When: The method {@link ProfileService#logoutUser(String)} is called.</li>
     *     <li>Then: A {@code UserNotFoundException} should be thrown.</li>
     * </ul>
     */
    @Test
    public void testLogoutUser_UserNotFound() {
        String uuid = "uuid-1234";
        when(userRepository.getProfile(uuid)).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> profileService.logoutUser(uuid));
    }

    /**
     * Tests the {@link ProfileService#getProfileByUUID(String)} method with an empty UUID.
     * <p>
     * This test verifies that an {@code IllegalArgumentException} is thrown when an empty UUID is provided.
     * </p>
     * <ul>
     *     <li>Given: An empty UUID.</li>
     *     <li>When: The method {@link ProfileService#getProfileByUUID(String)} is called.</li>
     *     <li>Then: An {@code IllegalArgumentException} should be thrown.</li>
     * </ul>
     */
    @Test
    public void testGetProfileByUUID_EmptyUUID() {
        assertThrows(IllegalArgumentException.class, () -> profileService.getProfileByUUID(""));
    }

    /**
     * Tests the {@link ProfileService#getProfileByUUID(String)} method for a non-existent UUID.
     * <p>
     * This test verifies that a {@code UserNotFoundException} is thrown when attempting to retrieve a profile with a non-existent UUID.
     * </p>
     * <ul>
     *     <li>Given: A UUID for a non-existent user.</li>
     *     <li>When: The method {@link ProfileService#getProfileByUUID(String)} is called.</li>
     *     <li>Then: A {@code UserNotFoundException} should be thrown.</li>
     * </ul>
     */
    @Test
    public void testGetProfileByUUID_UserNotFound() {
        String uuid = "uuid-1234";
        when(userRepository.getProfile(uuid)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> profileService.getProfileByUUID(uuid));
    }

    /**
     * Tests the {@link ProfileService#getNamesByUUIDList(List)} method with a null list of UUIDs.
     * <p>
     * This test verifies that passing a null list returns an empty map.
     * </p>
     * <ul>
     *     <li>Given: A null list of UUIDs.</li>
     *     <li>When: The method {@link ProfileService#getNamesByUUIDList(List)} is called.</li>
     *     <li>Then: An empty map should be returned.</li>
     * </ul>
     */
    @Test
    public void testGetNamesByUUIDList_NullList() {
        Map<String, String> result = profileService.getNamesByUUIDList(null);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests the {@link ProfileService#getNamesByUUIDList(List)} method for an error during name retrieval.
     * <p>
     * This test verifies that an error during name retrieval returns an appropriate error message in the result map.
     * </p>
     * <ul>
     *     <li>Given: A list of UUIDs with one that causes an error during retrieval.</li>
     *     <li>When: The method {@link ProfileService#getNamesByUUIDList(List)} is called.</li>
     *     <li>Then: The result map should contain error messages for the failed retrieval.</li>
     * </ul>
     */
    @Test
    public void testGetNamesByUUIDList_ErrorDuringFetch() {
        List<String> uuids = Arrays.asList("uuid-1234", "uuid-5678");
        when(userRepository.getName("uuid-1234")).thenThrow(new RuntimeException("Database error"));

        Map<String, String> result = profileService.getNamesByUUIDList(uuids);

        assertEquals(2, result.size());
        assertEquals("Error retrieving name", result.get("uuid-1234"));
        assertEquals("Not Found", result.get("uuid-5678"));
    }

    /**
     * Tests the {@link ProfileService#updateElo(String, Integer)} method for a successful update of a user's Elo rating.
     * <p>
     * This test verifies that the user's Elo rating is updated successfully in the repository.
     * </p>
     * <ul>
     *     <li>Given: A valid UUID and Elo rating.</li>
     *     <li>When: The method {@link ProfileService#updateElo(String, Integer)} is called.</li>
     *     <li>Then: The repository's update method should be called with the correct parameters.</li>
     * </ul>
     */
    @Test
    public void testUpdateElo_Success() {
        String uuid = "uuid-1234";
        Integer elo = 1500;

        when(userRepository.getProfile(uuid)).thenReturn(new User());

        assertDoesNotThrow(() -> profileService.updateElo(uuid, elo));
        verify(userRepository).updateElo(uuid, elo);
    }

    /**
     * Tests the {@link ProfileService#updateElo(String, Integer)} method with a null UUID.
     * <p>
     * This test verifies that an {@code IllegalArgumentException} is thrown when a null UUID is provided.
     * </p>
     * <ul>
     *     <li>Given: A null UUID.</li>
     *     <li>When: The method {@link ProfileService#updateElo(String, Integer)} is called.</li>
     *     <li>Then: An {@code IllegalArgumentException} should be thrown.</li>
     * </ul>
     */
    @Test
    public void testUpdateElo_InvalidUUID() {
        assertThrows(IllegalArgumentException.class, () -> profileService.updateElo(null, 1500));
    }

    /**
     * Tests the {@link ProfileService#updateUser(String, String, String, String, Byte, LocalDate)} method for a successful update of a user's information.
     * <p>
     * This test verifies that updating user information successfully reflects in the repository.
     * </p>
     * <ul>
     *     <li>Given: A valid UUID and user details.</li>
     *     <li>When: The method {@link ProfileService#updateUser(String, String, String, String, Byte, LocalDate)} is called.</li>
     *     <li>Then: The repository's update method should be called with the correct parameters.</li>
     * </ul>
     */
    @Test
    public void testUpdateUser_Success() {
        String uuid = "uuid-1234";
        String email = "test@example.com";
        String password = "newPassword";
        String name = "Updated User";
        Byte isAdmin = 0;
        LocalDate dob = LocalDate.now();

        when(userRepository.getProfile(uuid)).thenReturn(new User());
        when(passwordEncoder.encode(password)).thenReturn("encryptedPassword");

        boolean result = profileService.updateUser(uuid, email, password, name, isAdmin, dob);

        assertTrue(result);
        verify(userRepository).updateUser(uuid, email, "encryptedPassword", name, isAdmin, java.sql.Date.valueOf(dob));
    }

    /**
     * Tests the {@link ProfileService#updateUser(String, String, String, String, Byte, LocalDate)} method with an invalid email.
     * <p>
     * This test verifies that an {@code IllegalArgumentException} is thrown when an invalid email is provided.
     * </p>
     * <ul>
     *     <li>Given: A {@link ProfileRequest} with an invalid email.</li>
     *     <li>When: The method {@link ProfileService#updateUser(String, String, String, String, Byte, LocalDate)} is called.</li>
     *     <li>Then: An {@code IllegalArgumentException} should be thrown.</li>
     * </ul>
     */
    @Test
    public void testUpdateUser_InvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> profileService.updateUser(
                "uuid-1234", "", "newPassword", "Updated User", (byte) 0, LocalDate.now()));
    }

    /**
     * Tests the {@link ProfileService#updateUser(String, String, String, String, Byte, LocalDate)} method for a non-existent user.
     * <p>
     * This test verifies that a {@code UserNotFoundException} is thrown when attempting to update a non-existent user's information.
     * </p>
     * <ul>
     *     <li>Given: A UUID for a non-existent user.</li>
     *     <li>When: The method {@link ProfileService#updateUser(String, String, String, String, Byte, LocalDate)} is called.</li>
     *     <li>Then: A {@code UserNotFoundException} should be thrown.</li>
     * </ul>
     */
    @Test
    public void testUpdateUser_UserNotFound() {
        when(userRepository.getProfile("uuid-1234")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> profileService.updateUser(
                "uuid-1234", "test@example.com", "newPassword", "Updated User", (byte) 0, LocalDate.now()));
    }
}
