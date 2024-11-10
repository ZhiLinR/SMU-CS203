package matchmaking.util;

import matchmaking.dto.*;
import matchmaking.model.*;

import java.util.List;
import java.util.Map;
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
                        .setUuid(signup.getId().getUuid())
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

    /**
     * Retrieves a list of player names based on their UUIDs in ranked order.
     * 
     * This method takes a list of UUIDs representing players in a ranked order,
     * and a map that associates each UUID with a corresponding player name. It
     * then constructs and returns a list of player names in the same order as the
     * UUIDs provided in the input list.
     * 
     * @param rankedUuids A list of player UUIDs in ranked order. The UUIDs will be
     *                    used to fetch the corresponding player names from the
     *                    provided map.
     * @param uuidNameMap A map that associates player UUIDs (as strings) with
     *                    player names (as strings). The method will use this map
     *                    to retrieve the name for each UUID in the rankedUuids
     *                    list.
     * @return A list of player names corresponding to the UUIDs in the rankedUuids
     *         list, in the same order.
     */
    public static List<String> getRankedNames(List<String> rankedUuids, Map<String, String> uuidNameMap) {
        // Use the rankedUuids list to fetch corresponding names from uuidNameMap
        ValidationUtil.validateListNotEmpty(rankedUuids, "UUID List");
        if (uuidNameMap.size() == 0) {
            throw new IllegalArgumentException("Missing Name List.");
        }
        return rankedUuids.stream()
                .map(uuid -> uuidNameMap.get(uuid)) // Get name for each UUID
                .collect(Collectors.toList()); // Collect the names into a list
    }

}
