package user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import user.dto.ProfileRequest;
import user.model.User;
import user.repository.JWTokenRepository;
import user.repository.UserRepository;
import user.util.JwtUtil;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @Transactional
    public boolean createProfile(ProfileRequest profileRequest) {
        String email = profileRequest.getEmail();
        String password = profileRequest.getPassword();
        String name = profileRequest.getName();
        Byte isAdmin = profileRequest.getIsAdmin();

        // Validate email and password
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password are required");
        }

        if (name == null) {
            throw new IllegalArgumentException("Name is required");
        }

        if (isAdmin == null) {
            throw new IllegalArgumentException("Role is required");
        }

        // Validate isAdmin
        if (isAdmin != 0 && isAdmin != 1) {
            throw new IllegalArgumentException("Invalid value for isAdmin. It must be 0 or 1.");
        }

        // Check if the email already exists
        String existingUUID = userRepository.checkEmail(email);
        if (existingUUID != null) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }

        String encrypted = passwordEncoder.encode(password);

        // Call stored procedure to insert user
        Integer status = userRepository.insertUser(email, encrypted, name, isAdmin);

        // Return true if the user was created successfully, false otherwise
        return status == 1;
    }

    @Transactional
    public String authenticateUser(ProfileRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password are required");
        }

        // Call stored procedure to get hashed password
        String hashedPassword = userRepository.getHashedPassword(email);

        if (hashedPassword == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Verify password using bcrypt (use BCryptPasswordEncoder)
        if (!passwordEncoder.matches(password, hashedPassword)) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // If successful login, check if JWT already exists. If yes, logout prev first
        String uuid = userRepository.checkEmail(email);
        jwTokenRepository.checkJWT(uuid);

        // Generate new token with Username:Email, uuid:uuid, isAdmin: isAdmin
        Byte isAdmin = userRepository.getRoleByUUID(uuid);

        // Generate JWT token
        String jwt = jwtUtil.generateToken(email, uuid, isAdmin);

        jwTokenRepository.updateJWTLastLogin(email, jwt);

        return jwt;
    }

    @Transactional
    public void logoutUser(String uuid) {
        try {
            // Step 1: Get user profile and check for errors (handled in getProfileByUUID)
            User user = getProfileByUUID(uuid);
    
            // Step 2: Check if JWT token exists and update the logout
            Integer rowsAffected = jwTokenRepository.updateLogout(uuid);
    
            // Step 3: Handle cases where the update fails (e.g., no token found or already logged out)
            if (rowsAffected == 0) {
                throw new IllegalStateException("Logout failed. Either no JWT token exists for this user or the user is already logged out.");
            }
        } catch (Exception e) {
            // Re-throw the exception to be handled by the controller or other layers
            throw e;
        } 
    }

    @Transactional
    public User getProfileByUUID(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            throw new IllegalArgumentException("Invalid UUID: UUID cannot be null or empty.");
        }
    
        User user = userRepository.getProfile(uuid);
    
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
    
        return user; // Return the user profile if everything is valid
    }
    

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

    // private boolean isValidUUID(String uuid) {
    //     // Check if the UUID is null or has the wrong length
    //     if (uuid == null || uuid.length() != 36) {
    //         return false;
    //     }
    
    //     // Check if the UUID has the correct format with hyphens
    //     return uuid.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    // }
    
    @Transactional
    public void updateElo(String uuid, Integer elo) {
        if (uuid == null || uuid.isEmpty()) {
            throw new IllegalArgumentException("UUID is required");
        }
        if (elo == null) {
            throw new IllegalArgumentException("ELO is required");
        }

        try {
            // Call the stored procedure to update user

            // Check if user exists
            User user = getProfileByUUID(uuid);

            userRepository.updateElo(uuid, elo);
        } catch (Exception e) {
            // Log the exception and return false for failure
            // You may want to log the exception here
            throw e;
        }
    }

    @Transactional
    public boolean updateUser(String uuid, String email, String password, String name, Byte isAdmin, LocalDate dob) {
        // Validate input parameters
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

        // Convert LocalDate to java.sql.Date
        java.sql.Date sqlDate = dob != null ? java.sql.Date.valueOf(dob) : null;

        try {
            // Call the stored procedure to update user

            // Check if user exists
            User user = getProfileByUUID(uuid);

            String encrypted = passwordEncoder.encode(password);

            userRepository.updateUser(uuid, email, encrypted, name, isAdmin, sqlDate);
            return true; // Assume procedure succeeds if no exception is thrown
        } catch (Exception e) {
            // Log the exception and return false for failure
            // You may want to log the exception here
            throw e;
        }
    }


}



