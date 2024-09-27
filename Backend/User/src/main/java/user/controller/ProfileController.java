package user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import user.dto.*;
import user.model.User;
import user.service.ProfileService;
import user.util.*;

import org.springframework.http.HttpStatus;


/**
 * REST controller for handling profile-related operations.
 * 
 * <p>This controller provides endpoints for user registration, login, logout, 
 * retrieving profiles, updating user information, and managing user ELO ratings.
 * It delegates business logic to the {@link ProfileService} and handles request 
 * validation and error management.
 * 
 * <p>Endpoints:
 * <ul>
 *   <li>{@code POST /api/register}: Registers a new user.</li>
 *   <li>{@code POST /api/login}: Authenticates a user and returns a JWT token.</li>
 *   <li>{@code POST /api/logout}: Logs out a user by invalidating their session.</li>
 *   <li>{@code POST /api/profile}: Retrieves a user profile by UUID.</li>
 *   <li>{@code POST /api/profile/all/names-only}: Retrieves names based on a list of UUIDs.</li>
 *   <li>{@code PUT /api/profile/update}: Updates user information (email, password, name, etc.).</li>
 *   <li>{@code PUT /api/profile/update/elo}: Updates the ELO rating for a user.</li>
 * </ul>
 * 
 * <p>Each endpoint uses {@link ResponseManager} to generate structured success or error responses 
 * with appropriate HTTP status codes.
 */
@RestController
@RequestMapping("/api")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    /**
     * Registers a new user profile.
     * 
     * @param profileRequest the request containing profile information
     * @return a {@link ResponseEntity} with success or error response based on the registration result
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> createProfile(@RequestBody ProfileRequest profileRequest) {
        try {
            boolean isCreated = profileService.createProfile(profileRequest);
            if (isCreated) {
                return ResponseManager.success("User registered successfully");
            } else {
                return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to register user");
            }
        } catch (IllegalArgumentException e) {
            return ResponseManager.error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    /**
     * Authenticates a user and returns a JWT token.
     * 
     * @param loginRequest the request containing login credentials
     * @return a {@link ResponseEntity} with the email and JWT token or an error message
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticateUser(@RequestBody ProfileRequest loginRequest) {
        try {
            // Call the service to authenticate the user and get the JWT token
            String jwtToken = profileService.authenticateUser(loginRequest);
            
            // Return a successful response with the JWT token
            return ResponseManager.success("Success", Map.of(
                "email", loginRequest.getEmail(),
                "token", jwtToken
            ));
        } catch (IllegalArgumentException e) {
            // Handle unauthorized errors
            return ResponseManager.error(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            // Handle other errors
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    /**
     * Logs out a user by invalidating their session or token.
     * 
     * @param uuidRequest the request containing the UUID of the user to log out
     * @return a {@link ResponseEntity} indicating whether the logout was successful
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody UUIDRequest uuidRequest) {
        try {
            profileService.logoutUser(uuidRequest.getUuid());
            return ResponseManager.success("User logged out successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseManager.error(HttpStatus.NOT_FOUND, "Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        } 
    }

    /**
     * Retrieves a user's profile based on their UUID.
     * 
     * @param uuidRequest the request containing the UUID of the user
     * @return a {@link ResponseEntity} with the user's info or an error message
     */
    @PostMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfileByUUID(@RequestBody UUIDRequest uuidRequest) {
        try {
            User user = profileService.getProfileByUUID(uuidRequest.getUuid());
            ProfileResponse profileResponse = new ProfileResponse(user);
            return ResponseManager.success("Profile found", profileResponse);
        } catch (UserNotFoundException e) {
            return ResponseManager.error(HttpStatus.NOT_FOUND, "Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    /**
     * Retrieves names of users based on a list of UUIDs.
     * 
     * @param request the request containing a list of UUIDs
     * @return a {@link ResponseEntity} with a dictionary of the users' uuid and names or an error message
     */
    @PostMapping("/profile/all/names-only")
    public ResponseEntity<Map<String, Object>> getNamesByUUIDList(@RequestBody NamelistRequest request) {
        try {
            List<String> uuids = request.getData();
            Map<String, String> result = profileService.getNamesByUUIDList(uuids);
            return ResponseManager.success("Names retrieved successfully", result);
        } catch (Exception e) {
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
    

    /**
     * Updates a user's profile information.
     * 
     * @param userUpdateRequest the request containing updated user details
     * @return a {@link ResponseEntity} indicating success or failure of the update
     */
    @PutMapping("/profile/update")
    public ResponseEntity<Map<String, Object>> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        try {
            boolean isUpdated = profileService.updateUser(
                userUpdateRequest.getUuid(),
                userUpdateRequest.getEmail(),
                userUpdateRequest.getPassword(),
                userUpdateRequest.getName(),
                userUpdateRequest.getIsAdmin(),
                userUpdateRequest.getDob()
            );

            if (isUpdated) {
                return ResponseManager.success("User updated successfully");
            } else {
                return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update user");
            }
        } catch (IllegalArgumentException e) {
            return ResponseManager.error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseManager.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    /**
     * Updates a user's ELO rating.
     * 
     * @param eloUpdateRequest the request containing the user's UUID and new ELO rating
     * @return a {@link ResponseEntity} indicating success or failure of the ELO update
     */
    @PutMapping("/profile/update/elo")
    public ResponseEntity<Map<String, Object>> updateElo(@RequestBody EloUpdateRequest eloUpdateRequest) {
        try {
            profileService.updateElo(
                eloUpdateRequest.getUuid(),
                eloUpdateRequest.getElo()
            );

            return ResponseManager.success("User ELO updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseManager.error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseManager.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

}

