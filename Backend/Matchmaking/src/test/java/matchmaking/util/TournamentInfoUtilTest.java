package matchmaking.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import matchmaking.dto.PlayerWins;
import matchmaking.model.*;
import matchmaking.repository.*;

import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for the {@link TournamentInfoUtil} class.
 *
 * This class tests the functionality of tournament information utility methods,
 * including
 * retrieving matchups, signups, player wins, creating matchups, and inserting
 * matchups.
 */
public class TournamentInfoUtilTest {

    /** ID of the tournament being tested. */
    private String tournamentId;

    /** Utilities for player sorting and tournament information. */
    @InjectMocks
    private TournamentInfoUtil tournamentInfoUtil;

    /** Repositories for accessing matchups and signups. */
    @Mock
    private MatchupsRepository matchupsRepository;

    @Mock
    private SignupsRepository signupsRepository;

    /**
     * Sets up the test environment before each test case.
     * Initializes tournament ID, mock repositories, and utility class.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes mocks

        tournamentId = "Tournament123";

        // Mock return values for repository methods
        when(matchupsRepository.getCurrentRoundByTournamentId(tournamentId)).thenReturn(2); // Original round is 2
        when(matchupsRepository.getMatchupsByTournamentId(tournamentId)).thenReturn(createMockMatchups());
        when(signupsRepository.getSignupsByTournamentId(tournamentId)).thenReturn(createMockSignups());
        when(matchupsRepository.getPlayerWinsByTournamentId(tournamentId)).thenReturn(createMockPlayerWins());
    }

    /**
     * Creates a list of mock matchups for testing.
     *
     * @return a list of mock Matchups.
     */
    private List<Matchups> createMockMatchups() {
        List<Matchups> matchups = new ArrayList<>();
        MatchupsId matchId = new MatchupsId()
                .setPlayer1("Player1")
                .setPlayer2("Player2")
                .setTournamentId(tournamentId);

        Matchups matchup = new Matchups()
                .setId(matchId)
                .setRoundNum(1)
                .setPlayerWon(null); // No winner set
        matchups.add(matchup);
        return matchups;
    }

    /**
     * Creates a list of mock signups for testing.
     *
     * @return a list of mock Signups.
     */
    private List<Signups> createMockSignups() {
        List<Signups> signups = new ArrayList<>();
        Signups player1 = new Signups()
                .setId(new PlayerTournamentId().setUuid("Player1"));
        Signups player2 = new Signups()
                .setId(new PlayerTournamentId().setUuid("Player2"));

        signups.add(player1);
        signups.add(player2);

        return signups;
    }

    /**
     * Creates a list of mock player wins for testing.
     *
     * @return a list of mock PlayerWins.
     */
    private List<Object[]> createMockPlayerWins() {
        List<Object[]> playerWins = new ArrayList<>();
        playerWins.add(new Object[] { "Player1", 3, 1 }); // UUID, wins, draws
        return playerWins;
    }

    /**
     * Tests the retrieval of the current round by tournament ID.
     * Asserts that the current round is equal to the expected value.
     */
    @Test
    public void testGetCurrentRoundByTournamentId() {
        int round = tournamentInfoUtil.getCurrentRoundByTournamentId(tournamentId);
        assertEquals(3, round, "The current round should be incremented to 3.");
    }

