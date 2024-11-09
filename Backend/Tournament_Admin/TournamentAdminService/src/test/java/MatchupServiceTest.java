import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import TournamentAdminService.model.Matchup;
import TournamentAdminService.repository.MatchupRepository;
import TournamentAdminService.service.MatchupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MatchupServiceTest {

    @InjectMocks
    private MatchupService matchupService;

    @Mock
    private MatchupRepository matchupRepository;

    private static final String TOURNAMENT_ID = "T001";
    private static final String PLAYER_1 = "Player1";
    private static final String PLAYER_2 = "Player2";
    private static final Integer ROUND_NUM = 1;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateGameResult_Success() {
        // Arrange
        List<String> participants = Arrays.asList(PLAYER_1, PLAYER_2);
        when(matchupRepository.getParticipantsByTournamentId(TOURNAMENT_ID)).thenReturn(participants);

        // Act
        matchupService.createGameResult(PLAYER_1, TOURNAMENT_ID, ROUND_NUM);

        // Assert
        verify(matchupRepository).createGameResult(PLAYER_1, TOURNAMENT_ID, ROUND_NUM);
        verify(matchupRepository).getParticipantsByTournamentId(TOURNAMENT_ID);
    }

    @Test
    public void testCreateGameResult_NullTournamentId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                matchupService.createGameResult(PLAYER_1, null, ROUND_NUM)
        );
        verify(matchupRepository, never()).createGameResult(any(), any(), any());
    }

    @Test
    public void testCreateGameResult_EmptyTournamentId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                matchupService.createGameResult(PLAYER_1, "", ROUND_NUM)
        );
        verify(matchupRepository, never()).createGameResult(any(), any(), any());
    }

    @Test
    public void testCreateGameResult_NullRoundNum() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                matchupService.createGameResult(PLAYER_1, TOURNAMENT_ID, null)
        );
        verify(matchupRepository, never()).createGameResult(any(), any(), any());
    }

    @Test
    public void testCreateGameResult_EmptyPlayerWon() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                matchupService.createGameResult("", TOURNAMENT_ID, ROUND_NUM)
        );
        verify(matchupRepository, never()).createGameResult(any(), any(), any());
    }

    @Test
    public void testCreateGameResult_InvalidPlayer() {
        // Arrange
        List<String> participants = Arrays.asList(PLAYER_1, PLAYER_2);
        when(matchupRepository.getParticipantsByTournamentId(TOURNAMENT_ID)).thenReturn(participants);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                matchupService.createGameResult("InvalidPlayer", TOURNAMENT_ID, ROUND_NUM)
        );
        verify(matchupRepository, never()).createGameResult(any(), any(), any());
    }

    @Test
    public void testUpdateGameResult_Success() {
        // Act
        matchupService.updateGameResult(PLAYER_1, TOURNAMENT_ID, ROUND_NUM);

        // Assert
        verify(matchupRepository).updateGameResult(PLAYER_1, TOURNAMENT_ID, ROUND_NUM);
    }

    @Test
    public void testDeleteGameResult_Success() {
        // Act
        matchupService.deleteGameResult(PLAYER_1, TOURNAMENT_ID, ROUND_NUM);

        // Assert
        verify(matchupRepository).deleteGameResult(PLAYER_1, TOURNAMENT_ID, ROUND_NUM);
    }

    @Test
    public void testGetGameResultsByTournamentId_Success() {
        // Arrange
        List<Matchup> expectedResults = Arrays.asList(new Matchup());
        when(matchupRepository.getGameResultsByTournamentId(TOURNAMENT_ID)).thenReturn(expectedResults);

        // Act
        List<Matchup> actualResults = matchupService.getGameResultsByTournamentId(TOURNAMENT_ID);

        // Assert
        assertEquals(expectedResults, actualResults);
        verify(matchupRepository).getGameResultsByTournamentId(TOURNAMENT_ID);
    }

    @Test
    public void testGetGameResultsByTournamentId_NoResults() {
        // Arrange
        when(matchupRepository.getGameResultsByTournamentId(TOURNAMENT_ID)).thenReturn(Collections.emptyList());

        // Act
        List<Matchup> results = matchupService.getGameResultsByTournamentId(TOURNAMENT_ID);

        // Assert
        assertTrue(results.isEmpty());
        verify(matchupRepository).getGameResultsByTournamentId(TOURNAMENT_ID);
    }

    @Test
    public void testGetParticipantsByTournamentId_Success() {
        // Arrange
        List<String> expectedParticipants = Arrays.asList(PLAYER_1, PLAYER_2);
        when(matchupRepository.getParticipantsByTournamentId(TOURNAMENT_ID)).thenReturn(expectedParticipants);

        // Act
        List<String> actualParticipants = matchupService.getParticipantsByTournamentId(TOURNAMENT_ID);

        // Assert
        assertEquals(expectedParticipants, actualParticipants);
        verify(matchupRepository).getParticipantsByTournamentId(TOURNAMENT_ID);
    }

    @Test
    public void testGetParticipantsByTournamentId_NoParticipants() {
        // Arrange
        when(matchupRepository.getParticipantsByTournamentId(TOURNAMENT_ID)).thenReturn(Collections.emptyList());

        // Act
        List<String> participants = matchupService.getParticipantsByTournamentId(TOURNAMENT_ID);

        // Assert
        assertTrue(participants.isEmpty());
        verify(matchupRepository).getParticipantsByTournamentId(TOURNAMENT_ID);
    }
}