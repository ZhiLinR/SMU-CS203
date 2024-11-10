package matchmaking.util;

import matchmaking.dto.PlayerResults;
import matchmaking.exception.InvalidRoundException;
import matchmaking.exception.InvalidTournamentException;
import matchmaking.exception.ResultsNotFoundException;
import matchmaking.exception.TournamentNotFoundException;
import matchmaking.model.Matchups;
import matchmaking.model.MatchupsId;
import matchmaking.model.Signups;
import matchmaking.model.Tournament;

import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Set;

/**
 * Utility class for common validation operations.
 * Provides methods to validate fields such as email, UUID, role, and required
 * fields.
 */
public class ValidationUtil {

    // Constant for the minimum number of signups required
    private static final int MIN_SIGNUPS = 2;

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
     * Validates that the provided list is not null or empty.
     *
     * @param list      the list to validate
     * @param fieldName the name of the field for error messages
     * @throws IllegalArgumentException if the list is null or empty
     */
    public static <E> void validateListNotEmpty(List<E> list, String fieldName) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Missing " + fieldName + ".");
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
    public static void isValidRoundNum(Integer roundNum, int playerCount) {
        if (roundNum == null || roundNum <= 0) {
            throw new IllegalArgumentException("Round number must not be null.");
        }
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
        // + 1 to account for odd players
        if (((players.size() + 1) / 2) != matchups.size()) {
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

    /**
     * Checks if there is enough signups for matchmaking.
     *
     * @param signups a List of Signups objects to be validated
     * @throws IllegalArgumentException if the signups list is null or empty
     */
    public static void isValidSignups(List<Signups> signups) {
        if (signups == null || signups.isEmpty() || signups.size() <= MIN_SIGNUPS) {
            throw new InvalidRoundException("Not enough signups for the tournament.");
        }
    }

    /**
     * Checks if the matchup is valid by ensuring there are no duplicate PlayerIDs.
     *
     * @param signups a List of Signups objects to be validated
     * @throws IllegalArgumentException if the signups list is null or empty
     */
    public static void isValidMatchup(Matchups matchup) {
        if (matchup == null) {
            throw new IllegalArgumentException("Matchup must not be null.");
        }

        MatchupsId id = matchup.getId();
        if (id.getPlayer1().equals(id.getPlayer2())) {
            throw new InvalidRoundException("Invalid matchup: Player1 and Player2 cannot be the same.");
        }
    }

    /**
     * Validates that matchups contain valid players.
     *
     * @param matchups      A list of Matchups to validate.
     * @param playerResults A list of PlayerResults to check against.
     * @throws ResultsNotFoundException if any player in the matchups cannot be
     *                                  found in player results.
     */
    public static void validateMatchups(List<Matchups> matchups, List<PlayerResults> playerResults)
            throws ResultsNotFoundException {
        if (playerResults == null || playerResults.isEmpty()) {
            throw new ResultsNotFoundException("Missing results.");
        }

        for (Matchups matchup : matchups) {
            String playerWon = matchup.getPlayerWon();

            if (playerWon == null || playerWon.isEmpty()) {
                throw new ResultsNotFoundException("Missing results");
            }

            String playerLost = playerWon.equals(matchup.getId().getPlayer1())
                    ? matchup.getId().getPlayer2()
                    : matchup.getId().getPlayer1();

            if (!isNullPlayer(playerWon)) {
                validatePlayerResult(playerResults, playerWon);
            }

            if (!isNullPlayer(playerLost)) {
                validatePlayerResult(playerResults, playerLost);
            }
        }
    }

    /**
     * Finds a PlayerResults instance by its UUID.
     *
     * @param playerResults A list of PlayerResults to search.
     * @param uuid          The UUID of the player to find.
     * @return The PlayerResults instance if found, or null if not found.
     */
    private static PlayerResults findPlayerResultsByUUID(List<PlayerResults> playerResults, String uuid) {
        return playerResults.stream()
                .filter(result -> result.getUuid().equals(uuid))
                .findFirst()
                .orElse(null); // Return null if not found
    }

    /**
     * Validates that the given PlayerResults is not null.
     *
     * @param playerResults The PlayerResults object to validate.
     * @param uuid          The UUID or identifying information for error messages.
     * @throws ResultsNotFoundException if the PlayerResults is null.
     */
    public static void validatePlayerResult(PlayerResults playerResults, String uuid)
            throws ResultsNotFoundException {
        if (playerResults == null) {
            throw new ResultsNotFoundException("Player result not found for UUID: " + uuid);
        }
    }

    /**
     * Validates that player results are not null and exist in the provided list.
     *
     * @param playerResults A list of PlayerResults to check against.
     * @param uuid          The UUID of the player whose results need to be
     *                      validated.
     * @throws ResultsNotFoundException if the player results cannot be found.
     */
    public static void validatePlayerResult(List<PlayerResults> playerResults, String uuid)
            throws ResultsNotFoundException {
        if (findPlayerResultsByUUID(playerResults, uuid) == null) {
            throw new ResultsNotFoundException("Player result not found for UUID: " + uuid);
        }
    }

    /**
     * Validates that player is not null or forfeit.
     *
     * @param uuid The UUID of the player whose results need to be
     *             validated.
     * @return is player null or forfeit as boolean.
     */
    public static boolean isNullPlayer(String uuid) {
        if (uuid.equals("null") || uuid.equals("forfeit")) {
            return true;
        }
        return false;
    }

    /**
     * Validates if the provided {@link Tournament} is eligible for ranking
     * requests.
     *
     * @param tournament the {@link Tournament} to validate for ranking eligibility.
     * @throws TournamentNotFoundException if the tournament does not exist (i.e.,
     *                                     is null).
     * @throws InvalidTournamentException  if the tournament status is not
     *                                     "Completed".
     */
    public static void isValidRankingRequest(Tournament tournament) {
        if (tournament == null) {
            throw new TournamentNotFoundException("Tournament does not exist.");
        } else if (tournament.getStatus() != "Completed") {
            throw new InvalidTournamentException("Tournament has not completed.");
        }
    }
}
