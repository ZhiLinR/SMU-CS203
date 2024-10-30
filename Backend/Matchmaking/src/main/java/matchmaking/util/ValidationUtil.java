package matchmaking.util;

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
}
