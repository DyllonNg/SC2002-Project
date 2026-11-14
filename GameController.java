import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.function.Function;
import java.util.Map;

// Set is no longer needed, so we delete the import.
public class GameController {

    private static final int MAX_TURNS = 18;
    private static final int TABLE_COUNT = 2;
    private static final Map<Integer, Function<String, Customer>> SPAWN_TURNS = Map.of( // These values now need to correspond to what type of customer spawned, Set might not be the best for this
        1, RegularCustomer::new, 
        4, VIPCustomer::new, 
        7, RegularCustomer::new, 
        10, CriticCustomer::new, 
        13, RegularCustomer::new, 
        16, RegularCustomer::new); 
    private static final int MIN_PAID_CUSTOMERS = 4;
    private static final int MIN_REVENUE_CENTS = 4500;
    private static final double MIN_AVERAGE_SATISFACTION = 60.0;

    private static final long SEED = 42L;

    private final GameState gameState;
    private final CLI ui;
    private final Menu menu;
    private final Random rng;

    private final Waiter waiter;
    private final Chef chef;
    private final Cashier cashier;
    private final Host host;

    private final Queue<Customer> readyToPay;

    private TurnResult turnResult;

    public GameController() {
        this.gameState = new GameState();
        this.ui = new CLI();
        this.menu = new Menu();
        this.rng = new Random(SEED);

        this.waiter = new Waiter("Waiter");
        this.chef = new Chef("Chef");
        this.cashier = new Cashier("Cashier");
        this.host = new Host("Host");
        this.readyToPay = new ArrayDeque<>();

        for (int id = 1; id <= TABLE_COUNT; id++) {
            gameState.addTable(new Table(id));
        }
    }

    public void run() {
        ui.start();
        while (gameState.turn <= MAX_TURNS) {
            playTurn();
        }
        ui.end(evaluateResult(turnResult)); // 6. Results
    }

    public void playTurn() {
        waiter.resetTurn();
        chef.resetTurn();
        cashier.resetTurn();
        host.resetTurn();

        List<Customer> finishedEating = customersInState(CustomerState.SERVED);

        processSpawnCustomer(); // 1. Arrivals
        processHappyHour();
        ui.displayTurnStart(gameState, new TurnResult(gameState), List.of(host, waiter, chef, cashier));

        processHost();
        processWaiter(); // 2. Waiter
        processChef(); // 3. Chef
        processCashier(); // 4.1 Cashier
        processEating(finishedEating); // 4.2 Eating
        processWaiting(); // 5. Waiting
        gameState.happyHour.endTurn();

        turnResult = new TurnResult(gameState);
        ui.displayTurnEnd(turnResult);
        gameState.advanceTurn();
    }

    // ===== ARRIVALS =====
    private void processSpawnCustomer() {
        Function<String, Customer> factory = SPAWN_TURNS.get(gameState.turn); // This is the new way to get the type of customer to spawn, using the Map instead of a Set
        if (factory != null) { // Now that SPAWN_TURNS is a Map containing the type of customer to spawn, the if statement needs to be modifed
            Customer customer = factory.apply("Customer " + gameState.turn);
            gameState.addQueueCustomer(customer);
            ui.displayMessage(customer.getName() + " joined the queue.");
        }
    }

        // ===== HAPPY HOUR =====
    private void processHappyHour() {
        if (gameState.happyHour.isAvailable() && ui.promptHappyHour(gameState.happyHour)) {
            gameState.happyHour.activate();
            ui.displayMessage("Happy Hour is now active!");
        }
        if (!gameState.happyHour.isActive()) {
            return;
        }

        List<Customer> awaiting = new ArrayList<>(gameState.getWaitingQueue());
        for (Table table : gameState.getTables()) {
            if (table.getOccupant() != null) {
                awaiting.add(table.getOccupant());
            }
        }
        for (Customer customer : awaiting) {
            if (customer.isAwaitingService()) {
                int before = customer.getSatisfaction();
                customer.recoverSatisfaction(gameState.happyHour.getSatisfactionBoost());
                ui.displayMessage(customer.getName() + " recovered satisfaction ("
                        + before + " -> " + customer.getSatisfaction() + ").");
            }
        }
    }

