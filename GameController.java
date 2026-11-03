import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Iterator;

public class GameController {
    
    private GameState gameState = new GameState();
    private CLI ui;
    private Menu menu;
    private TurnResult turnResult;
    private GameResult gameResult;
    private final int MAX_TURNS = 18;


    public void run() {
        ui.start();
        for (int i = 1; i <= MAX_TURNS; i++) {
            playTurn();
        }
        ui.end(evaluateResult(turnResult));
    }

    public void playTurn() {
        processSpawnCustomer();

        ui.displayTurnStart(gameState);
        WaiterAction waiterAction = ui.promptWaiterAction(gameState.waitingQueue, gameState.tables, gameState.orders);
        ChefAction chefAction = ui.promptChefAction(gameState.orders);
        ui.promptCashierAction(gameState.tables);

        processWaiter(waiterAction);
        processChef(chefAction);
        processCashier();
        processEating();
        processWaiting();

        turnResult = new TurnResult(gameState);

        ui.displayTurnEnd(turnResult);
    }

    private static final Set<Integer> SPAWN_TURNS = Set.of(1, 4, 7, 10, 13, 16);

    private void processSpawnCustomer() {
        if (SPAWN_TURNS.contains(gameState.turn)) {
            gameState.addQueueCustomer(new RegularCustomer("NAME"));
        }
    }

    private Table getFirstAvailableTable(List<Table> tables) {
        for (Table table : tables) {
            if (table.isFree()) {
                return table;
            }
        }
        return null;
    }

    private void processWaiter(WaiterAction waiterAction) {
        switch (waiterAction.getType()) {
            case SEAT -> {
                Table targetTable = getFirstAvailableTable(gameState.tables);
                if (targetTable != null) {
                    seatCustomer(waiterAction.targetCustomer, targetTable);
                }
            }
            case TAKE_ORDER -> {
                takeOrder(waiterAction.targetTable, menu);
            }
            case SERVE -> {
                serveOrder(waiterAction.targetOrder);
            }
            case WAIT -> {}
        }
    }

    // i would like a ChefAction
    private void processChef(Order order) {
        advanceOrder(order);
    }

    private void processWaiting() {
        // 1. Process customers waiting in line
        Iterator<Customer> iterator = gameState.queue.iterator();
        while (iterator.hasNext()) {
            Customer customer = iterator.next();
            if (customer.getState() == CustomerState.WAITING) {
                customer.loseSatisfaction();
                if (customer.getSatisfaction() <= 0) {
                    processWalk(customer); // Calls processLeave(Customer)
                    iterator.remove();       // Safely removes from queue
                }
            }
        }

        // 2. Process customers sitting at tables
        for (Table table : tables) {
            if (table.isOccupied()) {
                Customer customer = table.getCustomer();
                CustomerState state = customer.getState();

                if (state == CustomerState.WAITING || 
                    state == CustomerState.SEATED || 
                    state == CustomerState.ORDERED) {
                    
                    customer.loseSatisfaction();
                    if (customer.getSatisfaction() <= 0) {
                        processWalk(table); // Calls processLeave(Table)
                    }
                }
            }
        }
    }

    private void processEating() {
        for (Table table : tables) {
            if (table.isOccupied()) {
                Customer customer = table.getCustomer();
                if (customer.getState() == CustomerState.SERVED) {
                    customer.setState(CustomerState.READY_TO_PAY);
                }
            }
        }
    }

    private void processCashier() {
        for (Table table : tables) {
            if (table.isOccupied()) {
                Customer customer = table.getCustomer();
                if (customer.getState() == CustomerState.READY_TO_PAY) {
                    // 1. Process payment / collect money
                    int money = collectPayment(customer);
                    gameState.addRevenue(money);
                    turnResult.revenueCents += money;

                    table.free();
                    // 3. Stop after processing the first ready customer
                    break;
                }
            }
        }
    }

    private void processWalk(Customer customer) {
        // if customer in table empty table
        // if customer in queue leave queue
        gameState.que.remove(customer);
        gameState.addWalkedCustomer(customer);
    }

    private static final int MIN_SERVED_CUSTOMERS = 4;
    private static final int MIN_REVENUE_CENTS = 4500;
    private static final double MIN_AVERAGE_SATISFACTION = 60.0;

    private GameResult evaluateResult(TurnResult turnResult) {
        boolean metCustomerGoal = turnResult.getPaidCustomerCount() >= MIN_SERVED_CUSTOMERS;
        boolean metRevenueGoal = turnResult.getRevenue() >= MIN_REVENUE_CENTS;
        boolean metSatisfactionGoal = turnResult.getAverageSatisfaction() >= MIN_AVERAGE_SATISFACTION;

        if (metCustomerGoal && metRevenueGoal && metSatisfactionGoal) {
            return GameResult.VICTORY;
        } else {
            return GameResult.DEFEAT;
        }
    }
}
