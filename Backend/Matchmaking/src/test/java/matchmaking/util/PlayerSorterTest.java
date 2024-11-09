package matchmaking.util;

import matchmaking.model.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Unit tests for the {@link PlayerSorter} class.
 */
class PlayerSorterTest {

        @Autowired
        private PlayerSorter playerSorter;

        /**
         * Sets up the test environment before each test. Initializes the
         * {@link PlayerSorter}
         * instance and opens any required mock annotations.
         */
        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                playerSorter = new PlayerSorter();
        }

        /**
         * Tests the {@link PlayerSorter#sortPlayers(List, Map, boolean)} method to
         * ensure players
         * are sorted by ELO in descending order when no points map is provided.
         */
        @Test
        void testSortPlayersByElo() {
                Signups player1 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("Player 1")
                                                .setTournamentId("Tournament123"))
                                .setElo(1500);
                Signups player2 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("Player 2")
                                                .setTournamentId("Tournament123"))
                                .setElo(1800);
                Signups player3 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("Player 3")
                                                .setTournamentId("Tournament123"))
                                .setElo(1600);

                List<Signups> players = Arrays.asList(player1, player2, player3);

                List<Signups> sortedPlayers = playerSorter.sortPlayers(players, null, true);

                assertEquals(player2, sortedPlayers.get(0)); // Player 2 should come first
                assertEquals(player3, sortedPlayers.get(1)); // Player 3 second
                assertEquals(player1, sortedPlayers.get(2)); // Player 1 last
        }

        /**
         * Tests the {@link PlayerSorter#sortPlayers(List, Map, boolean)} method to
         * ensure players
         * are sorted by points in descending order when a points map is provided.
         */
        @Test
        void testSortPlayersByPoints() {
                Map<String, Double> playerPoints = new HashMap<>();
                playerPoints.put("Player 1", 50.0);
                playerPoints.put("Player 2", 75.0);
                playerPoints.put("Player 3", 30.0);

                Signups player1 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("Player 1")
                                                .setTournamentId("Tournament123"))
                                .setElo(1500);
                Signups player2 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("Player 2")
                                                .setTournamentId("Tournament123"))
                                .setElo(1800);
                Signups player3 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("Player 3")
                                                .setTournamentId("Tournament123"))
                                .setElo(1600);
                List<Signups> players = Arrays.asList(player1, player2, player3);

                List<Signups> sortedPlayers = playerSorter.sortPlayers(players, playerPoints, false);

                assertEquals(player2, sortedPlayers.get(0)); // Player 2 should have most points
                assertEquals(player1, sortedPlayers.get(1)); // Player 1 second
                assertEquals(player3, sortedPlayers.get(2)); // Player 3 last
        }

        /**
         * Tests the {@link PlayerSorter#sortPlayersForRound(List, String, int)} method
         * to ensure players
         * are sorted by ELO in descending order for the first round.
         */
        @Test
        void testSortPlayersForRoundRound1() {

                Signups player1 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("Player 1")
                                                .setTournamentId("Tournament123"))
                                .setElo(1500);
                Signups player2 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("Player 2")
                                                .setTournamentId("Tournament123"))
                                .setElo(1800);
                Signups player3 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("Player 3")
                                                .setTournamentId("Tournament123"))
                                .setElo(1600);

                List<Signups> players = Arrays.asList(player1, player2, player3);

                List<Signups> sortedPlayers = playerSorter.sortPlayersForRound(players, "tournament1", 1);

                assertEquals(player2, sortedPlayers.get(0)); // Player 2 should come first
                assertEquals(player3, sortedPlayers.get(1)); // Player 3 second
                assertEquals(player1, sortedPlayers.get(2)); // Player 1 last
        }

        /**
         * Tests the {@link PlayerSorter#sortPlayersByRank(List)} method to ensure
         * players are
         * sorted by their ranking in ascending order.
         */
        @Test
        void testSortPlayersByRank() {
                Results result1 = new Results()
                                .setId(new PlayerTournamentId()
                                                .setUuid("Player 1")
                                                .setTournamentId("Tournament123"))
                                .setRanking(2);
                Results result2 = new Results()
                                .setId(new PlayerTournamentId()
                                                .setUuid("Player 2")
                                                .setTournamentId("Tournament123"))
                                .setRanking(1);
                Results result3 = new Results()
                                .setId(new PlayerTournamentId()
                                                .setUuid("Player 1")
                                                .setTournamentId("Tournament123"))
                                .setRanking(3);

                List<Results> results = Arrays.asList(result1, result2, result3);

                List<String> sortedPlayerIds = playerSorter.sortPlayersByRank(results);

                assertEquals(result2.getId().getUuid(), sortedPlayerIds.get(0)); // Player 2 should be first (rank 1)
                assertEquals(result1.getId().getUuid(), sortedPlayerIds.get(1)); // Player 1 second
                assertEquals(result3.getId().getUuid(), sortedPlayerIds.get(2)); // Player 3 last
        }
}
