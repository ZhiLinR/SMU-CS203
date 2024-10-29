package matchmaking.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import matchmaking.model.*;
import matchmaking.repository.*;
import matchmaking.util.PlayerSorter;

/**
 * Utility class for managing matchups in a tournament.
 */
@Component
public class MatchupManager {

    @Autowired
    private MatchupsRepository matchupsRepository;

    /**
     * Creates unique matchups for players, considering played pairs.
     *
     * @param players      the list of players to match.
     * @param tournamentId the ID of the tournament.
     * @param roundNum     the current round number.
     * @param playedPairs  the set of already played pairs.
     * @return a list of created matchups.
     */
    public List<Matchups> createUniqueMatchups(List<Signups> players, String tournamentId, int roundNum, Set<String> playedPairs) {
        List<Matchups> matchups = new ArrayList<>();

        // Sort players for the first round or apply Swiss-style for subsequent rounds
        if (roundNum == 1) {
            players = new PlayerSorter().sortPlayersByELO(players);
        } else {
            List<PlayerWins> playerWins = matchupsRepository.getPlayerWinsByTournamentId(tournamentId);
            players = swissPairing(playerWins, players, playedPairs);
        }

        // Create matchups from the sorted or paired players
        for (int i = 0; i < players.size() - 1; i += 2) {
            Signups player1 = players.get(i);
            Signups player2 = players.get(i + 1);

            String currentPair1 = player1.getUuid() + "-" + player2.getUuid();
            String currentPair2 = player2.getUuid() + "-" + player1.getUuid();

            // Skip duplicate pairs
            if (playedPairs.contains(currentPair1) || playedPairs.contains(currentPair2)) {
                System.out.println("Duplicate pair found, skipping to next available pair...");
                continue; // Skip to the next pair
            }

            // Create and add the valid matchup
            Matchups matchup = new Matchups();
            matchup.setPlayer1(player1.getUuid());
            matchup.setPlayer2(player2.getUuid());
            matchup.setPlayerWon(null);
            matchups.add(matchup);

            // Track the current pair to avoid future duplicates
            playedPairs.add(currentPair1);
            playedPairs.add(currentPair2);
        }

        // Handle odd number of players: Assign a bye to the last player
        if (players.size() % 2 != 0) {
            Signups byePlayer = players.get(players.size() - 1);
            Matchups byeMatchup = new Matchups();
            byeMatchup.setPlayer1(byePlayer.getUuid());
            byeMatchup.setPlayer2("No Player");
            byeMatchup.setPlayerWon(byePlayer.getUuid()); // Bye counts as a win
            matchups.add(byeMatchup);
        }

        // Insert matchups using the stored procedure
        insertMatchups(matchups, tournamentId, roundNum);
        return matchups;
    }

    /**
     * Inserts matchups into the repository.
     *
     * @param matchups      the list of matchups to insert.
     * @param tournamentId  the ID of the tournament.
     * @param roundNum      the current round number.
     */
    public void insertMatchups(List<Matchups> matchups, String tournamentId, int roundNum) {
        for (Matchups matchup : matchups) {
            matchupsRepository.insertMatchup(
                matchup.getPlayer1(),
                matchup.getPlayer2(),
                matchup.getPlayerWon(),
                tournamentId,
                roundNum
            );
        }
    }

    /**
     * Swiss-style pairing logic for players based on their current scores.
     *
     * @param playerWinsList the list of players with their wins and points.
     * @param players        the list of players to pair.
     * @param playedPairs    the set of already played pairs.
     * @return a list of players sorted for Swiss pairing.
     */
    private List<Signups> swissPairing(List<PlayerWins> playerWinsList, List<Signups> players, Set<String> playedPairs) {
        // Create a map from UUID to PlayerWins for quick access
        Map<String, PlayerWins> playerWinsMap = playerWinsList.stream()
                .collect(Collectors.toMap(PlayerWins::getUuid, playerWins -> playerWins));

        // Sort players by their scores based on PlayerWins
        players.sort((p1, p2) -> {
            double points1 = playerWinsMap.getOrDefault(p1.getUuid(), new PlayerWins()).getPoints();
            double points2 = playerWinsMap.getOrDefault(p2.getUuid(), new PlayerWins()).getPoints();
            return Double.compare(points2, points1); // Sort in descending order
        });

        return players;
    }

    /**
     * Retrieves the set of played pairs from previous matchups.
     *
     * @param previousMatchups the list of previous matchups.
     * @return a set of played pairs.
     */
    public Set<String> getPlayedPairs(List<Matchups> previousMatchups) {
        Set<String> playedPairs = new HashSet<>();
        for (Matchups matchup : previousMatchups) {
            String pair1 = matchup.getPlayer1() + "-" + matchup.getPlayer2();
            String pair2 = matchup.getPlayer2() + "-" + matchup.getPlayer1();
            playedPairs.add(pair1);
            playedPairs.add(pair2); // Add both orders to handle symmetry
        }
        return playedPairs;
    }
}

