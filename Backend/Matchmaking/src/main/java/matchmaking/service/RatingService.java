package matchmaking.service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import matchmaking.dto.PlayerResults;
import matchmaking.dto.PlayerWins;
import matchmaking.model.Matchups;
import matchmaking.model.Signups;
import matchmaking.util.ConversionUtil;
import matchmaking.util.RatingUtil;
import matchmaking.util.TournamentInfoUtil;
import matchmaking.util.ValidationUtil;

/**
 * Service class responsible for handling rating updates for players
 * in tournaments. This includes retrieving signups, previous matchups,
 * and player wins, and updating their Elo ratings accordingly.
 */
@Service
public class RatingService {

    @Value("${USERMSVC_URL}")
    private String usermsvcUrl;

    @Autowired
    private TournamentInfoUtil tournamentInfoUtil;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Retrieves the ranking update for players based on the provided tournament ID.
     *
     * <p>
     * This method validates the tournament ID, fetches the current signups,
     * previous matchups, and player wins. It then updates the Elo ratings of
     * the players and returns a list of ranked UUIDs.
     *
     * @param tournamentId the ID of the tournament for which to update player
     *                     rankings
     * @return a list of UUIDs ordered by the updated Elo rankings
     * @throws IllegalArgumentException if the tournament ID is null or empty
     * @throws Exception                if any unexpected error occurs during
     *                                  processing
     */
    public List<String> getRankingUpdateElo(String tournamentId) {
        try {
            System.out.println(tournamentId);
            ValidationUtil.validateNotEmpty(tournamentId, "Tournament ID");

            // Use the TournamentInfoUtil to get current round and signups
            System.out.println("Getting info");
            List<Signups> signups = tournamentInfoUtil.getSignupsByTournamentId(tournamentId);
            List<Matchups> previousMatchups = tournamentInfoUtil.getMatchupsByTournamentId(tournamentId);
            List<PlayerWins> playerWins = tournamentInfoUtil.getPlayerWinsByTournamentId(tournamentId);

            List<PlayerResults> playerResults = ConversionUtil.convertSignupsToPlayerResults(signups);

            RatingUtil.updateRatings(previousMatchups, playerWins, playerResults);

            List<String> rankedUuids = ConversionUtil.getUuidsOrderedByRank(playerResults);

            // Update User's Elo in Signups table and calls UserMSVC to update user profile.
            for (PlayerResults results : playerResults) {
                updatePlayerElo(results.getUuid(), results.getElo());
            }

            return rankedUuids;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Updates the Elo rating for a player identified by the given UUID.
     *
     * <p>
     * This method validates the UUID and Elo rating before updating
     * the player's Elo rating in the signups repository and the user profile
     * in the User microservice.
     *
     * @param uuid the unique identifier of the player whose Elo rating is to be
     *             updated
     * @param elo  the new Elo rating to assign to the player
     * @throws IllegalArgumentException if the UUID is null or empty, or if the Elo
     *                                  rating is non-positive
     */
    private void updatePlayerElo(String uuid, int elo) {
        ValidationUtil.validateNotEmpty(uuid, "UUID");

        if (elo <= 0) {
            throw new IllegalArgumentException("Elo must be greater than zero");
        }
        tournamentInfoUtil.updateSignupsPlayerElo(uuid, elo);
        updateUserMsvcPlayerElo(uuid, elo);
    }

    /**
     * Updates the player's Elo rating in the User microservice.
     *
     * <p>
     * This method prepares a request body with the player's UUID and Elo rating,
     * and sends a PUT request to the User microservice to update the player's
     * profile.
     *
     * @param uuid the unique identifier of the player whose Elo rating is to be
     *             updated
     * @param elo  the new Elo rating to assign to the player
     * @throws RuntimeException if the update request to the User microservice fails
     */
    private void updateUserMsvcPlayerElo(String uuid, int elo) {
        // Prepare the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("uuid", uuid);
        requestBody.put("elo", elo);

        // Create an HttpEntity with the request body
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody);

        // Make the PUT request
        ResponseEntity<Void> response = restTemplate.exchange(usermsvcUrl, HttpMethod.PUT, requestEntity, Void.class);

        // Check response status
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to update Elo: " + response.getStatusCode());
        }
    }
}