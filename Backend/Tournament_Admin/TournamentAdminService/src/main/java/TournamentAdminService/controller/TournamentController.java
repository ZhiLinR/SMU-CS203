package TournamentAdminService.controller;

import TournamentAdminService.response.ApiResponse;
import TournamentAdminService.model.Tournament;
import TournamentAdminService.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {
    @Autowired
    private TournamentService tournamentService;

     /**
     * Creates a new tournament.
     *
     * @param tournament the tournament object to be created
     * @return ResponseEntity containing an ApiResponse with success or failure message
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createTournament(@RequestBody Tournament tournament) {
        try {
            tournamentService.createTournament(tournament);
            return ResponseEntity.ok(new ApiResponse("Successfully created tournament.", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to create tournament: " + e.getMessage(), false));
        }
    }


    /**
     * Updates an existing tournament by its ID.
     *
     * @param tournamentId the ID of the tournament to be updated
     * @param tournament the updated tournament object
     * @return ResponseEntity containing an ApiResponse with success or failure message
     */
    @PutMapping("/{tournamentId}")
    public ResponseEntity<ApiResponse> updateTournament(@PathVariable String tournamentId, @RequestBody Tournament tournament) {
        try {
            tournament.setTournamentID(tournamentId);
            tournamentService.updateTournament(tournament);
            return ResponseEntity.ok(new ApiResponse("Successfully updated tournament.", true, tournament));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to update tournament: " + e.getMessage(), false));
        }
    }


    /**
     * Deletes an existing tournament by its ID.
     *
     * @param tournamentId the ID of the tournament to be deleted
     * @return ResponseEntity containing an ApiResponse with success or failure message
     */
    @DeleteMapping("/{tournamentId}")
    public ResponseEntity<ApiResponse> deleteTournament(@PathVariable String tournamentId) {
        try {
            tournamentService.deleteTournament(tournamentId);
            return ResponseEntity.ok(new ApiResponse("Successfully deleted tournament.", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to delete tournament: " + e.getMessage(), false));
        }
    }

     /**
     * Retrieves a tournament by its ID.
     *
     * @param tournamentId the ID of the tournament to retrieve
     * @return ResponseEntity containing the tournament object or a not-found response if the tournament does not exist
     */
    @GetMapping("/{tournamentId}")
    public ResponseEntity<ApiResponse> getTournamentById(@PathVariable String tournamentId) {
        try {
            Tournament tournament = tournamentService.getTournamentById(tournamentId);
            return ResponseEntity.ok(new ApiResponse("Successfully retrieved tournament.", true, tournament));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ApiResponse("Tournament not found: " + e.getMessage(), false));
        }
    }

     /**
     * Retrieves all tournaments.
     *
     * @return ResponseEntity containing a list of all tournaments
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(new ApiResponse("Successfully retrieved tournaments.", true, tournaments));
    }
}