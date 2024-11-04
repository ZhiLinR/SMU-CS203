package matchmaking.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

import matchmaking.dto.*;
import matchmaking.model.Matchups;
import matchmaking.enums.*;
import matchmaking.exception.ResultsNotFoundException;

/**
 * Utility class for calculating Buchholz scores, updating Elo ratings, and
 * determining ranks for players in a tournament.
 */
public class RatingUtil {

    /**
     * Updates the Buchholz scores, Elo ratings, and ranks for players based on
     * matchups and player wins.
     *
     * @param matchups      A list of matchups in the tournament.
     * @param playerWins    A list of PlayerWins containing players' win
     *                      information.
     * @param playerResults A list of PlayerResults containing player information to
     *                      be updated.
     * @throws ResultsNotFoundException if a player's results cannot be found.
     */
    public static void updateRatings(List<Matchups> matchups, List<PlayerWins> playerWins,
            List<PlayerResults> playerResults) throws ResultsNotFoundException {
        // Use a map for quick lookups of player results by UUID
        Map<String, PlayerResults> playerResultsMap = createPlayerResultsMap(playerResults);

        // Validate matchups to ensure all player results exist
        ValidationUtil.validateMatchups(matchups, playerResults);

        // Calculate Buchholz scores
        calculateBuchholz(matchups, playerResultsMap);

        // Update Elo ratings
        updateEloRatings(matchups, playerResultsMap);

        // Calculate ranks
        calculateRanks(playerWins, playerResults);
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
     * @throws ResultsNotFoundException if a player's results cannot be found.
     */
    private static void calculateBuchholz(List<Matchups> matchups, Map<String, PlayerResults> playerResultsMap)
            throws ResultsNotFoundException {
        for (Matchups matchup : matchups) {
            String playerWon = matchup.getPlayerWon();
            String playerLost = playerWon.equals(matchup.getId().getPlayer1())
                    ? matchup.getId().getPlayer2()
                    : matchup.getId().getPlayer1();

            // Find PlayerResults for both players using the map
            PlayerResults winningPlayer = playerResultsMap.get(playerWon);
            PlayerResults losingPlayer = playerResultsMap.get(playerLost);

            if (winningPlayer == null || losingPlayer == null) {
                throw new ResultsNotFoundException(
                        "Player results not found for UUIDs: " + playerWon + ", " + playerLost);
            }

            // Update the winning player's Buchholz score
            winningPlayer.setBuchholz(winningPlayer.getBuchholz() + losingPlayer.getElo());
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
     * @throws ResultsNotFoundException if a player's results cannot be found.
     */
    private static void calculateRanks(List<PlayerWins> playerWins, List<PlayerResults> playerResults)
            throws ResultsNotFoundException {
        List<PlayerRanking> rankings = new ArrayList<>();

        for (PlayerWins playerWin : playerWins) {
            String uuid = playerWin.getUuid();
            PlayerResults playerResult = findPlayerResultsByUUID(playerResults, uuid);
            if (playerResult == null) {
                throw new ResultsNotFoundException("Player result not found for UUID: " + uuid);
            }

            double points = playerWin.getPoints();
            Integer buchholz = playerResult.getBuchholz();

            PlayerRanking playerRanking = new PlayerRanking();
            playerRanking.setUuid(uuid);
            playerRanking.setPoints(points);
            playerRanking.setBuchholz(buchholz);
            playerRanking.setPlayerResult(playerResult);

            rankings.add(playerRanking);
        }

        // Sort rankings by points (descending) and then by Buchholz (descending)
        rankings.sort(Comparator.comparingDouble(PlayerRanking::getPoints).reversed()
                .thenComparingInt(PlayerRanking::getBuchholz).reversed());

        // Update the rank in PlayerResults based on sorted order
        for (int i = 0; i < rankings.size(); i++) {
            PlayerRanking ranking = rankings.get(i);
            ranking.getPlayerResult().setRank(i + 1); // Assign rank (1-based index)
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
     * Updates Elo ratings for players based on the results of matchups.
     *
     * @param matchups         A list of Matchups containing the results of the
     *                         matches.
     * @param playerResultsMap A map of PlayerResults keyed by player UUID for quick
     *                         access.
     * @throws ResultsNotFoundException if a player's results cannot be found.
     */
    private static void updateEloRatings(List<Matchups> matchups, Map<String, PlayerResults> playerResultsMap)
            throws ResultsNotFoundException {
        for (Matchups matchup : matchups) {
            String playerWon = matchup.getPlayerWon();
            String playerLost = playerWon.equals(matchup.getId().getPlayer1())
                    ? matchup.getId().getPlayer2()
                    : matchup.getId().getPlayer1();

            // Find PlayerResults for both players using the map
            PlayerResults winningPlayer = playerResultsMap.get(playerWon);
            PlayerResults losingPlayer = playerResultsMap.get(playerLost);

            if (winningPlayer == null || losingPlayer == null) {
                throw new ResultsNotFoundException(
                        "Player results not found for UUIDs: " + playerWon + ", " + playerLost);
            }

            // Determine K values based on player's Elo ratings using the enum
            int kForWinner = EloKFactor.getKValue(winningPlayer.getElo());
            int kForLoser = EloKFactor.getKValue(losingPlayer.getElo());

            // Calculate expected scores
            double expectedWinningScore = calculateExpectedScore(winningPlayer.getElo(), losingPlayer.getElo());
            double expectedLosingScore = calculateExpectedScore(losingPlayer.getElo(), winningPlayer.getElo());

            // Update Elo ratings
            int newWinningElo = (int) Math.round(winningPlayer.getElo() + kForWinner * (1 - expectedWinningScore));
            int newLosingElo = (int) Math.round(losingPlayer.getElo() + kForLoser * (0 - expectedLosingScore));

            // Set new Elo ratings
            winningPlayer.setElo(newWinningElo);
            losingPlayer.setElo(newLosingElo);
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
