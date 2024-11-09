package matchmaking.util;

import matchmaking.model.PlayerTournamentId;
import matchmaking.model.Signups;
import matchmaking.dto.*;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ConversionUtil class, which provides utility methods
 * for converting and processing player data, specifically converting Signups
 * to PlayerResults and ordering players by rank.
 */
public class ConversionUtilTest {

    /**
     * Tests that the getUuidsOrderedByRank method throws an exception when
     * an empty list is provided.
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
     * Tests that the convertSignupsToPlayerResults method throws an exception when
     * provided with a null input, indicating missing Signups data.
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
     * Tests that the convertSignupsToPlayerResults method throws an exception when
     * an empty list of Signups is provided, indicating no signup data is available.
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
     * Tests that the convertSignupsToPlayerResults method correctly converts
     * a single Signup entry to a corresponding PlayerResults entry.
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
     * Tests the getUuidsOrderedByRank method when multiple players have
     * duplicate ranks, ensuring correct ordering based on input order.
     */
    @Test
    public void testGetUuidsOrderedByRank_DuplicateRanks() {
        PlayerResults player1 = new PlayerResults()
                .setUuid("uuid1")
                .setRank(1);

        PlayerResults player2 = new PlayerResults()
                .setUuid("uuid2")
                .setRank(1); // Same rank as player1

        PlayerResults player3 = new PlayerResults()
                .setUuid("uuid3")
                .setRank(2);

        List<PlayerResults> playerResults = Arrays.asList(player1, player2, player3);

        List<String> orderedUuids = ConversionUtil.getUuidsOrderedByRank(playerResults);

        assertEquals(Arrays.asList("uuid1", "uuid2", "uuid3"), orderedUuids);
        // The order of uuid1 and uuid2 should be preserved (based on the order of
        // input)
    }

    /**
     * Tests the performance of the convertSignupsToPlayerResults method with a
     * large
     * number of entries, ensuring that it handles high input volume efficiently.
     */
    @Test
    public void testConvertSignupsToPlayerResults_LargeInput() {
        List<Signups> signups = Collections.nCopies(10000, createSignup("uuid", 1000));
        List<PlayerResults> playerResults = ConversionUtil.convertSignupsToPlayerResults(signups);
        assertEquals(10000, playerResults.size());
    }

    /**
     * Tests that the convertSignupsToPlayerResults method correctly handles
     * Signups entries with negative Elo values.
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
     * Helper method to create a Signups object with the specified UUID and Elo
     * rating
     * for testing purposes.
     *
     * @param uuid The UUID to set for the Signup object.
     * @param elo  The Elo rating to set for the Signup object.
     * @return A new Signups object with the specified UUID and Elo rating.
     */
    private Signups createSignup(String uuid, int elo) {
        PlayerTournamentId id = new PlayerTournamentId()
                .setUuid(uuid);
        Signups signup = new Signups()
                .setId(id)
                .setElo(elo);
        return signup;
    }
}
