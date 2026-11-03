public class TurnResult {

    private int turn;

    private int queueCustomerCount;
    private int paidCustomerCount;
    private int tableCount;
    private int walkedCustomerCount;

    private int revenueCents;
    private double averageSatisfaction;

    public TurnResult(GameState gameState) {
        this.turn = gameState.getTurn();

        this.queueCustomerCount = gameState.getQueueCustomerCount();
        this.paidCustomerCount = gameState.getPaidCustomerCount();
        this.tableCount = gameState.getTableCount();
        this.walkedCustomerCount = gameState.getWalkedCustomerCount();

        this.revenueCents = gameState.getRevenue();
        this.averageSatisfaction = (double) gameState.getSatisfactionSum() / (paidCustomerCount + walkedCustomerCount);
    }

    public int getTurn() {return turn;}

    public int getQueueCustomerCount() {return queueCustomerCount;}
    public int getPaidCustomerCount() {return paidCustomerCount;}
    public int getTableCount() {return tableCount;}
    public int walkedCustomerCount() {return walkedCustomerCount;}
}
