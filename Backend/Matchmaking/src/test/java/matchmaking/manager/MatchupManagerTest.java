package matchmaking.manager;

import matchmaking.util.*;
import matchmaking.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchupManagerTest {

    @InjectMocks
    private MatchupManager matchupManager;

    @Mock
    private TournamentInfoUtil tournamentInfoUtil;

    @Mock
    private PlayerSorter playerSorter;

    private String tournamentId;
    private int roundNum;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        tournamentId = "Tournament123";
        roundNum = 1;
    }

    private MatchupsId setUpMatchupId(String player1, String player2) {
        MatchupsId matchupsId = new MatchupsId()
                .setPlayer1(player1)
                .setPlayer2(player2)
                .setTournamentId(tournamentId);

        return matchupsId;
    }

    /**
     * Tests creating unique matchups with an even number of players.
     */
    @Test
    public void testCreateUniqueMatchupsWithEvenPlayers() {
        System.out.println("CreateUniqueMatchupsWithEvenPlayers");
        List<Signups> players = new ArrayList<>();
        players.add(new Signups()
                .setUuid("Player1")
                .setTournamentId(tournamentId)
                .setElo(1500));
        players.add(new Signups()
                .setUuid("Player2")
                .setTournamentId(tournamentId)
                .setElo(1400));

        Set<String> playedPairs = new HashSet<>();
        when(playerSorter.sortPlayersForRound(players, tournamentId, roundNum)).thenReturn(players);
        when(tournamentInfoUtil.createMatchup(any(Signups.class), any(Signups.class), eq(tournamentId), eq(roundNum)))
                .thenAnswer(invocation -> {
                    Signups player1 = invocation.getArgument(0);
                    Signups player2 = invocation.getArgument(1);
                    Matchups match1 = new Matchups()
                            .setId(setUpMatchupId(player1.getUuid(), player2.getUuid()));
                    return match1;
                });

        List<Matchups> matchups = matchupManager.createUniqueMatchups(players, tournamentId, roundNum, playedPairs);

        assertNotNull(matchups);
        assertEquals(1, matchups.size());
        assertEquals("Player1", matchups.get(0).getId().getPlayer1());
        assertEquals("Player2", matchups.get(0).getId().getPlayer2());
        verify(tournamentInfoUtil, times(1)).insertMatchups(matchups, tournamentId, roundNum);
    }

    /**
     * Tests handling of validation errors in matchups.
     */
    @Test
    public void testCreateUniqueMatchupsValidationError() {
        System.out.println("CreateUniqueMatchupsValidationError");
        List<Signups> players = new ArrayList<>();
        players.add(new Signups()
                .setUuid("Player1")
                .setTournamentId(tournamentId)
                .setElo(1500));
        players.add(new Signups()
                .setUuid("Player2")
                .setTournamentId(tournamentId)
                .setElo(1400));

        Set<String> playedPairs = new HashSet<>();
        when(playerSorter.sortPlayersForRound(players, tournamentId, roundNum)).thenReturn(players);
        when(tournamentInfoUtil.createMatchup(any(Signups.class), any(Signups.class), eq(tournamentId), eq(roundNum)))
                .thenThrow(new IllegalArgumentException("Invalid matchup"));

        assertThrows(IllegalArgumentException.class, () -> {
            matchupManager.createUniqueMatchups(players, tournamentId, roundNum, playedPairs);
        });
    }

    /**
     * Tests creating unique matchups with an odd number of players, ensuring a bye
     * is handled.
     */
    @Test
    public void testCreateUniqueMatchupsWithOddPlayers() {
        System.out.println("CreateUniqueMatchupsWithOddPlayers");
        List<Signups> players = new ArrayList<>();
        players.add(new Signups()
                .setUuid("Player1")
                .setTournamentId(tournamentId)
                .setElo(1500));
        players.add(new Signups()
                .setUuid("Player2")
                .setTournamentId(tournamentId)
                .setElo(1400));
        players.add(new Signups()
                .setUuid("Player3")
                .setTournamentId(tournamentId)
                .setElo(1300)); // Odd player

        Set<String> playedPairs = new HashSet<>();
        when(playerSorter.sortPlayersForRound(players, tournamentId, roundNum)).thenReturn(players);
        when(tournamentInfoUtil.createMatchup(any(Signups.class), any(Signups.class), eq(tournamentId), eq(roundNum)))
                .thenAnswer(invocation -> {
                    Signups player1 = invocation.getArgument(0);
                    Signups player2 = invocation.getArgument(1);
                    Matchups match1 = new Matchups()
                            .setId(setUpMatchupId(player1.getUuid(), player2.getUuid()));
                    return match1;
                });

        List<Matchups> matchups = matchupManager.createUniqueMatchups(players, tournamentId, roundNum, playedPairs);

        assertNotNull(matchups);
        assertEquals(2, matchups.size()); // Two matchups, one bye
        assertTrue(matchups.stream().anyMatch(m -> m.getId().getPlayer2().equals("null"))); // Check for bye
        verify(tournamentInfoUtil, times(1)).insertMatchups(matchups, tournamentId, roundNum);
    }

    /**
     * Tests creating matchups with played pairs to ensure duplicates are avoided.
     */
    @Test
    public void testCreateUniqueMatchupsWithPlayedPairs() {
        System.out.println("CreateUniqueMatchupsWithPlayedPairs");
        List<Signups> players = new ArrayList<>();
        players.add(new Signups()
                .setUuid("Player1")
                .setTournamentId(tournamentId)
                .setElo(1500));
        players.add(new Signups()
                .setUuid("Player2")
                .setTournamentId(tournamentId)
                .setElo(1400));
        players.add(new Signups()
                .setUuid("Player3")
                .setTournamentId(tournamentId)
                .setElo(1300));

        Set<String> playedPairs = new HashSet<>();
        playedPairs.add("Player1_Player2"); // Already played

        when(playerSorter.sortPlayersForRound(players, tournamentId, roundNum)).thenReturn(players);
        when(tournamentInfoUtil.createMatchup(any(Signups.class), any(Signups.class), eq(tournamentId), eq(roundNum)))
                .thenAnswer(invocation -> {
                    Signups player1 = invocation.getArgument(0);
                    Signups player2 = invocation.getArgument(1);
                    Matchups match1 = new Matchups()
                            .setId(setUpMatchupId(player1.getUuid(), player2.getUuid()));
                    return match1;
                });

        List<Matchups> matchups = matchupManager.createUniqueMatchups(players, tournamentId, roundNum, playedPairs);

        assertNotNull(matchups);
        assertEquals(1, matchups.size()); // Should only create one valid matchup
        assertEquals("Player3", matchups.get(0).getId().getPlayer1());
        assertEquals("Player2", matchups.get(0).getId().getPlayer2()); // Player2 paired with Player3
    }

    /**
     * Tests for handling cases where no valid matchup is found.
     */
    @Test
    public void testCreateUniqueMatchupsNoValidMatchup() {
        System.out.println("CreateUniqueMatchupsNoValidMatchup");
        List<Signups> players = new ArrayList<>();
        players.add(new Signups()
                .setUuid("Player1")
                .setTournamentId(tournamentId)
                .setElo(1500));
        players.add(new Signups()
                .setUuid("Player2")
                .setTournamentId(tournamentId)
                .setElo(1400));

        Set<String> playedPairs = new HashSet<>();
        // Simulate that players can't be matched
        when(playerSorter.sortPlayersForRound(players, tournamentId, roundNum)).thenReturn(players);
        when(tournamentInfoUtil.createMatchup(any(Signups.class), any(Signups.class), eq(tournamentId), eq(roundNum)))
                .thenThrow(new IllegalArgumentException("No valid matchups available"));

        assertThrows(IllegalArgumentException.class, () -> {
            matchupManager.createUniqueMatchups(players, tournamentId, roundNum, playedPairs);
        });
    }
}
