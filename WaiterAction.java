public class WaiterAction {
    private WaiterActionType type;
    private Customer customer;
    private Table table;
    private Order targetOrder;

    public WaiterAction() {
        this.type = WaiterActionType.WAIT;
        this.customer = null;
        this.table = null;
        this.targetOrder = null;
    }

    public WaiterActionType getType() {
        return type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Table getTable() {
        return table;
    }

    public Order getTargetOrder() {
        return targetOrder;
    }

    public void setType(WaiterActionType type) {
        this.type = type;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public void setTargetOrder(Order targetOrder) {
        this.targetOrder = targetOrder;
    }
}