    // ===== HOST =====
    private void processHost() {
        while (host.canPerformTask()) {
            Customer customer = ui.promptHostAction(gameState.waitingQueue);
            if (customer == null) {
                return;
            }
            try {
                Table table = gameState.getFirstAvailableTable();
                if (table == null) {
                    throw new IllegalStateException("There is no free table.");
                }
                host.seatCustomer(customer, table);
                gameState.removeQueueCustomer(customer);
                ui.displayMessage(customer.getName() + " was seated at table "
                        + table.getTableId() + " by the host.");
            } catch (IllegalStateException | IllegalArgumentException e) {
                ui.displayMessage("Cannot do that: " + e.getMessage());
            }
        }
    }

    // ===== WAITER =====
    private void processWaiter() {
        while (waiter.canPerformTask()) {
            WaiterAction action = ui.promptWaiterAction(
                    gameState.waitingQueue, takeableTables(), serveableOrders());
            if (action.getType() == WaiterActionType.WAIT) {
                return;
            }
            try {
                executeWaiterAction(action);
            } catch (IllegalStateException | IllegalArgumentException e) {
                ui.displayMessage("Cannot do that: " + e.getMessage());
            }
        }
    }

    private void executeWaiterAction(WaiterAction action) {
        switch (action.getType()) {
            case SEAT -> seatCustomer(action.getCustomer());
            case TAKE_ORDER -> takeOrder(action.getTable());
            case SERVE -> serveOrder(action.getTargetOrder());
            case WAIT -> {}
        }
    }

