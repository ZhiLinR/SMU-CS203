package matchmaking.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EloKFactorTest {

    /**
     * Tests that the K value is correctly returned for Elo ratings below 1200.
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
     */
    @Test
    public void testGetKValue_Above1800() {
        assertEquals(10, EloKFactor.getKValue(1800)); // Lower edge
        assertEquals(10, EloKFactor.getKValue(2000)); // A middle value above 1800
        assertEquals(10, EloKFactor.getKValue(Integer.MAX_VALUE)); // Maximum possible Elo
    }

    /**
     * Tests that the K value is returned correctly for Elo exactly at the boundary
     * conditions.
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
     */
    @Test
    public void testGetKValue_OutsideDefinedRanges() {
        assertEquals(10, EloKFactor.getKValue(-1)); // Below minimum range
        assertEquals(10, EloKFactor.getKValue(10000)); // Above maximum range
    }
}
