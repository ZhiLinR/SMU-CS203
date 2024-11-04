package matchmaking.util;

import matchmaking.dto.*;
import matchmaking.model.*;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Utility class for converting Signups to PlayerResults.
 */
public class ConversionUtil {

    /**
     * Converts a list of Signups to a list of PlayerResults.
     *
     * @param signups A list of Signups representing players signed up for a
     *                tournament.
     * @return A list of PlayerResults initialized with UUID, Elo, and default
     *         Buchholz and rank.
     */
    public static List<PlayerResults> convertSignupsToPlayerResults(List<Signups> signups) {
        ValidationUtil.validateListNotEmpty(signups, "Signups");
        return signups.stream()
                .map(signup -> new PlayerResults()
                        .setUuid(signup.getUuid())
                        .setElo(signup.getElo())
                        .setBuchholz(0) // Initialize Buchholz score to 0
                        .setRank(0)) // Initialize rank to 0
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of UUIDs from PlayerResults ordered by rank.
     *
     * @param playerResults A list of PlayerResults containing player data.
     * @return A list of UUIDs sorted by rank in ascending order.
     */
    public static List<String> getUuidsOrderedByRank(List<PlayerResults> playerResults) {
        ValidationUtil.validateListNotEmpty(playerResults, "Player Results");
        return playerResults.stream()
                .sorted(Comparator.comparingInt(PlayerResults::getRank)) // Sort by rank
                .map(PlayerResults::getUuid) // Extract UUIDs
                .collect(Collectors.toList()); // Collect into a list
    }
}
