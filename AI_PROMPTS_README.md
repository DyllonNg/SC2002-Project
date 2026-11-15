# Operations Design Roleplay

Fictional dialogue for learning and illustration. This is not a transcript or evidence of the actual development process or AI assistance received.

The student is responsible for Dish, Menu, Order, OrderState and Table, followed by ComboMeal and HappyHour. The AI provides design ideas and reasoning rather than complete implementations.

## 1 Starting the Operations section

Student: im doing the operations part. dish menu order orderstate and table. idk java that well where do i even start? just ideas first pls

AI: Start by giving each class one clear job. Dish describes something that can be ordered. Menu holds the available items. Order represents one customer’s request and its progress. OrderState lists the possible order statuses. Table tracks who occupies a table. Agree with your teammates on the methods they need before implementing the details.

## 2 Distinguishing dishes and orders

Student: why cant dish just store whether its ready also? why need order

AI: A Dish is the reusable menu description, such as Pasta costing 1,800 cents and requiring two preparation units. An Order is one particular request for that dish. Two customers can order the same Pasta object but have different preparation progress. Store that progress in separate Order objects.

## 3 Choosing the Dish data

Student: okay what should i put in dish then and how do i stop ppl changing the price randomly

AI: Give Dish a name, price in cents and preparation units. Keep those fields private and final, initialise them in the constructor, and provide getters. Validate the name and the numeric values when constructing it. Other classes can read the item’s data without setters changing the stored values.

## 4 Representing money

Student: cents as int? wouldnt double be easier like 18.00

AI: Integer cents make the calculation exact for the amounts you store. Pasta is 1800 internally; the CLI formats it as $18.00. Percentage calculations still need an explicit rounding rule, but using cents avoids introducing floating-point approximations into the saved bill.

## 5 Building the Menu

Student: menu should create all the dishes right? do i make a new pasta each time someone orders

AI: Menu can create the standard dishes once and keep them in a list. Orders can reference those existing items because their stored dish data does not change. Every new customer request gets a new Order, rather than a new menu definition. Keep the menu’s registration order consistent if seeded random selection depends on list positions.

## 6 Exposing the menu safely

Student: if getDishes returns the list can another guy add stuff into it tho

AI: Yes, if you return an ordinary mutable list directly. Store an unmodifiable list or return an unmodifiable copy. Returning the field is only read-only if that field already refers to an unmodifiable list. This protects the collection against additions, removals and replacements through that reference.

## 7 Introducing the enum

Student: whats orderstate supposed to contain? are READY and those things java commands

AI: They are constants you define in an enum, not built-in commands. Use PENDING, PREPARING, READY, SERVED, PAID and CANCELLED. Order stores one OrderState value. The expression state == OrderState.READY checks whether its current value is READY. The enum names the states; Order decides which changes are valid.

## 8 Constructing an Order

Student: so when i make an order what values do i start it with

AI: Require a valid Dish reference, copy its preparation units into remainingPrepUnits, and initialise the state to PENDING. Also store the agreed bill in lockedPriceCents. Reject a null dish and a negative bill. Keep the dish reference and saved bill final so they are assigned during construction and cannot later be reassigned.

## 9 Advancing preparation

Student: chef cooks once per turn yeah? how do i do that without writing the entire turn controller in order

AI: Give Order an advancePreparation() method that represents one unit of work. Allow it only while PENDING or PREPARING. Subtract one remaining unit, then use an ordinary if/else: zero means READY; otherwise PREPARING. Chef checks its own action availability, and GameController decides when the Chef acts. Order does not need a turn counter.

## 10 Serving at the right time

Student: pasta finishes turn 3 can waiter serve it turn 3 also or do i have to block that in order somehow

AI: The controller’s phase order handles this. The Waiter acts before the Chef, so an order completed by the Chef on turn 3 becomes available for service on a later turn. Order should still reject serving unless it is READY. Do not add a second timing system inside Order when the controller already enforces the turn sequence.

## 11 Validating state changes

Student: can i just do setState whatever the waiter sends? also when does cancelled happen

