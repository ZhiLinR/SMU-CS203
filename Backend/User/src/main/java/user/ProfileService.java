package user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public boolean createProfile(ProfileRequest profileRequest) {
        String email = profileRequest.getEmail();
        String password = profileRequest.getPassword();
        String name = profileRequest.getName();
        Byte isAdmin = profileRequest.getIsAdmin();

        // Validate email and password
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password are required");
        }

        // Validate isAdmin
        if (isAdmin != 0 && isAdmin != 1) {
            throw new IllegalArgumentException("Invalid value for isAdmin. It must be 0 or 1.");
        }

        // Call stored procedure to insert user
        Integer status = userRepository.insertUser(email, password, name, isAdmin);

        // Return true if the user was created successfully, false otherwise
        return status == 1;
    }

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

        // Generate JWT token
        String jwt = jwtUtil.generateToken(email);

        // Update last login time
        jwTokenRepository.updateJWTLastLogin(email, jwt);

        return jwt;
    }

    public User getProfileByUUID(String uuid) {
        return userRepository.getProfile(uuid);
    }

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
    

    public boolean updateUser(String uuid, String email, String password, String name, Byte isAdmin, LocalDate dob, String elo) {
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
            throw new IllegalArgumentException("Invalid value for isAdmin. It must be 0 or 1.");
        }
        
        // Convert LocalDate to java.sql.Date
        java.sql.Date sqlDate = dob != null ? java.sql.Date.valueOf(dob) : null;

        try {
            // Call the stored procedure to update user
            userRepository.updateUser(uuid, email, password, name, isAdmin, sqlDate, elo);
            return true; // Assume procedure succeeds if no exception is thrown
        } catch (Exception e) {
            // Log the exception and return false for failure
            // You may want to log the exception here
            return false;
        }
    }
}



