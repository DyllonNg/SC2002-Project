import java.util.List;
import java.util.Random;

// This is seperated from customer because it's kinda obvious that we're gonna add more types of customers since that's pretty much the easiest way to increase the difficulty.
public class RegularCustomer extends Customer {

    public RegularCustomer(String name) {
        super(name);
    }

    @Override
    public int getSatisfactionLossPerTurn() {
        return 8; // Regular customers lose 8 satisfaction points
    }

    @Override
    public Dish chooseDish(Menu menu, Random rng) {
        // Regular customers choose a random dish from the menu
        List<Dish> dishes = menu.getDishes();
        return dishes.get(rng.nextInt(dishes.size()));
    }

    @Override
    public String getTypeName() {
        return "Regular";
    }
}
