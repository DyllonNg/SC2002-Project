import java.util.List;
import java.util.Queue;

public class TurnResult {

    private int turn;

    private int queueCustomerCount;
    protected int paidCustomerCount;
    private int tableCount;
    private int walkedCustomerCount;

    private int revenueCents;
    private double averageSatisfaction;

    public TurnResult(GameState gameState) {
        this.turn = getTurn(gameState);
        this.queueCustomerCount = get
    }

    public int getpaidCustomerCount() {
        return paidCustomerCount;
    }

    public int getUnhappyDepartures() {
        return unhappyDepartures;
    }

    public int getRevenueCents() {
        return revenueCents;
    }

    public double getAverageSatisfaction() {
        return averageSatisfaction;
    }

    public int getTurn() {
        return turn;
    }
}