    /**
     * Tests the exception thrown when retrieving the current round with a null
     * tournament ID.
     * Asserts that an IllegalArgumentException is thrown.
     */
    @Test
    public void testGetCurrentRoundByTournamentIdThrowsExceptionIfTournamentIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> tournamentInfoUtil.getCurrentRoundByTournamentId(null),
                "Expected IllegalArgumentException when tournamentId is null.");
    }

    /**
     * Tests the retrieval of matchups by tournament ID.
     * Asserts that the returned matchups are as expected.
     */
    @Test
    public void testGetMatchupsByTournamentId() {
        List<Matchups> matchups = tournamentInfoUtil.getMatchupsByTournamentId(tournamentId);
        assertNotNull(matchups, "Matchups list should not be null.");
        assertEquals(1, matchups.size(), "There should be one matchup.");
        assertEquals("Player1", matchups.get(0).getId().getPlayer1(), "First player should be Player1.");
        assertEquals("Player2", matchups.get(0).getId().getPlayer2(), "Second player should be Player2.");
    }

    /**
     * Tests the exception thrown when retrieving matchups with a null tournament
     * ID.
     * Asserts that an IllegalArgumentException is thrown.
     */
    @Test
    public void testGetMatchupsByTournamentIdThrowsExceptionIfTournamentIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> tournamentInfoUtil.getMatchupsByTournamentId(null),
                "Expected IllegalArgumentException when tournamentId is null.");
    }

    /**
     * Tests the retrieval of signups by tournament ID.
     * Asserts that the returned signups are as expected.
     */
    @Test
    public void testGetSignupsByTournamentId() {
        List<Signups> signups = tournamentInfoUtil.getSignupsByTournamentId(tournamentId);
        assertNotNull(signups, "Signups list should not be null.");
        assertEquals(2, signups.size(), "There should be two signups.");
        assertEquals("Player1", signups.get(0).getId().getUuid(), "First signup should be Player1.");
        assertEquals("Player2", signups.get(1).getId().getUuid(), "Second signup should be Player2.");
    }

    /**
     * Tests the exception thrown when retrieving signups with a null tournament ID.
     * Asserts that an IllegalArgumentException is thrown.
     */
    @Test
    public void testGetSignupsByTournamentIdThrowsExceptionIfTournamentIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> tournamentInfoUtil.getSignupsByTournamentId(null),
                "Expected IllegalArgumentException when tournamentId is null.");
    }

    /**
     * Tests the retrieval of player wins by tournament ID.
     * Asserts that the returned player wins are as expected.
     */
    @Test
    public void testGetPlayerWinsByTournamentId() {
        List<PlayerWins> playerWins = tournamentInfoUtil.getPlayerWinsByTournamentId(tournamentId);
        assertNotNull(playerWins, "Player wins list should not be null.");
        assertEquals(1, playerWins.size(), "There should be one player's wins record.");
        assertEquals("Player1", playerWins.get(0).getUuid(), "The player's UUID should be Player1.");
        assertEquals(3, playerWins.get(0).getWins(), "Player1 should have 3 wins.");
        assertEquals(1, playerWins.get(0).getDraws(), "Player1 should have 1 draw.");
    }

    /**
     * Tests the exception thrown when retrieving player wins with a null tournament
     * ID.
     * Asserts that an IllegalArgumentException is thrown.
     */
    @Test
    public void testGetPlayerWinsByTournamentIdThrowsExceptionIfTournamentIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> tournamentInfoUtil.getPlayerWinsByTournamentId(null),
                "Expected IllegalArgumentException when tournamentId is null.");
    }

    /**
     * Tests the creation of a matchup without specifying a winner.
     * Asserts that the created matchup is as expected.
     */
    @Test
    public void testCreateMatchupWithoutWinner() {
        Signups player1 = new Signups()
                .setId(new PlayerTournamentId().setUuid("Player1"));
        Signups player2 = new Signups()
                .setId(new PlayerTournamentId().setUuid("Player2"));

        Matchups matchup = tournamentInfoUtil.createMatchup(player1, player2, tournamentId, 1);

        assertNotNull(matchup, "Matchup should not be null.");
        assertEquals("Player1", matchup.getId().getPlayer1(), "First player should be Player1.");
        assertEquals("Player2", matchup.getId().getPlayer2(), "Second player should be Player2.");
        assertEquals(1, matchup.getRoundNum(), "Round number should be 1.");
        assertNull(matchup.getPlayerWon(), "Player won should be null.");
    }

    /**
     * Tests the creation of a matchup with a specified winner.
     * Asserts that the created matchup reflects the winner correctly.
     */
    @Test
    public void testCreateMatchupWithWinner() {
        Signups player1 = new Signups()
                .setId(new PlayerTournamentId().setUuid("Player1"));
        Signups player2 = new Signups()
                .setId(new PlayerTournamentId().setUuid("Player2"));
        Signups winner = player1;

        Matchups matchup = tournamentInfoUtil.createMatchup(player1, player2, winner, tournamentId, 1);

        assertNotNull(matchup, "Matchup should not be null.");
        assertEquals("Player1", matchup.getId().getPlayer1(), "First player should be Player1.");
        assertEquals("Player2", matchup.getId().getPlayer2(), "Second player should be Player2.");
        assertEquals(1, matchup.getRoundNum(), "Round number should be 1.");
        assertEquals("Player1", matchup.getPlayerWon(), "Player won should be Player1.");
    }

    /**
     * Tests the insertion of matchups into the repository.
     * Verifies that the insert method of the repository is called with the correct
     * parameters.
     */
    @Test
    public void testInsertMatchups() {
        Signups player1 = new Signups()
                .setId(new PlayerTournamentId().setUuid("Player1"));
        Signups player2 = new Signups()
                .setId(new PlayerTournamentId().setUuid("Player2"));

        Matchups matchup = tournamentInfoUtil.createMatchup(player1, player2, tournamentId, 1);
        tournamentInfoUtil.insertMatchups(List.of(matchup), tournamentId, 1);

        verify(matchupsRepository, times(1)).insertMatchup(
                matchup.getId().getPlayer1(),
                matchup.getId().getPlayer2(),
                null,
                tournamentId,
                1);
    }

    /**
     * Tests the exception thrown when trying to insert an empty list of matchups.
     * Asserts that an IllegalArgumentException is thrown.
     */
    @Test
    public void testInsertMatchupsThrowsExceptionWithEmptyMatchupsList() {
        assertThrows(IllegalArgumentException.class,
                () -> tournamentInfoUtil.insertMatchups(new ArrayList<>(), tournamentId, 1),
                "Expected IllegalArgumentException when matchups list is empty.");
    }

    /**
     * Tests updating Elo rating with valid parameters.
     */
    @Test
    public void testUpdateSignupsPlayerElo_ValidInput() {
        String uuid = "Player1";
        int newElo = 1500;

        // Call the method under test
        tournamentInfoUtil.updateSignupsPlayerElo(uuid, newElo);

        // Verify that the updateElo method was called with the correct parameters
        verify(signupsRepository).updateElo(uuid, newElo);
    }

    /**
     * Tests updating Elo rating with a null UUID.
     */
    @Test
    public void testUpdateSignupsPlayerElo_NullUuid() {
        assertThrows(IllegalArgumentException.class, () -> {
            tournamentInfoUtil.updateSignupsPlayerElo(null, 1500);
        });
    }

    /**
     * Tests updating Elo rating with an empty UUID.
     */
    @Test
    public void testUpdateSignupsPlayerElo_EmptyUuid() {
        assertThrows(IllegalArgumentException.class, () -> {
            tournamentInfoUtil.updateSignupsPlayerElo("", 1500);
        });
    }

    /**
     * Tests updating Elo rating with a zero Elo value.
     */
    @Test
    public void testUpdateSignupsPlayerElo_ZeroElo() {
        String uuid = "Player1";
        assertThrows(IllegalArgumentException.class, () -> {
            tournamentInfoUtil.updateSignupsPlayerElo(uuid, 0);
        });
    }

    /**
     * Tests updating Elo rating with a negative Elo value.
     */
    @Test
    public void testUpdateSignupsPlayerElo_NegativeElo() {
        String uuid = "Player1";
        assertThrows(IllegalArgumentException.class, () -> {
            tournamentInfoUtil.updateSignupsPlayerElo(uuid, -100);
        });
    }
}
