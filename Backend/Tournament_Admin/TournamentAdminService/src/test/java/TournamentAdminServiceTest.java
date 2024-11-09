import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import TournamentAdminService.model.Tournament;
import TournamentAdminService.repository.TournamentRepository;
import TournamentAdminService.service.TournamentService;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for TournamentService, focusing on CRUD operations for the Tournament entity.
 */
public class TournamentAdminServiceTest {

    @InjectMocks
    private TournamentService tournamentService;

    @Mock
    private TournamentRepository tournamentRepository;

    private Tournament tournament;

    /**
     * Initializes mock objects and a sample Tournament instance before each test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        tournament = new Tournament();
        tournament.setTournamentID("T001");
        tournament.setName("Championship");
        tournament.setStartDate(new Date(System.currentTimeMillis() + 86400000)); // tomorrow
        tournament.setEndDate(new Date(System.currentTimeMillis() + (5 * 86400000))); // 5 days from now
        tournament.setLocation("Stadium A");
        tournament.setPlayerLimit(16);
        tournament.setDescOID("Description of Tournament");
        tournament.updateStatus(); // This will set the status based on dates
    }

    /**
     * Tests the creation of a tournament by verifying that the repository's createTournament
     * method is called once with the appropriate parameters.
     */
    @Test
    public void testCreateTournament() {
        // Act
        tournamentService.createTournament(tournament);

        // Assert
        verify(tournamentRepository, times(1)).createTournament(
                eq(tournament.getName()),
                eq(tournament.getStartDate()),
                eq(tournament.getEndDate()),
                eq(tournament.getLocation()),
                eq(tournament.getPlayerLimit()),
                eq(tournament.getStatus()),
                eq(tournament.getDescOID())
        );
    }

    /**
     * Tests retrieving a tournament by ID, ensuring the correct tournament data is returned.
     */
    @Test
    public void testGetTournamentById() {
        // Arrange
        when(tournamentRepository.getTournamentById("T001")).thenReturn(tournament);

        // Act
        Tournament retrievedTournament = tournamentService.getTournamentById("T001");

        // Assert
        assertNotNull(retrievedTournament);
        assertEquals("Championship", retrievedTournament.getName());
        assertEquals("T001", retrievedTournament.getTournamentID());
        verify(tournamentRepository, times(1)).getTournamentById("T001");
    }

    /**
     * Tests updating a tournament by verifying that the repository's updateTournament
     * method is called with appropriate parameters.
     */
    @Test
    public void testUpdateTournament() {
        // Arrange
        tournament.setName("Updated Championship");
        tournament.updateStatus();

        // Act
        tournamentService.updateTournament(tournament);

        // Assert
        verify(tournamentRepository, times(1)).updateTournament(
                eq(tournament.getTournamentID()),
                eq(tournament.getName()),
                eq(tournament.getStartDate()),
                eq(tournament.getEndDate()),
                eq(tournament.getLocation()),
                eq(tournament.getPlayerLimit()),
                eq(tournament.getStatus()),
                eq(tournament.getDescOID())
        );
    }

    /**
     * Tests deleting a tournament by verifying that the repository's deleteTournament
     * method is called with the correct ID.
     */
    @Test
    public void testDeleteTournament() {
        // Act
        tournamentService.deleteTournament("T001");

        // Assert
        verify(tournamentRepository, times(1)).deleteTournament("T001");
    }

    /**
     * Tests retrieving all tournaments by checking that the list returned by the service
     * matches the expected data.
     */
    @Test
    public void testGetAllTournaments() {
        // Arrange
        when(tournamentRepository.getAllTournaments()).thenReturn(Arrays.asList(tournament));

        // Act
        List<Tournament> tournaments = tournamentService.getAllTournaments();

        // Assert
        assertNotNull(tournaments);
        assertEquals(1, tournaments.size());
        assertEquals("Championship", tournaments.get(0).getName());
        assertEquals("T001", tournaments.get(0).getTournamentID());
        verify(tournamentRepository, times(1)).getAllTournaments();
    }

    /**
     * Tests that the tournament status is correctly updated based on dates
     */
    @Test
    public void testTournamentStatusUpdate() {
        // Test Upcoming status
        tournament.setStartDate(new Date(System.currentTimeMillis() + 86400000)); // tomorrow
        tournament.setEndDate(new Date(System.currentTimeMillis() + (5 * 86400000))); // 5 days from now
        assertEquals("Upcoming", tournament.updateStatus());

        // Test Ongoing status
        tournament.setStartDate(new Date(System.currentTimeMillis() - 86400000)); // yesterday
        tournament.setEndDate(new Date(System.currentTimeMillis() + 86400000)); // tomorrow
        assertEquals("Ongoing", tournament.updateStatus());

        // Test Completed status
        tournament.setStartDate(new Date(System.currentTimeMillis() - (5 * 86400000))); // 5 days ago
        tournament.setEndDate(new Date(System.currentTimeMillis() - 86400000)); // yesterday
        assertEquals("Completed", tournament.updateStatus());
    }
}