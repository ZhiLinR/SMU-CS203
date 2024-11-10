package matchmaking.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import matchmaking.dto.*;
import matchmaking.exception.*;
import matchmaking.model.*;
import matchmaking.repository.*;

/**
 * A test class for validating utility methods in the {@code ValidationUtil}
 * class.
 * Tests various validation scenarios related to tournaments, player matchups,
 * and player signups.
 */
public class ValidationUtilTest {

    /** ID of the tournament being tested. */
    private String tournamentId;

    /** Set of played pairs to track match history. */
    private Set<String> playedPairs;

    /** Player objects representing participants in the tournament. */
    private Signups player1;
    private Signups player2;
    private Signups player3;
    private Signups player4;

    /** Sample matchups for testing different validation scenarios. */
    private Matchups matchup;
    private Matchups matchupWonLoss;

    /** Utility objects for player sorting and tournament information. */
    private PlayerSorter playerSorter;
    private TournamentInfoUtil tournamentInfoUtil;

    /** Repositories for accessing matchups and signups data. */
    private MatchupsRepository matchupsRepository;
    private SignupsRepository signupsRepository;

    /**
     * Sets up the test environment before each test case, initializing
     * player objects, mock repositories, and other dependencies.
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
     * Initializes mock repositories for testing repository-dependent utilities.
     */
    private void initializeRepositories() {
        matchupsRepository = mock(MatchupsRepository.class);
        signupsRepository = mock(SignupsRepository.class);
    }

    /**
     * Initializes utility classes and injects mock dependencies into them.
     */
    private void initializeUtilities() {
        tournamentInfoUtil = new TournamentInfoUtil();
        ReflectionTestUtils.setField(tournamentInfoUtil, "matchupsRepository", matchupsRepository);

        playerSorter = new PlayerSorter();
        ReflectionTestUtils.setField(playerSorter, "tournamentInfoUtil", tournamentInfoUtil);
    }

    /**
     * Initializes player objects with predefined UUIDs and ELO ratings for testing.
     */
    private void initializePlayers() {
        player1 = createPlayer("Player1", 1100);
        player2 = createPlayer("Player2", 1000);
        player3 = createPlayer("Player3", 1200);
        player4 = createPlayer("Player4", 1050);
    }

    /**
     * Creates a player for testing with a given UUID and ELO rating.
     *
     * @param uuid the unique identifier for the player.
     * @param elo  the ELO rating of the player.
     * @return a new {@code Signups} object representing the player.
     */
    private Signups createPlayer(String uuid, int elo) {
        return new Signups()
                .setId(new PlayerTournamentId()
                        .setTournamentId(tournamentId)
                        .setUuid(uuid))
                .setElo(elo);
    }

    /**
     * Initializes mock responses for tournament utilities to simulate real data.
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

        matchup = new Matchups()
                .setId(matchId1)
                .setRoundNum(1)
                .setPlayerWon("Player1");

        MatchupsId matchupsIdWonLoss = new MatchupsId()
                .setPlayer1("player-won")
                .setPlayer2("player-lost")
                .setTournamentId(tournamentId);

        matchupWonLoss = new Matchups()
                .setPlayerWon("player-won")
                .setId(matchupsIdWonLoss);

        when(matchupsRepository.getMatchupsByTournamentId(tournamentId)).thenReturn(List.of(matchup));

        // Mock signups
        when(signupsRepository.getSignupsByTournamentId(tournamentId))
                .thenReturn(List.of(player1, player2, player3, player4));

        // Set up current round response
        when(matchupsRepository.getCurrentRoundByTournamentId(tournamentId)).thenReturn(2);
    }

    /**
     * Creates a {@code PlayerWins} object for testing with a specified UUID, wins,
     * and draws.
     *
     * @param uuid  the UUID of the player.
     * @param wins  the number of wins the player has achieved.
     * @param draws the number of draws the player has.
     * @return a new {@code PlayerWins} object representing the player's results.
     */
    private PlayerWins createPlayerWins(String uuid, int wins, int draws) {
        PlayerWins win = new PlayerWins();
        win.setUuid(uuid);
        win.setWins(wins);
        win.setDraws(draws);
        return win;
    }

