package TournamentAdminService.controller;

import TournamentAdminService.dto.GameResultRequest;
import TournamentAdminService.response.StandardApiResponse;
import TournamentAdminService.model.Matchup;
import TournamentAdminService.service.MatchupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/matchups")
public class MatchupController {

    @Autowired
    private MatchupService matchupService;

    /**
     * Creates a new game result for a tournament matchup.
     *
     * @param gameResultRequest the request containing the details of the game result
     * @return ResponseEntity containing an ApiResponse with success or failure message
     */
    @PostMapping("/results")
    public ResponseEntity<StandardApiResponse<Void>> createGameResult(@RequestBody GameResultRequest gameResultRequest) {
        try {
            matchupService.createGameResult(
                    gameResultRequest.getPlayerWon(),
                    gameResultRequest.getTournamentID(),
                    gameResultRequest.getRoundNum()
            );
            return ResponseEntity.ok(new StandardApiResponse<>(
                    "Game result created successfully",
                    true,
                    null
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new StandardApiResponse<>(
                    "Invalid input: " + e.getMessage(),
                    false,
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandardApiResponse<>(
                    "Failed to create game result: " + e.getMessage(),
                    false,
                    null
            ));
        }
    }

    /**
     * Updates the game result for a specific tournament matchup.
     *
     * @param gameResultRequest the request containing the details of the game result (player who won, tournament ID, round number) inside the function body
     * @return ResponseEntity containing an ApiResponse with success or failure message
     */
    @PutMapping("/results")
    public ResponseEntity<StandardApiResponse<Void>> updateGameResult(@RequestBody GameResultRequest gameResultRequest) {
        try {
            matchupService.updateGameResult(
                    gameResultRequest.getPlayerWon(),
                    gameResultRequest.getTournamentID(),
                    gameResultRequest.getRoundNum()
            );
            return ResponseEntity.ok(new StandardApiResponse<>(
                    "Game result updated successfully",
                    true,
                    null
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new StandardApiResponse<>(
                    "Invalid input: " + e.getMessage(),
                    false,
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandardApiResponse<>(
                    "Failed to update game result: " + e.getMessage(),
                    false,
                    null
            ));
        }
    }

    /**
     * Deletes the game result for a specific tournament matchup.
     *
     * @param gameResultRequest the request containing the details of the game result (playerWon, tournamentID, roundNum) inside the function body
     * @return ResponseEntity containing an ApiResponse with success or failure message
     */
    @DeleteMapping("/results")
    public ResponseEntity<StandardApiResponse<Void>> deleteGameResult(@RequestBody GameResultRequest gameResultRequest) {
        try {
            matchupService.deleteGameResult(
                    gameResultRequest.getPlayerWon(),
                    gameResultRequest.getTournamentID(),
                    gameResultRequest.getRoundNum()
            );
            return ResponseEntity.ok(new StandardApiResponse<>(
                    "Game result deleted successfully",
                    true,
                    null
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new StandardApiResponse<>(
                    "Invalid input: " + e.getMessage(),
                    false,
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandardApiResponse<>(
                    "Failed to delete game result: " + e.getMessage(),
                    false,
                    null
            ));
        }
    }



    /**
     * Retrieves all game results for a specific tournament by its ID.
     *
     * @param tournamentId the ID of the tournament inside the function body
     * @return ResponseEntity containing a list of matchups for the given tournament, or a not-found response
     */

    @GetMapping("/results/{tournamentId}")
    public ResponseEntity<StandardApiResponse<List<Matchup>>> getGameResultsByTournamentId(@PathVariable String tournamentId) {
        try {
            List<Matchup> results = matchupService.getGameResultsByTournamentId(tournamentId);
            return ResponseEntity.ok(new StandardApiResponse<>(
                    "Game results retrieved successfully",
                    true,
                    results
            ));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new StandardApiResponse<>(
                    "No game results found for tournament",
                    false,
                    null
            ));
        }
    }

     /**
     * Retrieves the list of participants for a specific tournament by its ID.
     *
     * @param tournamentId the ID of the tournament inside the function body
     * @return ResponseEntity containing a list of participants, or a not-found response
     */
    @GetMapping("/participants/{tournamentId}")
    public ResponseEntity<StandardApiResponse<List<String>>> getParticipantsByTournamentId(@PathVariable String tournamentId) {
        try {
            List<String> participants = matchupService.getParticipantsByTournamentId(tournamentId);
            return ResponseEntity.ok(new StandardApiResponse<>(
                    "Participants retrieved successfully",
                    true,
                    participants
            ));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new StandardApiResponse<>(
                    "No participants found for tournament",
                    false,
                    null
            ));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<StandardApiResponse<Void>> healthCheckGame() {
        return ResponseEntity.ok(new StandardApiResponse<>(
                "Service is running",
                true,
                null
        ));
    }
}
