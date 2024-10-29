package TournamentAdminService.controller;

import TournamentAdminService.dto.GameResultRequest;
import TournamentAdminService.response.ApiResponse;
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
    public ResponseEntity<ApiResponse> createGameResult(@RequestBody GameResultRequest gameResultRequest) {
        try {
            matchupService.createGameResult(
                    gameResultRequest.getPlayerWon(),
                    gameResultRequest.getTournamentID(),
                    gameResultRequest.getRoundNum()
            );
            return ResponseEntity.ok(new ApiResponse("Successfully created tournament result", true));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid input: " + e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to create game result: " + e.getMessage(), false));
        }
    }

    /**
     * Updates the game result for a specific tournament matchup.
     *
     * @param gameResultRequest the request containing the details of the game result (player who won, tournament ID, round number) inside the function body
     * @return ResponseEntity containing an ApiResponse with success or failure message
     */
    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateGameResult(@RequestBody GameResultRequest gameResultRequest) {
        try {
            matchupService.updateGameResult(gameResultRequest.getPlayerWon(), gameResultRequest.getTournamentID(), gameResultRequest.getRoundNum());
            return ResponseEntity.ok(new ApiResponse("Successfully updated tournament result.", true));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to update tournament result: " + e.getMessage(), false));
        }
        
    }

    /**
     * Deletes the game result for a specific tournament matchup.
     *
     * @param gameResultRequest the request containing the details of the game result (playerWon, tournamentID, roundNum) inside the function body
     * @return ResponseEntity containing an ApiResponse with success or failure message
     */

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteGameResult(@RequestBody GameResultRequest gameResultRequest) {
        try {
            matchupService.deleteGameResult(gameResultRequest.getPlayerWon(), gameResultRequest.getTournamentID(), gameResultRequest.getRoundNum());
            return ResponseEntity.ok(new ApiResponse("Successfully deleted tournament result", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to delete tournament result: " + e.getMessage(), false));
        }
        
    }

    /**
     * Retrieves all game results for a specific tournament by its ID.
     *
     * @param tournamentId the ID of the tournament inside the function body
     * @return ResponseEntity containing a list of matchups for the given tournament, or a not-found response
     */

    @GetMapping("/results/{tournamentId}")
    public ResponseEntity<ApiResponse> getGameResultsByTournamentId(@PathVariable String tournamentId) {
        try {
            List<Matchup> results = matchupService.getGameResultsByTournamentId(tournamentId);
            return ResponseEntity.ok(new ApiResponse("Successfully found game results for the tournament", true, results));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ApiResponse("Unable to find any game results for the tournament", false));
        }
       
    }

     /**
     * Retrieves the list of participants for a specific tournament by its ID.
     *
     * @param tournamentId the ID of the tournament inside the function body
     * @return ResponseEntity containing a list of participants, or a not-found response
     */
    @GetMapping("/participants/{tournamentId}")
    public ResponseEntity<ApiResponse> getParticipantsByTournamentId(@PathVariable String tournamentId) {
        try {
            List<String> participants = matchupService.getParticipantsByTournamentId(tournamentId);
            return ResponseEntity.ok(new ApiResponse("Successfully found participants for the tournament", true, participants));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ApiResponse("Unable to find any particpants for the tournament", false));
        }
        
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse> healthCheckGame() {
        return ResponseEntity.ok(new ApiResponse("Application running successfully", true));
    }
}
