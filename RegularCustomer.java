public class RegularCustomer extends Customer {

    public RegularCustomer(String name) {
        super(name);
    }

    @Override
    public int getSatisfactionLoss() {
        return 8; // Regular customers lose 8 satisfaction points
    }

    @Override
    public Dish chooseDish(Menu menu, Random rng) {
        // Regular customers choose a random dish from the menu
        List<Dish> dishes = menu.getDishes();
        return dishes.get(rng.nextInt(dishes.size()));
    }

}
