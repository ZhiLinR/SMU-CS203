package matchmaking.util;

import java.util.List;
import java.util.HashMap;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import matchmaking.model.*;

/**
 * Utility class for sorting players based on ELO ratings and points.
 */
@Component
public class PlayerSorter {

    /**
     * Sorts players by their ELO ratings in descending order.
     *
     * @param signups the list of signups to sort.
     * @return a sorted list of players by ELO ratings in descending order.
     */
    public List<Signups> sortPlayersByELO(List<Signups> signups) {
        return signups.stream()
                .sorted(Comparator.comparingInt(Signups::getElo).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Sorts players by their points in descending order.
     *
     * @param signups      the list of signups to sort.
     * @param playerPoints a map of player UUIDs to their points.
     * @return a sorted list of players by points in descending order.
     */
    public List<Signups> sortPlayersByPoints(List<Signups> signups, HashMap<String, Double> playerPoints) {
        return signups.stream()
                .sorted((p1, p2) -> {
                    Double points1 = playerPoints.getOrDefault(p1.getUuid(), 0.0);
                    Double points2 = playerPoints.getOrDefault(p2.getUuid(), 0.0);
                    return points2.compareTo(points1); // Descending order
                })
                .collect(Collectors.toList());
    }
}

