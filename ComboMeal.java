import java.util.Objects;

/** Two existing dishes sold at 90 percent of their combined price. */
public class ComboMeal extends Dish {
    private final Dish first;
    private final Dish second;

    public ComboMeal(Dish first, Dish second) {
        super(comboName(first, second), comboPrice(first, second),
                Math.addExact(first.getPrepUnits(), second.getPrepUnits()));
        this.first = first;
        this.second = second;
    }

    private static String comboName(Dish first, Dish second) {
        Objects.requireNonNull(first, "First combo dish must not be null.");
        Objects.requireNonNull(second, "Second combo dish must not be null.");
        return first.getName() + " + " + second.getName();
    }

    private static int comboPrice(Dish first, Dish second) {
        // Use long arithmetic to avoid overflow before rounding to integer cents.
        long sum = (long) first.getPriceCents() + second.getPriceCents();
        return Math.toIntExact((sum * 90 + 50) / 100);
    }

    public Dish getFirst() { return first; }
    public Dish getSecond() { return second; }
}
