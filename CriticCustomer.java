import java.util.List;
import java.util.Random;

// New to Stage 2
public class CriticCustomer extends Customer {

    public CriticCustomer(String name) {
        super(name);
    }

    @Override
    public int getSatisfactionLossPerTurn() {
        return 12; // Critic customers lose 12 satisfaction points
    }

    // Critics chooses the most expensive dish from the menu
    @Override
    public Dish chooseDish(Menu menu, Random rng) {
        List<Dish> dishes = menu.getDishes();
        Dish mostExpensiveDish = dishes.get(0);
        for (Dish dish : dishes) {
            if (dish.getPriceCents() > mostExpensiveDish.getPriceCents()) {
                mostExpensiveDish = dish;
            }
        }
        return mostExpensiveDish;
    }

    @Override
    public String getTypeName() {
        return "Critic";
    }

}
