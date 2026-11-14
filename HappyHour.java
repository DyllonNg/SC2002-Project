/** Once-per-game policy; the controller activates and ticks it at turn boundaries. */
public class HappyHour {
    private static final int DURATION_TURNS = 2;
    private static final int DISCOUNT_PERCENT = 20;
    private static final int SATISFACTION_BOOST = 15;

    private boolean activated;
    private int remainingTurns;

    public boolean isAvailable() { return !activated; }
    public boolean isActive() { return remainingTurns > 0; }
    public boolean isUsed() { return activated && !isActive(); }
    public int getRemainingTurns() { return remainingTurns; }

    /** Call only at the start of a turn. Reactivation is never allowed. */
    public void activate() {
        if (activated) {
            throw new IllegalStateException("Happy Hour can only be activated once per game.");
        }
        activated = true;
        remainingTurns = DURATION_TURNS;
    }

    /** Call once after waiting and abandonment, at the end of each game turn. */
    public void endTurn() {
        if (isActive()) {
            remainingTurns = remainingTurns - 1;
        }
    }

    /** Apply after the customer's own discount. Round half up to integer cents. */
    public int applyDiscount(int cents) {
        if (cents < 0) {
            throw new IllegalArgumentException("Bill must not be negative.");
        }
        if (!isActive()) { return cents; }
        long discounted = (long) cents * (100 - DISCOUNT_PERCENT);
        return (int) ((discounted + 50) / 100);
    }

    /** The controller applies this to eligible customers; this class edits no customer. */
    public int getSatisfactionBoost() {
        if (isActive()) { return SATISFACTION_BOOST; }
        return 0;
    }
}
