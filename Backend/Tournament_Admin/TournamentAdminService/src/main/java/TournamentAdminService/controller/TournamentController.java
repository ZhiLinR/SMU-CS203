package TournamentAdminService.controller;

import TournamentAdminService.response.StandardApiResponse;
import TournamentAdminService.model.Tournament;
import TournamentAdminService.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "${ORIGIN}")
@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    @Value("${ORIGIN}")
    private String origin;

    @Autowired
    private TournamentService tournamentService;

     /**
     * Creates a new tournament.
     *
     * @param tournament the tournament object to be created
     * @return ResponseEntity containing an ApiResponse with success or failure message
     */

    @PostMapping
    public ResponseEntity<StandardApiResponse<Void>> createTournament(@RequestBody Tournament tournament) {
        try {
            tournament.updateStatus();
            tournamentService.createTournament(tournament);
            return ResponseEntity.ok(new StandardApiResponse<>(
                    "Tournament created successfully",
                    true,
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new StandardApiResponse<>(
                    "Failed to create tournament: " + e.getMessage(),
                    false,
                    null
            ));
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
    public ResponseEntity<StandardApiResponse<Tournament>> updateTournament(
            @PathVariable String tournamentId,
            @RequestBody Tournament tournament) {
        try {
            tournament.setTournamentID(tournamentId);
            if (tournament.getStartDate() != null) {
                tournament.setStartDate(tournament.getStartDate());
            }
            if (tournament.getEndDate() != null) {
                tournament.setEndDate(tournament.getEndDate());
            }
            tournament.updateStatus();
            tournamentService.updateTournament(tournament);
            return ResponseEntity.ok(new StandardApiResponse<>(
                    "Tournament updated successfully",
                    true,
                    tournament
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new StandardApiResponse<>(
                    "Failed to update tournament: " + e.getMessage(),
                    false,
                    null
            ));
        }
    }

    /**
     * Deletes an existing tournament by its ID.
     *
     * @param tournamentId the ID of the tournament to be deleted
     * @return ResponseEntity containing an ApiResponse with success or failure message
     */
    @DeleteMapping("/{tournamentId}")
    public ResponseEntity<StandardApiResponse<Void>> deleteTournament(@PathVariable String tournamentId) {
        try {
            tournamentService.deleteTournament(tournamentId);
            return ResponseEntity.ok(new StandardApiResponse<>(
                    "Tournament deleted successfully",
                    true,
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new StandardApiResponse<>(
                    "Failed to delete tournament: " + e.getMessage(),
                    false,
                    null
            ));
        }
    }

     /**
     * Retrieves a tournament by its ID.
     *
     * @param tournamentId the ID of the tournament to retrieve
     * @return ResponseEntity containing the tournament object or a not-found response if the tournament does not exist
     */
    @GetMapping("/{tournamentId}")
    public ResponseEntity<StandardApiResponse<Tournament>> getTournamentById(@PathVariable String tournamentId) {
        try {
            Tournament tournament = tournamentService.getTournamentById(tournamentId);
            return ResponseEntity.ok(new StandardApiResponse<>(
                    "Tournament retrieved successfully",
                    true,
                    tournament
            ));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new StandardApiResponse<>(
                    "Tournament not found: " + e.getMessage(),
                    false,
                    null
            ));
        }
    }

     /**
     * Retrieves all tournaments.
     *
     * @return ResponseEntity containing a list of all tournaments
     */
    @GetMapping
    public ResponseEntity<StandardApiResponse<List<Tournament>>> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(new StandardApiResponse<>(
                "Tournaments retrieved successfully",
                true,
                tournaments
        ));
    }


    @GetMapping("/health")
    public ResponseEntity<StandardApiResponse<Void>> healthCheckTournament() {
        return ResponseEntity.ok(new StandardApiResponse<>(
                "Application running successfully",
                true,
                null
        ));
    }
}