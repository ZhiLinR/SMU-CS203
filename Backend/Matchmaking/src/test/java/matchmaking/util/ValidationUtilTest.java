package matchmaking.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import matchmaking.dto.PlayerResults;
import matchmaking.dto.PlayerWins;
import matchmaking.exception.InvalidRoundException;
import matchmaking.exception.ResultsNotFoundException;
import matchmaking.model.Matchups;
import matchmaking.model.MatchupsId;
import matchmaking.model.Signups;
import matchmaking.repository.MatchupsRepository;
import matchmaking.repository.SignupsRepository;

public class ValidationUtilTest {

    /** ID of the tournament being tested. */
    private String tournamentId;

    /** Set of played pairs to track match history. */
    private Set<String> playedPairs;

    /** Players participating in the tournament. */
    private Signups player1;
    private Signups player2;
    private Signups player3;
    private Signups player4;

    private Matchups matchup;
    private Matchups matchupWonLoss;

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
        Signups player = new Signups();
        player.setUuid(uuid);
        player.setElo(elo);
        player.setTournamentId(tournamentId);
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
        MatchupsId matchId1 = new MatchupsId();
        matchId1.setPlayer1(player1.getUuid());
        matchId1.setPlayer2(player2.getUuid());
        matchId1.setTournamentId(tournamentId);

        matchup = new Matchups(); // Fixed: Use instance variable 'matchup' instead of local variable
        matchup.setId(matchId1);
        matchup.setRoundNum(1);
        matchup.setPlayerWon("Player1");

        MatchupsId matchupsIdWonLoss = new MatchupsId();
        matchupsIdWonLoss.setPlayer1("player-won");
        matchupsIdWonLoss.setPlayer2("player-lost");
        matchupsIdWonLoss.setTournamentId(tournamentId);

        matchupWonLoss = new Matchups();
        matchupWonLoss.setPlayerWon("player-won");
        matchupWonLoss.setId(matchupsIdWonLoss);

        // System.out.println(matchupWonLoss);

        when(matchupsRepository.getMatchupsByTournamentId(tournamentId)).thenReturn(List.of(matchup));

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
        PlayerWins win = new PlayerWins();
        win.setUuid(uuid);
        win.setWins(wins);
        win.setDraws(draws);
        return win;
    }

    @Test
    public void testValidateNotEmptyThrowsExceptionWhenEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateNotEmpty("", "TestField"));
        assertEquals("TestField must not be null or empty.", exception.getMessage());
    }

    @Test
    public void testIsValidPairReturnsTrueForNewPair() {
        boolean result = ValidationUtil.isValidPair("Player1", "Player2", playedPairs);
        assertTrue(result);
    }

    @Test
    public void testIsValidPairReturnsFalseForExistingPair() {
        playedPairs.add("Player1-Player2");
        boolean result = ValidationUtil.isValidPair("Player1", "Player2", playedPairs);
        assertFalse(result);
    }

    @Test
    public void testIsValidRoundNumThrowsExceptionForInvalidRound() {
        int playerCount = 3;
        assertThrows(InvalidRoundException.class, () -> ValidationUtil.isValidRoundNum(4, playerCount));
    }

    @Test
    public void testIsPrevRoundOverThrowsExceptionWhenWinnerNotAllocated() {
        Matchups unfinishedMatchup = new Matchups();
        unfinishedMatchup.setId(matchup.getId());
        unfinishedMatchup.setPlayerWon(null);

        assertThrows(InvalidRoundException.class, () -> ValidationUtil.isPrevRoundOver(unfinishedMatchup));
    }

    @Test
    public void testIsAllPlayersMatchedThrowsExceptionWhenNotAllMatched() {
        List<Matchups> matchups = List.of(matchup);

        List<Signups> playersList = List.of(player1, player2, new Signups());

        assertThrows(InvalidRoundException.class, () -> ValidationUtil.isAllPlayersMatched(matchups, playersList));
    }

    @Test
    public void testIsValidPairThrowsExceptionWhenPairIsNull() {
        assertThrows(InvalidRoundException.class, () -> ValidationUtil.isValidPair(null));
    }

    @Test
    public void testIsValidSignupsThrowsExceptionWhenNotEnoughSignups() {
        List<Signups> signups = List.of(player1);

        assertThrows(InvalidRoundException.class, () -> ValidationUtil.isValidSignups(signups));
    }

    @Test
    public void testIsValidMatchupThrowsExceptionWhenPlayersAreSame() {
        MatchupsId invalidMatchupId = new MatchupsId();
        invalidMatchupId.setPlayer1("Player1");
        invalidMatchupId.setPlayer2("Player1");

        Matchups invalidMatchup = new Matchups();
        invalidMatchup.setId(invalidMatchupId);

        assertThrows(InvalidRoundException.class, () -> ValidationUtil.isValidMatchup(invalidMatchup));
    }

    /**
     * Tests that validatePlayerResults throws ResultsNotFoundException
     * when the player results list is empty.
     */
    @Test
    public void testValidatePlayerResults_ThrowsException_WhenEmptyList() {
        List<PlayerResults> playerResults = Collections.emptyList();
        String uuid = "some-uuid";

        ResultsNotFoundException exception = assertThrows(
                ResultsNotFoundException.class,
                () -> ValidationUtil.validatePlayerResults(playerResults, uuid));

        assertEquals("Player results not found for UUID: " + uuid, exception.getMessage());
    }

    /**
     * Tests that validatePlayerResults throws ResultsNotFoundException
     * when the player results do not contain the specified UUID.
     */
    @Test
    public void testValidatePlayerResults_ThrowsException_WhenUuidNotFound() {
        PlayerResults result1 = new PlayerResults();
        result1.setUuid("uuid1");
        List<PlayerResults> playerResults = Arrays.asList(result1);
        String uuid = "some-other-uuid";

        ResultsNotFoundException exception = assertThrows(
                ResultsNotFoundException.class,
                () -> ValidationUtil.validatePlayerResults(playerResults, uuid));

        assertEquals("Player results not found for UUID: " + uuid, exception.getMessage());
    }

    /**
     * Tests that validatePlayerResults passes when the UUID exists in the list.
     */
    @Test
    public void testValidatePlayerResults_Succeeds_WhenUuidFound() {
        PlayerResults result1 = new PlayerResults();
        result1.setUuid("uuid1");
        List<PlayerResults> playerResults = Arrays.asList(result1);
        String uuid = "uuid1"; // Matching UUID

        assertDoesNotThrow(() -> ValidationUtil.validatePlayerResults(playerResults, uuid));
    }

    /**
     * Tests that validateMatchups throws ResultsNotFoundException
     * when a player in the matchups cannot be found in player results.
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
     * Tests that validateMatchups passes when all players in the matchups
     * can be found in player results.
     */
    @Test
    public void testValidateMatchups_Succeeds_WhenPlayersFound() {
        PlayerResults player1Results = new PlayerResults();
        player1Results.setUuid("player-won");

        PlayerResults player2Results = new PlayerResults();
        player2Results.setUuid("player-lost");

        List<PlayerResults> playerResults = Arrays.asList(player1Results, player2Results);

        List<Matchups> matchups = Collections.singletonList(matchupWonLoss);

        System.out.println(matchups);

        assertDoesNotThrow(() -> ValidationUtil.validateMatchups(matchups, playerResults));
    }
}