    private void seatCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("No customer was selected.");
        }
        Table table = gameState.getFirstAvailableTable();
        if (table == null) {
            throw new IllegalStateException("There is no free table.");
        }
        waiter.seatCustomer(customer, table);
        gameState.removeQueueCustomer(customer);
        ui.displayMessage(customer.getName() + " was seated at table " + table.getTableId() + ".");
    }

    private void takeOrder(Table table) {
        if (table == null) {
            throw new IllegalArgumentException("No table was selected.");
        }
        Customer customer = table.getOccupant();
        if (customer == null) {
            throw new IllegalStateException("That table is empty.");
        }
        Order order = waiter.takeOrder(customer, menu, rng, gameState.happyHour);
        gameState.addOrder(order);
        ui.displayMessage(customer.getName() + " ordered " + order.getDish().getName() + "($" + String.format("%.2f", order.getLockedPriceCents() / 100.0) + ")" + "."); // Maybe show the price of the dish? Mostly to make sure the VIP discount is applied correctly
        // TODO: Find a way to conver the price from cents to dollars and cents. The helper in the CLI might need to be moved here or to dish, or use what processCashier is using.
    }

    private void serveOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("No order was selected.");
        }
        Customer customer = findCustomerByOrder(order);
        if (customer == null) {
            throw new IllegalStateException("No seated customer is waiting for that order.");
        }
        waiter.serveOrder(customer);
        gameState.addServedCustomer(customer);
        ui.displayMessage(customer.getName() + " was served " + order.getDish().getName() + ".");
    }

    // ===== CHEF =====
    private void processChef() {
        while (chef.canPerformTask()) {
            Order order = ui.promptChefOrder(cookableOrders());
            if (order == null) {
                return;
            }
            try {
                chef.advanceOrder(order);
                String dish = order.getDish().getName();
                if (order.isReady()) {
                    ui.displayMessage(dish + " is READY.");
                } else {
                    ui.displayMessage(dish + " has " + order.getRemainingPrepUnits()
                            + " preparation unit(s) left.");
                }
            } catch (IllegalStateException | IllegalArgumentException e) {
                ui.displayMessage("Cannot do that: " + e.getMessage());
            }
        }
    }

    // ===== CASHIER =====
    private void processCashier() {
        Customer customer = readyToPay.peek();
        if (customer == null || !cashier.canPerformTask()) {
            return;
        }
        int billCents = cashier.collectPayment(customer);
        readyToPay.poll();

        gameState.addPaidCustomer(customer);
        gameState.addRevenue(billCents);
        gameState.addSatisfaction(customer.getSatisfaction());

        ui.displayMessage(customer.getName() + " paid $"
                + String.format("%.2f", billCents / 100.0) + "."); // TODO: During run, ensure the payment for VIP reflects the 10% discount.
    }

    // ===== EATING =====
    private void processEating(List<Customer> finishedEating) {
        for (Customer customer : finishedEating) {
            customer.setState(CustomerState.READY_TO_PAY);
            readyToPay.add(customer);
            ui.displayMessage(customer.getName() + " finished eating and is ready to pay.");
        }
    }

    // ===== WAITING =====
    private void processWaiting() {
        List<Customer> leaving = new ArrayList<>();

        for (Customer customer : gameState.waitingQueue) {
            customer.loseSatisfaction();
            if (customer.getSatisfaction() <= 0) {
                leaving.add(customer);
            }
        }
        for (Customer customer : leaving) {
            gameState.removeQueueCustomer(customer);
            processWalk(customer);
        }

        for (Table table : gameState.tables) {
            Customer customer = table.getOccupant();
            if (customer == null) {
                continue;
            }
            if (customer.isAwaitingService()) {
                customer.loseSatisfaction();
                if (customer.getSatisfaction() <= 0) {
                    processWalk(customer);
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

        Table table = customer.getTable();
        if (table != null) {
            table.free();
        }

        gameState.addWalkedCustomer(customer);
        ui.displayMessage(customer.getName() + " lost patience and left.");
    }

    // ===== END GAME =====
    private GameResult evaluateResult(TurnResult result) {
        boolean metCustomerGoal = result.getPaidCustomerCount() >= MIN_PAID_CUSTOMERS;
        boolean metRevenueGoal = result.getRevenue() >= MIN_REVENUE_CENTS;
        boolean metSatisfactionGoal = result.getPaidCustomerCount() > 0
                && result.getAverageSatisfaction() >= MIN_AVERAGE_SATISFACTION;

        return (metCustomerGoal && metRevenueGoal && metSatisfactionGoal)
                ? GameResult.VICTORY
                : GameResult.DEFEAT;
    }

    // ===== MISC CLI FUNCTIONS =====
    private List<Customer> customersInState(CustomerState state) {
        List<Customer> result = new ArrayList<>();
        for (Table table : gameState.tables) {
            Customer customer = table.getOccupant();
            if (customer != null && customer.getState() == state) {
                result.add(customer);
            }
        }
        return result;
    }

    private List<Table> takeableTables() {
        List<Table> result = new ArrayList<>();
        for (Table table : gameState.tables) {
            Customer customer = table.getOccupant();
            if (customer != null
                    && customer.getState() == CustomerState.SEATED
                    && customer.getOrder() == null) {
                result.add(table);
            }
        }
        return result;
    }

    private List<Order> serveableOrders() {
        List<Order> result = new ArrayList<>();
        for (Order order : gameState.orders) {
            if (order.getState() == OrderState.READY) {
                result.add(order);
            }
        }
        return result;
    }

    private List<Order> cookableOrders() {
        List<Order> result = new ArrayList<>();
        for (Order order : gameState.orders) {
            if (order.getState() == OrderState.PENDING
                    || order.getState() == OrderState.PREPARING) {
                result.add(order);
            }
        }
        return result;
    }

    private Customer findCustomerByOrder(Order order) {
        for (Table table : gameState.tables) {
            Customer customer = table.getOccupant();
            if (customer != null && customer.getOrder() == order) {
                return customer;
            }
        }
        return null;
    }
}
