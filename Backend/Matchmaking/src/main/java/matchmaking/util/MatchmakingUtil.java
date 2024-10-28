package matchmaking.util;

import java.util.HashMap;
import java.util.List;

import matchmaking.model.PlayerWins;

public class MatchmakingUtil {

    public static HashMap<String, Double> calculatePoints(List<PlayerWins> playerWinList) {
        HashMap<String, Double> pointsMap = new HashMap<>();

        for (PlayerWins player : playerWinList) {
            // Calculate points
            double points = player.getWins() + (player.getDraws() * 0.5);
            pointsMap.put(player.getUuid(), points);
        }

        return pointsMap;
    }

}
