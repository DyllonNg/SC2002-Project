# Victory Run

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