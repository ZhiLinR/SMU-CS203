package matchmaking.util;

import matchmaking.dto.*;
import matchmaking.exception.ResultsNotFoundException;
import matchmaking.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the RatingUtil class, which provides methods for updating
 * player ratings, calculating Buchholz scores, updating Elo ratings, and
 * calculating player ranks based on match results.
 */
public class RatingUtilTest {

        private List<Matchups> matchups;
        private List<PlayerWins> playerWins;
        private List<PlayerResults> playerResults;

        /**
         * Sets up initial test data structures for each test case by
         * initializing the matchups, playerWins, and playerResults lists.
         */
        @BeforeEach
        public void setUp() {
                matchups = new ArrayList<>();
                playerWins = new ArrayList<>();
                playerResults = new ArrayList<>();
        }

        /**
         * Utility method to set up mock results for a specified list of players.
         *
         * @param uuids    The UUIDs of the players to set up.
         * @param elos     The Elo ratings for the players.
         * @param buchholz The initial Buchholz scores for the players.
         * @param wins     The number of wins for the players.
         * @param draws    The number of draws for the players.
         */
        private void setupMockResults(String[] uuids, int[] elos, int[] buchholz, Integer[] wins, Integer[] draws) {
                for (int i = 0; i < uuids.length; i++) {
                        // Set up PlayerResults
                        PlayerResults playerResults = new PlayerResults()
                                        .setUuid(uuids[i])
                                        .setElo(elos[i])
                                        .setBuchholz(buchholz[i])
                                        .setRank(0); // Initial rank
                        this.playerResults.add(playerResults);

                        // Set up PlayerWins
                        PlayerWins playerWins = new PlayerWins()
                                        .setUuid(uuids[i])
                                        .setWins(wins[i])
                                        .setDraws(draws[i]);
                        this.playerWins.add(playerWins);
                }
        }

        /**
         * Tests the successful update of player ratings. Verifies that ranks,
         * Buchholz scores, and Elo ratings are correctly calculated and assigned.
         *
         * @throws ResultsNotFoundException if player results cannot be found
         */
        @Test
        public void testUpdateRatings_Success() throws ResultsNotFoundException {
                // Setup mock matchups
                MatchupsId id1 = new MatchupsId()
                                .setPlayer1("Player1")
                                .setPlayer2("Player2");
                Matchups matchup1 = new Matchups()
                                .setPlayerWon("Player1")
                                .setId(id1);
                matchups.add(matchup1);

                // Setup mock results
                setupMockResults(new String[] { "Player1", "Player2" }, new int[] { 1600, 1500 }, new int[] { 0, 0 },
                                new Integer[] { 3, 1 }, new Integer[] { 0, 1 });

                // Execute the method under test
                RatingUtil.updateRatings(matchups, playerWins, playerResults);

                System.out.println(playerResults);

                // Verify results
                assertEquals(1, playerResults.get(0).getRank()); // Player1 should have rank 1
                assertEquals(1500, playerResults.get(0).getBuchholz()); // Player1's Buchholz updated correctly
                assertEquals(1607, playerResults.get(0).getElo()); // Elo should be updated
                assertEquals(1493, playerResults.get(1).getElo()); // Elo should be updated for Player2
        }

        /**
         * Tests that an exception is thrown if there are missing player results.
         * Verifies that a ResultsNotFoundException is thrown when player data
         * cannot be found in the player results list.
         */
        @Test
        public void testUpdateRatings_MissingPlayerResults() {
                MatchupsId id1 = new MatchupsId()
                                .setPlayer1("Player1")
                                .setPlayer2("Player2");

                Matchups matchup1 = new Matchups()
                                .setPlayerWon("Player1")
                                .setId(id1);
                matchups.add(matchup1);

                // Setup mock result only for Player1
                PlayerResults player1Results = new PlayerResults()
                                .setUuid("Player1");
                playerResults.add(player1Results);

                // Setup mock results for PlayerWins, missing Player2
                PlayerWins playerWins = new PlayerWins()
                                .setUuid("Player1")
                                .setWins(3)
                                .setDraws(0);
                this.playerWins.add(playerWins);

                // Expect exception
                assertThrows(ResultsNotFoundException.class, () -> {
                        RatingUtil.updateRatings(matchups, this.playerWins, playerResults);
                });
        }

        /**
         * Tests that the Buchholz score is correctly calculated for players
         * based on their opponents' Elo ratings.
         *
         * @throws ResultsNotFoundException if player results cannot be found
         */
        @Test
        public void testCalculateBuchholzScore() throws ResultsNotFoundException {
                // Setup mock matchups
                MatchupsId id1 = new MatchupsId()
                                .setPlayer1("Player1")
                                .setPlayer2("Player2");

                Matchups matchup1 = new Matchups()
                                .setPlayerWon("Player1")
                                .setId(id1);
                matchups.add(matchup1);

                // Setup mock results
                setupMockResults(new String[] { "Player1", "Player2" }, new int[] { 1600, 1500 }, new int[] { 0, 0 },
                                new Integer[] { 3, 1 }, new Integer[] { 0, 0 });

                // Execute the method under test
                RatingUtil.updateRatings(matchups, playerWins, playerResults);

                // Verify Buchholz scores
                assertEquals(1500, playerResults.get(0).getBuchholz()); // Player1's Buchholz should include Player2's
                                                                        // Elo
        }

        /**
         * Tests that Elo ratings are correctly updated for players based on match
         * results.
         *
         * @throws ResultsNotFoundException if player results cannot be found
         */
        @Test
        public void testUpdateEloRatings() throws ResultsNotFoundException {
                // Setup mock matchups
                MatchupsId id1 = new MatchupsId()
                                .setPlayer1("Player1")
                                .setPlayer2("Player2");

                Matchups matchup1 = new Matchups()
                                .setPlayerWon("Player1")
                                .setId(id1);
                matchups.add(matchup1);

                // Setup mock results
                setupMockResults(new String[] { "Player1", "Player2" }, new int[] { 1600, 1500 }, new int[] { 0, 0 },
                                new Integer[] { 3, 1 }, new Integer[] { 0, 0 });

                // Execute the method under test
                RatingUtil.updateRatings(matchups, playerWins, playerResults);

                // Verify Elo ratings
                assertEquals(1607, playerResults.get(0).getElo()); // Updated Elo for Player1
                assertEquals(1493, playerResults.get(1).getElo()); // Updated Elo for Player2
        }

        /**
         * Tests that ranks are correctly calculated based on player wins, draws, and
         * Buchholz scores.
         *
         * @throws ResultsNotFoundException if player results cannot be found
         */
        @Test
        public void testCalculateRanks() throws ResultsNotFoundException {
                // Setup mock results
                setupMockResults(new String[] { "Player1", "Player2" }, new int[] { 1600, 1500 },
                                new int[] { 100, 200 },
                                new Integer[] { 3, 5 }, new Integer[] { 0, 1 });

                // Execute the method under test
                RatingUtil.updateRatings(matchups, playerWins, playerResults);

                // Verify ranks
                assertEquals(2, playerResults.get(0).getRank()); // Player1 should be rank 2
                assertEquals(1, playerResults.get(1).getRank()); // Player2 should be rank 1
        }
}
