package matchmaking.util;

import matchmaking.exception.InvalidRoundException;
import matchmaking.model.Matchups;
import matchmaking.model.Signups;

import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Set;

/**
 * Utility class for common validation operations.
 * Provides methods to validate fields such as email, UUID, role, and required
 * fields.
 */
public class ValidationUtil {

    /**
     * Validates that a string is not null or empty.
     *
     * @param value     The string to validate.
     * @param fieldName The name of the field being validated (for error messages).
     * @throws IllegalArgumentException if the string is null or empty.
     */
    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be null or empty.");
        }
    }

    /**
     * Checks if a given pair of players is valid for a new matchup.
     * A pair is considered valid if the two players have not been matched before
     * in either order (e.g., "player1-player2" or "player2-player1").
     *
     * @param player1     the UUID of the first player.
     * @param player2     the UUID of the second player.
     * @param playedPairs a set containing previously played pairs,
     *                    stored in both "player1-player2" and "player2-player1"
     *                    formats.
     * @return {@code true} if the pair has not played before, {@code false}
     *         otherwise.
     */
    public static boolean isValidPair(String player1, String player2, Set<String> playedPairs) {
        return !playedPairs.contains(player1 + "-" + player2) &&
                !playedPairs.contains(player2 + "-" + player1);
    }

    /**
     * Validates whether the provided round number is within the valid range based
     * on the number of players.
     *
     * @param roundNum    The current round number to validate.
     * @param playerCount The total number of players in the tournament.
     * @throws InvalidRoundException if the round number exceeds the maximum
     *                               possible rounds.
     */
    public static void isValidRoundNum(int roundNum, int playerCount) {
        int maxRound = playerCount * (playerCount - 1) / 2;
        if (roundNum > maxRound) {
            throw new InvalidRoundException("Max number of rounds for signups have been reached");
        }
    }

    /**
     * Checks if the previous round has been completed by verifying if the winner
     * for all matchups have been allocated.
     *
     * @param matchup The matchup object containing the result of the previous
     *                round.
     * @throws InvalidRoundException if the winner of the previous round has not
     *                               been allocated (i.e., {@code playerWon} is
     *                               null).
     */
    public static void isPrevRoundOver(Matchups matchup) {
        if (matchup.getPlayerWon() == null) {
            throw new InvalidRoundException("Previous round results have not been allocated");
        }
    }

    /**
     * Verifies if all players have been uniquely matched in the current round.
     *
     * @param matchups A list of current round matchups.
     * @param players  A list of players who signed up for the tournament.
     * @throws InvalidRoundException if not all players have been uniquely matched
     *                               (i.e., the
     *                               number of matchups is insufficient).
     */
    public static void isAllPlayersMatched(List<Matchups> matchups, List<Signups> players) {
        if (matchups.size() < (players.size() / 2)) {
            throw new InvalidRoundException("Failed to match up all players uniquely");
        }
    }

    /**
     * Checks if the provided player pair is valid.
     *
     * @param pair the pair of players to check.
     * @throws InvalidRoundException if the pair is null.
     */
    public static void isValidPair(Pair<Signups, Signups> pair) {
        if (pair == null) {
            throw new InvalidRoundException("Invalid matchup made");
        }
    }
}
