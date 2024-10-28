package TournamentAdminService.service;

import TournamentAdminService.model.Tournament;
import TournamentAdminService.repository.TournamentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.time.LocalDate;
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
     * Creates a new tournament.
     * Converts the {@link LocalDate} to {@link java.sql.Date} before calling the repository.
     *
     * @param tournament the {@link Tournament} entity to create
     */
    @Transactional
    public void createTournament(Tournament tournament) {
        LocalDate localStartDate = tournament.getStartDate();
        java.sql.Date sqlStartDate = Date.valueOf(localStartDate);
        LocalDate localEndDate = tournament.getEndDate();
        java.sql.Date sqlEndDate = Date.valueOf(localEndDate);
        tournamentRepository.createTournament(
                tournament.getName(), sqlStartDate, sqlEndDate, tournament.getLocation(),
                tournament.getPlayerLimit(), tournament.getStatus(), tournament.getDescOID());
    }

    /**
     * Updates an existing tournament.
     * Converts the {@link LocalDate} to {@link java.sql.Date} before calling the repository.
     *
     * @param tournament the {@link Tournament} entity to update
     */
    @Transactional
    public void updateTournament(Tournament tournament) {
        LocalDate localStartDate = tournament.getStartDate();
        java.sql.Date sqlStartDate = Date.valueOf(localStartDate);
        LocalDate localEndDate = tournament.getEndDate();
        java.sql.Date sqlEndDate = Date.valueOf(localEndDate);;
        tournamentRepository.updateTournament(tournament.getTournamentID(), tournament.getName(),
                sqlStartDate, sqlEndDate, tournament.getLocation(),
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