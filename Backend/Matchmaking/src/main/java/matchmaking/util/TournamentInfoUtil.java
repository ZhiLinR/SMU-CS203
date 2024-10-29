package matchmaking.util;

import matchmaking.repository.*;
import matchmaking.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Utility class for retrieving tournament information.
 */
@Component
public class TournamentInfoUtil {

    @Autowired
    private MatchupsRepository matchupsRepository;

    @Autowired
    private SignupsRepository signupsRepository;

    /**
     * Retrieves the current round number for a given tournament.
     *
     * @param tournamentId the ID of the tournament.
     * @return the current round number.
     */
    public int getCurrentRoundByTournamentId(String tournamentId) {
        return matchupsRepository.getCurrentRoundByTournamentId(tournamentId);
    }

    /**
     * Retrieves all matchups for a given tournament.
     *
     * @param tournamentId the ID of the tournament.
     * @return a list of matchups for the tournament.
     */
    public List<Matchups> getMatchupsByTournamentId(String tournamentId) {
        return matchupsRepository.getMatchupsByTournamentId(tournamentId);
    }

    /**
     * Retrieves the list of signups for a given tournament.
     *
     * @param tournamentId the ID of the tournament.
     * @return a list of signups for the tournament.
     */
    public List<Signups> getSignupsByTournamentId(String tournamentId) {
        return signupsRepository.getSignupsByTournamentId(tournamentId);
    }
}

