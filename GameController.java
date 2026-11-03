import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.ArrayList;

public class GameController {
    
    private final GameState gameState;
    private final CLI ui;
    private final Menu menu;
    private TurnResult turnResult;
    private static final int MAX_TURNS = 18;
    private static final Set<Integer> SPAWN_TURNS = Set.of(1, 4, 7, 10, 13, 16);
    private static final int MIN_SERVED_CUSTOMERS = 4;
    private static final int MIN_REVENUE_CENTS = 4500;
    private static final double MIN_AVERAGE_SATISFACTION = 60.0;

    public GameController() {
        this.gameState = new GameState();
        this.ui = new CLI();
        this.menu = new Menu();
    }

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
    
    private void processSpawnCustomer() {
        if (SPAWN_TURNS.contains(gameState.turn)) {
            gameState.addQueueCustomer(new RegularCustomer("NAME"));
        }
    }

    private void processWaiter(WaiterAction waiterAction) {
        switch (waiterAction.getType()) {
            case SEAT -> {
                Table targetTable = gameState.getFirstAvailableTable();
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
    
    private void processChef(ChefAction chefAction) {
        // i would like a ChefAction
    }

    private void processCashier(CashierAction cashierAction) {
        //i would like a CashierAction please
    }

    private void processWaiting() {
        for (Customer customer : gameState.waitingQueue) {
            customer.loseSatisfaction();
        }

        List<Customer> leaving = new ArrayList<>();

        for (Customer customer : gameState.waitingQueue) {
            if (customer.getSatisfaction() <= 0) {
                leaving.add(customer);
            }
        }

        for (Customer customer : leaving) {
            gameState.removeQueueCustomer(customer);
            processWalk(customer);
        }

        for (Table table : gameState.tables) {
            if (!table.isFree()) {
                Customer customer = table.getOccupant();
                CustomerState customerState = customer.getState();

                if (customerState == CustomerState.SEATED ||
                    customerState == CustomerState.ORDERED) {
                    customer.loseSatisfaction();
                    if (customer.getSatisfaction() <= 0) {
                        processWalk(customer);
                    }
                }
            }
        }
    }

    private void processEating() {
        for (Table table : gameState.tables) {
            if (!table.isFree()) {
                Customer customer = table.getOccupant();
                if (customer.getState() == CustomerState.SERVED) {
                    customer.setState(CustomerState.READY_TO_PAY);
                }
            }
        }
    }

    private void processWalk(Customer customer) {
        customer.setState(CustomerState.ABANDONED);

        Order order = customer.getOrder();
        if (order != null) {
            order.setState(OrderState.CANCELLED);
        }

        Table table = customer.getAssignedTable();
        if (table != null) {
            table.free();
        }

        gameState.addWalkedCustomer(customer);
    }

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
