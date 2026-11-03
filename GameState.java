import java.util.List;
import java.util.Queue;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class GameState {

    private int turn;

    private Queue<Customer> waitingQueue;
    private List<Table> tables;
    private List<Customer> paidCustomers;
    private List<Customer> walkedCustomers;

    private int revenueCents;
    private int satisfactionSum;

    public GameState() {
        this.turn = 0;
        this.waitingQueue = new ArrayDeque<>();
        this.tables = new ArrayList<>();
        this.paidCustomers = new ArrayList<>();
        this.walkedCustomers = new ArrayList<>();
        this.revenueCents = 0;
        this.satisfactionSum = 0;
    }

    public int getTurn() {return turn;}

    public Queue<Customer> getWaitingQueue() {return waitingQueue;}
    public List<Table> getTables() {return tables;}
    public List<Customer> getPaidCustomers() {return paidCustomers;}
    public List<Customer> getWalkedCustomers() {return walkedCustomers;}

    public int getRevenue() {return revenueCents;}
    public int getSatisfactionSum() {return satisfactionSum;}

    public void advanceTurn() {turn++;}

    public boolean addQueueCustomer(Customer customer) {return waitingQueue.offer(customer);}
    public Customer removeQueueCustomer() {return waitingQueue.poll();}
    public boolean removeQueueCustomer(Customer customer) {return waitingQueue.remove(customer);}
    public int getQueueCustomerCount() {return waitingQueue.size();}

    public void addTable(Table table) {tables.add(table);}
    public boolean removeTable(Table table) {return tables.remove(table);}
    public boolean removeTable(int tableId) {return tables.removeIf(table -> table.getTableId() == tableId);}
    public int getTableCount() {return tables.size();}

    public boolean addWalkedCustomer(Customer customer) {return walkedCustomers.add(customer);}
    public int getWalkedCustomerCount() {return walkedCustomers.size();}

    public void addPaidCustomer(Customer customer) {paidCustomers.add(customer);}
    public int getPaidCustomerCount() { return paidCustomers.size(); }

    public void addRevenue(int revenue) {revenueCents += revenue;}

    public void addSatisfaction(int satisfaction) {satisfactionSum += satisfaction;}
}
