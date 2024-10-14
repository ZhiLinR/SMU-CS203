package TournamentAdminService.controller;

import TournamentAdminService.dto.GameResultRequest;
import TournamentAdminService.response.ApiResponse;
import TournamentAdminService.model.Matchup;
import TournamentAdminService.service.MatchupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/matchups")
public class MatchupController {

    @Autowired
    private MatchupService matchupService;

    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateGameResult(@RequestBody GameResultRequest gameResultRequest) {
        try {
            matchupService.updateGameResult(gameResultRequest.getPlayerWon(), gameResultRequest.getTournamentID(), gameResultRequest.getRoundNum());
            return ResponseEntity.ok(new ApiResponse("Successfully updated tournament result.", true));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to update tournament result: " + e.getMessage(), false));
        }
        
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteGameResult(@RequestBody GameResultRequest gameResultRequest) {
        try {
            matchupService.deleteGameResult(gameResultRequest.getPlayerWon(), gameResultRequest.getTournamentID(), gameResultRequest.getRoundNum());
            return ResponseEntity.ok(new ApiResponse("Successfully deleted tournament result", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to delete tournament result: " + e.getMessage(), false));
        }
        
    }

    @GetMapping("/results/{tournamentId}")
    public ResponseEntity<List<Matchup>> getGameResultsByTournamentId(@PathVariable String tournamentId) {
        try {
            List<Matchup> results = matchupService.getGameResultsByTournamentId(tournamentId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
       
    }

    @GetMapping("/participants/{tournamentId}")
    public ResponseEntity<List<String>> getParticipantsByTournamentId(@PathVariable String tournamentId) {
        try {
            List<String> participants = matchupService.getParticipantsByTournamentId(tournamentId);
            return ResponseEntity.ok(participants);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        
    }
}
