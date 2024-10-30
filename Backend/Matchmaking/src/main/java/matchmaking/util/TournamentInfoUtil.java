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

    /**
     * Creates a matchup between two players.
     *
     * @param player1 the first player.
     * @param player2 the second player.
     * @return a new Matchups object.
     */
    public Matchups createMatchup(Signups player1, Signups player2) {
        Matchups matchup = new Matchups();
        matchup.setPlayer1(player1.getUuid());
        matchup.setPlayer2(player2.getUuid());
        matchup.setPlayerWon(null);
        System.out.println("Matchup created: " + matchup);
        return matchup;
    }

    /**
     * Inserts matchups into the repository.
     *
     * @param matchups     the list of matchups to insert.
     * @param tournamentId the ID of the tournament.
     * @param roundNum     the current round number.
     */
    public void insertMatchups(List<Matchups> matchups, String tournamentId, int roundNum) {
        for (Matchups matchup : matchups) {
            matchupsRepository.insertMatchup(
                    matchup.getPlayer1(),
                    matchup.getPlayer2(),
                    matchup.getPlayerWon(),
                    tournamentId,
                    roundNum);
        }
    }
}
