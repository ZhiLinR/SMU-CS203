package user.service;

import static org.junit.jupiter.api.Assertions.*;
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

class ProfileServiceTest {

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the successful creation of a profile.
     * Verifies that the user is inserted into the repository with the correct parameters.
     */
    @Test
    void testCreateProfile_Success() {
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
     * Tests creating a profile with an empty email.
     * Expects an IllegalArgumentException to be thrown.
     */
    @Test
    void testCreateProfile_EmptyEmail() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setEmail("");
        profileRequest.setPassword("password123");
        profileRequest.setName("Test User");
        profileRequest.setIsAdmin((byte) 0);

        assertThrows(IllegalArgumentException.class, () -> profileService.createProfile(profileRequest));
    }

    /**
     * Tests creating a profile with a null password.
     * Expects an IllegalArgumentException to be thrown.
     */
    @Test
    void testCreateProfile_NullPassword() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setEmail("test@example.com");
        profileRequest.setPassword(null);
        profileRequest.setName("Test User");
        profileRequest.setIsAdmin((byte) 0);

        assertThrows(IllegalArgumentException.class, () -> profileService.createProfile(profileRequest));
    }

    /**
     * Tests creating a profile with an email that exceeds the typical length limit.
     * Expects an IllegalArgumentException to be thrown.
     */
    @Test
    void testCreateProfile_TooLongEmail() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setEmail("a".repeat(256) + "@example.com"); // Email length exceeds typical limits
        profileRequest.setPassword("password123");
        profileRequest.setName("Test User");
        profileRequest.setIsAdmin((byte) 0);

        assertThrows(IllegalArgumentException.class, () -> profileService.createProfile(profileRequest));
    }

    /**
     * Tests authenticating a user with an empty email.
     * Expects an IllegalArgumentException to be thrown.
     */
    @Test
    void testAuthenticateUser_EmptyEmail() {
        ProfileRequest loginRequest = new ProfileRequest();
        loginRequest.setEmail("");
        loginRequest.setPassword("password123");

        assertThrows(IllegalArgumentException.class, () -> profileService.authenticateUser(loginRequest));
    }

    /**
     * Tests authenticating a user when the user is not found.
     * Expects an UnauthorizedException to be thrown.
     */
    @Test
    void testAuthenticateUser_UserNotFound() {
        ProfileRequest loginRequest = new ProfileRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongPassword");

        when(userRepository.getHashedPassword(loginRequest.getEmail())).thenReturn(null); // User not found

        assertThrows(UnauthorizedException.class, () -> profileService.authenticateUser(loginRequest));
    }

    /**
     * Tests logging out a user when the user is not found.
     * Expects a UserNotFoundException to be thrown.
     */
    @Test
    void testLogoutUser_UserNotFound() {
        String uuid = "uuid-1234";
        when(userRepository.getProfile(uuid)).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> profileService.logoutUser(uuid));
    }

    /**
     * Tests retrieving a user profile by an empty UUID.
     * Expects an IllegalArgumentException to be thrown.
     */
    @Test
    void testGetProfileByUUID_EmptyUUID() {
        String uuid = "";

        assertThrows(IllegalArgumentException.class, () -> profileService.getProfileByUUID(uuid));
    }

    /**
     * Tests retrieving a user profile by UUID when the user is not found.
     * Expects a UserNotFoundException to be thrown.
     */
    @Test
    void testGetProfileByUUID_UserNotFound() {
        String uuid = "uuid-1234";
        when(userRepository.getProfile(uuid)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> profileService.getProfileByUUID(uuid));
    }

    /**
     * Tests retrieving names by a null list of UUIDs.
     * Expects an empty map to be returned.
     */
    @Test
    void testGetNamesByUUIDList_NullList() {
        List<String> uuids = null;

        Map<String, String> result = profileService.getNamesByUUIDList(uuids);

        assertTrue(result.isEmpty());
    }

    /**
     * Tests retrieving names by a list of UUIDs where an error occurs during the fetch.
     * Expects the returned map to contain appropriate error messages.
     */
    @Test
    void testGetNamesByUUIDList_ErrorDuringFetch() {
        List<String> uuids = Arrays.asList("uuid-1234", "uuid-5678");
        when(userRepository.getName("uuid-1234")).thenThrow(new RuntimeException("Database error"));

        Map<String, String> result = profileService.getNamesByUUIDList(uuids);

        assertEquals(2, result.size());
        assertEquals("Error retrieving name", result.get("uuid-1234"));
        assertEquals("Not Found", result.get("uuid-5678")); // should still handle this case
    }

    /**
     * Tests updating the Elo rating for a user successfully.
     * Verifies that the user repository's update method is called with the correct parameters.
     */
    @Test
    void testUpdateElo_Success() {
        String uuid = "uuid-1234";
        Integer elo = 1500;

        when(userRepository.getProfile(uuid)).thenReturn(new User());

        assertDoesNotThrow(() -> profileService.updateElo(uuid, elo));
        verify(userRepository).updateElo(uuid, elo);
    }

    /**
     * Tests updating the Elo rating with a null UUID.
     * Expects an IllegalArgumentException to be thrown.
     */
    @Test
    void testUpdateElo_InvalidUUID() {
        String uuid = null; // null UUID
        Integer elo = 1500;

        assertThrows(IllegalArgumentException.class, () -> profileService.updateElo(uuid, elo));
    }

    /**
     * Tests updating a user's information successfully.
     * Verifies that the user repository's update method is called with the correct parameters.
     */
    @Test
    void testUpdateUser_Success() {
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
     * Tests updating a user's information with an invalid email.
     * Expects an IllegalArgumentException to be thrown.
     */
    @Test
    void testUpdateUser_InvalidEmail() {
        String uuid = "uuid-1234";
        String email = "";
        String password = "newPassword";
        String name = "Updated User";
        Byte isAdmin = 0;
        LocalDate dob = LocalDate.now();

        assertThrows(IllegalArgumentException.class, () -> profileService.updateUser(uuid, email, password, name, isAdmin, dob));
    }

    /**
     * Tests updating a user's information when the user is not found.
     * Expects a UserNotFoundException to be thrown.
     */
    @Test
    void testUpdateUser_UserNotFound() {
        String uuid = "uuid-1234";
        String email = "test@example.com";
        String password = "newPassword";
        String name = "Updated User";
        Byte isAdmin = 0;
        LocalDate dob = LocalDate.now();

        when(userRepository.getProfile(uuid)).thenReturn(null); // User not found

        assertThrows(UserNotFoundException.class, () -> profileService.updateUser(uuid, email, password, name, isAdmin, dob));
    }
}
