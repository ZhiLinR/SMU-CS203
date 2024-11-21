package TournamentAdminService.service;

import TournamentAdminService.model.Tournament;
import TournamentAdminService.repository.TournamentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

/**
 * Service class for managing tournament-related operations.
 * This class contains methods to create, update, delete, and retrieve tournaments.
 */
@Service
public class TournamentService {
    @Autowired
    private TournamentRepository tournamentRepository;

    /**
     * Creates a new tournament entry in the repository.
     *
     * @param tournament The {@link Tournament} object containing the tournament details to be created.
     */
    @Transactional
    public void createTournament(Tournament tournament) {
        tournament.updateStatus();
        tournamentRepository.createTournament(
                tournament.getName(), tournament.getStartDate(), tournament.getEndDate(), tournament.getLocation(),
                tournament.getPlayerLimit(), tournament.getStatus(), tournament.getDescOID());
    }

    /**
     * Updates an existing tournament in the repository.
     *
     * @param tournament The {@link Tournament} object containing the tournament details to be updated.
     */
    @Transactional
    public void updateTournament(Tournament tournament) {
        tournament.updateStatus();
        tournamentRepository.updateTournament(
                tournament.getTournamentID(),tournament.getName(), tournament.getStartDate(), tournament.getEndDate(), tournament.getLocation(),
                tournament.getPlayerLimit(), tournament.getStatus(), tournament.getDescOID());
    }

    /**
     * Deletes a tournament by its ID.
     *
     * @param tournamentId the {@link String} ID of the tournament to delete
     */
    @Transactional
    public void deleteTournament(String tournamentId) {

        tournamentRepository.deleteTournament(tournamentId);
    }

    /**
     * Retrieves a tournament by its ID.
     *
     * @param tournamentId the {@link String} ID of the tournament to retrieve
     * @return the {@link Tournament} entity
     */
    @Transactional
    public Tournament getTournamentById(String tournamentId) {
        return tournamentRepository.getTournamentById(tournamentId);
    }

    /**
     * Retrieves all tournaments.
     *
     * @return a {@link List} of all {@link Tournament} entities
     */
    @Transactional
    public List<Tournament> getAllTournaments() {

        return tournamentRepository.getAllTournaments();
    }

}