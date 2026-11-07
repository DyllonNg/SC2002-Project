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

    public Table getAssignedTable() { 
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
}