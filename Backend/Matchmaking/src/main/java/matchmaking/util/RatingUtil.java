package matchmaking.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import matchmaking.dto.*;
import matchmaking.model.Matchups;
import matchmaking.enums.*;

/**
 * Utility class for calculating Buchholz scores, updating Elo ratings, and
 * determining ranks for players in a tournament.
 */
public class RatingUtil {

    private static final PlayerResults NULL_PLAYER = new PlayerResults()
            .setUuid("null")
            .setElo(0)
            .setBuchholz(0)
            .setRank(-1);

    /**
     * Updates the Buchholz scores, Elo ratings, and ranks for players based on
     * matchups and player wins.
     *
     * @param matchups      A list of matchups in the tournament.
     * @param playerWins    A list of PlayerWins containing players' win
     *                      information.
     * @param playerResults A list of PlayerResults containing player information to
     *                      be updated.
     */
    public static void updateRatings(List<Matchups> matchups, List<PlayerWins> playerWins,
            List<PlayerResults> playerResults) {

        System.out.println("Getting player results map");
        // Use a map for quick lookups of player results by UUID
        Map<String, PlayerResults> playerResultsMap = createPlayerResultsMap(playerResults);

        System.out.println("Validating Matchups");
        // Validate matchups to ensure all player results exist
        ValidationUtil.validateMatchups(matchups, playerResults);

        // Calculate Buchholz scores
        System.out.println("Calculating Buchholz");
        calculateBuchholz(matchups, playerResultsMap);

        // Update Elo ratings
        System.out.println("Updating Ratings");
        updateEloRatings(matchups, playerResultsMap);

        // Calculate ranks
        System.out.println("Calculating Ranks");
        calculateRanks(playerWins, playerResults);

        System.out.println("Player Results: " + playerResults);
    }

    /**
     * Creates a map of PlayerResults keyed by player UUID for quick access.
     *
     * @param playerResults A list of PlayerResults to be converted into a map.
     * @return A map of PlayerResults keyed by UUID.
     */
    private static Map<String, PlayerResults> createPlayerResultsMap(List<PlayerResults> playerResults) {
        Map<String, PlayerResults> playerResultsMap = new HashMap<>();
        for (PlayerResults result : playerResults) {
            playerResultsMap.put(result.getUuid(), result);
        }
        return playerResultsMap;
    }

    /**
     * Calculates the Buchholz scores for players based on match outcomes.
     *
     * @param matchups         A list of matchups in the tournament.
     * @param playerResultsMap A map of PlayerResults keyed by player UUID for quick
     *                         access.
     */
    private static void calculateBuchholz(List<Matchups> matchups, Map<String, PlayerResults> playerResultsMap) {
        for (Matchups matchup : matchups) {
            String winUuid = matchup.getPlayerWon();
            String loseUuid = winUuid.equals(matchup.getId().getPlayer1())
                    ? matchup.getId().getPlayer2()
                    : matchup.getId().getPlayer1();

            // Find PlayerResults for both players using the map
            PlayerResults winResult = playerResultsMap.get(winUuid);
            PlayerResults loseResult = "null".equals(loseUuid) ? NULL_PLAYER : playerResultsMap.get(loseUuid);

            // Check results not null
            ValidationUtil.validatePlayerResult(winResult, loseUuid);
            ValidationUtil.validatePlayerResult(loseResult, winUuid);

            // Update the winning player's Buchholz score
            winResult.setBuchholz(winResult.getBuchholz() + loseResult.getElo());
        }
    }

