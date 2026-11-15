# Stage 1
## Victory Run
Customers 1, 4, 7, 10, and 13 pay. Their orders are Pasta($18.00), Salad($9.00),  Salad($9.00), Pasta($18.00), and Salad($9.00). Customer 16 was seated and ordered a Burger, but was never served.
Expected Result: **Victory** with 5 customers served, $63.00 revenue, 0 unhappy departures, and average satisfaction 74.4.
One invalid choice is shown at Turn 11, serving when no dish is READY. Shows the menu again without using up task.

| Turn       | Waiter                      | Customer/Table/Order index | Chef     | Chef index | Enter (turn end) |
| ---------- | --------------------------- | -------------------------- | -------- | ---------- | ---------------- |
| Start      | —                           | —                          | —        | —          | `\n`             |
| 1          | `1` seat                    | `1`                        | `0` wait | —          | `\n`             |
| 2          | `2` take                    | `1`                        | `1` cook | `1`        | `\n`             |
| 3          | `0` wait                    | —                          | `1` cook | `1`        | `\n`             |
| 4          | `3` serve                   | `1`                        | `0` wait | —          | `\n`             |
| 5          | `1` seat                    | `1`                        | `0` wait | —          | `\n`             |
| 6          | `2` take                    | `1`                        | `1` cook | `1`        | `\n`             |
| 7          | `3` serve                   | `1`                        | `0` wait | —          | `\n`             |
| 8          | `1` seat                    | `1`                        | `0` wait | —          | `\n`             |
| 9          | `2` take                    | `1`                        | `1` cook | `1`        | `\n`             |
| 10         | `3` serve                   | `1`                        | `0` wait | —          | `\n`             |
| 11         | `3` serve (invalid → retry) | —                          | —        | —          | —                |
| 11 (retry) | `1` seat                    | `1`                        | `0` wait | —          | `\n`             |
| 12         | `2` take                    | `1`                        | `1` cook | `1`        | `\n`             |
| 13         | `1` seat                    | `1`                        | `1` cook | `1`        | `\n`             |
| 14         | `3` serve                   | `1`                        | `0` wait | —          | `\n`             |
| 15         | `2` take                    | `1`                        | `1` cook | `1`        | `\n`             |
| 16         | `3` serve                   | `1`                        | `0` wait | —          | `\n`             |
| 17         | `1` seat                    | `1`                        | `0` wait | —          | `\n`             |
| 18         | `2` take                    | `1`                        | `1` cook | `1`        | `\n`             |
| End        | —                           | —                          | —        | —          | `\n`             |

## Defeat Run
The manager follows the script but stops making progress after Turn 13. Customers 1, 4, and 7 pay (3 customers, $36.00). Customer 10's Pasta is left at READY and never served. Customer 13 is seated but never orders. Customer 16 is never seated. The log shows intentional stray inputs on turns 11, 14, and 15 (typed during the `Press Enter to continue...` pause) which the program correctly ignores/re-prompts for without crashing.
Expected Result: **Defeat** with 3 customers served, $36.00 revenue, 0 unhappy departures, and average satisfaction 76.0.

