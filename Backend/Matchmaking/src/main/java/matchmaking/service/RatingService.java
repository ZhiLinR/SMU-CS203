package matchmaking.service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import matchmaking.dto.PlayerResults;
import matchmaking.dto.PlayerWins;
import matchmaking.model.Matchups;
import matchmaking.model.Results;
import matchmaking.model.Signups;
import matchmaking.util.ConversionUtil;
import matchmaking.util.PlayerSorter;
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

    /**
     * URL for the User Microservice, set via `USERMSVC_ELO_URL` property.
     */
    @Value("${USERMSVC_ELO_URL}")
    private String usermsvcEloUrl;

    /**
     * URL for the User Microservice, set via `USERMSVC_NAMES_URL` property.
     */
    @Value("${USERMSVC_NAMES_URL}")
    private String usermsvcNamesUrl;

    @Autowired
    private TournamentInfoUtil tournamentInfoUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PlayerSorter playerSorter;

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
     * @throws RuntimeException         if any unexpected error occurs during
     *                                  processing
     */
    @Transactional
    public List<String> getRankingUpdateElo(String tournamentId) {
        try {
            System.out.println(tournamentId);
            ValidationUtil.validateNotEmpty(tournamentId, "Tournament ID");

            // Validate that tournament exists and is completed
            ValidationUtil.isValidRankingRequest(
                    tournamentInfoUtil.getTournamentById(tournamentId));

            List<Results> databaseResults = tournamentInfoUtil.getTournamentResults(tournamentId);
            List<String> rankedUuids;

            if (databaseResults != null && !databaseResults.isEmpty()) {
                // Sort Results by rank and return list in order of rank
                rankedUuids = playerSorter.sortPlayersByRank(databaseResults);
            } else {
                // Retrieve current round and signups information
                System.out.println("Getting tournament info");
                List<Signups> signups = tournamentInfoUtil.getSignupsByTournamentId(tournamentId);
                List<Matchups> previousMatchups = tournamentInfoUtil.getMatchupsByTournamentId(tournamentId);
                List<PlayerWins> playerWins = tournamentInfoUtil.getPlayerWinsByTournamentId(tournamentId);

                // Convert signups to PlayerResults format and update ratings based on previous
                // matchups
                List<PlayerResults> playerResults = ConversionUtil.convertSignupsToPlayerResults(signups);
                RatingUtil.updateRatings(previousMatchups, playerWins, playerResults);

                // Get sorted list of player UUIDs based on rank
                rankedUuids = ConversionUtil.getUuidsOrderedByRank(playerResults);

                // Update each player's Elo in the Signups table and insert tournament results
                System.out.println("UpdatePlayerElo");
                playerResults.forEach(results -> {
                    updatePlayerElo(results.getUuid(), results.getElo());
                    tournamentInfoUtil.insertTournamentResults(results.getUuid(), tournamentId, results.getRank());
                });
            }

            Map<String, String> uuidNameMap = getPlayerNames(rankedUuids);

            List<String> rankedNames = ConversionUtil.getRankedNames(rankedUuids, uuidNameMap);

            return rankedNames;
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
        ResponseEntity<Void> response = restTemplate.exchange(usermsvcEloUrl, HttpMethod.PUT, requestEntity,
                Void.class);

        // Check response status
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to update Elo: " + response.getStatusCode());
        }
    }

    /**
     * Retrieves a map of player UUIDs to player names by making a PUT request
     * to a user service with a list of UUIDs.
     * 
     * This method constructs a request body containing the provided UUIDs, sends
     * the request to the user service, and then extracts the 'content' field from
     * the response body, which is expected to be a map where the keys are player
     * UUIDs and the values are player names.
     * 
     * @param uuids A list of UUIDs of players whose names are to be retrieved.
     *              The service will return the names associated with these UUIDs.
     * @return A map where the keys are player UUIDs (as strings) and the values
     *         are player names (as strings), corresponding to the UUIDs in the
     *         input list.
     * @throws RuntimeException if the response from the user service has a non-OK
     *                          status
     *                          or if the 'content' field in the response is missing
     *                          or not as expected.
     */
    private Map<String, String> getPlayerNames(List<String> uuids) {
        // Prepare the request body
        System.out.println(uuids);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("data", uuids);

        System.out.println(requestBody);

        // Create an HttpEntity with the request body
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody);

        // Make the PUT request and expect a response with a Map containing the content
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                usermsvcNamesUrl, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        // Retrieve the "content" dictionary from the response body
        Map<String, Object> responseBody = response.getBody();

        // Check response status
        if (responseBody.get("success").equals(false)) {
            throw new RuntimeException("Failed to retrieve player names: " + response.getStatusCode());
        }

        System.out.println(responseBody);
        if (responseBody != null && responseBody.containsKey("content")) {
            return (Map<String, String>) responseBody.get("content");
        } else {
            throw new RuntimeException("Response body or 'content' field is missing");
        }
    }

}