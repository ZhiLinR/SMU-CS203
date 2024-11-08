package matchmaking.enums;

/**
 * Enumeration representing the K values used in Elo rating calculations based
 * on a player's Elo rating.
 *
 * <p>
 * The K value determines the sensitivity of the rating adjustments based on
 * match outcomes,
 * with different ranges of Elo ratings mapped to specific K values.
 * </p>
 */
public enum EloKFactor {

    /**
     * K value for players with Elo ratings below 1200.
     * <p>
     * This indicates a high sensitivity to match outcomes for beginners,
     * allowing for rapid adjustments in their ratings as they learn and improve.
     * </p>
     */
    BEGINNER(0, 1200, 40),

    /**
     * K value for players with Elo ratings between 1200 and 1800.
     * <p>
     * This indicates a moderate sensitivity to match outcomes for intermediate
     * players, balancing the need for adjustments as they become more experienced.
     * </p>
     */
    INTERMEDIATE(1200, 1800, 20),

    /**
     * K value for players with Elo ratings of 1800 and above.
     * <p>
     * This indicates a lower sensitivity to match outcomes for advanced players,
     * reflecting their established skill levels and reducing the volatility of
     * their ratings.
     * </p>
     */
    ADVANCED(1800, Integer.MAX_VALUE, 10);

    private final int minElo;
    private final int maxElo;
    private final int kValue;

    /**
     * Constructs an EloKFactor enumeration constant with specified Elo range and K
     * value.
     *
     * @param minElo the minimum Elo rating for this K value range (inclusive)
     * @param maxElo the maximum Elo rating for this K value range (exclusive)
     * @param kValue the K value associated with this range
     */
    EloKFactor(int minElo, int maxElo, int kValue) {
        this.minElo = minElo;
        this.maxElo = maxElo;
        this.kValue = kValue;
    }

    /**
     * Retrieves the K value corresponding to the given Elo rating.
     *
     * @param elo the Elo rating for which the K value is to be determined
     * @return the K value associated with the Elo rating
     */
    public static int getKValue(int elo) {
        for (EloKFactor factor : EloKFactor.values()) {
            if (elo >= factor.minElo && elo < factor.maxElo) {
                return factor.kValue;
            }
        }
        return 10; // Default value (though all ranges should be covered)
    }
}