| Turn  | Waiter    | Customer/Table/Order index | Chef                                      | Chef index | Enter (turn end)                      |
| ----- | --------- | -------------------------- | ----------------------------------------- | ---------- | ------------------------------------- |
| Start | —         | —                          | —                                         | —          | `\n`                                  |
| 1     | `1` seat  | `1`                        | `0` wait                                  | —          | `\n`                                  |
| 2     | `2` take  | `1`                        | `1` cook                                  | `1`        | `\n`                                  |
| 3     | `0` wait  | —                          | `1` cook                                  | `1`        | `\n`                                  |
| 4     | `3` serve | `1`                        | `0` wait                                  | —          | `\n`                                  |
| 5     | `1` seat  | `1`                        | `0` wait                                  | —          | `\n`                                  |
| 6     | `2` take  | `1`                        | `1` cook                                  | `1`        | `\n`                                  |
| 7     | `3` serve | `1`                        | `0` wait                                  | —          | `\n`                                  |
| 8     | `1` seat  | `1`                        | `0` wait                                  | —          | `\n`                                  |
| 9     | `2` take  | `1`                        | `1` cook                                  | `1`        | `\n`                                  |
| 10    | `3` serve | `1`                        | `0` wait                                  | —          | `\n`                                  |
| 11    | `1` seat  | `1`                        | `0` wait                                  | —          | `\n` _(stray `1` typed during pause)_ |
| 12    | `2` take  | `1`                        | `1` cook                                  | `1`        | `\n`                                  |
| 13    | `1` seat  | `1`                        | `1` cook                                  | `1`        | `\n`                                  |
| 14    | `0` wait  | —                          | `0` wait                                  | —          | `\n` _(stray `0` typed during pause)_ |
| 15    | `0` wait  | —                          | `0` wait                                  | —          | `\n` _(stray `0` typed during pause)_ |
| 16    | `0` wait  | —                          | `0` wait _(2× blank input retries first)_ | —          | `\n`                                  |
| 17    | `0` wait  | —                          | `0` wait                                  | —          | `\n`                                  |
| 18    | `0` wait  | —                          | `0` wait                                  | —          | `\n`                                  |
| End   | —         | —                          | —                                         | —          | `\n`                                  |

# Stage 2

## Victory Run

