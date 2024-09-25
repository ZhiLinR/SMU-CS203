package user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import user.dto.EloUpdateRequest;
import user.dto.LogoutRequest;
import user.dto.NamelistRequest;
import user.dto.ProfileRequest;
import user.dto.ProfileResponse;
import user.model.User;
import user.model.UserUpdateRequest;
import user.service.ProfileService;
import user.util.ResponseManager;

import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/profile")
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

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody LogoutRequest logoutRequest) {
        try {
            profileService.logoutUser(logoutRequest.getUuid());
            return ResponseManager.success("User logged out successfully.");
        } catch (Exception e) {
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    @GetMapping("/profile/{uuid}")
    public ResponseEntity<Map<String, Object>> getProfileByUUID(@PathVariable String uuid) {
        try {
            User user = profileService.getProfileByUUID(uuid);
            if (user != null) {
                ProfileResponse profileResponse = new ProfileResponse(user);
                return ResponseManager.success("Profile found", profileResponse);
            } else {
                return ResponseManager.error(HttpStatus.NOT_FOUND, "Profile not found");
            }
        } catch (Exception e) {
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    @PostMapping("/namelist")
    public ResponseEntity<Map<String, Object>> getNamesByUUIDList(@RequestBody NamelistRequest request) {
        try {
            List<String> uuids = request.getData();
            Map<String, String> result = profileService.getNamesByUUIDList(uuids);
            return ResponseManager.success("Names retrieved successfully", result);
        } catch (Exception e) {
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
    

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
        } catch (Exception e) {
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

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
        } catch (Exception e) {
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

}

