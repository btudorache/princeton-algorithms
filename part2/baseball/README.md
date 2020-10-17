## Baseball Elimination
Given the standings in a sports division at some point during the season, determine which teams have been mathematically eliminated from winning their division.

**The baseball elimination problem.** In the [baseball elimination problem](https://en.wikipedia.org/wiki/Maximum_flow_problem#Baseball_elimination), there is a division consisting of n teams. At some point during the season, team i has w[i] wins, l[i] losses, r[i] remaining games, and g[i][j] games left to play against team j. A team is mathematically eliminated if it cannot possibly finish the season in (or tied for) first place. The goal is to determine exactly which teams are mathematically eliminated. For simplicity, we assume that no games end in a tie (as is the case in Major League Baseball) and that there are no rainouts (i.e., every scheduled game is played).

For more information, check the [official assignment description](https://coursera.cs.princeton.edu/algs4/assignments/baseball/specification.php)

## How to compile
Linux / Windows
```
javac -cp ../../lib/algs4.jar BaseballElimination.java
```