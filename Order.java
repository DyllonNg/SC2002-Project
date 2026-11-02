import java.util.Objects;

/** Owns preparation progress and a bill fixed at the moment of ordering. */
public class Order {
    private final Dish dish;
    private int remainingPrepUnits;
    private OrderState state;
    private final int lockedPriceCents;

    public Order(Dish dish) {
        this(dish, Objects.requireNonNull(dish, "Dish must not be null.").getPriceCents());
    }

    /** Stage 1 callers normally use Order(dish), which locks the full menu price. */
    public Order(Dish dish, int lockedPriceCents) {
        this.dish = Objects.requireNonNull(dish, "Dish must not be null.");
        if (lockedPriceCents < 0) {
            throw new IllegalArgumentException("Locked price must not be negative.");
        }
        this.remainingPrepUnits = dish.getPrepUnits();
        this.lockedPriceCents = lockedPriceCents;
        this.state = OrderState.PENDING;
    }

    public Dish getDish() { return dish; }
    public OrderState getState() { return state; }
    public int getRemainingPrepUnits() { return remainingPrepUnits; }
    public int getLockedPriceCents() { return lockedPriceCents; }
    public boolean isReady() { return state == OrderState.READY; }

    /** One Chef action. The controller must limit calls to one per turn. */
    public void advancePreparation() {
        if (state != OrderState.PENDING && state != OrderState.PREPARING) {
            throw new IllegalStateException("Only pending or preparing orders can be cooked.");
        }
        remainingPrepUnits--;
        state = remainingPrepUnits == 0 ? OrderState.READY : OrderState.PREPARING;
    }

    /**
     * Retains the UML setter while rejecting invalid lifecycle changes.
     * PREPARING and READY are reached by advancePreparation(), never by skipping work.
     * The controller must also check serving/payment turn eligibility.
     */
    public void setState(OrderState next) {
        Objects.requireNonNull(next, "Order state must not be null.");
        if (next == state) { return; }
        boolean serve = state == OrderState.READY && next == OrderState.SERVED;
        boolean pay = state == OrderState.SERVED && next == OrderState.PAID;
        boolean cancel = next == OrderState.CANCELLED
                && (state == OrderState.PENDING || state == OrderState.PREPARING
                    || state == OrderState.READY);
        if (!serve && !pay && !cancel) {
            throw new IllegalStateException("Invalid order transition: " + state + " -> " + next);
        }
        state = next;
    }
}
