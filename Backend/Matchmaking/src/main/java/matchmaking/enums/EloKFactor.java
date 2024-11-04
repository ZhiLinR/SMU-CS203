package matchmaking.enums;

public enum EloKFactor {
    BEGINNER(0, 1200, 40), // K value for Elo < 1200
    INTERMEDIATE(1200, 1800, 20), // K value for Elo between 1200 and 1800
    ADVANCED(1800, Integer.MAX_VALUE, 10); // K value for Elo >= 1800

    private final int minElo;
    private final int maxElo;
    private final int kValue;

    EloKFactor(int minElo, int maxElo, int kValue) {
        this.minElo = minElo;
        this.maxElo = maxElo;
        this.kValue = kValue;
    }

    public static int getKValue(int elo) {
        for (EloKFactor factor : EloKFactor.values()) {
            if (elo >= factor.minElo && elo < factor.maxElo) {
                return factor.kValue;
            }
        }
        return 10; // Default value (though all ranges should be covered)
    }
}
