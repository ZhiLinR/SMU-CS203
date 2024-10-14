package TournamentAdminService.service;

import TournamentAdminService.model.Tournament;
import TournamentAdminService.repository.TournamentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class TournamentService {
    @Autowired
    private TournamentRepository tournamentRepository;

    @Transactional
    public void createTournament(Tournament tournament) {
        LocalDate localStartDate = tournament.getStartDate();
        java.sql.Date sqlStartDate = Date.valueOf(localStartDate);
        LocalDate localEndDate = tournament.getEndDate();
        java.sql.Date sqlEndDate = Date.valueOf(localEndDate);
        tournamentRepository.createTournament(
                tournament.getName(), sqlStartDate, sqlEndDate, tournament.getLocation(),
                tournament.getPlayerLimit(), tournament.getIsActive(), tournament.getDescOID());
    }
    @Transactional
    public void updateTournament(Tournament tournament) {
        LocalDate localStartDate = tournament.getStartDate();
        java.sql.Date sqlStartDate = Date.valueOf(localStartDate);
        LocalDate localEndDate = tournament.getEndDate();
        java.sql.Date sqlEndDate = Date.valueOf(localEndDate);;
        tournamentRepository.updateTournament(tournament.getTournamentID(), tournament.getName(),
                sqlStartDate, sqlEndDate, tournament.getLocation(),
                tournament.getPlayerLimit(), tournament.getIsActive(), tournament.getDescOID());
    }
    @Transactional
    public void deleteTournament(String tournamentId) {

        tournamentRepository.deleteTournament(tournamentId);
    }
    @Transactional
    public Tournament getTournamentById(String tournamentId) {
        return tournamentRepository.getTournamentById(tournamentId);
    }
    @Transactional
    public List<Tournament> getAllTournaments() {

        return tournamentRepository.getAllTournaments();
    }

}