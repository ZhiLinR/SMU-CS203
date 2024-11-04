package matchmaking.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for the {@link Matchups} entity.
 * This class contains various test cases to ensure that the
 * {@link Matchups} class behaves as expected, including
 * testing its constructors, getters, setters, and equality methods.
 */
public class MatchupsTest {

    private Matchups matchups;

    /**
     * Sets up a {@link Matchups} instance before each test.
     * Initializes the matchup with predefined player IDs and other properties.
     */
    @BeforeEach
    public void setUp() {
        MatchupsId matchupId = new MatchupsId()
                .setPlayer1("Player1")
                .setPlayer2("Player2")
                .setTournamentId("Tournament123");

        matchups = new Matchups()
                .setId(matchupId)
                .setPlayerWon("Player1")
                .setRoundNum(1);
    }

    /**
     * Tests the default constructor of the {@link Matchups} class.
     * Verifies that the default values are set correctly.
     */
    @Test
    public void testDefaultConstructor() {
        Matchups defaultMatchup = new Matchups();
        assertNotNull(defaultMatchup);
        assertNull(defaultMatchup.getId());
        assertNull(defaultMatchup.getPlayerWon());
        assertEquals(0, defaultMatchup.getRoundNum());
    }

    /**
     * Tests the setter and getter for the matchup ID.
     */
    @Test
    public void testSetAndGetId() {
        MatchupsId id = new MatchupsId()
                .setPlayer1("Player1")
                .setPlayer2("Player2")
                .setTournamentId("Tournament123");
        matchups.setId(id);
        assertEquals(id, matchups.getId());
    }

    /**
     * Tests the setter and getter for the winning player.
     */
    @Test
    public void testSetAndGetPlayerWon() {
        matchups.setPlayerWon("Player2");
        assertEquals("Player2", matchups.getPlayerWon());
    }

    /**
     * Tests the setter and getter for the round number.
     */
    @Test
    public void testSetAndGetRoundNum() {
        matchups.setRoundNum(2);
        assertEquals(2, matchups.getRoundNum());
    }

    /**
     * Tests equality comparison with different matchup IDs.
     */
    @Test
    public void testEqualsWithDifferentPlayers() {
        MatchupsId id1 = new MatchupsId()
                .setPlayer1("Player1")
                .setPlayer2("Player2")
                .setTournamentId("Tournament123");

        MatchupsId id2 = new MatchupsId()
                .setPlayer1("Player3")
                .setPlayer2("Player4")
                .setTournamentId("Tournament123");

        Matchups matchups1 = new Matchups();
        matchups1.setId(id1);

        Matchups matchups2 = new Matchups();
        matchups2.setId(id2);

        assertNotEquals(matchups1, matchups2);
        assertNotEquals(matchups1.hashCode(), matchups2.hashCode());
    }

    /**
     * Tests equality comparison with the same attributes.
     */
    @Test
    public void testEqualsWithSameAttributes() {
        MatchupsId id = new MatchupsId()
                .setPlayer1("Player1")
                .setPlayer2("Player2")
                .setTournamentId("Tournament123");

        Matchups matchups1 = new Matchups()
                .setId(id)
                .setPlayerWon("Player1")
                .setRoundNum(1);

        Matchups matchups2 = new Matchups()
                .setId(id)
                .setPlayerWon("Player1")
                .setRoundNum(1);

        assertEquals(matchups1, matchups2);
        assertEquals(matchups1.hashCode(), matchups2.hashCode());
    }

    /**
     * Tests the string representation of the {@link Matchups} object.
     */
    @Test
    public void testToString() {
        MatchupsId id = new MatchupsId()
                .setPlayer1("Player1")
                .setPlayer2("Player2")
                .setTournamentId("Tournament123");

        matchups.setId(id)
                .setPlayerWon("Player1")
                .setRoundNum(1);

        String expectedString = "Matchups(id=" + id + ", playerWon=Player1, roundNum=1)";
        assertEquals(expectedString, matchups.toString());
    }

    /**
     * Tests setting the matchup ID to null.
     */
    @Test
    public void testSetIdToNull() {
        matchups.setId(null);
        assertNull(matchups.getId());
    }

    /**
     * Tests setting the winning player to null.
     */
    @Test
    public void testSetPlayerWonToNull() {
        matchups.setPlayerWon(null);
        assertNull(matchups.getPlayerWon());
    }

    /**
     * Tests setting the round number to zero.
     */
    @Test
    public void testSetRoundNumToZero() {
        matchups.setRoundNum(0);
        assertEquals(0, matchups.getRoundNum());
    }

    /**
     * Tests equality and hash code implementation for the {@link MatchupsId}.
     */
    @Test
    public void testMatchupsIdEqualsAndHashCode() {
        MatchupsId id1 = new MatchupsId()
                .setPlayer1("Player1")
                .setPlayer2("Player2")
                .setTournamentId("Tournament123");

        MatchupsId id2 = new MatchupsId()
                .setPlayer1("Player1")
                .setPlayer2("Player2")
                .setTournamentId("Tournament123");

        MatchupsId id3 = new MatchupsId()
                .setPlayer1("Player3")
                .setPlayer2("Player4")
                .setTournamentId("Tournament123");

        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }
}
