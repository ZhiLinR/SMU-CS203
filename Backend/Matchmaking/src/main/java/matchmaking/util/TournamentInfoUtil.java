package matchmaking.util;

import matchmaking.repository.*;
import matchmaking.dto.PlayerWins;
import matchmaking.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
        return matchupsRepository.getCurrentRoundByTournamentId(tournamentId) + 1;
    }

    /**
     * Retrieves all matchups for a given tournament.
     *
     * @param tournamentId the ID of the tournament.
     * @return a list of matchups for the tournament.
     */
    public List<Matchups> getMatchupsByTournamentId(String tournamentId) {
        System.out.println(matchupsRepository.getMatchupsByTournamentId(tournamentId));
        return matchupsRepository.getMatchupsByTournamentId(tournamentId);
    }

    /**
     * Retrieves all player results for a given tournament.
     *
     * @param tournamentId the ID of the tournament.
     * @return a list of matchups for the tournament.
     */
    public List<PlayerWins> getPlayerWinsByTournamentId(String tournamentId) {
        List<Object[]> results = matchupsRepository.getPlayerWinsByTournamentId(tournamentId);

        return results.stream().map(this::mapToPlayerWins).collect(Collectors.toList());
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
    public Matchups createMatchup(Signups player1, Signups player2, String tournamentId, int roundNum) {
        MatchupsId matchupsId = new MatchupsId();
        matchupsId.setPlayer1(player1.getUuid());
        matchupsId.setPlayer2(player2.getUuid());
        matchupsId.setTournamentId(tournamentId);

        Matchups matchup = new Matchups();
        matchup.setId(matchupsId);
        matchup.setRoundNum(roundNum);
        matchup.setPlayerWon(null);
        System.out.println("Matchup created: " + matchup);
        return matchup;
    }

    /**
     * Creates a matchup between two players.
     *
     * @param player1 the first player.
     * @param player2 the second player.
     * @return a new Matchups object.
     */
    public Matchups createMatchup(Signups player1, Signups player2, Signups playerWon, String tournamentId,
            int roundNum) {
        MatchupsId matchupsId = new MatchupsId();
        matchupsId.setPlayer1(player1.getUuid());
        matchupsId.setPlayer2(player2.getUuid());
        matchupsId.setTournamentId(tournamentId);

        Matchups matchup = new Matchups();
        matchup.setId(matchupsId);
        matchup.setRoundNum(roundNum);
        matchup.setPlayerWon(playerWon.getUuid());
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
                    matchup.getId().getPlayer1(),
                    matchup.getId().getPlayer2(),
                    matchup.getPlayerWon(),
                    tournamentId,
                    roundNum);
        }
    }

    private PlayerWins mapToPlayerWins(Object[] result) {
        PlayerWins playerWins = new PlayerWins();
        playerWins.setUuid((String) result[0]);
        playerWins.setWins(result[1] != null ? ((Number) result[1]).intValue() : 0);
        playerWins.setDraws(result[2] != null ? ((Number) result[2]).intValue() : 0);
        return playerWins;
    }
}
