package matchmaking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import matchmaking.manager.MatchupManager;
import matchmaking.model.*;
import matchmaking.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MatchingServiceTest {

    @InjectMocks
    private MatchingService matchingService;

    @Mock
    private MatchupManager matchupManager;

    @Mock
    private PlayerSorter playerSorter;

    @Mock
    private TournamentInfoUtil tournamentInfoUtil;

    private String tournamentId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        tournamentId = "tournament123";
    }

    @Test
    public void testGenerateUniqueMatchups_Round1_NoMatchups() {
        // Arrange
        when(tournamentInfoUtil.getCurrentRoundByTournamentId(tournamentId)).thenReturn(1);
        List<Signups> signups = new ArrayList<>();
        when(tournamentInfoUtil.getSignupsByTournamentId(tournamentId)).thenReturn(signups);
        when(tournamentInfoUtil.getMatchupsByTournamentId(tournamentId)).thenReturn(new ArrayList<>());

        // Act
        List<Matchups> result = matchingService.generateUniqueMatchups(tournamentId);

        // Assert
        assertTrue(result.isEmpty(), "Expected no matchups to be generated for round 1 with no signups.");
        verify(tournamentInfoUtil).getCurrentRoundByTournamentId(tournamentId);
        verify(tournamentInfoUtil).getSignupsByTournamentId(tournamentId);
    }

    @Test
    public void testGenerateUniqueMatchups_OddNumberOfSignups() {
        // Arrange
        when(tournamentInfoUtil.getCurrentRoundByTournamentId(tournamentId)).thenReturn(1);

        // Add 3 signups (odd number)
        List<Signups> signups = new ArrayList<>();

        Signups player1 = new Signups();
        player1.setUuid("player1");
        player1.setElo(1200);

        Signups player2 = new Signups();
        player2.setUuid("player2");
        player2.setElo(1300);

        Signups player3 = new Signups();
        player3.setUuid("player3");
        player3.setElo(1400);

        signups.add(player1);
        signups.add(player2);
        signups.add(player3);

        when(tournamentInfoUtil.getSignupsByTournamentId(tournamentId)).thenReturn(signups);
        when(tournamentInfoUtil.getMatchupsByTournamentId(tournamentId)).thenReturn(new ArrayList<>());

        // Act
        List<Matchups> result = matchingService.generateUniqueMatchups(tournamentId);

        // Assert
        assertEquals(2, result.size(), "Expected 2 matchups to be created, with 1 bye.");
        assertEquals("player3", result.get(1).getPlayer1(), "Expected the bye player to be player3.");
    }

    @Test
    public void testGenerateUniqueMatchups_Round2_ExistingSignups() {
        // Arrange
        when(tournamentInfoUtil.getCurrentRoundByTournamentId(tournamentId)).thenReturn(2);
        List<Signups> signups = new ArrayList<>();
        // Add 4 signups (even number)
        Signups player1 = new Signups();
        player1.setUuid("player1");
        player1.setElo(1200);

        Signups player2 = new Signups();
        player2.setUuid("player2");
        player2.setElo(1300);

        Signups player3 = new Signups();
        player3.setUuid("player3");
        player3.setElo(1400);

        Signups player4 = new Signups();
        player3.setUuid("player4");
        player3.setElo(1500);

        signups.add(player1);
        signups.add(player2);
        signups.add(player3);
        signups.add(player4);

        when(tournamentInfoUtil.getSignupsByTournamentId(tournamentId)).thenReturn(signups);
        
        // Previous matchups
        List<Matchups> previousMatchups = new ArrayList<>();

        Matchups matchup1 = new Matchups();
        matchup1.setPlayer1("player1");
        matchup1.setPlayer2("player3");
        matchup1.setPlayerWon(null);

        previousMatchups.add(matchup1);
        when(tournamentInfoUtil.getMatchupsByTournamentId(tournamentId)).thenReturn(previousMatchups);

        // Act
        List<Matchups> result = matchingService.generateUniqueMatchups(tournamentId);

        // Assert
        assertEquals(2, result.size(), "Expected 2 matchups to be created in round 2.");
    }

    @Test
    public void testGenerateUniqueMatchups_InvalidTournamentId() {
        // Arrange
        String invalidTournamentId = null; // Simulating an invalid ID
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            matchingService.generateUniqueMatchups(invalidTournamentId);
        }, "Expected IllegalArgumentException for invalid tournament ID.");
    }
}