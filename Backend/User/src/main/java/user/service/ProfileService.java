package user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import user.dto.ProfileRequest;
import user.model.User;
import user.repository.JWTokenRepository;
import user.repository.UserRepository;
import user.util.JwtUtil;
import user.util.UserNotFoundException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Service class for managing user profiles, including creating, authenticating,
 * logging out, and updating user information. This class interacts with the 
 * {@link UserRepository} and {@link JWTokenRepository} to perform necessary 
 * database operations and uses {@link JwtUtil} for JWT token management.
 *
 * <p>This service class provides various methods for user profile management,
 * ensuring validation and transaction handling throughout the processes.</p>
 *
 * <p>All public methods in this service class are transactional, ensuring that 
 * operations are completed atomically.</p>
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
     * @return {@code true} if the profile was created successfully; {@code false} otherwise
     * @throws IllegalArgumentException if the provided profile request is invalid
     */
    @Transactional
    public boolean createProfile(ProfileRequest profileRequest) {
        validateProfileRequest(profileRequest);

        String encrypted = passwordEncoder.encode(profileRequest.getPassword());

        Integer status = userRepository.insertUser(
                profileRequest.getEmail(),
                encrypted,
                profileRequest.getName(),
                profileRequest.getIsAdmin()
        );

        return status == 1;
    }

    /**
     * Authenticates a user based on the provided login request and returns a JWT token.
     *
     * @param loginRequest the login request containing user credentials
     * @return the generated JWT token
     * @throws IllegalArgumentException if the login request is invalid or authentication fails
     */
    public String authenticateUser(ProfileRequest loginRequest) {
        validateLoginRequest(loginRequest);

        String hashedPassword = userRepository.getHashedPassword(loginRequest.getEmail());
        if (hashedPassword == null || !passwordEncoder.matches(loginRequest.getPassword(), hashedPassword)) {
            throw new IllegalArgumentException("Invalid email or password");
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
     * @throws UserNotFoundException if user not found with the given UUID
     * @throws IllegalArgumentException if the UUID is invalid
     * @throws IllegalStateException if logout fails
     */
    @Transactional
    public void logoutUser(String uuid) {
        try {
            // Get user profile and check for errors (handled in getProfileByUUID)
            User user = getProfileByUUID(uuid);
    
            // Check if JWT token exists and update the logout
            Integer rowsAffected = jwTokenRepository.updateLogout(uuid);
    
            // Handle cases where the update fails (e.g., no token found or already logged out)
            if (rowsAffected == 0) {
                throw new IllegalStateException("Logout failed. Either no JWT token exists for this user or the user is already logged out.");
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
     * @throws UserNotFoundException if no user is found with the given UUID
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
     * @param elo the new ELO rating to set
     * @throws IllegalArgumentException if the UUID is invalid or ELO is null
     * @throws UserNotFoundException if no user is found with the given UUID
     */
    @Transactional
    public void updateElo(String uuid, Integer elo) {
        validateUUID(uuid);
        if (elo == null) {
            throw new IllegalArgumentException("ELO is required");
        }

        try {
            // Check if user exists
            User user = getProfileByUUID(uuid);

            userRepository.updateElo(uuid, elo);
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * Updates user information based on provided parameters.
     *
     * @param uuid the UUID of the user
     * @param email the new email of the user
     * @param password the new password of the user
     * @param name the new name of the user
     * @param isAdmin the new admin status of the user
     * @param dob the new date of birth of the user
     * @return {@code true} if the user was updated successfully; {@code false} otherwise
     * @throws IllegalArgumentException if any provided parameters are invalid
     * @throws UserNotFoundException if no user is found with the given UUID
     */
    @Transactional
    public boolean updateUser(String uuid, String email, String password, String name, Byte isAdmin, LocalDate dob) {
        validateUserUpdate(uuid, email, password, name, isAdmin);

        // Convert LocalDate to java.sql.Date
        java.sql.Date sqlDate = dob != null ? java.sql.Date.valueOf(dob) : null;

        try {
            // Check if user exists
            User user = getProfileByUUID(uuid);

            String encrypted = passwordEncoder.encode(password);

            userRepository.updateUser(uuid, email, encrypted, name, isAdmin, sqlDate);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Validates the provided profile request for missing fields.
     *
     * @param profileRequest the profile request to validate
     * @throws IllegalArgumentException if the profile request is missing required fields or has invalid data
     */
    private void validateProfileRequest(ProfileRequest profileRequest) {
        if (profileRequest.getEmail() == null || profileRequest.getPassword() == null ||
            profileRequest.getName() == null || profileRequest.getIsAdmin() == null) {
            throw new IllegalArgumentException("Missing value (Email, password, name, and/or role).");
        }
        if (profileRequest.getIsAdmin() != 0 && profileRequest.getIsAdmin() != 1) {
            throw new IllegalArgumentException("Invalid value for Role.");
        }
        if (!isValidEmail(profileRequest.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (userRepository.checkEmail(profileRequest.getEmail()) != null) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }
    }

    /**
     * Validates the provided email format.
     *
     * @param email the email to validate
     * @return {@code true} if the email format is valid; {@code false} otherwise
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    /**
     * Validates the provided login request for missing fields.
     *
     * @param loginRequest the login request to validate
     * @throws IllegalArgumentException if the login request is missing required fields
     */
    private void validateLoginRequest(ProfileRequest loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            throw new IllegalArgumentException("Email and password are required");
        }
        if (!isValidEmail(loginRequest.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    /**
     * Validates the user update parameters for any invalid inputs.
     *
     * @param uuid the UUID of the user
     * @param email the new email
     * @param password the new password
     * @param name the new name
     * @param isAdmin the new admin status
     * @throws IllegalArgumentException if any provided parameters are invalid
     */
    private void validateUserUpdate(String uuid, String email, String password, String name, Byte isAdmin) {
        if (uuid == null || uuid.isEmpty()) {
            throw new IllegalArgumentException("UUID is required");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (isAdmin == null || (isAdmin != 0 && isAdmin != 1)) {
            throw new IllegalArgumentException("Invalid value for Role.");
        }
    }

    /**
     * Validates the UUID for any null or empty values.
     *
     * @param uuid the UUID to validate
     * @throws IllegalArgumentException if the UUID is null or empty
     */
    private void validateUUID(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            throw new IllegalArgumentException("UUID is required");
        }
    }
}



