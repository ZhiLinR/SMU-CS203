package matchmaking.util;

import matchmaking.dto.PlayerWins;
import matchmaking.model.*;
import matchmaking.repository.MatchupsRepository;
import matchmaking.repository.SignupsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Test class for validating the tournament and matchmaking functionality.
 */
public class MatchupUtilTest {

    /** ID of the tournament being tested. */
    private String tournamentId;

    /** Set of played pairs to track match history. */
    private Set<String> playedPairs;

    /** Players participating in the tournament. */
    private Signups player1;
    private Signups player2;
    private Signups player3;
    private Signups player4;

    private Matchups match1;

    /** Utilities for player sorting and tournament information. */
    private PlayerSorter playerSorter;
    private TournamentInfoUtil tournamentInfoUtil;

    /** Repositories for accessing matchups and signups. */
    private MatchupsRepository matchupsRepository;
    private SignupsRepository signupsRepository;

    /**
     * Sets up the test environment before each test case.
     */
    @BeforeEach
    public void setUp() {
        tournamentId = "Tournament123";
        playedPairs = new HashSet<>();

        initializeRepositories();
        initializeUtilities();
        initializePlayers();
        initializeMockResponses();
    }

    /**
     * Initializes the mock repositories for testing.
     */
    private void initializeRepositories() {
        matchupsRepository = mock(MatchupsRepository.class);
        signupsRepository = mock(SignupsRepository.class);
    }

    /**
     * Initializes the utility classes and injects dependencies.
     */
    private void initializeUtilities() {
        tournamentInfoUtil = new TournamentInfoUtil();
        ReflectionTestUtils.setField(tournamentInfoUtil, "matchupsRepository", matchupsRepository);

        playerSorter = new PlayerSorter();
        ReflectionTestUtils.setField(playerSorter, "tournamentInfoUtil", tournamentInfoUtil);
    }

    /**
     * Initializes player objects for testing.
     */
    private void initializePlayers() {
        player1 = createPlayer("Player1", 1100);
        player2 = createPlayer("Player2", 1000);
        player3 = createPlayer("Player3", 1200);
        player4 = createPlayer("Player4", 1050);
    }

    /**
     * Helper method to create a player with a given UUID and ELO.
     *
     * @param uuid the unique identifier for the player.
     * @param elo  the ELO rating of the player.
     * @return a new Signups object.
     */
    private Signups createPlayer(String uuid, int elo) {
        PlayerTournamentId id = new PlayerTournamentId()
                .setUuid(uuid)
                .setTournamentId(uuid);
        Signups player = new Signups()
                .setId(id)
                .setElo(elo);
        return player;
    }

    /**
     * Initializes mock responses for the tournament information utility.
     */
    private void initializeMockResponses() {
        // Sample player wins data
        List<PlayerWins> playerWins = List.of(
                createPlayerWins("Player1", 3, 1),
                createPlayerWins("Player2", 2, 0),
                createPlayerWins("Player3", 1, 2),
                createPlayerWins("Player4", 4, 0));

        // Mock method responses for TournamentInfoUtil
        when(tournamentInfoUtil.getPlayerWinsByTournamentId(tournamentId)).thenReturn(playerWins);

        // Mock matchups
        MatchupsId matchId1 = new MatchupsId()
                .setPlayer1(player1.getId().getUuid())
                .setPlayer2(player2.getId().getUuid())
                .setTournamentId(tournamentId);

        match1 = new Matchups() // Properly initialize match1
                .setId(matchId1)
                .setRoundNum(1)
                .setPlayerWon("Player1");

        when(matchupsRepository.getMatchupsByTournamentId(tournamentId)).thenReturn(List.of(match1));

        // Mock signups
        when(signupsRepository.getSignupsByTournamentId(tournamentId))
                .thenReturn(List.of(player1, player2, player3, player4));

        // Set up current round response
        when(matchupsRepository.getCurrentRoundByTournamentId(tournamentId)).thenReturn(2);
    }

