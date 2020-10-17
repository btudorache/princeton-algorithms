import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {
    private int[][] board;
    private int n;

    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException();
        }
        
        n = tiles[0].length;
        board = copyArray(tiles);
    }

    private int[][] copyArray(int[][] array) {
        int len = array[0].length;
        int[][] newArray = new int[len][len];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                newArray[i][j] = array[i][j];
            }
        }
        return newArray;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.n + "\n");
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.board[i][j] != i * this.n + (j + 1) &&
                        this.board[i][j] != 0) {
                    hamming += 1;
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.board[i][j] != i * this.n + (j + 1) &&
                        this.board[i][j] != 0) {
                    int correctX = (this.board[i][j] - 1) % this.n;
                    int correctY = (this.board[i][j] - 1) / this.n;
                    manhattan += Math.abs(i - correctY) + Math.abs(j - correctX);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return (this.hamming() == 0);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return Arrays.deepEquals(this.board, that.board);
    }

    private void exchange(int[][] board, int x, int y, int xx, int yy) {
        int aux = board[y][x];
        board[y][x] = board[yy][xx];
        board[yy][xx] = aux;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<Board>();
        int zeroX = 0, zeroY = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.board[i][j] == 0) {
                    zeroX = j;
                    zeroY = i;
                }
            }
        }
        if (zeroX != 0) {
            int[][] newArray = copyArray(this.board);
            exchange(newArray, zeroX, zeroY, zeroX - 1, zeroY);
            neighbors.push(new Board(newArray));
        }
        if (zeroX != this.n - 1) {
            int[][] newArray = copyArray(this.board);
            exchange(newArray, zeroX, zeroY, zeroX + 1, zeroY);
            neighbors.push(new Board(newArray));
        }
        if (zeroY != 0) {
            int[][] newArray = copyArray(this.board);
            exchange(newArray, zeroX, zeroY, zeroX, zeroY - 1);
            neighbors.push(new Board(newArray));
        }
        if (zeroY != this.n - 1) {
            int[][] newArray = copyArray(this.board);
            exchange(newArray, zeroX, zeroY, zeroX, zeroY + 1);
            neighbors.push(new Board(newArray));
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int firstX = 0, firstY = 0, secondX = 0, secondY = 0;
        boolean firstFound = false, secondFound = false;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (!firstFound && this.board[i][j] != 0) {
                    firstX = j;
                    firstY = i;
                    firstFound = true;
                }
                if (!secondFound && firstFound && this.board[i][j] != 0 &&
                        this.board[i][j] != this.board[firstY][firstX]) {
                    secondX = j;
                    secondY = i;
                    secondFound = true;
                }
                if (firstFound && secondFound) {
                    break;
                }
            }
            if (firstFound && secondFound) {
                break;
            }
        }
        int[][] newArray = copyArray(this.board);
        exchange(newArray, firstX, firstY, secondX, secondX);
        return new Board(newArray);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In();
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        System.out.println("---- Check functions ----");
        System.out.println("---- Check toString ----");
        System.out.println(initial.toString());
        System.out.println("---- Check isGoal ----");
        System.out.println(initial.isGoal());
        System.out.println("---- Check hamming ----");
        System.out.println(initial.hamming());
        System.out.println("---- Check manhattan ----");
        System.out.println(initial.manhattan());

        System.out.println("---- Check neighbours ----");
        Iterable<Board> neighbors = initial.neighbors();
        for (Board board : neighbors) {
            System.out.println(board.toString());
        }

        System.out.println("---- Check twin ----");
        System.out.println(initial.twin().toString());
    }
}
