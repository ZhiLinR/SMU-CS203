package matchmaking.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link MatchupsId}.
 * This class verifies the behavior of the MatchupsId class, including
 * its equality, hash code generation, and field access methods.
 */
public class MatchupsIdTest {

    private MatchupsId matchupId;

    /**
     * Sets up a MatchupsId instance before each test.
     */
    @BeforeEach
    public void setUp() {
        matchupId = new MatchupsId();
        matchupId.setPlayer1("Player1");
        matchupId.setPlayer2("Player2");
        matchupId.setTournamentId("Tournament123");
    }

    /**
     * Tests the default constructor of MatchupsId.
     * Verifies that a newly created instance is not null and fields are null.
     */
    @Test
    public void testDefaultConstructor() {
        MatchupsId defaultId = new MatchupsId();
        assertNotNull(defaultId);
        assertNull(defaultId.getPlayer1());
        assertNull(defaultId.getPlayer2());
        assertNull(defaultId.getTournamentId());
    }

    /**
     * Tests the setter and getter for player1.
     */
    @Test
    public void testSetAndGetPlayer1() {
        matchupId.setPlayer1("NewPlayer1");
        assertEquals("NewPlayer1", matchupId.getPlayer1());
    }

    /**
     * Tests the setter and getter for player2.
     */
    @Test
    public void testSetAndGetPlayer2() {
        matchupId.setPlayer2("NewPlayer2");
        assertEquals("NewPlayer2", matchupId.getPlayer2());
    }

    /**
     * Tests the setter and getter for tournamentId.
     */
    @Test
    public void testSetAndGetTournamentId() {
        matchupId.setTournamentId("Tournament456");
        assertEquals("Tournament456", matchupId.getTournamentId());
    }

    /**
     * Tests equality between two MatchupsId instances with different player
     * attributes.
     */
    @Test
    public void testEqualsWithDifferentPlayers() {
        MatchupsId matchupId1 = new MatchupsId();
        matchupId1.setPlayer1("Player1");
        matchupId1.setPlayer2("Player2");
        matchupId1.setTournamentId("Tournament123");

        MatchupsId matchupId2 = new MatchupsId();
        matchupId2.setPlayer1("Player3");
        matchupId2.setPlayer2("Player2");
        matchupId2.setTournamentId("Tournament123");

        assertNotEquals(matchupId1, matchupId2);
    }

    /**
     * Tests equality between two MatchupsId instances with identical attributes.
     */
    @Test
    public void testEqualsWithSameAttributes() {
        MatchupsId matchupId1 = new MatchupsId();
        matchupId1.setPlayer1("Player1");
        matchupId1.setPlayer2("Player2");
        matchupId1.setTournamentId("Tournament123");

        MatchupsId matchupId2 = new MatchupsId();
        matchupId2.setPlayer1("Player1");
        matchupId2.setPlayer2("Player2");
        matchupId2.setTournamentId("Tournament123");

        assertEquals(matchupId1, matchupId2);
        assertEquals(matchupId1.hashCode(), matchupId2.hashCode());
    }

    /**
     * Tests equality between two MatchupsId instances with different tournament
     * IDs.
     */
    @Test
    public void testEqualsWithDifferentTournamentId() {
        MatchupsId matchupId1 = new MatchupsId();
        matchupId1.setPlayer1("Player1");
        matchupId1.setPlayer2("Player2");
        matchupId1.setTournamentId("Tournament123");

        MatchupsId matchupId2 = new MatchupsId();
        matchupId2.setPlayer1("Player1");
        matchupId2.setPlayer2("Player2");
        matchupId2.setTournamentId("Tournament456");

        assertNotEquals(matchupId1, matchupId2);
    }

    /**
     * Tests the hashCode method for MatchupsId instances with identical attributes.
     */
    @Test
    public void testHashCodeWithSameAttributes() {
        MatchupsId matchupId1 = new MatchupsId();
        matchupId1.setPlayer1("Player1");
        matchupId1.setPlayer2("Player2");
        matchupId1.setTournamentId("Tournament123");

        MatchupsId matchupId2 = new MatchupsId();
        matchupId2.setPlayer1("Player1");
        matchupId2.setPlayer2("Player2");
        matchupId2.setTournamentId("Tournament123");

        assertEquals(matchupId1.hashCode(), matchupId2.hashCode());
    }

    /**
     * Tests the hashCode method for MatchupsId instances with different attributes.
     */
    @Test
    public void testHashCodeWithDifferentAttributes() {
        MatchupsId matchupId1 = new MatchupsId();
        matchupId1.setPlayer1("Player1");
        matchupId1.setPlayer2("Player2");
        matchupId1.setTournamentId("Tournament123");

        MatchupsId matchupId2 = new MatchupsId();
        matchupId2.setPlayer1("Player3");
        matchupId2.setPlayer2("Player2");
        matchupId2.setTournamentId("Tournament123");

        assertNotEquals(matchupId1.hashCode(), matchupId2.hashCode());
    }

    /**
     * Tests the toString method for the MatchupsId class.
     */
    @Test
    public void testToString() {
        String expectedString = "MatchupsId(player1=Player1, player2=Player2, tournamentId=Tournament123)";
        assertEquals(expectedString, matchupId.toString());
    }

    /**
     * Tests setting player1 to null.
     */
    @Test
    public void testSetPlayer1ToNull() {
        matchupId.setPlayer1(null);
        assertNull(matchupId.getPlayer1());
    }

    /**
     * Tests setting player2 to null.
     */
    @Test
    public void testSetPlayer2ToNull() {
        matchupId.setPlayer2(null);
        assertNull(matchupId.getPlayer2());
    }

    /**
     * Tests setting tournamentId to null.
     */
    @Test
    public void testSetTournamentIdToNull() {
        matchupId.setTournamentId(null);
        assertNull(matchupId.getTournamentId());
    }
}
