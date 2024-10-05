package TournamentAdminService.controller;

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
    public ResponseEntity<String> createTournament(@RequestBody Tournament tournament) {
        tournamentService.createTournament(tournament);
        return ResponseEntity.ok("Successfully created tournament.");
    }

    @PutMapping("/{tournamentId}")
    public ResponseEntity<String> updateTournament(@PathVariable String tournamentId, @RequestBody Tournament tournament) {
        tournament.setTournamentID(tournamentId);
        tournamentService.updateTournament(tournament);
        return ResponseEntity.ok("Successfully updated tournament.");
    }

    @DeleteMapping("/{tournamentId}")
    public ResponseEntity<String> deleteTournament(@PathVariable String tournamentId) {
        tournamentService.deleteTournament(tournamentId);
        return ResponseEntity.ok("Successfully deleted tournament.");
    }

    @GetMapping("/{tournamentId}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable String tournamentId) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        return ResponseEntity.ok(tournament);
    }

    @GetMapping
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }
}
