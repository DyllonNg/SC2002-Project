# SC2002-Project

## Stage 1

First working release. Complete playable 18-turn game with regular
customers and the base menu.

New files:

| File | Reason |
|---|---|
| Customer.java | Abstract base: state, satisfaction, order/table references |
| CustomerState.java | Customer lifecycle enum |
| RegularCustomer.java | Regular customer: 8 loss/turn, random dish choice |
| Staff.java | Abstract staff base with per-turn task tracking |
| Waiter.java | Seat, take order, serve |
| Chef.java | Advance one preparation unit per turn |
| Cashier.java | Collect one payment per turn |
| WaiterAction.java | Waiter's chosen action for the turn |
| WaiterActionType.java | Seat / Take order / Serve / Wait |
| Table.java | Occupancy and assigned-table reference |
| Menu.java | The three base dishes, read-only |
| Dish.java | Name, price in cents, preparation units |
| Order.java | Preparation progress and locked bill |
| OrderState.java | Order lifecycle enum |
| GameState.java | Turn, queue, tables, orders, revenue, counters |
| GameController.java | Turn sequence, arrivals, end-game evaluation |
| TurnResult.java | Per-turn display snapshot |
| GameResult.java | Victory / Defeat |
| CLI.java | Boundary: prompts and display |
| Main.java | Entry point |
| TEST_NOTES.md | Victory and defeat run scripts |
| README.md | This file |

Compare: `stage-1` (first tagged release)

## Stage 1 → 2

New files:

| File | Reason |
|---|---|
| VIPCustomer.java | VIP: 5 loss/turn, 10% discount |
| CriticCustomer.java | Critic: 12 loss/turn, most expensive single dish |

Changed files:

| File                 | Change                                                          | Reason                                                                      |
| -------------------- | --------------------------------------------------------------- | --------------------------------------------------------------------------- |
| Customer.java        | `calculateBill(Dish)`, `getTypeName()`                          | Hooks for VIP pricing and type display                                      |
| RegularCustomer.java | `getTypeName()`                                                 | Returns "Regular"                                                           |
| Waiter.java          | `new Order(dish, customer.calculateBill(dish))`                 | Locks the customer's own price into the order                               |
| Order.java           | New constructor `public Order(Dish dish, int lockedPriceCents)` | Accepts modified pricing to account for VIPCustomer's price discount        |
| GameController.java  | `Map` spawn schedule                                            | Turn 4 → VIP, turn 10 → Critic, no type checks                              |
| CLI.java             | displays `getTypeName()`                                        | Queue, tables and selection lists show the type                             |
| TEST_NOTES.md        | Appended Stage 2 runs                                           | Shows the VIP discount ($8.10) and Critic dish choice in a full playthrough |

Unchanged:

| Files | Reason |
|---|---|
| Dish, Menu, Table, OrderState, Cashier, Chef, Staff, GameState, TurnResult, GameResult, Main, WaiterAction, WaiterActionType, CustomerState | no Stage 2 impact |

Compare: [`stage-1...stage-2`](https://github.com/DyllonNg/SC2002-Project/compare/Stage-1...Stage-2)

## Stage 2 → 3

New files:

|File|Reason|
|---|---|
|Host.java|Extends Staff: seats at most one queued customer per turn, in addition to the Waiter|
|ComboMeal.java|Extends Dish: two-item combo, price 90% of the pair (rounded to the nearest cent), preparation units = sum|
|HappyHour.java|Once-per-game pricing policy: lasts 2 turns, 20% off orders taken while active, +15 satisfaction for waiting customers|

Changed files:

|File|Change|Reason|
|---|---|---|
|Customer.java|`recoverSatisfaction(int)`, `isAwaitingService()`|Happy Hour recovery and the satisfaction loss share one "still waiting" rule|
|RegularCustomer.java|`chooseDish` picks from `menu.getOrderableItems()`|Regular customers can now order combos|
|VIPCustomer.java|`chooseDish` picks from `menu.getOrderableItems()`|VIP customers can now order combos (the 10% discount applies to the combo price)|
|Menu.java|`getCombos()`, `getOrderableItems()`; `findDish` searches combos|Adds Salad + Pasta and Salad + Burger. `getDishes()` still returns only the three singles|
|Waiter.java|`takeOrder` takes a `HappyHour`|Applies Happy Hour after the customer's own bill (VIP first, then Happy Hour) and locks it|
|GameState.java|`happyHour` field, `addServedCustomer(...)`, `getServedCustomerCount()`|Holds the Happy Hour shared by the controller and display; counts customers served|
|TurnResult.java|`getServedCustomerCount()`|End-of-turn screen shows customers served, not only customers paid|
|GameController.java|Host step before the Waiter; Happy Hour prompt, recovery and `endTurn()`; Host in staff list|Turn order: arrivals, Happy Hour, display, Host, Waiter, Chef, Cashier, eating, waiting|
|CLI.java|`promptHostAction`, `promptHappyHour`, Happy Hour status line, `displayOrderPlaced`, `displayPayment`|Host and Happy Hour choices, remaining duration display, money formatting kept in the UI|
|TEST_NOTES.md|Appended Stage 3 runs|Victory run using the Host, combos and Happy Hour, and a defeat run|

Unchanged:

|Files|Reason|
|---|---|
|Dish, Order, OrderState, Table, Chef, Cashier, Staff, CustomerState, WaiterAction, WaiterActionType|no Stage 3 impact: a `ComboMeal` is a `Dish`, so ordering, cooking and payment work as before|
|CriticCustomer, GameResult, Main|no Stage 3 impact (the Critic still orders the most expensive single dish)|

Compare: [`stage-2...stage-3`](https://github.com/DyllonNg/SC2002-Project/compare/Stage-2...Stage-3)
