## Boggle
Write a program to play the word game Boggle®.

The Boggle game. Boggle is a word game designed by Allan Turoff and distributed by Hasbro. It involves a board made up of 16 cubic dice, where each die has a letter printed on each of its 6 sides. At the beginning of the game, the 16 dice are shaken and randomly distributed into a 4-by-4 tray, with only the top sides of the dice visible. The players compete to accumulate points by building valid words from the dice, according to these rules:

* A valid word must be composed by following a sequence of adjacent dice—two dice are adjacent if they are horizontal, vertical, or diagonal neighbors.
* A valid word can use each die at most once.
* A valid word must contain at least 3 letters.
* A valid word must be in the dictionary (which typically does not contain proper nouns).

For more information, check the [official assignment description](https://coursera.cs.princeton.edu/algs4/assignments/boggle/specification.php)

## How to compile
Linux / Windows
```
javac -cp ../../lib/algs4.jar BoggleSolver.java Trie.java
```
Mention: Trie.java file is mostly the one in the algs4.jar library, with some modifications for better perfomance on the assignment