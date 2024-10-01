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

    // Testing successful case for creating a profile
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

    // Testing boundary values and invalid inputs
    @Test
    void testCreateProfile_EmptyEmail() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setEmail("");
        profileRequest.setPassword("password123");
        profileRequest.setName("Test User");
        profileRequest.setIsAdmin((byte) 0);

        assertThrows(IllegalArgumentException.class, () -> profileService.createProfile(profileRequest));
    }

    @Test
    void testCreateProfile_NullPassword() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setEmail("test@example.com");
        profileRequest.setPassword(null);
        profileRequest.setName("Test User");
        profileRequest.setIsAdmin((byte) 0);

        assertThrows(IllegalArgumentException.class, () -> profileService.createProfile(profileRequest));
    }

    @Test
    void testCreateProfile_TooLongEmail() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setEmail("a".repeat(256) + "@example.com"); // Email length exceeds typical limits
        profileRequest.setPassword("password123");
        profileRequest.setName("Test User");
        profileRequest.setIsAdmin((byte) 0);

        assertThrows(IllegalArgumentException.class, () -> profileService.createProfile(profileRequest));
    }

    @Test
    void testAuthenticateUser_EmptyEmail() {
        ProfileRequest loginRequest = new ProfileRequest();
        loginRequest.setEmail("");
        loginRequest.setPassword("password123");

        assertThrows(IllegalArgumentException.class, () -> profileService.authenticateUser(loginRequest));
    }

    @Test
    void testAuthenticateUser_UserNotFound() {
        ProfileRequest loginRequest = new ProfileRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongPassword");

        when(userRepository.getHashedPassword(loginRequest.getEmail())).thenReturn(null); // User not found

        assertThrows(UnauthorizedException.class, () -> profileService.authenticateUser(loginRequest));
    }

    @Test
    void testLogoutUser_UserNotFound() {
        String uuid = "uuid-1234";
        when(userRepository.getProfile(uuid)).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> profileService.logoutUser(uuid));
    }

    @Test
    void testGetProfileByUUID_EmptyUUID() {
        String uuid = "";
        
        assertThrows(IllegalArgumentException.class, () -> profileService.getProfileByUUID(uuid));
    }

    @Test
    void testGetProfileByUUID_UserNotFound() {
        String uuid = "uuid-1234";
        when(userRepository.getProfile(uuid)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> profileService.getProfileByUUID(uuid));
    }

    @Test
    void testGetNamesByUUIDList_NullList() {
        List<String> uuids = null;

        Map<String, String> result = profileService.getNamesByUUIDList(uuids);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetNamesByUUIDList_ErrorDuringFetch() {
        List<String> uuids = Arrays.asList("uuid-1234", "uuid-5678");
        when(userRepository.getName("uuid-1234")).thenThrow(new RuntimeException("Database error"));

        Map<String, String> result = profileService.getNamesByUUIDList(uuids);

        assertEquals(2, result.size());
        assertEquals("Error retrieving name", result.get("uuid-1234"));
        assertEquals("Not Found", result.get("uuid-5678")); // should still handle this case
    }

    @Test
    void testUpdateElo_Success() {
        String uuid = "uuid-1234";
        Integer elo = 1500;

        when(userRepository.getProfile(uuid)).thenReturn(new User());

        assertDoesNotThrow(() -> profileService.updateElo(uuid, elo));
        verify(userRepository).updateElo(uuid, elo);
    }

    @Test
    void testUpdateElo_InvalidUUID() {
        String uuid = null; // null UUID
        Integer elo = 1500;

        assertThrows(IllegalArgumentException.class, () -> profileService.updateElo(uuid, elo));
    }

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

