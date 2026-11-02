# Restaurant Rush Stage 1 Operations

This module implements the tables, menu and orders for Restaurant Rush. It follows the team's Stage 1 class diagram and connects to the supplied Customer and CustomerState classes. It is one part of the game, not a standalone playable application.

## Files and responsibilities

| File | Responsibility |
| --- | --- |
| Dish.java | Stores a food item's name, price in cents and preparation units |
| Menu.java | Creates the three required dishes and provides read-only access and name lookup |
| Table.java | Manages occupancy and keeps the customer's assigned-table reference consistent |
| Order.java | Tracks preparation, validates state changes and stores the locked bill |
| OrderState.java | Defines the possible order statuses |

## Menu and dishes

| Dish | Price in cents | Display price | Preparation units |
| --- | ---: | ---: | ---: |
| Salad | 900 | $9.00 | 1 |
| Burger | 1500 | $15.00 | 1 |
| Pasta | 1800 | $18.00 | 2 |

Money is stored as integer cents. Dish fields are private and final, with getters and no setters. The constructor rejects blank names, nonpositive prices and nonpositive preparation units.

### Menu methods

| Method | Behaviour |
| --- | --- |
| Menu() | Creates the base menu in the order Salad, Burger, Pasta |
| getDishes() | Returns an unmodifiable list; callers cannot add, remove or replace entries |
| findDish(String name) | Finds a dish ignoring case and surrounding spaces; returns null if absent |

Customers select their own dishes. The manager must not choose their food. The existing customer method is chooseDish(Menu, Random). The controller should provide one reusable seeded Random and document its seed for reproducible runs.

## Tables

| Method | Behaviour |
| --- | --- |
| Table(int tableId) | Creates an empty table with a positive ID |
| getTableId() | Returns its ID |
| isFree() | Returns true when it has no occupant |
| getOccupant() | Returns the occupant, or null if empty |
| occupy(Customer customer) | Seats an unassigned WAITING customer, updates both references and sets the customer to SEATED |
| free() | Releases a PAID or ABANDONED customer and clears both references; does nothing if already empty |

The controller must give tables distinct IDs and remove a successfully seated customer from the waiting queue. A customer cannot be seated at two tables using occupy(). A table cannot be released while its customer is still being served or waiting to pay.

## Orders

### Constructors

```java
Order regularOrder = new Order(dish);
```

Uses the dish's normal price. This is the constructor Stage 1 callers should use.

```java
Order explicitlyPricedOrder = new Order(dish, finalPriceCents);
```

Stores an explicitly supplied nonnegative bill. This supports later integration with discounted pricing, but does not calculate discounts itself. Both constructors start the order as PENDING and initialise remaining preparation from the dish. Neither accepts a null dish.

The bill is final and cannot change after construction. This allows later stages to lock discounted prices when orders are taken.

### Methods

| Method | Behaviour |
| --- | --- |
| getDish() | Returns the selected dish |
| getState() | Returns the current order state |
| getRemainingPrepUnits() | Returns preparation units still required |
| getLockedPriceCents() | Returns the fixed bill |
| isReady() | Returns true only when the state is READY |
| advancePreparation() | Completes one preparation unit and updates PREPARING or READY |
| setState(OrderState next) | Validates and applies serving, payment or cancellation transitions |

### Lifecycle and ownership

| Transition | Responsible caller | Method |
| --- | --- | --- |
| Creation to PENDING | Waiter during order taking | new Order(dish) |
| PENDING/PREPARING to PREPARING/READY | Chef | advancePreparation() |
| READY to SERVED | Waiter following the manager's decision | setState(OrderState.SERVED) |
| SERVED to PAID | Cashier after payment eligibility is confirmed | setState(OrderState.PAID) |
| PENDING/PREPARING/READY to CANCELLED | Controller after customer abandonment | setState(OrderState.CANCELLED) |

A one-unit dish goes directly from PENDING to READY after one preparation action. A multi-unit dish passes through PREPARING. Only advancePreparation() can move an order into PREPARING or READY; callers cannot use setState() to skip cooking.

Setting the existing state again does nothing. Other invalid transitions throw IllegalStateException without changing the order. A READY dish can remain unserved across turns, but its customer continues losing satisfaction and may abandon.

Because setState() is public, Java allows any class to call it. It validates the transition, not the caller's identity. Team members must follow the responsibility split above.

