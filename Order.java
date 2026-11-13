import java.util.Objects;

/** Owns preparation progress and a bill fixed at the moment of ordering. */
public class Order {
    private final Dish dish;
    private int remainingPrepUnits;
    private OrderState state;
    private final int lockedPriceCents;
    
    /** Constructor that constructs the price and does not accept null dishes
    */
    public Order(Dish dish) {
    this.dish = Objects.requireNonNull(
            dish, "Dish must not be null.");

    this.remainingPrepUnits = dish.getPrepUnits();
    this.lockedPriceCents = dish.getPriceCents();
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
        remainingPrepUnits = remainingPrepUnits - 1;

        if (remainingPrepUnits == 0) {
            state = OrderState.READY;
        } else {
            state = OrderState.PREPARING;
        }
    }

    /**
     * PREPARING and READY are reached by advancePreparation(), never by skipping work.
     * The controller must also check serving/payment turn eligibility ie:
     * The method doesn't know about turns. 
     * The controller must prevent the Chef from calling it more than once per turn.
     */
    public void setState(OrderState next) {
        Objects.requireNonNull(next, "Order state must not be null.");
        if (next == state) { return; }
        boolean serve = state == OrderState.READY && next == OrderState.SERVED; //becomes yes when both conditions are satisfied
        boolean pay = state == OrderState.SERVED && next == OrderState.PAID; //same thing here
        boolean cancel = next == OrderState.CANCELLED
                && (state == OrderState.PENDING || state == OrderState.PREPARING
                    || state == OrderState.READY);
       /** The above allows for a cancellation iff the current state is pending, ready or preparing.*/
        
        if (!serve && !pay && !cancel) {
            throw new IllegalStateException("Invalid order transition: " + state + " -> " + next);
        }
        state = next;
    }
}
