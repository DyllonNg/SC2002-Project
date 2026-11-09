import java.util.Objects;

/** Keeps table occupancy and the customer's assigned-table reference consistent. */
public class Table {
    private final int tableId;
    private Customer occupant;

    public Table(int tableId) {
        if (tableId <= 0) { throw new IllegalArgumentException("Table ID must be positive."); }
        this.tableId = tableId;
    }

    public int getTableId() { return tableId; }
    public boolean isFree() { return occupant == null; }
    public Customer getOccupant() { return occupant; }

    public void occupy(Customer customer) {
        Objects.requireNonNull(customer, "Customer must not be null.");

        if (!isFree()) { throw new IllegalStateException("Table is already occupied."); }

        if (customer.getTable() != null || customer.getState() != CustomerState.WAITING) {
            throw new IllegalStateException("Only an unseated waiting customer can be seated.");
        }
        
        occupant = customer;
        customer.assignTable(this);
        customer.setState(CustomerState.SEATED);
    }

    /** Mark the customer PAID or ABANDONED before releasing their table. */
    public void free() {
        if (isFree()) { return; }
        CustomerState status = occupant.getState();
        if (status != CustomerState.PAID && status != CustomerState.ABANDONED) {
            throw new IllegalStateException("Table remains occupied until payment or abandonment.");
        }
        occupant.assignTable(null);
        occupant = null;
    }
}
