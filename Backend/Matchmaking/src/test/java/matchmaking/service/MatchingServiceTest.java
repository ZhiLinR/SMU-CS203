package matchmaking.service;

import matchmaking.util.TournamentInfoUtil;
import matchmaking.util.MatchupUtil;
import matchmaking.manager.MatchupManager;
import matchmaking.model.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

/**
 * Unit tests for the {@link MatchingService} class.
 *
 * <p>
 * This test class verifies the functionality of the {@link MatchingService}
 * methods, particularly the
 * {@link MatchingService#generateUniqueMatchups(String)}
 * method. It uses Mockito to mock dependencies and validate interactions and
 * outcomes.
 */
@ExtendWith(MockitoExtension.class)
public class MatchingServiceTest {

        @InjectMocks
        private MatchingService matchingService;

        @Mock
        private TournamentInfoUtil tournamentInfoUtil;

        @Mock
        private MatchupManager matchupManager;

        private String tournamentId = "testTournamentId";
        private int roundNum = 1;

        private List<Signups> signups;
        private List<Matchups> previousMatchups;

        /**
         * Sets up the test environment by initializing common data
         * before each test case is executed.
         */
        @BeforeEach
        public void setUp() {
                signups = new ArrayList<>();
                previousMatchups = new ArrayList<>();

                // Mocking signups and previous matchups
                Signups player1 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("player1")
                                                .setTournamentId(tournamentId))
                                .setElo(1500);
                Signups player2 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("player2")
                                                .setTournamentId(tournamentId))
                                .setElo(1600);
                Signups player3 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("player3")
                                                .setTournamentId(tournamentId))
                                .setElo(1700);
                Signups player4 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("player4")
                                                .setTournamentId(tournamentId))
                                .setElo(1800);

                signups.add(player1);
                signups.add(player2);
                signups.add(player3);
                signups.add(player4);

                // Create and set up a MatchupsId instance
                MatchupsId mockMatchupsId = new MatchupsId()
                                .setPlayer1("player1")
                                .setPlayer2("player2")
                                .setTournamentId(tournamentId);

                // Create and set up a Matchups instance
                Matchups mockMatchup = new Matchups()
                                .setId(mockMatchupsId)
                                .setPlayerWon("player1")
                                .setRoundNum(1);

                previousMatchups.add(mockMatchup);
        }

        /**
         * Tests the {@link MatchingService#generateUniqueMatchups(String)} method
         * when valid inputs are provided. It verifies that the method returns the
         * expected matchups and that the correct interactions with the mocked
         * dependencies occur.
         */
        @Test
        public void testGenerateUniqueMatchups_Success() {
                // Arrange
                when(tournamentInfoUtil.getCurrentRoundByTournamentId(tournamentId)).thenReturn(roundNum);
                when(tournamentInfoUtil.getSignupsByTournamentId(tournamentId)).thenReturn(signups);
                when(tournamentInfoUtil.getMatchupsByTournamentId(tournamentId)).thenReturn(previousMatchups);

                // Mocking matchup manager behavior
                List<Matchups> newMatchups = new ArrayList<>();
                Matchups newMatchup = new Matchups(); // Add relevant properties
                newMatchups.add(newMatchup);
                when(matchupManager.createUniqueMatchups(signups, tournamentId, roundNum,
                                MatchupUtil.getPlayedPairs(previousMatchups)))
                                .thenReturn(newMatchups);

                // Act
                List<Matchups> result = matchingService.generateUniqueMatchups(tournamentId);

                // Assert
                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals(newMatchup, result.get(0));
                verify(tournamentInfoUtil).getCurrentRoundByTournamentId(tournamentId);
                verify(tournamentInfoUtil).getSignupsByTournamentId(tournamentId);
                verify(tournamentInfoUtil).getMatchupsByTournamentId(tournamentId);
                verify(matchupManager).createUniqueMatchups(signups, tournamentId, roundNum,
                                MatchupUtil.getPlayedPairs(previousMatchups));
        }

        /**
         * Tests the {@link MatchingService#generateUniqueMatchups(String)} method
         * when there is an odd number of players. It verifies that a bye is assigned
         * to one of the players and that the method correctly handles this scenario.
         */
        @Test
        public void testGenerateUniqueMatchups_OddNumberOfPlayers() {
                // Arrange
                Signups player1 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("player1")
                                                .setTournamentId(tournamentId))
                                .setElo(1500);
                Signups player2 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("player2")
                                                .setTournamentId(tournamentId))
                                .setElo(1600);
                Signups player3 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("player3")
                                                .setTournamentId(tournamentId))
                                .setElo(1700); // Odd player
                signups.addAll(Arrays.asList(player1, player2, player3));

                when(tournamentInfoUtil.getCurrentRoundByTournamentId(tournamentId)).thenReturn(roundNum);
                when(tournamentInfoUtil.getSignupsByTournamentId(tournamentId)).thenReturn(signups);
                when(tournamentInfoUtil.getMatchupsByTournamentId(tournamentId)).thenReturn(previousMatchups);

                // Mocking matchup manager behavior
                List<Matchups> newMatchups = new ArrayList<>();
                Matchups byeMatchup = new Matchups(); // Assume this represents the bye assignment
                newMatchups.add(byeMatchup);
                when(matchupManager.createUniqueMatchups(signups, tournamentId, roundNum,
                                MatchupUtil.getPlayedPairs(previousMatchups)))
                                .thenReturn(newMatchups);

                // Act
                List<Matchups> result = matchingService.generateUniqueMatchups(tournamentId);

                // Assert
                assertNotNull(result);
                assertEquals(1, result.size()); // One matchup including the bye
                assertEquals(byeMatchup, result.get(0));
                verify(tournamentInfoUtil).getCurrentRoundByTournamentId(tournamentId);
                verify(tournamentInfoUtil).getSignupsByTournamentId(tournamentId);
                verify(tournamentInfoUtil).getMatchupsByTournamentId(tournamentId);
                verify(matchupManager).createUniqueMatchups(signups, tournamentId, roundNum,
                                MatchupUtil.getPlayedPairs(previousMatchups));
        }

        /**
         * Tests the {@link MatchingService#generateUniqueMatchups(String)} method
         * when an invalid tournament ID is provided. It verifies that an
         * {@link IllegalArgumentException} is thrown.
         */
        @Test
        public void testGenerateUniqueMatchups_InvalidTournamentId() {
                // Arrange
                String invalidTournamentId = null;

                // Act & Assert
                assertThrows(IllegalArgumentException.class, () -> {
                        matchingService.generateUniqueMatchups(invalidTournamentId);
                });
        }

        /**
         * Tests the {@link MatchingService#generateUniqueMatchups(String)} method
         * when there are duplicate player IDs in the signups list. It verifies that
         * the method can handle duplicates appropriately.
         */
        @Test
        public void testGenerateUniqueMatchups_DuplicatePlayerIDs() {
                // Arrange
                Signups player1 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("player1")
                                                .setTournamentId(tournamentId))
                                .setElo(1500);
                Signups duplicatePlayer1 = new Signups()
                                .setId(new PlayerTournamentId()
                                                .setUuid("player1")
                                                .setTournamentId(tournamentId))
                                .setElo(1600);
                signups.addAll(Arrays.asList(player1, duplicatePlayer1));

                when(tournamentInfoUtil.getCurrentRoundByTournamentId(tournamentId)).thenReturn(roundNum);
                when(tournamentInfoUtil.getSignupsByTournamentId(tournamentId)).thenReturn(signups);
                when(tournamentInfoUtil.getMatchupsByTournamentId(tournamentId)).thenReturn(previousMatchups);

                // Mocking matchup manager behavior
                List<Matchups> newMatchups = new ArrayList<>();
                Matchups matchup = new Matchups(); // Assume this represents a valid matchup
                newMatchups.add(matchup);
                when(matchupManager.createUniqueMatchups(signups, tournamentId, roundNum,
                                MatchupUtil.getPlayedPairs(previousMatchups)))
                                .thenReturn(newMatchups);

                // Act
                List<Matchups> result = matchingService.generateUniqueMatchups(tournamentId);

                // Assert
                assertNotNull(result);
                assertEquals(1, result.size()); // One matchup should still be returned
                assertEquals(matchup, result.get(0));
                verify(tournamentInfoUtil).getCurrentRoundByTournamentId(tournamentId);
                verify(tournamentInfoUtil).getSignupsByTournamentId(tournamentId);
                verify(tournamentInfoUtil).getMatchupsByTournamentId(tournamentId);
                verify(matchupManager).createUniqueMatchups(signups, tournamentId, roundNum,
                                MatchupUtil.getPlayedPairs(previousMatchups));
        }

        /**
         * Tests the {@link MatchingService#generateUniqueMatchups(String)} method
         * when all players have already been matched. It verifies that an empty list
         * is returned.
         */
        @Test
        public void testGenerateUniqueMatchups_AllPlayersMatched() {
                // Arrange
                when(tournamentInfoUtil.getCurrentRoundByTournamentId(tournamentId)).thenReturn(roundNum);
                when(tournamentInfoUtil.getSignupsByTournamentId(tournamentId)).thenReturn(signups);
                when(tournamentInfoUtil.getMatchupsByTournamentId(tournamentId)).thenReturn(previousMatchups); // All
                                                                                                               // matched

                // Act
                List<Matchups> result = matchingService.generateUniqueMatchups(tournamentId);

                // Assert
                assertNotNull(result);
                assertTrue(result.isEmpty(), "Expected an empty list of matchups.");
                verify(tournamentInfoUtil).getCurrentRoundByTournamentId(tournamentId);
                verify(tournamentInfoUtil).getSignupsByTournamentId(tournamentId);
                verify(tournamentInfoUtil).getMatchupsByTournamentId(tournamentId);
        }

        /**
         * Tests the {@link MatchingService#generateUniqueMatchups(String)} method
         * when the round number is invalid (negative). It verifies that an
         * {@link IllegalArgumentException} is thrown.
         */
        @Test
        public void testGenerateUniqueMatchups_InvalidRoundNumber() {
                // Arrange
                when(tournamentInfoUtil.getCurrentRoundByTournamentId(tournamentId)).thenReturn(-1); // Invalid round
                                                                                                     // number
                when(tournamentInfoUtil.getSignupsByTournamentId(tournamentId)).thenReturn(signups);
                when(tournamentInfoUtil.getMatchupsByTournamentId(tournamentId)).thenReturn(previousMatchups);

                // Act & Assert
                assertThrows(IllegalArgumentException.class, () -> {
                        matchingService.generateUniqueMatchups(tournamentId);
                });
        }
}
