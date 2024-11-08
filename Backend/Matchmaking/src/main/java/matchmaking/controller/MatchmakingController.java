package matchmaking.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import matchmaking.exception.*;
import matchmaking.service.MatchingService;
import matchmaking.service.RatingService;
import matchmaking.util.*;
import matchmaking.model.*;

import org.springframework.http.HttpStatus;

/**
 * The {@code MatchmakingController} class is responsible for handling
 * matchmaking requests
 * related to tournaments. It provides endpoints for registering user profiles
 * and generating matchups based on the specified tournament ID.
 *
 * <p>
 * This class is annotated with {@link RestController} and
 * {@link RequestMapping}, making it a Spring REST controller that handles
 * requests to the "/api" endpoint.
 * </p>
 */
@CrossOrigin(origins = "${ORIGIN}")
@RestController
@RequestMapping("/api")
public class MatchmakingController {

    /**
     * Origin URL, set via `ORIGIN` property.
     */
    @Value("${ORIGIN}")
    private String origin;

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private RatingService ratingService;

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
     * prints each matchup to the console. It returns a success response if the
     * matchups are generated successfully, or an error response if there are any
     * issues during the process.
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
     * Ranks players for the specified tournament and retrieves the ranking results.
     *
     * This method retrieves the ranking updates for a given tournament ID,
     * prints the ranking results to the console, and returns a response indicating
     * whether the ranking process was successful or if there were any issues during
     * the process.
     *
     * @param tournamentId the ID of the tournament for which players are to be
     *                     ranked
     * @return a {@link ResponseEntity} containing a map with success or error
     *         message,
     *         along with the ranking results if successful
     * @throws IllegalArgumentException    if the tournament ID is invalid
     * @throws TournamentNotFoundException if no tournament is found with the given
     *                                     ID
     * @throws ResultsNotFoundException    if no ranking results are found for the
     *                                     tournament
     * @throws Exception                   for any other unexpected errors that may
     *                                     occur
     */
    @GetMapping("/ranking/{tournamentId}")
    public ResponseEntity<Map<String, Object>> rankPlayers(@PathVariable("tournamentId") String tournamentId) {
        try {
            List<String> rankingResults = ratingService.getRankingUpdateElo(tournamentId);
            for (int i = 0; i < rankingResults.size(); i++) {
                System.out.println((i + 1) + ": " + rankingResults.get(i));
            }

            // Create a response map in the desired format
            Map<String, Object> response = new HashMap<>();
            response.put("results", rankingResults);

            return ResponseManager.success("Players ranked successfully", response);
        } catch (IllegalArgumentException e) {
            return ResponseManager.error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (ResultsNotFoundException e) {
            return ResponseManager.error(HttpStatus.NOT_FOUND, e.getMessage());
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
     * without a tournament ID. It returns a {@code BAD_REQUEST} response with an
     * error message indicating that the tournament ID must not be null or empty.
     *
     * @return a {@link ResponseEntity} containing an error response with status
     *         {@code BAD_REQUEST} and a message specifying that the tournament ID
     *         is required.
     */
    @GetMapping("/matchmaking/")
    public ResponseEntity<Map<String, Object>> missingTournamentIdMatchmaking() {
        return ResponseManager.error(HttpStatus.BAD_REQUEST, "TournamentID must not be null or empty.");
    }

    /**
     * Handles requests to the ranking endpoint when no tournament ID is
     * provided.
     *
     * <p>
     * This endpoint is triggered when the {@code /api/ranking/} URL is accessed
     * without a tournament ID. It returns a {@code BAD_REQUEST} response with an
     * error message indicating that the tournament ID must not be null or empty.
     *
     * @return a {@link ResponseEntity} containing an error response with status
     *         {@code BAD_REQUEST} and a message specifying that the tournament ID
     *         is required.
     */
    @GetMapping("/ranking/")
    public ResponseEntity<Map<String, Object>> missingTournamentIdRanking() {
        return ResponseManager.error(HttpStatus.BAD_REQUEST, "TournamentID must not be null or empty.");
    }
}
