public class Host extends Staff {
    public Host(String name) {
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
}
