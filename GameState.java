import java.util.List;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class GameState {

    protected int turn;

    protected Queue<Customer> waitingQueue;
    protected List<Table> tables;
    protected List<Customer> paidCustomers;
    protected List<Customer> servedCustomers;
    protected List<Customer> walkedCustomers;

    protected List<Order> orders;

    protected int revenueCents;
    protected int satisfactionSum;

    public GameState() {
        this.turn = 1;
        this.waitingQueue = new ArrayDeque<>();
        this.tables = new ArrayList<>();
        this.paidCustomers = new ArrayList<>();
        this.servedCustomers = new ArrayList<>();
        this.walkedCustomers = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.revenueCents = 0;
        this.satisfactionSum = 0;
    }

    public int getTurn() {return turn;}
    public void advanceTurn() {turn++;}

    public boolean addQueueCustomer(Customer customer) {return waitingQueue.offer(customer);}
    public Customer removeQueueCustomer() {return waitingQueue.poll();}
    public boolean removeQueueCustomer(Customer customer) {return waitingQueue.remove(customer);}
    public int getQueueCustomerCount() {return waitingQueue.size();}
    public List<Customer> getWaitingQueue() {return List.copyOf(waitingQueue);}

    public void addTable(Table table) {tables.add(table);}
    public boolean removeTable(Table table) {return tables.remove(table);}
    public boolean removeTable(int tableId) {return tables.removeIf(table -> table.getTableId() == tableId);}
    public int getTableCount() {return tables.size();}
    public List<Table> getTables() {return List.copyOf(tables);}
    public Table getFirstAvailableTable() {
        for (Table table : tables) {
            if (table.isFree()) {
                return table;
            }
        }
        return null;
    }

    public boolean addWalkedCustomer(Customer customer) {return walkedCustomers.add(customer);}
    public int getWalkedCustomerCount() {return walkedCustomers.size();}

    public void addPaidCustomer(Customer customer) {paidCustomers.add(customer);}
    public int getPaidCustomerCount() {return paidCustomers.size();}

    public void addServedCustomer(Customer customer) { servedCustomers.add(customer); }
    public int getServedCustomerCount() { return servedCustomers.size(); }

    public boolean addOrder(Order order) {return orders.add(order);}

    public void addRevenue(int revenue) {revenueCents += revenue;}

    public void addSatisfaction(int satisfaction) {satisfactionSum += satisfaction;}
}