import java.util.List;
import java.util.Random;

// New to Stage 2
public class VIPCustomer extends Customer {

    public VIPCustomer(String name) {
        super(name);
    }

    @Override
    public int getSatisfactionLossPerTurn() {
        return 5; // VIP customers lose 5 satisfaction points
    }

    @Override
    public Dish chooseDish(Menu menu, Random rng) {
        // VIP customers choose a random dish/combo from the menu
        List<Dish> dishes = menu.getOrderableItems();
        return dishes.get(rng.nextInt(dishes.size()));
    }

    @Override
    public int calculateBill(Dish dish) {
        // 10% off for VIP customers
        // 810 for salad, 1350 for burger, 1620 for pasta
        return (dish.getPriceCents() * 90 + 50)/100;
    }

    @Override
    public String getTypeName() {
        return "VIP";
    }

}
