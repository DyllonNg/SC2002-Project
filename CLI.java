import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.Scanner;

public class CLI {
    private final Scanner scanner;

    public CLI() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        String text = """
                =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
                                RESTAURANT RUSH
                =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
                Objective: At the end of turn 18:
                - At least 4 paid customers
                - Revenue at least $45
                - Average Satisfaction at least 60

                Each turn you will:
                1. Decide whether to activate Happy Hour (once per game)
                2. Direct the host (seat a customer from the queue)
                3. Direct the waiter (seat, take order, serve, or wait)
                4. Direct the chef (choose which order to cook)
                The cashier collects one payment automatically each turn.

                Waiting customers lose satisfaction every turn, and leave if it hits zero.
                There are three types of customers:
                - Regular: normal satisfaction loss, pays full price.
                - VIP: slower satisfaction loss, pays 10% less.
                - Critic: faster satisfaction loss, pays full price, but always chooses the most expensive dish on the menu.
                
                Orders may be combos of several dishes (for example Salad + Pasta).
                The chef must finish every dish in a combo before it is ready to serve.

                Happy Hour: once per game, the manager can activate it. For the current and next turn,
                customers waiting for service recover 10 satisfaction at the start of each turn,
                and orders placed during it cost 20% less.
                """;
        System.out.println(text);
        System.out.print("Press Enter to begin...");
        scanner.nextLine();
    }

    //===== START =====
    public void displayTurnStart(GameState gameState, TurnResult turnResult, List<Staff> staff) {
        System.out.println("=============================================");
        System.out.println("                TURN " + turnResult.getTurn());
        System.out.println("=============================================");
        System.out.println("Revenue             : " + money(turnResult.getRevenue()));
        System.out.println("Average Satisfaction: " + average(turnResult));
        System.out.println("Customers paid      : " + turnResult.getPaidCustomerCount()
                + "   Walked out: " + turnResult.walkedCustomerCount());
        System.out.println("---------------------------------------------");

        System.out.println("WAITING QUEUE");
        List<Customer> queue = gameState.getWaitingQueue();
        if (queue.isEmpty()) {
            System.out.println("  (empty)");
        }
        for (int i = 0; i < queue.size(); i++) {
            Customer c = queue.get(i);
            System.out.println("  " + (i + 1) + ". " + "(" + c.getTypeName() + ")" + " " + c.getName()
                    + " (satisfaction " + c.getSatisfaction() + ")");
        }

        System.out.println("TABLES");
        for (Table table : gameState.getTables()) {
            Customer c = table.getOccupant();
            if (c == null) {
                System.out.println("  Table " + table.getTableId() + ": free");
                continue;
            }
            Order o = c.getOrder();
            String food = (o == null) ? "no order yet"
                    : o.getDish().getName() + " [" + o.getState() + "]";
            System.out.println("  Table " + table.getTableId() + ": " + "(" + c.getTypeName() + ")" + " " + c.getName()
                    + " | " + c.getState() + " | " + food
                    + " | satisfaction " + c.getSatisfaction());
        }
        System.out.println("---------------------------------------------");

        System.out.println("STAFF");
        for (Staff member : staff) {
            System.out.println("  " + member.getName() + ": "
                    + (member.canPerformTask() ? "1 task available" : "task used"));
        }
        System.out.println("---------------------------------------------");
        System.out.println("HAPPY HOUR: " + happyHourStatus(gameState.happyHour));
        System.out.println("=============================================");
    }

    // ===== END =====
    public void displayTurnEnd(TurnResult turnResult) {
        String text = """
                =============================================
                            END OF TURN %d
                =============================================
                Customers served    : %d
                Unhappy departures  : %d
                Revenue             : %s
                Average Satisfaction: %s
                Turns used          : %d
                =============================================
                """.formatted(turnResult.getTurn(),
                              turnResult.getServedCustomerCount(),
                              turnResult.walkedCustomerCount(),
                              money(turnResult.getRevenue()),
                              average(turnResult),
                              turnResult.getTurn());
        System.out.println(text);
        pause();
    }

    public void end(GameResult gameResult) {
        String message = (gameResult == GameResult.VICTORY)
                ? "Congratulations! You met all the objectives."
                : "The restaurant closed. Better luck next time!";

        String text = """
                =============================================
                                GAME OVER
                ---------------------------------------------
                %s
                =============================================
                """.formatted(message);
        System.out.println(text);
    }

    // ===== STAFF METHODS =====
    public boolean promptHappyHour() {
        String text = """
                =============================================
                                MANAGER
                =============================================
                1. Activate Happy Hour
                0. Not now
                ---------------------------------------------""";
        System.out.println(text);
        return readInt("Enter your choice (0-1): ", 0, 1) == 1;
    }
    
    public Customer promptHostAction(Queue<Customer> queue) {
        while (true) {
            String text = """
                    =============================================
                                    HOST ACTION
                    =============================================
                    1. Seat a customer
                    0. Do nothing
                    ---------------------------------------------""";
            System.out.println(text);
            int choice = readInt("Enter your choice (0-1): ", 0, 1);

            if (choice == 0) {
                return null;
            }
            Customer customer = customerSelection(queue);
            if (customer == null && (queue == null || queue.isEmpty())) {
                return null; // nobody to seat, don't loop forever
            }
            if (customer != null) {
                return customer;
            }
        }
    }

    public WaiterAction promptWaiterAction(Queue<Customer> queue, List<Table> takeableTables, List<Order> serveableOrders) {
        WaiterAction action = new WaiterAction();
        while (true) {
            String text = """
                    =============================================
                                  WAITER ACTION
                    =============================================
                    1. Seat a customer
                    2. Take a table's order
                    3. Serve a ready order
                    0. Do nothing
                    ---------------------------------------------""";
            System.out.println(text);
            int choice = readInt("Enter your choice (0-3): ", 0, 3);

            switch (choice) {
                case 0 -> {
                    return action;
                }
                case 1 -> {
                    Customer customer = customerSelection(queue);
                    if (customer != null) {
                        action.setType(WaiterActionType.SEAT);
                        action.setCustomer(customer);
                        return action;
                    }
                }
                case 2 -> {
                    Table table = tableSelection(takeableTables);
                    if (table != null) {
                        action.setType(WaiterActionType.TAKE_ORDER);
                        action.setTable(table);
                        return action;
                    }
                }
                case 3 -> {
                    Order order = orderSelection(serveableOrders);
                    if (order != null) {
                        action.setType(WaiterActionType.SERVE);
                        action.setTargetOrder(order);
                        return action;
                    }
                }
                default -> {}
            }
        }
    }

    public Order promptChefOrder(List<Order> cookableOrders) {
        while (true) {
            String text = """
                    =============================================
                                   CHEF ACTION
                    =============================================
                    1. Cook an order
                    0. Do nothing
                    ---------------------------------------------""";
            System.out.println(text);
            int choice = readInt("Enter your choice (0-1): ", 0, 1);

            if (choice == 0) {
                return null;
            }
            if (cookableOrders == null || cookableOrders.isEmpty()) {
                System.out.println("There are no orders to cook. The chef will wait.");
                return null;
            }
            Order order = orderSelection(cookableOrders);
            if (order != null) {
                return order;
            }
        }
    }

    public void displayOrderPlaced(Customer customer, Order order) {
        System.out.println(customer.getName() + " ordered " + order.getDish()
                + " (" + money(order.getLockedPriceCents()) + ").");
    }

    public void displayPayment(Customer customer, int billCents) {
        System.out.println(customer.getName() + " paid " + money(billCents) + ".");
    }

    // ===== SELECTION METHODS =====
    public Customer customerSelection(Queue<Customer> queue) {
        if (queue == null || queue.isEmpty()) {
            System.out.println("The queue is currently empty.");
            return null;
        }

        List<Customer> customers = List.copyOf(queue);
        System.out.println("Select a customer:");
        System.out.println("0: Back");
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            System.out.println((i + 1) + ": " + "(" + c.getTypeName() + ")" +" " + c.getName() + " (satisfaction " + c.getSatisfaction() + ")");
        }

        int choice = readInt("Enter your choice (0 to " + customers.size() + "): ", 0, customers.size());
        return choice == 0 ? null : customers.get(choice - 1);
    }

    public Table tableSelection(List<Table> tables) {
        if (tables == null || tables.isEmpty()) {
            System.out.println("No table has a customer waiting to order.");
            return null;
        }

        System.out.println("Select a table:");
        System.out.println("0: Back");
        for (int i = 0; i < tables.size(); i++) {
            Table t = tables.get(i);
            System.out.println((i + 1) + ": Table " + t.getTableId()
                    + " (" + "(" + t.getOccupant().getTypeName() + ")" +" " + t.getOccupant().getName() + ")");
        }

        int choice = readInt("Enter your choice (0 to " + tables.size() + "): ", 0, tables.size());
        return choice == 0 ? null : tables.get(choice - 1);
    }

    public Order orderSelection(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            System.out.println("There are currently no orders to choose from.");
            return null;
        }

        System.out.println("Select an order:");
        System.out.println("0: Back");
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            System.out.println((i + 1) + ": " + o.getDish() + " [" + o.getState()
                    + ", " + o.getRemainingPrepUnits() + " unit(s) left]");
        }

        int choice = readInt("Enter your choice (0 to " + orders.size() + "): ", 0, orders.size());
        return choice == 0 ? null : orders.get(choice - 1);
    }

    // ===== HELPER FUNCTIONS =====
    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {}
            System.out.println("Please enter a whole number from " + min + " to " + max + ".");
        }
    }

    private void pause() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

    private String money(int cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }

    private String average(TurnResult result) {
        if (result.getPaidCustomerCount() == 0) {
            return "N/A";
        }
        return String.format(Locale.US, "%.1f", result.getAverageSatisfaction());
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    private String happyHourStatus(HappyHour happyHour) {
        if (happyHour.isActive()) {
            return "ACTIVE (" + happyHour.getRemainingTurns() + " turns remaining)";
        }
        return happyHour.isAvailable() ? "available" : "used";
    }
}
