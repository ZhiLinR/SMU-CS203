package TournamentAdminService.service;

import TournamentAdminService.model.Matchup;
import TournamentAdminService.repository.MatchupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MatchupService {

    @Autowired
    private MatchupRepository matchupRepository;
    @Transactional
    public void updateGameResult(String playerWon, String tournamentId, Integer roundNum) {
        matchupRepository.updateGameResult(playerWon, tournamentId, roundNum);
    }
    @Transactional
    public void deleteGameResult(String playerWon, String tournamentId, Integer roundNum) {
        matchupRepository.deleteGameResult(playerWon, tournamentId, roundNum);
    }
    @Transactional
    public List<Matchup> getGameResultsByTournamentId(String tournamentId) {
        return matchupRepository.getGameResultsByTournamentId(tournamentId);
    }

    @Transactional
    public List<String> getParticipantsByTournamentId(String tournamentId) {
        return matchupRepository.getParticipantsByTournamentId(tournamentId);
    }
}
