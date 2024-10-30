package matchmaking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import matchmaking.util.ValidationUtil;
import matchmaking.util.TournamentInfoUtil;
import matchmaking.manager.MatchupManager;
import matchmaking.model.*;

import java.util.List;
import java.util.Set;

/**
 * The {@code MatchingService} class provides functionality for matchmaking
 * users
 * in a tournament based on their ELO ratings and the current round number.
 * It uses the {@link MatchupManager} to handle matchmaking logic.
 */
@Service
public class MatchingService {

    @Autowired
    private TournamentInfoUtil tournamentInfoUtil;

    @Autowired
    private MatchupManager matchupManager;

    /**
     * Generates unique matchups for users in the tournament with the specified
     * {@code tournamentId}. It retrieves the current round number, the list of
     * signups, and the previous matchups to ensure that players are matched
     * uniquely without repeating pairs.
     *
     * @param tournamentId the ID of the tournament for which to generate matchups.
     * @return a list of {@link Matchups} representing the newly generated matchups.
     * @throws IllegalArgumentException if the provided {@code tournamentId} is null
     *                                  or empty.
     */
    @Transactional
    public List<Matchups> generateUniqueMatchups(String tournamentId) {
        try {
            ValidationUtil.validateNotEmpty(tournamentId, "Tournament ID");

            // Use the TournamentInfoUtil to get current round and signups
            System.out.println("Getting info");
            int roundNum = tournamentInfoUtil.getCurrentRoundByTournamentId(tournamentId);
            List<Signups> signups = tournamentInfoUtil.getSignupsByTournamentId(tournamentId);
            List<Matchups> previousMatchups = tournamentInfoUtil.getMatchupsByTournamentId(tournamentId);

            ValidationUtil.isValidRoundNum(roundNum, signups.size());

            System.out.println("Finished getting info");

            // Create a set of played pairs to track unique matches
            Set<String> playedPairs = matchupManager.getPlayedPairs(previousMatchups);

            System.out.println("Creating Unique Matchups");
            List<Matchups> newMatchups = matchupManager.createUniqueMatchups(signups, tournamentId, roundNum,
                    playedPairs);

            for (Matchups m : newMatchups) {
                System.out.println(m.toString());
            }
            return newMatchups;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
