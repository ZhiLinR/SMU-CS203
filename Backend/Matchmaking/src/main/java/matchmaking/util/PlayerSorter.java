package matchmaking.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import matchmaking.model.*;
import matchmaking.dto.*;

/**
 * Utility class for sorting players based on ELO ratings and points.
 * This class provides methods to sort players for tournament matchmaking
 * based on different criteria such as ELO ratings or accumulated points.
 */
@Component
public class PlayerSorter {

    @Autowired
    private TournamentInfoUtil tournamentInfoUtil;

    /**
     * Sorts players based on the provided criterion (ELO, points, or PlayerWins).
     *
     * @param players      the list of players to sort.
     * @param playerPoints a map of player UUIDs to their points (optional).
     * @param sortByELO    whether to sort by ELO (if true).
     * @return a sorted list of players based on the given criterion.
     */
    public List<Signups> sortPlayers(List<Signups> players,
            Map<String, Double> playerPoints,
            boolean sortByELO) {
        return players.stream()
                .sorted((p1, p2) -> {
                    if (sortByELO) {
                        return Integer.compare(p2.getElo(), p1.getElo());
                    } else {
                        Double points1 = playerPoints.getOrDefault(p1.getId().getUuid(), 0.0);
                        Double points2 = playerPoints.getOrDefault(p2.getId().getUuid(), 0.0);
                        return points2.compareTo(points1); // Descending order
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Sorts players for the current round using ELO or Swiss-style logic.
     *
     * @param players      the list of players.
     * @param tournamentId the ID of the tournament.
     * @param roundNum     the current round number.
     * @return the sorted list of players.
     * @throws IllegalArgumentException if the provided {@code tournamentId} is null
     *                                  or empty, or if {@code roundNum} is less
     *                                  than 1.
     */
    public List<Signups> sortPlayersForRound(List<Signups> players, String tournamentId, int roundNum) {
        System.out.println("Sort players called");
        if (roundNum == 1) {
            System.out.println("Round 1: Sorting players by ELO");
            return sortPlayers(players, null, true); // Sort by ELO
        } else {
            System.out.println("Not round 1: Using Swiss pairing");
            List<PlayerWins> playerWins = tournamentInfoUtil.getPlayerWinsByTournamentId(tournamentId);

            Map<String, Double> playerPoints = convertPlayerWinsToMap(playerWins);
            return sortPlayers(players, playerPoints, false); // Sort by points
        }
    }

    /**
     * Sorts a list of {@link Results} objects by player ranking in ascending order
     * and returns a list of player UUIDs in sorted order.
     * <p>
     * Each {@link Results} object contains a composite primary key
     * {@link PlayerTournamentId} with the player's UUID and the tournament ID,
     * along with the player's ranking in the tournament. This method retrieves the
     * UUIDs in the order of the players' rankings.
     * </p>
     *
     * @param results A list of {@link Results} objects representing players'
     *                results in a tournament.
     *                Each object must have a non-null {@code ranking} field.
     * @return A list of player UUIDs, sorted by ascending order of ranking.
     */
    public List<String> sortPlayersByRank(List<Results> results) {
        return results.stream()
                .sorted(Comparator.comparingInt(Results::getRanking)) // Sort by ranking in ascending order
                .map(result -> result.getId().getUuid()) // Extract the UUID from the composite key
                .collect(Collectors.toList()); // Collect into a list of UUIDs
    }

    /**
     * Converts a list of PlayerWins to a map of player UUIDs to points.
     *
     * @param playerWinsList the list of PlayerWins.
     * @return a map of player UUIDs to their points.
     */
    private Map<String, Double> convertPlayerWinsToMap(List<PlayerWins> playerWinsList) {
        return playerWinsList.stream()
                .collect(Collectors.toMap(PlayerWins::getUuid, PlayerWins::getPoints, (p1, p2) -> p1));
    }
}
