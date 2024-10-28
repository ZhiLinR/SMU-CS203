package matchmaking.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import matchmaking.dto.*;
import matchmaking.service.MatchingService;
import matching.util.*;

import org.springframework.http.HttpStatus;


/**
 * TODO: Javadocs
 */
@RestController
@RequestMapping("/api")
public class MatchmakingController {

    @Autowired
    private MatchingService matchingService;

    /**
     * Registers a new user profile.
     *
     * @param profileRequest the request containing profile information
     * @return a {@link ResponseEntity} with success or error response based on the registration result
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> createProfile(@RequestBody ProfileRequest profileRequest) {
        try {

                return ResponseManager.success("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseManager.error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

}