    /**
     * Helper method to create PlayerWins objects for testing.
     *
     * @param uuid  the UUID of the player.
     * @param wins  the number of wins.
     * @param draws the number of draws.
     * @return a new PlayerWins object.
     */
    private PlayerWins createPlayerWins(String uuid, int wins, int draws) {
        PlayerWins win = new PlayerWins()
                .setUuid(uuid)
                .setWins(wins)
                .setDraws(draws);
        return win;
    }

    /**
     * Tests that adding a played pair registers both directions (A-B and B-A).
     */
    @Test
    public void testAddPlayedPairAddsBothDirections() {
        MatchupUtil.addPlayedPair(player1.getId().getUuid(), player2.getId().getUuid(), playedPairs);

        assertTrue(playedPairs.contains("Player1-Player2"), "The pair 'Player1-Player2' should be added.");
        assertTrue(playedPairs.contains("Player2-Player1"), "The pair 'Player2-Player1' should be added.");
    }

    /**
     * Tests that adding an already existing played pair does not increase the size
     * of the set.
     */
    @Test
    public void testAddPlayedPairDoesNotDuplicateExistingPairs() {
        MatchupUtil.addPlayedPair(player1.getId().getUuid(), player2.getId().getUuid(), playedPairs);
        int initialSize = playedPairs.size();

        MatchupUtil.addPlayedPair(player1.getId().getUuid(), player2.getId().getUuid(), playedPairs);

        assertEquals(initialSize, playedPairs.size(), "Adding the same pair again should not change the size.");
    }

    /**
     * Tests that retrieving played pairs correctly includes all previously played
     * matchups.
     */
    @Test
    public void testGetPlayedPairsRetrievesCorrectPairs() {
        // Set up additional players and matchups for this test
        MatchupsId matchId2 = new MatchupsId()
                .setPlayer1(player3.getId().getUuid())
                .setPlayer2(player4.getId().getUuid())
                .setTournamentId(tournamentId);

        Matchups match2 = new Matchups()
                .setId(matchId2)
                .setRoundNum(1);

        List<Matchups> previousMatchups = List.of(match1, match2);
        Set<String> result = MatchupUtil.getPlayedPairs(previousMatchups);

        assertTrue(result.contains("Player1-Player2"), "Should contain 'Player1-Player2'.");
        assertTrue(result.contains("Player2-Player1"), "Should contain 'Player2-Player1'.");
        assertTrue(result.contains("Player3-Player4"), "Should contain 'Player3-Player4'.");
        assertTrue(result.contains("Player4-Player3"), "Should contain 'Player4-Player3'.");
    }

    /**
     * Tests that retrieving played pairs from an empty list returns an empty set.
     */
    @Test
    public void testGetPlayedPairsWithEmptyListReturnsEmptySet() {
        List<Matchups> previousMatchups = List.of();

        Set<String> result = MatchupUtil.getPlayedPairs(previousMatchups);

        assertTrue(result.isEmpty(), "Should return an empty set when no matchups are provided.");
    }

    /**
     * Tests that retrieving played pairs handles duplicate matchups correctly.
     */
    @Test
    public void testGetPlayedPairsHandlesDuplicates() {
        // Creating duplicate matchup with swapped players for duplicate test
        MatchupsId duplicateMatchId = new MatchupsId()
                .setPlayer1(player2.getId().getUuid())
                .setPlayer2(player1.getId().getUuid())
                .setTournamentId(tournamentId);

        Matchups duplicateMatch = new Matchups()
                .setId(duplicateMatchId)
                .setRoundNum(2);

        List<Matchups> previousMatchups = List.of(match1, duplicateMatch);
        Set<String> result = MatchupUtil.getPlayedPairs(previousMatchups);

        assertEquals(2, result.size(), "Should contain exactly 2 unique pairs.");
        assertTrue(result.contains("Player1-Player2"), "Should contain 'Player1-Player2'.");
        assertTrue(result.contains("Player2-Player1"), "Should contain 'Player2-Player1'.");
    }
}
