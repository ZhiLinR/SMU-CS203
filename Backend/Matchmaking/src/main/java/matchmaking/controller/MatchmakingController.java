package matchmaking.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import matchmaking.exception.TournamentNotFoundException;
import matchmaking.service.MatchingService;
import matchmaking.util.*;

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
    @GetMapping("/matchmaking/{tournamentId}")
    public ResponseEntity<Map<String, Object>> createProfile(@PathVariable("tournamentId") String tournamentId) {
        try {

                return ResponseManager.success("Players matched successfully");
        } catch (IllegalArgumentException e) {
            return ResponseManager.error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (TournamentNotFoundException e) {
            return ResponseManager.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

}
