package matchmaking.manager;

import matchmaking.repository.*;
import matchmaking.util.PlayerSorter;
import matchmaking.util.TournamentInfoUtil;
import matchmaking.model.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MatchupManagerTest {

    @InjectMocks
    private MatchupManager matchupManager;

    @Mock
    private MatchupsRepository matchupsRepository;

    @Mock
    private PlayerSorter playerSorter;

    @Mock
    private TournamentInfoUtil tournamentInfoUtil;

    private List<Signups> players;
    private Set<String> playedPairs;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize sample players
        players = new ArrayList<>();

        Signups player1 = new Signups();
        player1.setUuid("player1-uuid");
        player1.setElo(1000);
        players.add(player1);

        Signups player2 = new Signups();
        player2.setUuid("player2-uuid");
        player2.setElo(1500);
        players.add(player2);

        Signups player3 = new Signups();
        player3.setUuid("player3-uuid");
        player3.setElo(1200);
        players.add(player3);

        Signups player4 = new Signups();
        player4.setUuid("player4-uuid");
        player4.setElo(1300);
        players.add(player4);

        playedPairs = new HashSet<>();

    }

    // @Test
    public void testCreateUniqueMatchups_DuplicatePair() {
        // Arrange
        String tournamentId = "tournament1";
        int roundNum = 1;

        // Set up previously played pairs (including a duplicate pair)
        playedPairs.add("player2-uuid-player4-uuid");
        playedPairs.add("player4-uuid-player2-uuid"); // Include both orders to handle symmetry

        // Act
        List<Matchups> matchups = matchupManager.createUniqueMatchups(players,
                tournamentId, roundNum, playedPairs);

        System.out.println(matchups);
        verify(playerSorter, times(1)).sortPlayersForRound(players, tournamentId, roundNum);

        // Assert
        assertEquals(2, matchups.size()); // Expecting two matchups (with one pair skipped)

        // Validate the matchups
        assertEquals("player2-uuid", matchups.get(0).getPlayer1()); // Player with highest ELO
        assertEquals("player4-uuid", matchups.get(0).getPlayer2()); // Next highest ELO
        assertEquals("player3-uuid", matchups.get(1).getPlayer1()); // Third player
        assertEquals("player1-uuid", matchups.get(1).getPlayer2()); // Fourth player
        assertNull(matchups.get(0).getPlayerWon()); // playerWon should be null
        assertNull(matchups.get(1).getPlayerWon()); // playerWon should be null

        // Verify that insertMatchup was called with expected parameters
        verify(matchupsRepository, times(1)).insertMatchup(eq("player3-uuid"),
                eq("player4-uuid"), isNull(),
                eq(tournamentId), eq(roundNum));
        verify(matchupsRepository, times(1)).insertMatchup(eq("player1-uuid"),
                eq("No Player"), eq("player1-uuid"),
                eq(tournamentId), eq(roundNum));
    }

    // @Test
    // public void testCreateUniqueMatchups_FirstRound() {
    // // Arrange
    // String tournamentId = "tournament1";
    // int roundNum = 1;

    // // Act
    // List<Matchups> matchups = matchupManager.createUniqueMatchups(players,
    // tournamentId, roundNum, playedPairs);

    // // Assert
    // assertEquals(2, matchups.size()); // Expecting two matchups
    // assertEquals("player2-uuid", matchups.get(0).getPlayer1());
    // assertEquals("player4-uuid", matchups.get(0).getPlayer2());

    // // Second matchup assertions
    // assertEquals("player3-uuid", matchups.get(1).getPlayer1());
    // assertEquals("player1-uuid", matchups.get(1).getPlayer2());

    // // Verify that insertMatchup was called with expected parameters
    // verify(matchupsRepository, times(1)).insertMatchup(eq("player2-uuid"),
    // eq("player4-uuid"), isNull(),
    // eq(tournamentId), eq(roundNum));
    // verify(matchupsRepository, times(1)).insertMatchup(eq("player3-uuid"),
    // eq("player1-uuid"), isNull(),
    // eq(tournamentId), eq(roundNum));
    // }

    // @Test
    // public void testCreateUniqueMatchups_SubsequentRounds() {
    // // Arrange
    // String tournamentId = "tournament1";
    // int roundNum = 2;

    // List<PlayerWins> playerWins = new ArrayList<>();
    // PlayerWins pw1 = new PlayerWins();
    // pw1.setUuid("player1-uuid");
    // pw1.setWins(3);
    // playerWins.add(pw1);

    // PlayerWins pw2 = new PlayerWins();
    // pw2.setUuid("player2-uuid");
    // pw2.setWins(2);
    // playerWins.add(pw2);

    // PlayerWins pw3 = new PlayerWins();
    // pw3.setUuid("player3-uuid");
    // pw3.setWins(1);
    // playerWins.add(pw3);

    // PlayerWins pw4 = new PlayerWins();
    // pw4.setUuid("player4-uuid");
    // pw4.setWins(0);
    // playerWins.add(pw4);

    // when(matchupsRepository.getPlayerWinsByTournamentId(tournamentId)).thenReturn(playerWins);

    // // Act
    // List<Matchups> matchups = matchupManager.createUniqueMatchups(players,
    // tournamentId, roundNum, playedPairs);

    // // Assert
    // assertEquals(2, matchups.size()); // Expecting two matchups
    // assertEquals("player1-uuid", matchups.get(0).getPlayer1());
    // assertEquals("player2-uuid", matchups.get(0).getPlayer2());
    // assertEquals("player3-uuid", matchups.get(1).getPlayer1());
    // assertEquals("player4-uuid", matchups.get(1).getPlayer2());

    // // Verify that insertMatchup was called with expected parameters
    // verify(matchupsRepository, times(1)).insertMatchup(eq("player1-uuid"),
    // eq("player2-uuid"), isNull(),
    // eq(tournamentId), eq(roundNum));
    // verify(matchupsRepository, times(1)).insertMatchup(eq("player3-uuid"),
    // eq("player4-uuid"), isNull(),
    // eq(tournamentId), eq(roundNum));
    // }

    // @Test
    // public void testCreateUniqueMatchups_WithBye() {
    // // Arrange
    // String tournamentId = "tournament1";
    // int roundNum = 1;

    // Signups player5 = new Signups();
    // player5.setUuid("player5-uuid");
    // players.add(player5); // Adding an odd player for bye

    // // Act
    // List<Matchups> matchups = matchupManager.createUniqueMatchups(players,
    // tournamentId, roundNum, playedPairs);

    // // Assert
    // assertEquals(3, matchups.size()); // Expecting two matchups and one bye
    // assertEquals("player5-uuid", matchups.get(2).getPlayer1()); // Bye should be
    // the last player
    // assertEquals("No Player", matchups.get(2).getPlayer2());
    // assertEquals("player5-uuid", matchups.get(2).getPlayerWon()); // Bye counts
    // as a win

    // // Verify that insertMatchup was called with expected parameters
    // verify(matchupsRepository, times(1)).insertMatchup(eq("player2-uuid"),
    // eq("player4-uuid"), isNull(),
    // eq(tournamentId), eq(roundNum));
    // verify(matchupsRepository, times(1)).insertMatchup(eq("player3-uuid"),
    // eq("player1-uuid"), isNull(),
    // eq(tournamentId), eq(roundNum));
    // verify(matchupsRepository, times(1)).insertMatchup(eq("player5-uuid"), eq("No
    // Player"), eq("player5-uuid"),
    // eq(tournamentId), eq(roundNum));
    // }

    // @Test
    // public void testGetPlayedPairs() {
    // // Arrange
    // List<Matchups> previousMatchups = new ArrayList<>();
    // Matchups matchup1 = new Matchups();
    // matchup1.setPlayer1("player1-uuid");
    // matchup1.setPlayer2("player2-uuid");
    // matchup1.setPlayerWon("player1-uuid");
    // previousMatchups.add(matchup1);

    // Matchups matchup2 = new Matchups();
    // matchup2.setPlayer1("player3-uuid");
    // matchup2.setPlayer2("player4-uuid");
    // matchup2.setPlayerWon("player4-uuid");
    // previousMatchups.add(matchup2);

    // // Act
    // Set<String> pairs = matchupManager.getPlayedPairs(previousMatchups);

    // // Assert
    // assertEquals(4, pairs.size()); // There should be two pairs in both orders
    // assertTrue(pairs.contains("player1-uuid-player2-uuid"));
    // assertTrue(pairs.contains("player2-uuid-player1-uuid"));
    // assertTrue(pairs.contains("player3-uuid-player4-uuid"));
    // assertTrue(pairs.contains("player4-uuid-player3-uuid"));
    // }
}
