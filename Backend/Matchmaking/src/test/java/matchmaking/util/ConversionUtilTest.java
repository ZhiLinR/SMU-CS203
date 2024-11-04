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
     * Tests the getUuidsOrderedByRank method with an empty list.
     */
    @Test
    public void testGetUuidsOrderedByRank_EmptyList() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ConversionUtil.getUuidsOrderedByRank(Collections.emptyList());
        });

        assertEquals("Missing Player Results.", exception.getMessage());
    }

    /**
     * Tests the conversion from null Signups to PlayerResults.
     */
    @Test
    public void testConvertSignupsToPlayerResults_NullInput() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ConversionUtil.convertSignupsToPlayerResults(null);
        });

        assertEquals("Missing Signups.", exception.getMessage());
    }

    /**
     * Tests the conversion from an empty list of Signups to PlayerResults.
     */
    @Test
    public void testConvertSignupsToPlayerResults_EmptyInput() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ConversionUtil.convertSignupsToPlayerResults(Collections.emptyList());
        });

        assertEquals("Missing Signups.", exception.getMessage());
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