Customers 1, 4, 7, and 10 pay. Their orders are Pasta ($18.00), Salad (VIP price $8.10, down from $9.00), Salad ($9.00) and Pasta ($18.00, the Critic's choice). Customer 13 is served on turn 17 but cannot finish paying within 18 turns, and Customer 16 is never seated.
Expected result: **VICTORY** with 5 customers served, $53.10 revenue, 0 unhappy departures and average satisfaction 72.3.
Two invalid choices are shown. On turn 11, serving when no dish is READY prints "There are currently no orders to choose from." On turn 16, seating when both tables are taken prints "Cannot do that: There is no free table." Both show the Waiter menu again without using up the task.

| Turn       | Waiter                      | Customer/Table/Order index | Chef     | Chef index | Enter (turn end) |
| ---------- | --------------------------- | -------------------------- | -------- | ---------- | ---------------- |
| Start      | —                           | —                          | —        | —          | `\n`             |
| 1          | `1` seat                    | `1`                        | `0` wait | —          | `\n`             |
| 2          | `2` take                    | `1`                        | `1` cook | `1`        | `\n`             |
| 3          | `0` wait                    | —                          | `1` cook | `1`        | `\n`             |
| 4          | `3` serve                   | `1`                        | `0` wait | —          | `\n`             |
| 5          | `1` seat                    | `1`                        | `0` wait | —          | `\n`             |
| 6          | `2` take                    | `1`                        | `1` cook | `1`        | `\n`             |
| 7          | `3` serve                   | `1`                        | `0` wait | —          | `\n`             |
| 8          | `1` seat                    | `1`                        | `0` wait | —          | `\n`             |
| 9          | `2` take                    | `1`                        | `1` cook | `1`        | `\n`             |
| 10         | `3` serve                   | `1`                        | `0` wait | —          | `\n`             |
| 11         | `3` serve (invalid → retry) | —                          | —        | —          | —                |
| 11 (retry) | `1` seat                    | `1`                        | `0` wait | —          | `\n`             |
| 12         | `2` take                    | `1`                        | `1` cook | `1`        | `\n`             |
| 13         | `1` seat                    | `1`                        | `1` cook | `1`        | `\n`             |
| 14         | `3` serve                   | `1`                        | `0` wait | —          | `\n`             |
| 15         | `2` take                    | `1`                        | `1` cook | `1`        | `\n`             |
| 16         | `1` seat (invalid → retry)  | `1`                        | —        | —          | —                |
| 16 (retry) | `0` wait                    | —                          | `1` cook | `1`        | `\n`             |
| 17         | `3` serve                   | `1`                        | `0` wait | —          | `\n`             |
| 18         | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| End        | —                           | —                          | —        | —          | `\n`             |

## Defeat Run

The manager does nothing on every turn. Customer 1 (Regular) leaves on turn 13 and Customer 10 (Critic) leaves on turn 18 once their satisfaction reaches 0. Customer 4 (VIP) is still waiting at the end, because a VIP loses satisfaction more slowly.
Expected result: **DEFEAT** with 0 customers paid, $0.00 revenue, 2 unhappy departures and average satisfaction shown as N/A.

| Turn       | Waiter                      | Customer/Table/Order index | Chef     | Chef index | Enter (turn end) |
| ---------- | --------------------------- | -------------------------- | -------- | ---------- | ---------------- |
| Start      | —                           | —                          | —        | —          | `\n`             |
| 1          | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| 2          | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| 3          | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| 4          | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| 5          | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| 6          | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| 7          | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| 8          | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| 9          | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| 10         | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| 11         | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| 12         | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| 13         | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| 14         | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| 15         | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| 16         | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| 17         | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| 18         | `0` wait                    | —                          | `0` wait | —          | `\n`             |
| End        | —                           | —                          | —        | —          | `\n`             |

# Stage 3
## Victory Run

Customers 1, 4, 7, 10 and 13 pay. Their orders are Salad ($9.00), Salad + Pasta ($21.87 VIP price, down from $24.30), Salad + Pasta ($24.30), Pasta ($18.00, the Critic's choice) and Salad + Burger ($21.60). Customer 16 is served on turn 18 but does not pay before the game ends.

Happy Hour is activated at the start of turn 11 and remains active for turns 11 and 12. Customer 7 (Regular) is still waiting for food on turn 11, so they recover 15 satisfaction (68 -> 83). They are served on turn 11, so they do not recover on turn 12. Customer 10 (Critic) enters each active turn at 88 (having lost 12 points at the end of the previous turn), recovers 15, and is capped at 100. No order is placed while Happy Hour is active in this run, so the 20% discount is shown in the Defeat Run below.

Expected result: **VICTORY** with 6 customers served, 5 customers paid, $69.57 revenue, 0 unhappy departures, and average satisfaction 87.5.

Invalid choices automatically reprompts the user.

|Turn|HH|Host|Host idx|Waiter|Waiter idx|Chef|Chef idx|Enter|
|---|---|---|---|---|---|---|---|---|
|Start|-|-|-|-|-|-|-|`\n`|
|1|`0`|`1` seat|`1`|`2` take|`1`|`1` cook|`1`|`\n`|
|2|`0`|`0` wait|-|`3` serve|`1`|`0` wait|-|`\n`|
|3|`0`|`0` wait|-|`0` wait|-|`0` wait|-|`\n`|
|4|`0`|`1` seat|`1`|`2` take|`1`|`1` cook|`1`|`\n`|
|5|`0`|`0` wait|-|`0` wait|-|`1` cook|`1`|`\n`|
|6|`0`|`0` wait|-|`0` wait|-|`1` cook|`1`|`\n`|
|7|`0`|`1` seat|`1`|`2` take|`1`|`1` cook|`1`|`\n`|
|8|`0`|`1` seat|-|`3` serve|`1`|`1` cook|`1`|`\n`|
|9|`0`|`0` wait|-|`0` wait|-|`0` wait|-|`\n`|
|10|`0`|`0` wait|-|`0` wait|-|`1` cook|`1`|`\n`|
|11|`1`|`1` seat|`1`|`2` take|`1`|`1` cook|`1`|`\n`|
|12|-|`1` seat|-|`3` serve|`1`|`1` cook|`1`|`\n`|
|13|-|`0` wait|-|`3` serve|`1`|`1` cook|-|`\n`|
|14|-|`0` wait|-|`0` wait|-|`1` cook|-|`\n`|
|15|-|`1` seat|`1`|`2` take|`1`|`1` cook|`1`|`\n`|
|16|-|`1` seat|`1`|`2` take|`1`|`1` cook|`1`|`\n`|
|17|-|`1` seat|-|`3` serve|`1`|`1` cook|`1`|`\n`|
|18|-|`1` seat|-|`3` serve|`1`|`1` cook|-|`\n`|
|End|-|-|-|-|-|-|-|`\n`|

Key Stage 3 behaviour visible in this run:

- **Host:** Seats Customer 1 (T1), Customer 4 (T4), Customer 7 (T7), Customer 10 (T10), Customer 13 (T14) and Customer 16 (T16) in addition to the Waiter's tasks.
- **Combos:** Regular and VIP customers order combos (Salad + Pasta and Salad + Burger). The Critic (Customer 10) correctly orders the most expensive single dish (Pasta). Salad + Pasta takes 3 preparation units.
- **VIP Combo:** Customer 4's Salad + Pasta is charged $21.87 (10% off the $24.30 combo price).
- **Happy Hour:** Activated on turn 11. While active, Customer 10 (Critic) loses 12 points at the end of turn 10 and turn 11, entering the start of turns 11 and 12 at satisfaction 88. The 15-point recovery caps them at 100, showing `(88 -> 100)` on both turns.
- **Post-Happy-Hour order:** Customer 13's Salad + Burger (ordered on Turn 14) is charged full price ($21.60), because Happy Hour ended on Turn 12.
- **Served vs Paid:** Customer 16 is served on Turn 18 but the game ends before they can pay. Thus, 6 customers are served, but only 5 pay.

## Defeat Run

The restaurant plays optimally with the exception that happy hour is activated in turn 1. After turn 9, all the staff wait for the rest of the game.

Expected result: **DEFEAT** with 2 customers served, 2 customers paid, $29.07 revenue, 1 unhappy departures, and average satisfaction shown as 92.5.

|Turn|HH|Host|Host idx|Waiter|Waiter idx|Chef|Chef idx|Enter|
|---|---|---|---|---|---|---|---|---|
|Start|-|-|-|-|-|-|-|`\n`|
|1|`1`|`1` seat|`1`|`2` take|`1`|`1` cook|`1`|`\n`|
|2|-|`0` wait|-|`3` serve|`1`|`0` wait|-|`\n`|
|3|-|`0` wait|-|`0` wait|-|`0` wait|-|`\n`|
|4|-|`1` seat|`1`|`2` take|`1`|`1` cook|`1`|`\n`|
|5|-|`0` wait|-|`0` wait|-|`1` cook|`1`|`\n`|
|6|-|`0` wait|-|`0` wait|-|`1` cook|`1`|`\n`|
|7|-|`1` seat|`1`|`3` serve|`1`|`1` cook|-|`\n`|
|8|-|`0` wait|-|`2` take|`1`|`1` cook|`1`|`\n`|
|9|-|`0` wait|-|`0` wait|-|`1` cook|`1`|`\n`|
|10|-|`0` wait|-|`0` wait|-|`0` wait|-|`\n`|
|11|-|`0` wait|-|`0` wait|-|`0` wait|-|`\n`|
|12|-|`0` wait|-|`0` wait|-|`0` wait|-|`\n`|
|13|-|`0` wait|-|`0` wait|-|`0` wait|-|`\n`|
|14|-|`0` wait|-|`0` wait|-|`0` wait|-|`\n`|
|15|-|`0` wait|-|`0` wait|-|`0` wait|-|`\n`|
|16|-|`0` wait|-|`0` wait|-|`0` wait|-|`\n`|
|17|-|`0` wait|-|`0` wait|-|`0` wait|-|`\n`|
|18|-|`0` wait|-|`0` wait|-|`0` wait|-|`\n`|
|End|-|-|-|-|-|-|-|`\n`|