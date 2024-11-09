package matchmaking.util;

import matchmaking.repository.*;
import matchmaking.dto.PlayerWins;
import matchmaking.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for retrieving tournament information.
 * This class provides methods to access tournament-related data,
 * such as current round numbers, matchups, and player results.
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
     * @return the current round number, incremented by one.
     * @throws IllegalArgumentException if the {@code tournamentId} is null or
     *                                  empty.
     */
    @Transactional
    public int getCurrentRoundByTournamentId(String tournamentId) {
        ValidationUtil.validateNotEmpty(tournamentId, "Tournament ID");

        return matchupsRepository.getCurrentRoundByTournamentId(tournamentId) + 1;
    }

    /**
     * Retrieves all matchups for a given tournament.
     *
     * @param tournamentId the ID of the tournament.
     * @return a list of {@link Matchups} for the tournament.
     * @throws IllegalArgumentException if the {@code tournamentId} is null or
     *                                  empty.
     */
    @Transactional
    public List<Matchups> getMatchupsByTournamentId(String tournamentId) {
        ValidationUtil.validateNotEmpty(tournamentId, "Tournament ID");

        return matchupsRepository.getMatchupsByTournamentId(tournamentId);
    }

    /**
     * Retrieves the list of signups for a given tournament.
     *
     * @param tournamentId the ID of the tournament.
     * @return a list of {@link Signups} for the tournament.
     * @throws IllegalArgumentException if the {@code tournamentId} is null or
     *                                  empty.
     */
    @Transactional
    public List<Signups> getSignupsByTournamentId(String tournamentId) {
        ValidationUtil.validateNotEmpty(tournamentId, "Tournament ID");

        return signupsRepository.getSignupsByTournamentId(tournamentId);
    }

    /**
     * Retrieves all player results for a given tournament.
     *
     * @param tournamentId the ID of the tournament.
     * @return a list of {@link PlayerWins} for the tournament.
     * @throws IllegalArgumentException if the {@code tournamentId} is null or
     *                                  empty.
     */
    @Transactional
    public List<PlayerWins> getPlayerWinsByTournamentId(String tournamentId) {
        ValidationUtil.validateNotEmpty(tournamentId, "Tournament ID");

        List<Object[]> results = matchupsRepository.getPlayerWinsByTournamentId(tournamentId);

        return results.stream().map(this::mapToPlayerWins).collect(Collectors.toList());
    }

    /**
     * Creates a matchup between two players.
     *
     * @param player1      the first player.
     * @param player2      the second player.
     * @param tournamentId the ID of the tournament.
     * @param roundNum     the current round number.
     * @return a new {@link Matchups} object representing the matchup.
     */
    @Transactional
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
     * Creates a matchup between two players with a specified winner.
     *
     * @param player1      the first player.
     * @param player2      the second player.
     * @param playerWon    the player who won the matchup.
     * @param tournamentId the ID of the tournament.
     * @param roundNum     the current round number.
     * @return a new {@link Matchups} object representing the matchup with the
     *         winner set.
     */
    @Transactional
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
     * @param matchups     the list of {@link Matchups} to insert.
     * @param tournamentId the ID of the tournament.
     * @param roundNum     the current round number.
     * @throws IllegalArgumentException if the {@code matchups} list is null or
     *                                  empty.
     */
    @Transactional
    public void insertMatchups(List<Matchups> matchups, String tournamentId, int roundNum) {
        if (matchups.size() == 0) {
            throw new IllegalArgumentException("Missing matchup to insert");
        }
        for (Matchups matchup : matchups) {
            matchupsRepository.insertMatchup(
                    matchup.getId().getPlayer1(),
                    matchup.getId().getPlayer2(),
                    matchup.getPlayerWon(),
                    tournamentId,
                    roundNum);
        }
    }

    /**
     * Updates the Elo rating for a player in the signups repository.
     *
     * @param uuid the unique identifier of the player whose Elo rating is to be
     *             updated
     * @param elo  the new Elo rating to assign to the player
     * @throws IllegalArgumentException if the UUID is null or empty, or if the Elo
     *                                  rating is non-positive
     */
    @Transactional
    public void updateSignupsPlayerElo(String uuid, int elo) {
        ValidationUtil.validateNotEmpty(uuid, "UUID");

        if (elo <= 0) {
            throw new IllegalArgumentException("Elo must not be null or empty");
        }

        signupsRepository.updateElo(uuid, elo);
    }

    /**
     * Maps the result set from the database to a {@link PlayerWins} object.
     *
     * @param result the result array from the database query.
     * @return a new {@link PlayerWins} object populated with data from the result
     *         array.
     */
    private PlayerWins mapToPlayerWins(Object[] result) {
        PlayerWins playerWins = new PlayerWins();
        playerWins.setUuid((String) result[0]);
        playerWins.setWins(result[1] != null ? ((Number) result[1]).intValue() : 0);
        playerWins.setDraws(result[2] != null ? ((Number) result[2]).intValue() : 0);
        return playerWins;
    }
}
