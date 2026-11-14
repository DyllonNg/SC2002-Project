import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Stage 3 menu. Read-only lists have a fixed order for seeded customer choices. */
public class Menu {
    private final List<Dish> dishes;
    private final List<ComboMeal> combos;
    private final List<Dish> orderableItems;

    public Menu() {
        Dish salad = new Dish("Salad", 900, 1);
        Dish burger = new Dish("Burger", 1500, 1);
        Dish pasta = new Dish("Pasta", 1800, 2);

        dishes = Collections.unmodifiableList(Arrays.asList(salad, burger, pasta));
        combos = Collections.unmodifiableList(Arrays.asList(
                new ComboMeal(salad, pasta),
                new ComboMeal(salad, burger)));

        List<Dish> allItems = new ArrayList<>();
        allItems.addAll(dishes);
        allItems.addAll(combos);
        orderableItems = Collections.unmodifiableList(allItems);
    }

    /** Read-only singles: Salad, Burger, Pasta. Preserves the Stage 2 API. */
    public List<Dish> getDishes() { return dishes; }

    /** Read-only combos: Salad + Pasta, then Salad + Burger. */
    public List<ComboMeal> getCombos() { return combos; }

    /** Read-only choices: the three singles followed by the two combos. */
    public List<Dish> getOrderableItems() { return orderableItems; }

    /** Searches singles and combos, ignoring case and surrounding spaces. */
    public Dish findDish(String name) {
        if (name == null) { return null; }
        for (Dish dish : orderableItems) {
            if (dish.getName().equalsIgnoreCase(name.trim())) { return dish; }
        }
        return null;
    }
}
