import java.util.Random;

public abstract class Customer {
    protected final String name;
    protected CustomerState state;
    protected Order order;
    protected int satisfaction;
    protected Table assignedTable;

    public Customer(String name) {
        this.name = name;
        this.state = CustomerState.WAITING;
        this.satisfaction = 100;
    }

    public String getName() { 
        return name; 
    }
    
    public CustomerState getState() { 
        return state; 
    }
    
    public void setState(CustomerState s) { 
        this.state = s; 
    }

    public int getSatisfaction() { 
        return satisfaction; 
    }
    
    public void setSatisfaction(int s) { 
        this.satisfaction = Math.max(0, Math.min(100, s)); 
    }

    public Order getOrder() { 
        return order; 
    }
    
    public void assignOrder(Order o) { 
        this.order = o; 
    }

    public Table getTable() { 
        return assignedTable; 
    }
   
    public void assignTable(Table t) { 
        this.assignedTable = t; 
    }

    public abstract int getSatisfactionLossPerTurn();
    
    public abstract Dish chooseDish(Menu menu, Random rng);

    public void loseSatisfaction() { 
        setSatisfaction(satisfaction - getSatisfactionLossPerTurn());
    }

    // This had to be added now that VIP gets a 10% off
    public int calculateBill(Dish dish){
        return dish.getPriceCents();
    }

    public abstract String getTypeName();

    public void recoverSatisfaction(int points){
        if(points < 0) { 
            // Recovery must only add, negative value woud deduct instead
            throw new IllegalArgumentException("Recovery points must not be negative");
        }
        setSatisfaction(satisfaction + points);
    }

    // Stage 3 added a scenario where customers waiting to be seated or for their food gain satisfaction during happy hour
    // The states used for losing satisfaction and happy hour are the same, so it's better to have a method for processing them for easier modification
    public boolean isAwaitingService(){
        CustomerState s = getState();
        return s == CustomerState.WAITING
            || s == CustomerState.SEATED
            || s == CustomerState.ORDERED;
    }
}