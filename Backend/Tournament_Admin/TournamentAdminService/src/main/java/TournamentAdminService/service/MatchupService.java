package TournamentAdminService.service;

import TournamentAdminService.model.Matchup;
import TournamentAdminService.repository.MatchupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class for managing matchup-related operations.
 * This class provides methods to update, delete, and retrieve matchups and participants.
 */
@Service
public class MatchupService {
    @Autowired
    private MatchupRepository matchupRepository;

    /**
     * Creates a new game result for a tournament matchup.
     *
     * @param playerWon the player who won the game
     * @param tournamentId the ID of the tournament
     * @param roundNum the round number
     */
    @Transactional
    public void createGameResult(String playerWon, String tournamentId, Integer roundNum) {
        // Input validation
        if (tournamentId == null || roundNum == null || playerWon == null) {
            throw new IllegalArgumentException("All fields are required");
        }

        // Verify the matchup exists and the winner is a valid player
        List<String> participants = getParticipantsByTournamentId(tournamentId);
        if (!participants.contains(playerWon)) {
            throw new IllegalArgumentException("Winner must be a participant in the tournament");
        }

        matchupRepository.createGameResult(playerWon, tournamentId, roundNum);
    }

    /**
     * Updates the game result for a given tournament and round.
     *
     * @param playerWon   the player who won the game
     * @param tournamentId the ID of the tournament
     * @param roundNum    the round number of the matchup
     */
    @Transactional
    public void updateGameResult(String playerWon, String tournamentId, Integer roundNum) {
        matchupRepository.updateGameResult(playerWon, tournamentId, roundNum);
    }

    /**
     * Deletes the game result for a given player in a tournament and round.
     *
     * @param playerWon   the player whose result is being deleted
     * @param tournamentId the ID of the tournament
     * @param roundNum    the round number of the matchup
     */
    @Transactional
    public void deleteGameResult(String playerWon, String tournamentId, Integer roundNum) {
        matchupRepository.deleteGameResult(playerWon, tournamentId, roundNum);
    }

    /**
     * Retrieves the game results for a specific tournament.
     *
     * @param tournamentId the ID of the tournament
     * @return a list of matchups containing the results of the tournament
     */
    @Transactional
    public List getGameResultsByTournamentId(String tournamentId) {
        return matchupRepository.getGameResultsByTournamentId(tournamentId);
    }

    /**
     * Retrieves the list of participants for a specific tournament.
     *
     * @param tournamentId the ID of the tournament
     * @return a list of participant names for the tournament
     */
    @Transactional
    public List getParticipantsByTournamentId(String tournamentId) {
        return matchupRepository.getParticipantsByTournamentId(tournamentId);
    }
}
