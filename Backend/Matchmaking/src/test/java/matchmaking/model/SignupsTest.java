package matchmaking.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Signups} entity.
 * <p>
 * This test class verifies the functionality of the Signups class,
 * ensuring that getters, setters, and other methods work as expected.
 * </p>
 */
public class SignupsTest {

    private Signups signups;

    /**
     * Sets up a new {@link Signups} instance before each test.
     */
    @BeforeEach
    public void setUp() {
        signups = new Signups()
                .setId(new PlayerTournamentId()
                        .setUuid("player1")
                        .setTournamentId("Tournament123"))
                .setElo(1500);
    }

    /**
     * Tests the default constructor of the {@link Signups} class.
     * <p>
     * Verifies that a default instance is created with null values.
     * </p>
     */
    @Test
    public void testDefaultConstructor() {
        Signups defaultSignups = new Signups();
        assertNotNull(defaultSignups);
        assertNull(defaultSignups.getId());
        assertEquals(0, defaultSignups.getElo());
    }

    /**
     * Tests setting and getting the UUID of the {@link Signups} instance.
     */
    @Test
    public void testSetAndGetUuid() {
        signups.getId().setUuid("Player2");
        assertEquals("Player2", signups.getId().getUuid());
    }

    /**
     * Tests setting and getting the tournament ID of the {@link Signups} instance.
     */
    @Test
    public void testSetAndGetTournamentId() {
        signups.getId().setTournamentId("Tournament456");
        assertEquals("Tournament456", signups.getId().getTournamentId());
    }

    /**
     * Tests setting and getting the Elo rating of the {@link Signups} instance.
     */
    @Test
    public void testSetAndGetElo() {
        signups.setElo(1600);
        assertEquals(1600, signups.getElo());
    }

    /**
     * Tests the equality of two {@link Signups} instances with different UUIDs.
     * <p>
     * Verifies that two instances with different UUIDs are not equal.
     * </p>
     */
    @Test
    public void testEqualsWithDifferentUuids() {
        Signups signups1 = new Signups()
                .setId(new PlayerTournamentId()
                        .setUuid("player1")
                        .setTournamentId("Tournament123"))
                .setElo(1500);
        Signups signups2 = new Signups()
                .setId(new PlayerTournamentId()
                        .setUuid("player2")
                        .setTournamentId("Tournament123"))
                .setElo(1500);
        assertNotEquals(signups1, signups2);
        assertNotEquals(signups1.hashCode(), signups2.hashCode());
    }

    /**
     * Tests the equality of two {@link Signups} instances with the same attributes.
     * <p>
     * Verifies that two instances with the same UUID and tournament ID are equal.
     * </p>
     */
    @Test
    public void testEqualsWithSameAttributes() {
        Signups signups1 = new Signups()
                .setId(new PlayerTournamentId()
                        .setUuid("player2")
                        .setTournamentId("Tournament123"))
                .setElo(1500);
        Signups signups2 = new Signups()
                .setId(new PlayerTournamentId()
                        .setUuid("player2")
                        .setTournamentId("Tournament123"))
                .setElo(1500);
        assertEquals(signups1, signups2);
        assertEquals(signups1.hashCode(), signups2.hashCode());
    }

    /**
     * Tests the string representation of the {@link Signups} instance.
     * <p>
     * Verifies that the toString method returns the expected string format.
     * </p>
     */
    @Test
    public void testToString() {
        String expectedString = "Signups(id=PlayerTournamentId(uuid=player1, tournamentId=Tournament123), elo=1500)";
        assertEquals(expectedString, signups.toString());
    }

    /**
     * Tests setting the UUID of the {@link Signups} instance to null.
     */
    @Test
    public void testSetUuidToNull() {
        signups.getId().setUuid(null);
        assertNull(signups.getId().getUuid());
    }

    /**
     * Tests setting the tournament ID of the {@link Signups} instance to null.
     */
    @Test
    public void testSetTournamentIdToNull() {
        signups.getId().setTournamentId(null);
        assertNull(signups.getId().getTournamentId());
    }

    /**
     * Tests setting the Elo rating of the {@link Signups} instance to a negative
     * value.
     */
    @Test
    public void testSetEloToNegative() {
        signups.setElo(-100);
        assertEquals(-100, signups.getElo());
    }
}
