public class TurnResult {

    private int turn;

    private int queueCustomerCount;
    private int paidCustomerCount;
    private int servedCustomerCount;
    private int tableCount;
    private int walkedCustomerCount;

    private int revenueCents;
    private double averageSatisfaction;

    public TurnResult(GameState gameState) {
        this.turn = gameState.turn;

        this.queueCustomerCount = gameState.getQueueCustomerCount();
        this.paidCustomerCount = gameState.getPaidCustomerCount();
        this.servedCustomerCount = gameState.getServedCustomerCount();
        this.tableCount = gameState.getTableCount();
        this.walkedCustomerCount = gameState.getWalkedCustomerCount();

        this.revenueCents = gameState.revenueCents;
        this.averageSatisfaction = (paidCustomerCount == 0)
            ? 0.0
            : (double) gameState.satisfactionSum / paidCustomerCount;
    }

    public int getTurn() {return turn;}

    public int getQueueCustomerCount() {return queueCustomerCount;}
    public int getPaidCustomerCount() {return paidCustomerCount;}
    public int getServedCustomerCount() {return servedCustomerCount;}
    public int getTableCount() {return tableCount;}
    public int walkedCustomerCount() {return walkedCustomerCount;}
    
    public int getRevenue() {return revenueCents;}
    public double getAverageSatisfaction() {return averageSatisfaction;}
}
