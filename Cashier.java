public class Cashier extends Staff {
    public Cashier(String name) {
        super(name);
    }

    public int collectPayment(Customer customer) {
        checkAvailable();
        if (customer == null) {
            throw new IllegalArgumentException("Choose a customer to charge.");
        }
        Order order = customer.getOrder();
        Table table = customer.getTable();
        if (customer.getState() != CustomerState.READY_TO_PAY
                || order == null || order.getState() != OrderState.SERVED
                || table == null || table.getOccupant() != customer) {
            throw new IllegalStateException("Customer is not ready to pay.");
        }
        int billCents = customer.getBillCents();
        if (billCents < 0) {
            throw new IllegalStateException("Bill cannot be negative.");
        }

        table.free();
        customer.assignTable(null);
        order.setState(OrderState.PAID);
        customer.setState(CustomerState.PAID);
        taskDone = true;
        return billCents;
    }
}