    // Test methods
    /**
     * Tests that validateNotEmpty throws an exception when given an empty value.
     */
    @Test
    public void testValidateNotEmptyThrowsExceptionWhenEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateNotEmpty("", "TestField"));
        assertEquals("TestField must not be null or empty.", exception.getMessage());
    }

    /**
     * Tests that isValidPair returns true for a new pair of players.
     */
    @Test
    public void testIsValidPairReturnsTrueForNewPair() {
        boolean result = ValidationUtil.isValidPair("Player1", "Player2", playedPairs);
        assertTrue(result);
    }

    /**
     * Tests that isValidPair returns false for an existing pair of players.
     */
    @Test
    public void testIsValidPairReturnsFalseForExistingPair() {
        playedPairs.add("Player1-Player2");
        boolean result = ValidationUtil.isValidPair("Player1", "Player2", playedPairs);
        assertFalse(result);
    }

    /**
     * Tests that isValidRoundNum throws an exception for an invalid round number.
     */
    @Test
    public void testIsValidRoundNumThrowsExceptionForInvalidRound() {
        int playerCount = 3;
        assertThrows(InvalidRoundException.class, () -> ValidationUtil.isValidRoundNum(4, playerCount));
    }

    /**
     * Tests that isPrevRoundOver throws an exception when winner is not allocated.
     */
    @Test
    public void testIsPrevRoundOverThrowsExceptionWhenWinnerNotAllocated() {
        Matchups unfinishedMatchup = new Matchups();
        unfinishedMatchup.setId(matchup.getId());
        unfinishedMatchup.setPlayerWon(null);

        assertThrows(InvalidRoundException.class, () -> ValidationUtil.isPrevRoundOver(unfinishedMatchup));
    }

    /**
     * Tests that isAllPlayersMatched throws an exception when not all players are
     * matched.
     */
    @Test
    public void testIsAllPlayersMatchedThrowsExceptionWhenNotAllMatched() {
        List<Matchups> matchups = List.of(matchup);
        List<Signups> playersList = List.of(player1, player2, new Signups());

        assertThrows(InvalidRoundException.class, () -> ValidationUtil.isAllPlayersMatched(matchups, playersList));
    }

    /**
     * Tests that isValidPair throws an exception when the pair is null.
     */
    @Test
    public void testIsValidPairThrowsExceptionWhenPairIsNull() {
        assertThrows(InvalidRoundException.class, () -> ValidationUtil.isValidPair(null));
    }

    /**
     * Tests that isValidSignups throws an exception when signups list is too small.
     */
    @Test
    public void testIsValidSignupsThrowsExceptionWhenNotEnoughSignups() {
        List<Signups> signups = List.of(player1);
        assertThrows(InvalidRoundException.class, () -> ValidationUtil.isValidSignups(signups));
    }

    /**
     * Tests that isValidMatchup throws an exception when players in a matchup are
     * identical.
     */
    @Test
    public void testIsValidMatchupThrowsExceptionWhenPlayersAreSame() {
        MatchupsId invalidMatchupId = new MatchupsId()
                .setPlayer1("Player1")
                .setPlayer2("Player1");

        Matchups invalidMatchup = new Matchups();
        invalidMatchup.setId(invalidMatchupId);

        assertThrows(InvalidRoundException.class, () -> ValidationUtil.isValidMatchup(invalidMatchup));
    }

    /**
     * Tests that validatePlayerResults throws ResultsNotFoundException for empty
     * results list.
     */
    @Test
    public void testValidatePlayerResults_ThrowsException_WhenEmptyList() {
        List<PlayerResults> playerResults = Collections.emptyList();
        String uuid = "some-uuid";

        ResultsNotFoundException exception = assertThrows(
                ResultsNotFoundException.class,
                () -> ValidationUtil.validatePlayerResult(playerResults, uuid));

        assertEquals("Player result not found for UUID: " + uuid, exception.getMessage());
    }

    /**
     * Tests that validatePlayerResults throws ResultsNotFoundException when UUID is
     * not found.
     */
    @Test
    public void testValidatePlayerResults_ThrowsException_WhenUuidNotFound() {
        PlayerResults result1 = new PlayerResults()
                .setUuid("uuid1");
        List<PlayerResults> playerResults = Arrays.asList(result1);
        String uuid = "some-other-uuid";

        ResultsNotFoundException exception = assertThrows(
                ResultsNotFoundException.class,
                () -> ValidationUtil.validatePlayerResult(playerResults, uuid));

        assertEquals("Player result not found for UUID: " + uuid, exception.getMessage());
    }

    /**
     * Tests that validatePlayerResults succeeds when the specified UUID is found.
     */
    @Test
    public void testValidatePlayerResults_Succeeds_WhenUuidFound() {
        PlayerResults result1 = new PlayerResults()
                .setUuid("uuid1");
        List<PlayerResults> playerResults = Arrays.asList(result1);
        String uuid = "uuid1"; // Matching UUID

        assertDoesNotThrow(() -> ValidationUtil.validatePlayerResult(playerResults, uuid));
    }

    /**
     * Tests that validateMatchups throws ResultsNotFoundException when player
     * results are missing.
     */
    @Test
    public void testValidateMatchups_ThrowsException_WhenPlayerNotFound() {
        List<Matchups> matchups = Collections.singletonList(matchupWonLoss);
        List<PlayerResults> playerResults = Collections.emptyList(); // No results available

        ResultsNotFoundException exception = assertThrows(
                ResultsNotFoundException.class,
                () -> ValidationUtil.validateMatchups(matchups, playerResults));

        assertEquals("Missing results.", exception.getMessage());
    }

    /**
     * Tests that validateMatchups succeeds when all players in matchups are found
     * in results.
     */
    @Test
    public void testValidateMatchups_Succeeds_WhenPlayersFound() {
        PlayerResults player1Results = new PlayerResults()
                .setUuid("player-won");

        PlayerResults player2Results = new PlayerResults()
                .setUuid("player-lost");

        List<PlayerResults> playerResults = Arrays.asList(player1Results, player2Results);

        List<Matchups> matchups = Collections.singletonList(matchupWonLoss);

        assertDoesNotThrow(() -> ValidationUtil.validateMatchups(matchups, playerResults));
    }

    /**
     * Tests that validateListNotEmpty throws an exception when the signups list is
     * null.
     */
    @Test
    void validateListNotEmpty_ShouldThrowException_WhenSignupsIsNull() {
        List<Signups> signups = null;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtil.validateListNotEmpty(signups, "Signups");
        });
        assertEquals("Missing Signups.", exception.getMessage());
    }

    /**
     * Tests that validateListNotEmpty throws an exception when the signups list is
     * empty.
     */
    @Test
    void validateListNotEmpty_ShouldNotThrowException_WhenSignupsIsEmpty() {
        List<Signups> signups = Collections.emptyList();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtil.validateListNotEmpty(signups, "Signups");
        });
        assertEquals("Missing Signups.", exception.getMessage());
    }

    /**
     * Tests that validateListNotEmpty does not throw an exception when signups list
     * is not empty.
     */
    @Test
    void validateListNotEmpty_ShouldNotThrowException_WhenSignupsIsNotEmpty() {
        List<Signups> signups = List.of(new Signups(), new Signups());
        assertDoesNotThrow(() -> {
            ValidationUtil.validateListNotEmpty(signups, "Signups");
        });
    }
}
