package TournamentAdminService.service;

import TournamentAdminService.model.Tournament;
import TournamentAdminService.repository.TournamentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.sql.Date;
@Service
public class TournamentService {
    @Autowired
    private TournamentRepository tournamentRepository;

    @Transactional
    public void createTournament(Tournament tournament) {
        tournamentRepository.createTournament(
                tournament.getStartDate(), tournament.getEndDate(), tournament.getLocation(),
                tournament.getPlayerLimit(), tournament.getIsActive(), tournament.getDescOID(), tournament.getName());
    }
    @Transactional
    public void updateTournament(Tournament tournament) {
        tournamentRepository.updateTournament(tournament.getTournamentID(), tournament.getName(),
                tournament.getStartDate(), tournament.getEndDate(), tournament.getLocation(),
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

