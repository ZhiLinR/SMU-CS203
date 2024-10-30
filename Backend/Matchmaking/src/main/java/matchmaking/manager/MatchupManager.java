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

/**
 * Utility class for managing matchups in a tournament.
 */
@Component
public class MatchupManager {

    @Autowired
    private TournamentInfoUtil tournamentInfoUtil;

    @Autowired
    private PlayerSorter playerSorter;

    /**
     * Creates unique matchups for players while avoiding duplicate pairs.
     *
     * @param players      the list of players to match.
     * @param tournamentId the ID of the tournament.
     * @param roundNum     the current round number.
     * @param playedPairs  the set of already played pairs.
     * @return a list of created matchups.
     */
    public List<Matchups> createUniqueMatchups(List<Signups> players, String tournamentId, int roundNum,
            Set<String> playedPairs) {
        List<Matchups> matchups = new ArrayList<>();
        Set<String> pairedPlayers = new HashSet<>();

        // Sort players based on round
        players = playerSorter.sortPlayersForRound(players, tournamentId, roundNum);

        // Generate matchups by pairing players and returning player pairs
        List<Pair<Signups, Signups>> playerPairs = generateMatchups(players, pairedPlayers, playedPairs);

        // Create Matchups objects from player pairs
        for (Pair<Signups, Signups> pair : playerPairs) {
            Matchups matchup = tournamentInfoUtil.createMatchup(pair.getFirst(), pair.getSecond(), tournamentId,
                    roundNum);
            matchups.add(matchup);
        }

        // Handle any unpaired player with a bye, if odd number of players
        // Auto make player winner for round
        if (players.size() % 2 != 0) {
            handleBye(players, matchups, pairedPlayers);
        }

        // Insert matchups into the database
        ValidationUtil.isAllPlayersMatched(matchups, players);
        tournamentInfoUtil.insertMatchups(matchups, tournamentId, roundNum);

        return matchups;
    }

    /**
     * Generates matchups by pairing players while avoiding duplicate pairs.
     *
     * @param players       the list of players.
     * @param pairedPlayers a set to track players already paired.
     * @param playedPairs   the set of previously played pairs.
     * @return a list of valid player pairs.
     */
    private List<Pair<Signups, Signups>> generateMatchups(List<Signups> players,
            Set<String> pairedPlayers, Set<String> playedPairs) {
        List<Pair<Signups, Signups>> validPairs = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            Signups player1 = players.get(i);

            if (pairedPlayers.contains(player1.getUuid())) {
                continue; // Skip if already paired
            }

            // Find a valid matchup for the current player
            Signups player2 = findValidMatchup(player1, players, pairedPlayers, playedPairs, i);

            if (player2 != null) {
                validPairs.add(Pair.of(player1, player2));
                // Track the paired players
                pairedPlayers.add(player1.getUuid());
                pairedPlayers.add(player2.getUuid());
                // Track the played pair in both directions
                addPlayedPair(player1.getUuid(), player2.getUuid(), playedPairs);
            } else {
                System.out.println("No valid matchup found for player: " + player1.getUuid());
            }
        }
        return validPairs; // Return the list of valid player pairs
    }

    /**
     * Attempts to find a valid matchup for the given player.
     *
     * @param player1       the player for whom a pair is being searched.
     * @param players       the list of players to search.
     * @param pairedPlayers a set to track players already paired.
     * @param playedPairs   the set of previously played pairs.
     * @param currentIndex  the current index in the player list.
     * @return a valid Signups object or null if no valid pair is found.
     */
    private Signups findValidMatchup(Signups player1, List<Signups> players,
            Set<String> pairedPlayers, Set<String> playedPairs, int currentIndex) {
        for (int j = currentIndex + 1; j < players.size(); j++) {
            Signups player2 = players.get(j);

            if (pairedPlayers.contains(player2.getUuid())) {
                continue; // Skip if the player is already paired
            }

            // Check if the pair has already played
            if (ValidationUtil.isValidPair(player1.getUuid(), player2.getUuid(), playedPairs)) {
                System.out.println("Valid pair: " + player1.getUuid() + " vs " + player2.getUuid());
                return player2; // Return the second player for the pair
            }
        }
        return null; // No valid pair found
    }

    /**
     * Handles assigning a bye to the last unpaired player if needed.
     *
     * @param players       the list of players.
     * @param matchups      the list to store matchups.
     * @param pairedPlayers a set to track players already paired.
     */
    private void handleBye(List<Signups> players, List<Matchups> matchups, Set<String> pairedPlayers) {
        for (Signups player : players) {
            if (!pairedPlayers.contains(player.getUuid())) {
                Matchups byeMatchup = new Matchups();
                byeMatchup.getId().setPlayer1(player.getUuid());
                byeMatchup.getId().setPlayer2("No Player");
                byeMatchup.setPlayerWon(player.getUuid());
                matchups.add(byeMatchup);
                System.out.println("Bye matchup created: " + byeMatchup);
                break;
            }
        }
    }

    /**
     * Adds a pair in both directions to the played pairs set.
     *
     * @param player1     the first player.
     * @param player2     the second player.
     * @param playedPairs the set to track played pairs.
     */
    private void addPlayedPair(String player1, String player2, Set<String> playedPairs) {
        playedPairs.add(player1 + "-" + player2);
        playedPairs.add(player2 + "-" + player1); // Symmetric pair to prevent re-pairing
    }

    /**
     * Retrieves the set of played pairs from previous matchups.
     *
     * @param previousMatchups the list of previous matchups.
     * @return a set of played pairs.
     */
    public Set<String> getPlayedPairs(List<Matchups> previousMatchups) {
        Set<String> playedPairs = new HashSet<>();
        for (Matchups matchup : previousMatchups) {
            String pair1 = matchup.getId().getPlayer1() + "-" + matchup.getId().getPlayer2();
            String pair2 = matchup.getId().getPlayer2() + "-" + matchup.getId().getPlayer1();
            playedPairs.add(pair1);
            playedPairs.add(pair2); // Add both orders to handle symmetry
        }
        return playedPairs;
    }
}
