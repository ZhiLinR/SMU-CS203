package matchmaking.util;

import matchmaking.model.Signups;
import matchmaking.dto.*;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConversionUtilTest {

    /**
     * Tests the conversion from Signups to PlayerResults.
     */
    @Test
    public void testConvertSignupsToPlayerResults() {
        // Arrange
        List<Signups> signups = Arrays.asList(
                createSignup("uuid1", 1200),
                createSignup("uuid2", 1300),
                createSignup("uuid3", 1100));

        // Act
        List<PlayerResults> playerResults = ConversionUtil.convertSignupsToPlayerResults(signups);

        // Assert
        assertEquals(3, playerResults.size());

        assertEquals("uuid1", playerResults.get(0).getUuid());
        assertEquals(1200, playerResults.get(0).getElo());
        assertEquals(0, playerResults.get(0).getBuchholz());
        assertEquals(0, playerResults.get(0).getRank());

        assertEquals("uuid2", playerResults.get(1).getUuid());
        assertEquals(1300, playerResults.get(1).getElo());
        assertEquals(0, playerResults.get(1).getBuchholz());
        assertEquals(0, playerResults.get(1).getRank());

        assertEquals("uuid3", playerResults.get(2).getUuid());
        assertEquals(1100, playerResults.get(2).getElo());
        assertEquals(0, playerResults.get(2).getBuchholz());
        assertEquals(0, playerResults.get(2).getRank());
    }

    /**
     * Tests the ordering of UUIDs by rank.
     */
    @Test
    public void testGetUuidsOrderedByRank() {
        // Arrange
        PlayerResults player1 = new PlayerResults();
        player1.setUuid("uuid1");
        player1.setRank(2);

        PlayerResults player2 = new PlayerResults();
        player2.setUuid("uuid2");
        player2.setRank(1);

        PlayerResults player3 = new PlayerResults();
        player3.setUuid("uuid3");
        player3.setRank(3);

        List<PlayerResults> playerResults = Arrays.asList(player1, player2, player3);

        // Act
        List<String> orderedUuids = ConversionUtil.getUuidsOrderedByRank(playerResults);

        // Assert
        assertEquals(Arrays.asList("uuid2", "uuid1", "uuid3"), orderedUuids);
    }

    /**
     * Tests the getUuidsOrderedByRank method with an empty list.
     */
    @Test
    public void testGetUuidsOrderedByRank_EmptyList() {
        // Act
        List<String> orderedUuids = ConversionUtil.getUuidsOrderedByRank(Collections.emptyList());

        // Assert
        assertTrue(orderedUuids.isEmpty());
    }

    /**
     * Tests the conversion from null Signups to PlayerResults.
     */
    @Test
    public void testConvertSignupsToPlayerResults_NullInput() {
        assertThrows(NullPointerException.class, () -> {
            ConversionUtil.convertSignupsToPlayerResults(null);
        });
    }

    /**
     * Tests the conversion from an empty list of Signups to PlayerResults.
     */
    @Test
    public void testConvertSignupsToPlayerResults_EmptyInput() {
        List<PlayerResults> playerResults = ConversionUtil.convertSignupsToPlayerResults(Collections.emptyList());
        assertTrue(playerResults.isEmpty(), "Expected empty list of PlayerResults");
    }

    /**
     * Tests the conversion from a single Signup to PlayerResults.
     */
    @Test
    public void testConvertSignupsToPlayerResults_SingleEntry() {
        List<Signups> signups = Collections.singletonList(createSignup("uuid1", 1500));
        List<PlayerResults> playerResults = ConversionUtil.convertSignupsToPlayerResults(signups);

        assertEquals(1, playerResults.size());
        assertEquals("uuid1", playerResults.get(0).getUuid());
        assertEquals(1500, playerResults.get(0).getElo());
        assertEquals(0, playerResults.get(0).getBuchholz());
        assertEquals(0, playerResults.get(0).getRank());
    }

    /**
     * Tests the getUuidsOrderedByRank method with duplicate ranks.
     */
    @Test
    public void testGetUuidsOrderedByRank_DuplicateRanks() {
        PlayerResults player1 = new PlayerResults();
        player1.setUuid("uuid1");
        player1.setRank(1);

        PlayerResults player2 = new PlayerResults();
        player2.setUuid("uuid2");
        player2.setRank(1); // Same rank as player1

        PlayerResults player3 = new PlayerResults();
        player3.setUuid("uuid3");
        player3.setRank(2);

        List<PlayerResults> playerResults = Arrays.asList(player1, player2, player3);

        List<String> orderedUuids = ConversionUtil.getUuidsOrderedByRank(playerResults);

        assertEquals(Arrays.asList("uuid1", "uuid2", "uuid3"), orderedUuids);
        // The order of uuid1 and uuid2 should be preserved (based on the order of
        // input)
    }

    /**
     * Tests the performance of the convertSignupsToPlayerResults with a large
     * number of entries.
     */
    @Test
    public void testConvertSignupsToPlayerResults_LargeInput() {
        List<Signups> signups = Collections.nCopies(10000, createSignup("uuid", 1000));
        List<PlayerResults> playerResults = ConversionUtil.convertSignupsToPlayerResults(signups);
        assertEquals(10000, playerResults.size());
    }

    /**
     * Tests the conversion with negative Elo values.
     */
    @Test
    public void testConvertSignupsToPlayerResults_NegativeElo() {
        List<Signups> signups = Collections.singletonList(createSignup("uuid1", -1500));
        List<PlayerResults> playerResults = ConversionUtil.convertSignupsToPlayerResults(signups);

        assertEquals(1, playerResults.size());
        assertEquals("uuid1", playerResults.get(0).getUuid());
        assertEquals(-1500, playerResults.get(0).getElo());
        assertEquals(0, playerResults.get(0).getBuchholz());
        assertEquals(0, playerResults.get(0).getRank());
    }

    /**
     * Helper method to create a Signups object for testing.
     */
    private Signups createSignup(String uuid, int elo) {
        Signups signup = new Signups();
        signup.setUuid(uuid);
        signup.setElo(elo);
        return signup;
    }
}
