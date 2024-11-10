package matchmaking.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the EloKFactor class, which determines the K value used in
 * Elo rating calculations based on a player's Elo rating.
 */
public class EloKFactorTest {

    /**
     * Tests that the K value is correctly returned for Elo ratings below 1200.
     * <p>
     * Expected K value for ratings below 1200 is 40. This includes edge cases
     * such as the lowest possible Elo rating and various values just below 1200.
     * </p>
     */
    @Test
    public void testGetKValue_Below1200() {
        assertEquals(40, EloKFactor.getKValue(1199)); // Just below 1200
        assertEquals(40, EloKFactor.getKValue(0)); // Edge case of lowest possible Elo
        assertEquals(40, EloKFactor.getKValue(1190)); // A middle value below 1200
    }

    /**
     * Tests that the K value is correctly returned for Elo ratings between 1200 and
     * 1800.
     * <p>
     * Expected K value for ratings in this range is 20. This tests the boundary
     * conditions for the lower edge of the range and values near the upper edge.
     * </p>
     */
    @Test
    public void testGetKValue_Between1200And1800() {
        assertEquals(20, EloKFactor.getKValue(1200)); // Lower edge
        assertEquals(20, EloKFactor.getKValue(1799)); // Just below 1800
        assertEquals(20, EloKFactor.getKValue(1500)); // A middle value between 1200 and 1800
    }

    /**
     * Tests that the K value is correctly returned for Elo ratings of 1800 and
     * above.
     * <p>
     * Expected K value for ratings of 1800 or higher is 10. This includes tests
     * for the lower edge at 1800 and a range of higher values.
     * </p>
     */
    @Test
    public void testGetKValue_Above1800() {
        assertEquals(10, EloKFactor.getKValue(1800)); // Lower edge
        assertEquals(10, EloKFactor.getKValue(2000)); // A middle value above 1800
        assertEquals(10, EloKFactor.getKValue(Integer.MAX_VALUE)); // Maximum possible Elo
    }

    /**
     * Tests that the K value is returned correctly for Elo ratings at the defined
     * boundary conditions.
     * <p>
     * This test checks the K values at the critical points: just below 1200,
     * exactly 1200, just below 1800, and exactly 1800.
     * </p>
     */
    @Test
    public void testGetKValue_BoundaryConditions() {
        assertEquals(40, EloKFactor.getKValue(1199)); // Just below 1200
        assertEquals(20, EloKFactor.getKValue(1200)); // Exactly 1200
        assertEquals(20, EloKFactor.getKValue(1799)); // Just below 1800
        assertEquals(10, EloKFactor.getKValue(1800)); // Exactly 1800
    }

    /**
     * Tests that the K value returns the default for Elo ratings outside defined
     * ranges.
     * <p>
     * Expected K value for ratings below 0 and above the maximum defined range
     * is 10, testing the robustness of the method against extreme inputs.
     * </p>
     */
    @Test
    public void testGetKValue_OutsideDefinedRanges() {
        assertEquals(10, EloKFactor.getKValue(-1)); // Below minimum range
        assertEquals(10, EloKFactor.getKValue(10000)); // Above maximum range
    }
}
