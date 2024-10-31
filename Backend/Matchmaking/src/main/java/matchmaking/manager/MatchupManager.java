package matchmaking.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import matchmaking.model.*;
import matchmaking.util.PlayerSorter;
import matchmaking.util.TournamentInfoUtil;
import matchmaking.util.ValidationUtil;
import matchmaking.util.MatchupUtil;

/**
 * Utility class for managing matchups in a tournament.
 * 
 * It helps generate matchups while avoiding duplicate pairs, handles scenarios
 * with an odd number of players by granting auto-wins (bye), and manages
 * previous match history to avoid rematches.
 */
@Component
public class MatchupManager {

    @Autowired
    private TournamentInfoUtil tournamentInfoUtil;

    @Autowired
    private PlayerSorter playerSorter;

    // A placeholder for "no player" in case of odd number of participants
    private final Signups NO_PLAYER = new Signups()
            .setUuid("null")
            .setTournamentId(null)
            .setElo(-1);

    /**
     * Creates unique matchups for players while avoiding duplicate pairs.
     * If the number of players is odd, an auto-win (bye) is assigned to one player.
     *
     * @param players      the list of players to match.
     * @param tournamentId the ID of the tournament.
     * @param roundNum     the current round number.
     * @param playedPairs  the set of already played pairs to avoid duplicates.
     * @return a list of {@link Matchups} objects representing the generated
     *         matchups.
     */
    public List<Matchups> createUniqueMatchups(List<Signups> players, String tournamentId,
            int roundNum, Set<String> playedPairs) {
        List<Matchups> matchups = new ArrayList<>();
        Set<String> pairedPlayers = new HashSet<>();

        players = playerSorter.sortPlayersForRound(players, tournamentId, roundNum);

        List<Pair<Signups, Signups>> playerPairs = generateMatchups(players, pairedPlayers, playedPairs);

        for (Pair<Signups, Signups> pair : playerPairs) {
            Matchups matchup = tournamentInfoUtil.createMatchup(pair.getFirst(), pair.getSecond(),
                    tournamentId, roundNum);

            // Double check valid matchup
            ValidationUtil.isValidMatchup(matchup);
            matchups.add(matchup);
        }

        if (players.size() % 2 != 0) {
            Pair<Signups, Signups> byePair = handleBye(players, matchups, pairedPlayers);
            ValidationUtil.isValidPair(byePair);
            Matchups byeMatchup = tournamentInfoUtil.createMatchup(byePair.getFirst(), byePair.getSecond(),
                    byePair.getFirst(), tournamentId, roundNum);

            // Double check valid matchup
            ValidationUtil.isValidMatchup(byeMatchup);
            matchups.add(byeMatchup);
        }

        ValidationUtil.isAllPlayersMatched(matchups, players);
        tournamentInfoUtil.insertMatchups(matchups, tournamentId, roundNum);

        return matchups;
    }

    /**
     * Generates matchups by pairing players while avoiding duplicate pairs.
     *
     * @param players       the list of players to pair.
     * @param pairedPlayers a set to track already paired players.
     * @param playedPairs   the set of previously played pairs.
     * @return a list of valid player pairs.
     */
    private List<Pair<Signups, Signups>> generateMatchups(List<Signups> players,
            Set<String> pairedPlayers,
            Set<String> playedPairs) {
        List<Pair<Signups, Signups>> validPairs = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            Signups player1 = players.get(i);

            if (pairedPlayers.contains(player1.getUuid())) {
                continue; // Skip if already paired
            }

            Signups player2 = findValidMatchup(player1, players, pairedPlayers, playedPairs, i);

            if (player2 != null) {
                validPairs.add(Pair.of(player1, player2));
                pairedPlayers.add(player1.getUuid());
                pairedPlayers.add(player2.getUuid());
                MatchupUtil.addPlayedPair(player1.getUuid(), player2.getUuid(), playedPairs);
            }
        }
        return validPairs;
    }

    /**
     * Attempts to find a valid matchup for the given player by avoiding duplicates.
     *
     * @param player1       the player for whom a pair is being searched.
     * @param players       the list of players to search through.
     * @param pairedPlayers a set to track players already paired.
     * @param playedPairs   the set of previously played pairs.
     * @param currentIndex  the current index in the player list.
     * @return a valid {@link Signups} object or {@code null} if no valid pair is
     *         found.
     */
    private Signups findValidMatchup(Signups player1, List<Signups> players,
            Set<String> pairedPlayers, Set<String> playedPairs, int currentIndex) {
        for (int j = currentIndex + 1; j < players.size(); j++) {
            Signups player2 = players.get(j);

            if (pairedPlayers.contains(player2.getUuid())) {
                continue; // Skip if already paired
            }

            if (ValidationUtil.isValidPair(player1.getUuid(), player2.getUuid(), playedPairs)) {
                return player2;
            }
        }
        return null;
    }

    /**
     * Handles assigning a bye to the last unpaired player if needed.
     * The player with a bye is paired with {@code NO_PLAYER} and automatically
     * wins.
     *
     * @param players       the list of players.
     * @param matchups      the list to store matchups.
     * @param pairedPlayers a set to track players already paired.
     * @return a {@link Pair} of the unpaired player and {@code NO_PLAYER}, or
     *         {@code null} if all players are paired.
     */
    private Pair<Signups, Signups> handleBye(List<Signups> players, List<Matchups> matchups,
            Set<String> pairedPlayers) {
        for (Signups player : players) {
            if (!pairedPlayers.contains(player.getUuid())) {
                return Pair.of(player, NO_PLAYER);
            }
        }
        return null;
    }
}
