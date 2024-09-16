package com.example.profileapi.service;

import com.example.profileapi.model.ProfileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProfileService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ResponseEntity<?> createProfile(ProfileRequest profileRequest) {
        String email = profileRequest.getEmail();
        String password = profileRequest.getPassword();
        int isAdmin = profileRequest.getIsAdmin();


        // Validate email and password
        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email and password are required"));
        }

        // Validate isAdmin
        if (isAdmin != 0 && isAdmin != 1) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid value for isAdmin. It must be 0 or 1."));
        }

        try {
            // Call stored procedure to insert user and role
            String call = "CALL InsertUserAndRole(?, ?, ?, @status)";
            jdbcTemplate.update(call, email, password, isAdmin);

            // Fetch output parameter
            Integer status = jdbcTemplate.queryForObject("SELECT @status", Integer.class);

            if (status == 1) {
                return ResponseEntity.ok(Map.of("message", "User registered successfully"));
            } else {
                return ResponseEntity.status(500).body(Map.of("message", "Failed to register user"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> authenticateUser(ProfileRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email and password are required"));
        }

        try {
            // Call stored procedure to get hashed password
            String call = "CALL GetHashedPassword(?, @hashed_password)";
            jdbcTemplate.update(call, email);

            // Fetch hashed password
            String hashedPassword = jdbcTemplate.queryForObject("SELECT @hashed_password", String.class);

            if (hashedPassword == null) {
                return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
            }

            // Verify password using bcrypt (use BCryptPasswordEncoder)
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(password, hashedPassword)) {
                jdbcTemplate.update("CALL UpdateLastLogin(?)", email);
                return ResponseEntity.ok(Map.of("message", "Success", "email", email));
            } else {
                return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> getProfileByUUID(String uuid) {
        User user = userRepository.findByUUID(uuid);
        if (user != null) {
            ProfileResponse profileResponse = new ProfileResponse(user);
            return ResponseEntity.ok(profileResponse);
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Profile not found"));
        }
    }    
}