AI: Validate transitions inside Order. Serving allows READY to SERVED, payment allows SERVED to PAID, and cancellation allows PENDING, PREPARING or READY to CANCELLED. In the game flow, the controller cancels an unserved order when its customer abandons the restaurant. A public method does not identify whether its caller is a Waiter or Chef; it validates the requested operation.

## 12 Managing table occupancy

Student: table has a customer and customer has a table. wont those get out of sync? who sets what

AI: Agree on one place to maintain both links. Table.occupy(customer) can verify that the table is free and that the customer is waiting and unassigned. It then stores the occupant, assigns the table to the customer and updates the customer to SEATED. Staff call this operation, while the controller removes the customer from the waiting queue.

## 13 Releasing a table

Student: free just makes occupant null? what about the customer side

AI: Clear the customer’s table reference as well. Before releasing an occupied table, check that the customer has paid or abandoned the restaurant. Keep queue removal, revenue updates and payment processing outside Table. Its job is to manage occupancy consistently, not complete the whole checkout workflow.

## 14 Agreeing on team interfaces

Student: my teammates havent finished customer yet and mine uses getAssignedTable but his is getTable. how do we work separately without breaking everything

AI: Agree on the exact method names, parameter types and responsibilities together. If Customer exposes getTable() and assignTable(Table), Table must use those names. Write down who changes each state and collection, then integrate a small seating example early. Separate files still depend on shared interfaces, so discussing those contracts is part of working independently.

## 15 Supporting VIP prices

Student: stage 2 now. vip gets 10 percent off. should order check if customer is vip?

AI: Keep customer-specific pricing outside Order. The Waiter can call calculateBill(dish) through a Customer reference, allowing VIPCustomer to provide its discounted result. Pass that amount into Order and save it. Order then handles preparation and payment state in exactly the same way for all customer types.

## 16 Choosing the constructors

Student: earlier we had one constructor only. do we actually need two now or can one do everything

AI: One constructor accepting Dish and the final bill is enough if every caller supplies the calculated price. A convenience constructor accepting only Dish can delegate using the menu price, but it is optional. The important part is updating every call site consistently. Assign the final bill during construction; do not add a setter that changes it later.

## 17 Adding combo meals

Student: stage 3 combos. can combo extend dish but also have two dishes inside? sounds weird to me

AI: Yes. In this design a ComboMeal is an orderable Dish, and it also has two component Dish references. Menu passes existing dishes into it. Calculate the combo price as 90% of the component prices combined, rounded half up, and add their preparation units. That lets Order and Chef process the combo through the existing Dish methods.

## 18 Registering combos and checking numbers

Student: so menu constructs salad and pasta first then puts them into combo? what lists should i have and what should salad pasta cost

AI: Keep separate lists for individual dishes and combos, plus a combined list of orderable items. Salad at 900 cents and Pasta at 1800 cents give a 2430-cent combo requiring three preparation units. Regular and VIP selection can use all orderable items; the Critic’s most-expensive-single-dish rule should still use the individual dishes. A VIP’s additional 10% reduction makes this combo 2187 cents.

## 19 Designing Happy Hour

Student: happy hour is 2 turns once only right. what goes in my class and what stays controller? and if vip orders during it do both discounts apply

AI: HappyHour should track whether it has been activated and how many active turns remain. It can expose activation, active-status checks, endTurn(), discount calculation and the 15-point recovery amount. The controller decides when to activate it and which eligible customers recover satisfaction. Apply the customer’s pricing first, then the 20% Happy Hour discount, rounding each step. Save that final amount in Order so checkout does not recalculate it.

## 20 Checking the completed Operations flow

Student: okay how do i know this all makes sense now? dont generate tests just tell me what to check and what oo stuff i can explain

AI: Trace an individual dish and a combo through ordering, preparation, service and payment. Check that invalid state changes fail, an occupied table cannot be reused, and releasing a table clears both references. Check that Happy Hour lasts two turns, cannot be activated twice, and does not change existing bills. Explain encapsulation through Order’s private state and fixed bill, inheritance and substitution through ComboMeal extending Dish, aggregation through its two shared components, and separate responsibilities through the controller, staff and Operations classes.
