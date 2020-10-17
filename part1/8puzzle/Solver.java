import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private SearchNode solNode;
    private boolean solvable;

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
            if (firstcheckNode.currentBoard.isGoal()) {
                solNode = firstcheckNode;
                solvable = true;
                break;
            }
            if (secondcheckNode.currentBoard.isGoal()) {
                solNode = secondcheckNode;
                solvable = false;
                break;
            }
            for (Board board : firstcheckNode.currentBoard.neighbors()) {
                if (firstcheckNode.prevNode != null &&
                        !board.equals(firstcheckNode.prevNode.currentBoard)) {
                    SearchNode newNode = new SearchNode(firstcheckNode, board);
                    firstPq.insert(newNode);
                }
                else if (firstcheckNode.prevNode == null) {
                    SearchNode newNode = new SearchNode(firstcheckNode, board);
                    firstPq.insert(newNode);
                }
            }
            for (Board board : secondcheckNode.currentBoard.neighbors()) {
                if (secondcheckNode.prevNode != null &&
                        !board.equals(secondcheckNode.prevNode.currentBoard)) {
                    SearchNode newNode = new SearchNode(secondcheckNode, board);
                    secondPq.insert(newNode);
                }
                else if (secondcheckNode.prevNode == null) {
                    SearchNode newNode = new SearchNode(secondcheckNode, board);
                    secondPq.insert(newNode);
                }
            }
        }
    }

    private class SearchNode {
        private Board currentBoard;
        private SearchNode prevNode;
        private int moves;

        public SearchNode(SearchNode prev, Board current) {
            currentBoard = current;
            prevNode = prev;
            if (prev == null) {
                moves = 0;
            }
            else {
                moves = prevNode.moves + 1;
            }
        }
    }

    private class KeyOrder implements Comparator<SearchNode> {
        public int compare(SearchNode node1, SearchNode node2) {
            if (node1.currentBoard.manhattan() + node1.moves >
                    node2.currentBoard.manhattan() + node2.moves) {
                return 1;
            }
            else if (node1.currentBoard.manhattan() + node1.moves <
                    node2.currentBoard.manhattan() + node2.moves) {
                return -1;
            }
            else {
                return 0;
            }
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
        }
        else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (this.isSolvable()) {
            SearchNode copy = this.solNode;
            Queue<Board> sol = new Queue<Board>();
            while (copy.prevNode != null) {
                sol.enqueue(copy.currentBoard);
                copy = copy.prevNode;
            }
            return sol;
        }
        else {
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