    /**
     * Calculates and assigns ranks to players based on their wins and Buchholz
     * scores.
     *
     * @param playerWins    A list of PlayerWins containing players' win
     *                      information.
     * @param playerResults A list of PlayerResults containing player information to
     *                      be updated.
     */
    private static void calculateRanks(List<PlayerWins> playerWins, List<PlayerResults> playerResults) {

        List<PlayerRanking> rankings = new ArrayList<>(playerWins.stream()
                .filter(playerWin -> !"null".equals(playerWin.getUuid())) // Ignore players with "null" UUID
                .map(playerWin -> {
                    String uuid = playerWin.getUuid();
                    PlayerResults playerResult = findPlayerResultsByUUID(playerResults, uuid);

                    // Check results not null
                    ValidationUtil.validatePlayerResult(playerResult, uuid);

                    PlayerRanking playerRanking = new PlayerRanking();
                    playerRanking.setUuid(uuid);
                    playerRanking.setPoints(playerWin.getPoints());
                    playerRanking.setBuchholz(playerResult.getBuchholz());
                    playerRanking.setPlayerResult(playerResult);

                    return playerRanking;
                })
                .toList());

        // Sort rankings by points (descending) and then by Buchholz (descending)
        rankings.sort((r1, r2) -> {
            // First compare points in descending order
            int pointsComparison = Double.compare(r2.getPoints(), r1.getPoints());

            // If points are the same, then compare buchholz in descending order
            if (pointsComparison == 0) {
                return Integer.compare(r2.getPlayerResult().getBuchholz(), r1.getPlayerResult().getBuchholz());
            }

            // Otherwise, return the comparison based on points
            return pointsComparison;
        });

        // Update the rank in PlayerResults based on sorted order
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).getPlayerResult().setRank(i + 1); // Assign rank (1-based index)
        }

        System.out.println("Rankings: " + rankings);
    }

    /**
     * Finds a PlayerResults instance by its UUID.
     *
     * @param playerResults A list of PlayerResults to search.
     * @param uuid          The UUID of the player to find.
     * @return The PlayerResults instance if found, or NULL_PLAYER if not found.
     */
    private static PlayerResults findPlayerResultsByUUID(List<PlayerResults> playerResults, String uuid) {
        if ("null".equals(uuid)) {
            return NULL_PLAYER;
        }
        return playerResults.stream()
                .filter(result -> result.getUuid().equals(uuid))
                .findFirst()
                .orElse(NULL_PLAYER); // Return NULL_PLAYER if not found
    }

    /**
     * Updates Elo ratings for players based on the results of matchups.
     *
     * @param matchups         A list of Matchups containing the results of the
     *                         matches.
     * @param playerResultsMap A map of PlayerResults keyed by player UUID for quick
     *                         access.
     */
    private static void updateEloRatings(List<Matchups> matchups, Map<String, PlayerResults> playerResultsMap) {
        for (Matchups matchup : matchups) {
            String winUuid = matchup.getPlayerWon();
            String loseUuid = winUuid.equals(matchup.getId().getPlayer1())
                    ? matchup.getId().getPlayer2()
                    : matchup.getId().getPlayer1();

            // Find PlayerResults for both players using the map
            PlayerResults winResult = playerResultsMap.get(winUuid);
            PlayerResults loseResult = "null".equals(loseUuid) ? NULL_PLAYER : playerResultsMap.get(loseUuid);

            // Check results not null
            ValidationUtil.validatePlayerResult(loseResult, loseUuid);
            ValidationUtil.validatePlayerResult(winResult, winUuid);

            // Determine K values based on player's Elo ratings using the enum
            int kForWinner = EloKFactor.getKValue(winResult.getElo());
            int kForLoser = EloKFactor.getKValue(loseResult.getElo());

            // Calculate expected scores
            double expectedWinningScore = calculateExpectedScore(winResult.getElo(), loseResult.getElo());
            double expectedLosingScore = calculateExpectedScore(loseResult.getElo(), winResult.getElo());

            // Update Elo ratings
            int newWinningElo = (int) Math.round(winResult.getElo() + kForWinner * (1 - expectedWinningScore));
            int newLosingElo = (int) Math.round(loseResult.getElo() + kForLoser * (0 - expectedLosingScore));

            // Set new Elo ratings
            winResult.setElo(newWinningElo);
            loseResult.setElo(newLosingElo);
        }
    }

    /**
     * Calculates the expected score for a player.
     *
     * @param playerElo   The Elo rating of the player.
     * @param opponentElo The Elo rating of the opponent.
     * @return The expected score for the player.
     */
    private static double calculateExpectedScore(int playerElo, int opponentElo) {
        return 1.0 / (1 + Math.pow(10, (opponentElo - playerElo) / 400.0));
    }
}
