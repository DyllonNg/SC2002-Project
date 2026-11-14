public class Chef extends Staff {
    public Chef(String name) {
        super(name);
    }

    public void advanceOrder(Order order) {
        checkAvailable();
        if (order == null) {
            throw new IllegalArgumentException("Choose an order to prepare.");
        }
        if (order.getState() != OrderState.PENDING
                && order.getState() != OrderState.PREPARING) {
            throw new IllegalStateException("This order cannot be prepared.");
        }

        order.advancePreparation();
        taskDone = true;
    }
}
