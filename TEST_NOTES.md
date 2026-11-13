# Victory Run
Customers 1, 4, 7, 10,  and 13 pay. Their orders are Pasta($18.00), Salad($9.00),  Salad($9.00), Pasta($18.00), and Salad($9.00). Customer 16 was seated and ordered a Burger, but was never served.
Expected Result: Victory with 5 customers paid, $63.00 revenue, 0 unhappy departures, and average satisfaction 74.4.
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

# Defeat Run
The manager follows the script but stops making progress after Turn 13. Customers 1, 4, and 7 pay (3 customers, $36.00). Customer 10's Pasta is left at READY and never served. Customer 13 is seated but never orders. Customer 16 is never seated. The log shows intentional stray inputs on turns 11, 14, and 15 (typed during the `Press Enter to continue...` pause) which the program correctly ignores/re-prompts for without crashing.
Expected Result: **DEFEAT** with 3 customers paid, $36.00 revenue, 0 unhappy departures, and average satisfaction 76.0.

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