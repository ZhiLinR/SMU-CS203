package matchmaking.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import matchmaking.model.Matchups;

/**
 * Utility class for managing and tracking matchups.
 * Provides helper methods to add played pairs and retrieve previously played
 * pairs.
 */
public class MatchupUtil {

    /**
     * Adds a pair in both directions to the set of played pairs.
     *
     * @param player1     the first player.
     * @param player2     the second player.
     * @param playedPairs the set to track played pairs.
     */
    public static void addPlayedPair(String player1, String player2, Set<String> playedPairs) {
        playedPairs.add(player1 + "-" + player2);
        playedPairs.add(player2 + "-" + player1);
    }

    /**
     * Retrieves the set of played pairs from previous matchups.
     *
     * @param previousMatchups the list of previous matchups.
     * @return a set of played pairs.
     */
    public static Set<String> getPlayedPairs(List<Matchups> previousMatchups) {
        Set<String> playedPairs = new HashSet<>();

        for (Matchups matchup : previousMatchups) {
            String pair1 = matchup.getId().getPlayer1() + "-" + matchup.getId().getPlayer2();
            String pair2 = matchup.getId().getPlayer2() + "-" + matchup.getId().getPlayer1();
            playedPairs.add(pair1);
            playedPairs.add(pair2);
        }
        return playedPairs;
    }
}
