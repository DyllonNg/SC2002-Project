import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** The required Stage 1 menu, in a stable order for seeded customer choices. */
public class Menu {
    private final List<Dish> dishes;

    public Menu() {
        dishes = Collections.unmodifiableList(Arrays.asList(
                new Dish("Salad", 900, 1),
                new Dish("Burger", 1500, 1),
                new Dish("Pasta", 1800, 2)));
    }

    /** Read-only view: callers cannot add, remove, or replace menu entries. */
    public List<Dish> getDishes() { return dishes; }

    /** Case-insensitive lookup; returns null when no dish matches. */
    public Dish findDish(String name) {
        if (name == null) { return null; }
        for (Dish dish : dishes) {
            if (dish.getName().equalsIgnoreCase(name.trim())) { return dish; }
        }
        return null;
    }
}
