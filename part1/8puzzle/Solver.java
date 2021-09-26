import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Solver {
    private final SearchNode solNode;
    private final boolean solvable;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        MinPQ<SearchNode> firstPq = new MinPQ<SearchNode>(new KeyOrder());
        MinPQ<SearchNode> secondPq = new MinPQ<SearchNode>(new KeyOrder());

        SearchNode firstNode = new SearchNode(null, initial);
        SearchNode secondNode = new SearchNode(null, initial.twin());

        firstPq.insert(firstNode);
        secondPq.insert(secondNode);
        while (true) {
            SearchNode firstcheckNode = firstPq.delMin();
            SearchNode secondcheckNode = secondPq.delMin();
            if (firstcheckNode.isGoal) {
                solNode = firstcheckNode;
                solvable = true;
                break;
            }
            if (secondcheckNode.isGoal) {
                solNode = secondcheckNode;
                solvable = false;
                break;
            }

            for (Board board : firstcheckNode.currentBoard.neighbors()) {
                if (firstcheckNode.prevNode == null || !board.equals(firstcheckNode.prevNode.currentBoard)) {
                    SearchNode newNode = new SearchNode(firstcheckNode, board);
                    firstPq.insert(newNode);
                }
            }

            for (Board board : secondcheckNode.currentBoard.neighbors()) {
                if (secondcheckNode.prevNode == null || !board.equals(secondcheckNode.prevNode.currentBoard)) {
                    SearchNode newNode = new SearchNode(secondcheckNode, board);
                    secondPq.insert(newNode);
                }
            }
        }
    }

    private class SearchNode {
        private final Board currentBoard;
        private final SearchNode prevNode;
        private final boolean isGoal;
        private final int manhattan;
        private final int moves;

        public SearchNode(SearchNode prev, Board current) {
            currentBoard = current;
            prevNode = prev;
            manhattan = current.manhattan();
            isGoal = manhattan == 0;
            if (prev == null) {
                moves = 0;
            } else {
                moves = prevNode.moves + 1;
            }
        }
    }

    private class KeyOrder implements Comparator<SearchNode> {
        public int compare(SearchNode node1, SearchNode node2) {
            return Integer.compare(node1.manhattan + node1.moves,
                                   node2.manhattan + node2.moves);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (this.isSolvable()) {
            return this.solNode.moves;
        } else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (this.isSolvable()) {
            SearchNode copy = this.solNode;
            List<Board> sol = new ArrayList<>();
            while (copy != null) {
                sol.add(copy.currentBoard);
                copy = copy.prevNode;
            }

            Collections.reverse(sol);
            return sol;
        } else {
            return null;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In();
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}