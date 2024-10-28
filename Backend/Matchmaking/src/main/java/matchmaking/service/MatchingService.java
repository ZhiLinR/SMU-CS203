package matchmaking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import matchmaking.repository.MatchupsRepository;
import matchmaking.repository.SignupsRepository;
import matchmaking.model.Signups;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Javadocs
 */
@Service
public class MatchingService {

    @Autowired
    private MatchupsRepository matchupsRepository;

    @Autowired
    private SignupsRepository signupsRepository;

    /**
     * Matchmake users in tournament with {@code tournamentId}
     * based on ELO and round number.
     *
     * @param request the matching request containing the tournamentID.
     * @return {@code true} if the users successfully matched; {@code false} otherwise
     * @throws IllegalArgumentException if the provided profile request is invalid
     */
    @Transactional
    public void createProfile(String tournamentId) {
        // Get List<Signups> for UUID-ELO reference
        // Get Round num for current tournament
        // Get current player wins and draws (Win: 1pt, Draw: 0.5pts)
            // Call MatchmakingUtil.calculatePoints to calculate points of all users
    }
}
