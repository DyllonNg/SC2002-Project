import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Iterator;

public class GameController {
    
    private GameState gameState = new GameState();
    private CLI ui;
    private Menu menu;
    private int turn;
    private TurnResult tResult = new TurnResult();
    private GameResult gResult;
    private final int MAX_TURNS = 18;
    private Queue<Customer> queue;
    private List<Table> tables;
    private List<Order> unreadyOrders;
    private List<Order> readyOrders;

    public void run() {
        ui.start();
        
        for (int turn = 1; turn <= MAX_TURNS; turn++) {
            playTurn();
        }
        
        ui.end(evaluateResult());
    }

    public void playTurn() {
        processAddCustomer();

        ui.displayTurnStart(gameState);
        WaiterAction action = ui.promptWaiterAction(queue, tables, readyOrders);
        Order order = ui.promptChefAction(unreadyOrders);
        ui.promptCashierAction();

        processWaiter(action);
        processChef(order);
        processCashier();
        processEating();
        processWaiting();

        ui.displayTurnEnd(tResult);
    }

    private static final Set<Integer> SPAWN_TURNS = Set.of(1, 4, 7, 10, 13, 16);

    private void processAddCustomer() {
        if (SPAWN_TURNS.contains(turn)) {
            queue.add(new RegularCustomer("NAME"));
        }
    }

    private Table getFirstAvailableTable() {
        for (Table table : tables) {
            if (table.isFree()) {
                return table;
            }
        }
        return null; // All tables are currently full
    }

    private void processWaiter(WaiterAction action) {
        switch (action.getType()) {
            case SEAT -> {
                Table targetTable = getFirstAvailableTable();
                if (targetTable != null) {
                    seatCustomer(action.targetCustomer, targetTable);
                } else {
                    // Restaurant is full: handle overflow (e.g., add to queue, log, or ignore action)
                    queueCustomer(action.targetCustomer);
                }
            }
            case TAKE_ORDER -> {
                takeOrder(action.targetTable, menu);
            }
            case SERVE -> {
                serveOrder(action.targetOrder);
                tResult.servedCount++;
            }
            case WAIT -> {
                // Intentionally left blank: waiter is waiting
            }
        }
    }

    private void processChef(Order order) {
        advanceOrder(order);
    }

    private void processWaiting() {
        // 1. Process customers waiting in line
        Iterator<Customer> iterator = queue.iterator();
        while (iterator.hasNext()) {
            Customer customer = iterator.next();
            if (customer.getState() == CustomerState.WAITING) {
                customer.loseSatisfaction();
                if (customer.getSatisfaction() <= 0) {
                    processLeave(customer); // Calls processLeave(Customer)
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
                        processLeave(table); // Calls processLeave(Table)
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
                    tResult.revenueCents += money;

                    table.free();
                    // 3. Stop after processing the first ready customer
                    break;
                }
            }
        }
    }

        // Handles seated customers leaving
    private void processLeave(Table table) {
        tResult.unhappyDepartures++;
        table.free();
        // Add any table-departure logic (e.g., tResult.unhappyDepartures++)
    }

    // Handles queue customers leaving
    private void processLeave(Customer customer) {
        queue.remove(customer);
        tResult.unhappyDepartures++;
    }

    private static final int MIN_SERVED_CUSTOMERS = 4;
    private static final int MIN_REVENUE_CENTS = 4500; // $45.00
    private static final double MIN_AVERAGE_SATISFACTION = 60.0;

    private GameResult evaluateResult(TurnResult tresult) {
        boolean metCustomerGoal = tresult.getServedCount() >= MIN_SERVED_CUSTOMERS;
        boolean metRevenueGoal = tresult.getRevenueCents() >= MIN_REVENUE_CENTS;
        boolean metSatisfactionGoal = tresult.getAverageSatisfaction() >= MIN_AVERAGE_SATISFACTION;

        if (metCustomerGoal && metRevenueGoal && metSatisfactionGoal) {
            return GameResult.VICTORY;
        }
        
        return GameResult.DEFEAT; // Or your enum's equivalent loss state (e.g. LOSS, DEFEAT)
    }
}
