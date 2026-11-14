import java.util.Random;

public class Waiter extends Staff {
    public Waiter(String name) {
        super(name);
    }

    public void seatCustomer(Customer customer, Table table) {
        checkAvailable();
        if (customer == null || table == null) {
            throw new IllegalArgumentException("Choose a customer and a table.");
        }
        if (customer.getState() != CustomerState.WAITING || customer.getTable() != null) {
            throw new IllegalStateException("Customer is not waiting for a table.");
        }
        if (!table.isFree()) {
            throw new IllegalStateException("Table is already occupied.");
        }

        table.occupy(customer);
        taskDone = true;
    }

    public Order takeOrder(Customer customer, Menu menu, Random rng, HappyHour happyHour) {
        checkAvailable();
        if (customer == null || menu == null) {
            throw new IllegalArgumentException("Choose a customer and a menu.");
        }
        if (rng == null) {
            throw new IllegalArgumentException("Random generator cannot be null.");
        }
        if (customer.getState() != CustomerState.SEATED || customer.getOrder() != null) {
            throw new IllegalStateException("Customer cannot place an order now.");
        }
        if (customer.getTable() == null || customer.getTable().getOccupant() != customer) {
            throw new IllegalStateException("Customer must be seated at their table.");
        }

        Dish dish = customer.chooseDish(menu, rng);
        if (dish == null) {
            throw new IllegalStateException("Customer could not choose a dish.");
        }
        int billCents = happyHour.applyDiscount(customer.calculateBill(dish));
        Order order = new Order(dish, billCents);
        customer.assignOrder(order);
        customer.setState(CustomerState.ORDERED);
        taskDone = true;
        return order;
    }

    public void serveOrder(Customer customer) {
        checkAvailable();
        if (customer == null) {
            throw new IllegalArgumentException("Choose a customer to serve.");
        }
        Order order = customer.getOrder();
        if (customer.getState() != CustomerState.ORDERED
                || order == null || order.getState() != OrderState.READY) {
            throw new IllegalStateException("Customer's order is not ready to serve.");
        }

        order.setState(OrderState.SERVED);
        customer.setState(CustomerState.SERVED);
        taskDone = true;
    }
}
