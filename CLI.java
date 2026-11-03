import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class CLI {
    private final Scanner scanner;
    
    public CLI() {
        this.scanner = new Scanner(System.in);
    }
    
    public void start() {
        String intro = """
                =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
                                RESTAURANT RUSH                
                =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
                Objective: At the end of turn 18:
                - At least 4 paid customers
                - Revenue at least $45
                - Average Satisfaction at least 60
                
                Each turn you will:
                1. Direct the waiter (seat, take order, serve, or wait)
                2. Direct the chef (choose which order to cook)
                
                Waiting customers lose satisfaction every turn, and leave if it hits zero.
                """;
        System.out.println(intro);
        System.out.print("Press Enter to begin...");
        scanner.nextLine();
    }

    public void displayTurnStart(GameState state) {
        String header = """
            =============================================
                            TURN %d                     
            =============================================
            Revenue             : $%d
            Average Satisfaction: %d
            ---------------------------------------------
            """.formatted(state.getTurn(), 
                          state.getRevenue(), 
                          state.getAverageSatisfaction());
        System.out.println(header);

        System.out.println("Customer Queue:");
        Queue<Customer> queue = state.getWaitingQueue();
        if (queue == null || queue.isEmpty()) {
            System.out.println("(No customers waiting)");
        } else {
            int i = 1;
            for (Customer customer : queue) {
                String entry = "%d. %s [Satisfaction: %d]".formatted(i, customer.getName(), customer.getSatisfaction());
                System.out.println(entry);
                i++;
            }
        }
        System.out.println("=============================================");
    }

    public Customer customerSelection(Queue<Customer> queue) {
        if (queue == null || queue.isEmpty()) {
            System.out.println("The queue is currently empty.");
            return null;
        }

        // 1. Loop through the queue to print the menu
        System.out.println("Select a customer:");
        System.out.println("0: Return no one");
        int count = 1;
        for (Customer customer : queue) {
            System.out.println(count + ": " + customer.getName());
            count++;
        }

        System.out.print("Enter your choice (0 to " + (count - 1) + "): ");
        int choice = scanner.nextInt();

        // 2. Handle the selection dynamically (acting as our 1 to n switch)
        if (choice == 0) {
            return null;
        } else if (choice < 0 || choice >= count) {
            System.out.println("Invalid selection.");
            return null;
        }

        // 3. Loop through the queue again to find the matching customer
        int currentIndex = 1;
        for (Customer customer : queue) {
            if (currentIndex == choice) {
                return customer; // Found the selected customer
            }
            currentIndex++;
        }

        return null; // Fallback in case of unexpected errors
    }

    public Table tableSelection(List<Table> tables) {
        if (tables == null || tables.isEmpty()) {
            System.out.println("There are currently no tables.");
            return null;
        }

        System.out.println("Select a table:");
        System.out.println("0: Return no one");
        
        // Because it's a List, we can use a standard for loop
        for (int i = 0; i < tables.size(); i++) {
            // Assuming Table has a getTableNumber() or getName() method
            System.out.println((i + 1) + ": " + tables.get(i).getTableID());
        }

        System.out.print("Enter your choice (0 to " + tables.size() + "): ");
        int choice = scanner.nextInt();

        if (choice == 0) {
            return null;
        } else if (choice > 0 && choice <= tables.size()) {
            // We can access the List directly by index, subtracting 1 to account for 0-based indexing
            return tables.get(choice - 1);
        } else {
            System.out.println("Invalid selection.");
            return null;
        }
    }

    public Order orderSelection(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            System.out.println("There are currently no orders.");
            return null;
        }

        System.out.println("Select an order:");
        System.out.println("0: Return no one");
        
        for (int i = 0; i < orders.size(); i++) {
            // Assuming Order has a getOrderId() or getName() method to identify it
            System.out.println((i + 1) + ": Order #" + orders.get(i).getDish());
        }

        System.out.print("Enter your choice (0 to " + orders.size() + "): ");
        int choice = scanner.nextInt();

        if (choice == 0) {
            return null;
        } else if (choice > 0 && choice <= orders.size()) {
            return orders.get(choice - 1);
        } else {
            System.out.println("Invalid selection.");
            return null;
        }
    }

    public WaiterAction promptWaiterAction(Queue<Customer> queue, List<Table> tables, List<Order> orders) {
        WaiterAction action = new WaiterAction(); 
        while (true) {
            String menu = """
                    =============================================
                                WAITER ACTION                  
                    =============================================
                    1. Seat a customer
                    2. Take a table's order
                    3. Serve a table
                    0. Do nothing
                    ---------------------------------------------
                    Enter your choice (0-3): """;
            System.out.print(menu);

            int choice = scanner.nextInt();

            switch (choice) {
                case 0:
                    return action;
                    
                case 1:
                    action.type = SEAT;
                    action.targetCustomer = customerSelection(queue);
                    return action;
                    
                case 2:
                    action.type = TAKE_ORDER;
                    action.targetTable = tableSelection(tables);
                    return action;
                    
                case 3:
                    action.type = SERVE;
                    action.targetOrder = orderSelection(readyOrders);
                    return action;
                    
                default:
                    System.out.println("Invalid selection. Please try again.");
                    break;
            }
        }
    }    

    public Order promptChefAction(List<Order> unreadyOrders) {
        while (true) {
            String menu = """
                =============================================
                                CHEF ACTION                  
                =============================================
                1. Cook an order
                0. Do nothing
                ---------------------------------------------
                Enter your choice (0-1): """;
            System.out.print(menu);

            int choice = scanner.nextInt();
            
            switch (choice) {
                case 0:
                    return null; // 0 means do nothing, returning null
                    
                case 1:
                    // If they choose 1, show them the list of orders to pick from.
                    // (If they change their mind here, your orderSelection method 
                    // already has a "0: Return no one" option to back out safely!)
                    return orderSelection(unreadyOrders);
                    
                default:
                    System.out.println("Invalid selection. Please try again.");
                    break;
            }
        }
    }

    public CashierAction promptCashierAction(List<Table> tables) {
        // 1. Find the first table ready to pay
        Table readyTable = null;
        for (Table table : tables) {
            if (table.isOccupied() && table.getCustomer().getState() == CustomerState.READY_TO_PAY) {
                readyTable = table;
                break;
            }
        }

        // 2. Format details string for display
        String customerInfo = (readyTable != null) 
            ? String.format("at Table %d (%s)", readyTable.getId(), readyTable.getCustomer().getName()) 
            : "(No customer ready to pay)";

        // 3. Print status banner
        String menu = String.format("""
            =============================================
                            CASHIER ACTION                 
            =============================================
            Paying for most recent customer %s
            ---------------------------------------------""", customerInfo);

        System.out.println(menu);

        // 4. Return action automatically based on availability
        if (readyTable != null) {
            return new CashierAction(CashierActionType.PAY, readyTable);
        } else {
            return new CashierAction(CashierActionType.WAIT, null);
        }
    }

    public void displayTurnEnd(TurnResult turnResult) {
        String summary = """
                =============================================
                            END OF TURN SUMMARY             
                =============================================
                Turns Used          : %d
                Customers Served    : %d
                Unhappy Departures  : %d
                Revenue             : $%.2f
                Average Satisfaction: %.1f
                =============================================
                """.formatted(turnResult.getTurnsUsed(),
                              turnResult.getServedCount(),
                              turnResult.getUnhappyDepartures(),
                              turnResult.getRevenueCents() / 100.0,
                              turnResult.getAverageSatisfaction());
                
        System.out.println(summary);
        System.out.print("Press Enter to continue...");
        scanner.nextLine(); 
    }

    public void end(GameResult gameResult) {
        // Determine the text based on whether they won or lost
        String message;
        if (gameResult == GameResult.VICTORY) {
            message = "Congratulations! You met all the objectives. ";
        } else {
            message = "The restaurant closed. Better luck next time!";
        }

        String summary = """
                =============================================
                                GAME OVER                   
                ---------------------------------------------
                        %s
                =============================================
                """.formatted(message);
                
        System.out.println(summary);
    }
}
