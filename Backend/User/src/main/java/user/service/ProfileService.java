package user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import user.dto.ProfileRequest;
import user.exception.UnauthorizedException;
import user.exception.UserNotFoundException;
import user.model.User;
import user.repository.*;
import user.util.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing user profiles, including creating, authenticating,
 * logging out, and updating user information. This class interacts with the
 * {@link UserRepository} and {@link JWTokenRepository} to perform necessary
 * database operations and uses {@link JwtUtil} for JWT token management.
 *
 * <p>
 * This service class provides various methods for user profile management,
 * ensuring validation and transaction handling throughout the processes.
 * </p>
 *
 * <p>
 * All public methods in this service class are transactional, ensuring that
 * operations are completed atomically.
 * </p>
 */
@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTokenRepository jwTokenRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Creates a new user profile based on the provided {@link ProfileRequest}.
     *
     * @param profileRequest the profile request containing user details
     * @return {@code true} if the profile was created successfully; {@code false}
     *         otherwise
     * @throws IllegalArgumentException if the provided profile request is invalid
     */
    @Transactional
    public boolean createProfile(ProfileRequest profileRequest) {
        ValidationUtil.validateProfileRequest(profileRequest);

        if (userRepository.checkEmail(profileRequest.getEmail()) != null) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }

        String encrypted = passwordEncoder.encode(profileRequest.getPassword());

        Integer status = userRepository.insertUser(
                profileRequest.getEmail(),
                encrypted,
                profileRequest.getName(),
                profileRequest.getIsAdmin());

        return status == 1;
    }

    /**
     * Authenticates a user based on the provided login request and returns a JWT
     * token.
     *
     * @param loginRequest the login request containing user credentials
     * @return the generated JWT token
     * @throws IllegalArgumentException if the login request is invalid or
     *                                  authentication fails
     */
    @Transactional
    public String authenticateUser(ProfileRequest loginRequest) {
        ValidationUtil.validateLoginRequest(loginRequest);

        String hashedPassword = userRepository.getHashedPassword(loginRequest.getEmail());
        if (hashedPassword == null || !passwordEncoder.matches(loginRequest.getPassword(), hashedPassword)) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String uuid = userRepository.checkEmail(loginRequest.getEmail());
        jwTokenRepository.checkJWT(uuid);

        Byte isAdmin = userRepository.getRoleByUUID(uuid);
        String jwt = jwtUtil.generateToken(loginRequest.getEmail(), uuid, isAdmin);

        jwTokenRepository.updateJWTLastLogin(loginRequest.getEmail(), jwt);
        return jwt;
    }

    /**
     * Logs out a user by their UUID.
     *
     * @param uuid the UUID of the user to log out
     * @throws UserNotFoundException    if user not found with the given UUID
     * @throws IllegalArgumentException if the UUID is invalid
     * @throws IllegalStateException    if logout fails
     */
    @Transactional
    public void logoutUser(String uuid) {
        try {
            // Get user profile and check for errors (handled in getProfileByUUID)
            @SuppressWarnings("unused")
            User user = getProfileByUUID(uuid);

            // Check if JWT token exists and update the logout
            Integer rowsAffected = jwTokenRepository.updateLogout(uuid);

            // Handle cases where the update fails (e.g., no token found or already logged
            // out)
            if (rowsAffected == 0) {
                throw new IllegalStateException(
                        "Logout failed. Either no JWT token exists for this user or the user is already logged out.");
            }
        } catch (Exception e) {
            // Re-throw the exception to be handled by the controller or other layers
            throw e;
        }
    }

    /**
     * Retrieves the user profile by the specified UUID.
     *
     * @param uuid the UUID of the user
     * @return the {@link User} object associated with the UUID
     * @throws IllegalArgumentException if the UUID is null or empty
     * @throws UserNotFoundException    if no user is found with the given UUID
     */
    @Transactional
    public User getProfileByUUID(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            throw new IllegalArgumentException("Invalid UUID: UUID cannot be null or empty.");
        }

        User user = userRepository.getProfile(uuid);

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        return user; // Return the user profile if everything is valid
    }

    /**
     * Retrieves names of users based on a list of UUIDs.
     *
     * @param uuids the list of UUIDs to search for
     * @return a map containing UUIDs as keys and corresponding user names as values
     */
    @Transactional
    public Map<String, String> getNamesByUUIDList(List<String> uuids) {
        Map<String, String> nameMap = new HashMap<>();

        if (uuids == null || uuids.isEmpty()) {
            return nameMap; // Return empty map for an empty or null list
        }

        // Fetch names for valid UUIDs
        for (String uuid : uuids) {
            try {
                // Call the stored procedure for each UUID
                String name = userRepository.getName(uuid);
                if (name != null) {
                    nameMap.put(uuid, name);
                } else {
                    nameMap.put(uuid, "Not Found"); // Indicate UUID was not found
                }
            } catch (Exception e) {
                // Log exception and handle as needed
                nameMap.put(uuid, "Error retrieving name");
            }
        }
        return nameMap;
    }

    /**
     * Updates the ELO rating for the user identified by the specified UUID.
     *
     * @param uuid the UUID of the user
     * @param elo  the new ELO rating to set
     * @throws IllegalArgumentException if the UUID is invalid or ELO is null
     * @throws UserNotFoundException    if no user is found with the given UUID
     */
    @Transactional
    public void updateElo(String uuid, Integer elo) {
        ValidationUtil.validateUUID(uuid);

        if (elo == null || elo < 0) {
            throw new IllegalArgumentException("Invalid or missing ELO");
        }

        try {
            // Check if user exists
            @SuppressWarnings("unused")
            User user = getProfileByUUID(uuid);

            userRepository.updateElo(uuid, elo);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Updates user information based on provided parameters.
     *
     * @param uuid     the UUID of the user
     * @param email    the new email of the user
     * @param password the new password of the user
     * @param name     the new name of the user
     * @param isAdmin  the new admin status of the user
     * @param dob      the new date of birth of the user
     * @return {@code true} if the user was updated successfully; {@code false}
     *         otherwise
     * @throws IllegalArgumentException if any provided parameters are invalid
     * @throws UserNotFoundException    if no user is found with the given UUID
     */
    @Transactional
    public boolean updateUser(String uuid, String email, String password, String name, Byte isAdmin, LocalDate dob) {
        ValidationUtil.validateUserUpdate(uuid, email, password, name, isAdmin);

        // Convert LocalDate to java.sql.Date
        java.sql.Date sqlDate = dob != null ? java.sql.Date.valueOf(dob) : null;

        try {
            // Check if user exists
            @SuppressWarnings("unused")
            User user = getProfileByUUID(uuid);

            String encrypted = passwordEncoder.encode(password);

            userRepository.updateUser(uuid, email, encrypted, name, isAdmin, sqlDate);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

}
