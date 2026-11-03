public class TurnResult {

    private int turn;

    private int queueCustomerCount;
    private int paidCustomerCount;
    private int tableCount;
    private int walkedCustomerCount;

    private int revenueCents;
    private double averageSatisfaction;

    public TurnResult(GameState gameState) {
        this.turn = gameState.turn;

        this.queueCustomerCount = gameState.getQueueCustomerCount();
        this.paidCustomerCount = gameState.getPaidCustomerCount();
        this.tableCount = gameState.getTableCount();
        this.walkedCustomerCount = gameState.getWalkedCustomerCount();

        this.revenueCents = gameState.revenueCents;
        this.averageSatisfaction = getAverageSatisfaction(gameState.satisfactionSum);
    }

    public int getTurn() {return turn;}

    public int getQueueCustomerCount() {return queueCustomerCount;}
    public int getPaidCustomerCount() {return paidCustomerCount;}
    public int getTableCount() {return tableCount;}
    public int walkedCustomerCount() {return walkedCustomerCount;}
    
    public int getRevenue() {return revenueCents;}
    public double getAverageSatisfaction() {return averageSatisfaction;}
    public double getAverageSatisfaction(int satisfactionSum) {return (double) satisfactionSum / (paidCustomerCount + walkedCustomerCount);}
}
