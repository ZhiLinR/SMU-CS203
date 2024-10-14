package TournamentAdminService.controller;

import TournamentAdminService.dto.GameResultRequest;
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
    public ResponseEntity<String> updateGameResult(@RequestBody GameResultRequest gameResultRequest) {
        matchupService.updateGameResult(gameResultRequest.getPlayerWon(), gameResultRequest.getTournamentID(), gameResultRequest.getRoundNum());
        return ResponseEntity.ok("Successfully updated tournament result.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteGameResult(@RequestBody GameResultRequest gameResultRequest) {
        matchupService.deleteGameResult(gameResultRequest.getPlayerWon(), gameResultRequest.getTournamentID(), gameResultRequest.getRoundNum());
        return ResponseEntity.ok("Successfully deleted tournament result");
    }

    @GetMapping("/results/{tournamentId}")
    public ResponseEntity<List<Matchup>> getGameResultsByTournamentId(@PathVariable String tournamentId) {
        List<Matchup> results = matchupService.getGameResultsByTournamentId(tournamentId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/participants/{tournamentId}")
    public ResponseEntity<List<String>> getParticipantsByTournamentId(@PathVariable String tournamentId) {
        List<String> participants = matchupService.getParticipantsByTournamentId(tournamentId);
        return ResponseEntity.ok(participants);
    }
}
