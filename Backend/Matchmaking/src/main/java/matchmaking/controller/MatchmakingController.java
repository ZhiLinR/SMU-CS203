package matchmaking.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import matchmaking.exception.*;
import matchmaking.service.MatchingService;
import matchmaking.util.*;
import matchmaking.model.*;

import org.springframework.http.HttpStatus;

/**
 * The {@code MatchmakingController} class is responsible for handling
 * matchmaking requests
 * related to tournaments. It provides endpoints for registering user profiles
 * and generating
 * matchups based on the specified tournament ID.
 * <p>
 * This class is annotated with {@link RestController} and
 * {@link RequestMapping}, making it a
 * Spring REST controller that handles requests to the "/api" endpoint.
 * </p>
 */
@RestController
@RequestMapping("/api")
public class MatchmakingController {

    @Autowired
    private MatchingService matchingService;

    /**
     * Performs a health check for the application.
     *
     * This method is exposed as a GET endpoint to verify that the application is
     * running and responsive. It returns a success message if the health check
     * passes or an error message if an unexpected issue occurs during the check.
     *
     * @return a {@link ResponseEntity} containing a map with success or error
     *         message
     *         indicating the health status of the application
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        try {
            return ResponseManager.success("Health Check Success");
        } catch (Exception e) {
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    /**
     * Generates unique matchups for the specified tournament.
     *
     * This method retrieves the matchups associated with a given tournament ID and
     * prints
     * each matchup to the console. It returns a success response if the matchups
     * are generated
     * successfully, or an error response if there are any issues during the
     * process.
     *
     * @param tournamentId the ID of the tournament for which matchups are to be
     *                     generated
     * @return a {@link ResponseEntity} containing a map with success or error
     *         message
     * @throws IllegalArgumentException    if the tournament ID is invalid
     * @throws TournamentNotFoundException if no tournament is found with the given
     *                                     ID
     * @throws Exception                   for any other unexpected errors that may
     *                                     occur
     */
    @GetMapping("/matchmaking/{tournamentId}")
    public ResponseEntity<Map<String, Object>> matchPlayers(@PathVariable("tournamentId") String tournamentId) {
        try {
            List<Matchups> matchups = matchingService.generateUniqueMatchups(tournamentId);
            for (Matchups matchup : matchups) {
                System.out.println(matchup);
            }
            return ResponseManager.success("Players matched successfully");
        } catch (IllegalArgumentException e) {
            return ResponseManager.error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (InvalidRoundException e) {
            return ResponseManager.error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (TournamentNotFoundException e) {
            return ResponseManager.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    /**
     * Handles requests to the matchmaking endpoint when no tournament ID is
     * provided.
     *
     * <p>
     * This endpoint is triggered when the {@code /api/matchmaking/} URL is accessed
     * without
     * a tournament ID. It returns a {@code BAD_REQUEST} response with an error
     * message
     * indicating that the tournament ID must not be null or empty.
     *
     * @return a {@link ResponseEntity} containing an error response with status
     *         {@code BAD_REQUEST}
     *         and a message specifying that the tournament ID is required.
     */
    @GetMapping("/matchmaking/")
    public ResponseEntity<Map<String, Object>> missingTournamentId() {
        return ResponseManager.error(HttpStatus.BAD_REQUEST, "TournamentID must not be null or empty.");
    }
}
