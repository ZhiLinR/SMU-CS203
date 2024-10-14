package TournamentAdminService.controller;

import TournamentAdminService.response.ApiResponse;
import TournamentAdminService.model.Tournament;
import TournamentAdminService.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tournaments")
public class TournamentController {
    @Autowired
    private TournamentService tournamentService;

    @PostMapping
    public ResponseEntity<ApiResponse> createTournament(@RequestBody Tournament tournament) {
        try {
            tournamentService.createTournament(tournament);
            return ResponseEntity.ok(new ApiResponse("Successfully created tournament.", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to create tournament: " + e.getMessage(), false));
        }
    }

    @PutMapping("/{tournamentId}")
    public ResponseEntity<ApiResponse> updateTournament(@PathVariable String tournamentId, @RequestBody Tournament tournament) {
        try {
            tournament.setTournamentID(tournamentId);
            tournamentService.updateTournament(tournament);
            return ResponseEntity.ok(new ApiResponse("Successfully updated tournament.", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to update tournament: " + e.getMessage(), false));
        }
    }

    @DeleteMapping("/{tournamentId}")
    public ResponseEntity<ApiResponse> deleteTournament(@PathVariable String tournamentId) {
        try {
            tournamentService.deleteTournament(tournamentId);
            return ResponseEntity.ok(new ApiResponse("Successfully deleted tournament.", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to delete tournament: " + e.getMessage(), false));
        }
    }

    @GetMapping("/{tournamentId}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable String tournamentId) {
        try {
            Tournament tournament = tournamentService.getTournamentById(tournamentId);
            return ResponseEntity.ok(tournament);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }
}