## Controller and staff integration

Order does not track turns. The controller/GameState must track the current turn and coordinate these rules:

- Run the Waiter before the Chef and allow at most one task for each per turn.
- Allow the Chef to work on a newly taken order in the same turn.
- Prevent serving a dish completed later in that same turn.
- Track when customers were served and when they became READY_TO_PAY.
- Allow eating completion only on a later turn after service.
- Collect at most one payment per turn, oldest eligible customer first, excluding customers who only became READY_TO_PAY this turn.
- Apply satisfaction loss only to eligible unserved customers.
- Cancel an abandoned customer's order, mark the customer ABANDONED, and release their table or remove them from the waiting queue.
- Add revenue and record satisfaction exactly once at payment.
- Run all 18 turns and calculate the final result.

The controller must check these conditions before requesting state changes. For example, Order accepts SERVED to PAID, but only the controller knows whether the customer has finished eating and waited until a valid payment turn.

Order state and customer state are separate. When serving, update both the order to SERVED and the customer to CustomerState.SERVED. When paying, update both to PAID, record the bill and satisfaction, then release the table.

### Example of taking an order

```java
// Controller has checked: customer is SEATED, has no order,
// and the Waiter still has an action available.
Dish chosen = customer.chooseDish(menu, rng);
Order order = new Order(chosen);
customer.assignOrder(order);
customer.setState(CustomerState.ORDERED);
// Register the order in game state and display the selected food.
```

### Example of releasing a table after payment

```java
// Controller/Cashier has confirmed payment is eligible this turn.
order.setState(OrderState.PAID);
customer.setState(CustomerState.PAID);
int billCents = order.getLockedPriceCents();
// Add billCents to revenue and record satisfaction exactly once.
customer.getAssignedTable().free();
```

## Error handling

Invalid constructor values produce IllegalArgumentException or NullPointerException. Invalid operations, such as serving an uncooked order or freeing an unpaid customer's table, produce IllegalStateException. An attempted modification of the menu list produces UnsupportedOperationException.

For invalid player choices, the CLI/controller should show a helpful message and allow another attempt. Staff capacity must be consumed only after a successful action. These Operations classes do not read input or print game messages.

## Object-oriented concepts

| Concept | Application |
| --- | --- |
| Encapsulation | Private fields and validated methods protect preparation, bills and occupancy |
| Abstraction | Callers use operations such as occupy() and advancePreparation() without managing their internal steps |
| Association | Table references Customer; Order references Dish |
| Aggregation | Menu groups Dish objects that Orders can also reference |
| Immutability | Dish's stored values and Order's locked bill cannot be reassigned after construction |
| Constructor overloading | Order supports normal-price and explicitly priced construction |
| Single responsibility | Menu manages menu access, Table manages occupancy and Order manages its lifecycle |

These five files do not introduce an inheritance hierarchy. Inheritance and runtime polymorphism are demonstrated by the existing Customer/RegularCustomer classes: calling chooseDish() through a Customer reference invokes the concrete customer's implementation.

## Dependencies and setup

Use a JDK supporting Java 8 or later. No external libraries are needed. All supplied classes use the default package, so put the five Operations files in the same source directory as Customer.java and CustomerState.java. If the team adopts named packages later, coordinate package declarations and imports together.

The supplied RegularCustomer.java snapshot is missing two imports. Its owner must add:

```java
import java.util.List;
import java.util.Random;
```

The Operations implementation uses the real Customer API, including getAssignedTable(), assignTable(), getState() and setState(). The notes' different Customer method names should be updated in the final UML to match the team's actual source.

## Verification

The delivered OperationsTest.java passed 44 checks against the supplied, unchanged Customer and CustomerState classes. It uses a test-only Customer subclass. Checks cover menu values and read-only access, validation, preparation, invalid transitions, cancellation, locked bills, duplicate seating and table release.

From the delivered module directory, after copying Customer.java and CustomerState.java into src/:

```sh
mkdir -p build
javac -d build src/*.java tests/OperationsTest.java
java -cp build OperationsTest
```

The unchanged full teammate snapshot fails compilation because of the two missing RegularCustomer imports. A temporary copy with only those imports added compiled successfully with the Operations files. No teammate source files were modified in this delivery.

Full game behaviour, turn timing, victory and defeat require integration testing after the controller, CLI and staff modules are connected.
