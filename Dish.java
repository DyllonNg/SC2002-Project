/** A menu dish. Prices are integer cents; preparation is measured in whole units. */
public class Dish {
    private final String name;
    private final int priceCents;
    private final int prepUnits;

    public Dish(String name, int priceCents, int prepUnits) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Dish name must not be blank.");
        }
        if (priceCents <= 0 || prepUnits <= 0) {
            throw new IllegalArgumentException("Price and preparation units must be positive.");
        }
        this.name = name.trim();
        this.priceCents = priceCents;
        this.prepUnits = prepUnits;
    }

    public String getName() { return name; }
    public int getPriceCents() { return priceCents; }
    public int getPrepUnits() { return prepUnits; }
}
