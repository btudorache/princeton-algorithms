/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int gridSize;
    private boolean[][] grid;
    private WeightedQuickUnionUF mainUnionFind;
    private WeightedQuickUnionUF backwashUnionFind;
    private int openSites;

    private int topCell;
    private int botCell;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.openSites = 0;
        this.gridSize = n;
        this.grid = new boolean[n + 1][n + 1];
        this.mainUnionFind = new WeightedQuickUnionUF(n * n + 1);
        this.backwashUnionFind = new WeightedQuickUnionUF(n * n + 2);
        this.topCell = n * n;
        this.botCell = n * n + 1;

        for (int i = 0; i < n; i++) {
            this.backwashUnionFind.union(i, this.topCell);
            this.mainUnionFind.union(i, this.topCell);
            this.backwashUnionFind.union(n * n - 1 - i, this.botCell);
        }
    }

    private void openSites(int thisCell, int thatCell) {
        this.mainUnionFind.union(thisCell, thatCell);
        this.backwashUnionFind.union(thisCell, thatCell);
    }

    public void open(int row, int col) {
        if (row < 1 || row > this.gridSize || col < 1 || col > this.gridSize) {
            throw new IllegalArgumentException();
        }

        if (this.isOpen(row, col)) {
            return;
        }

        int unionFindPos = this.gridSize * (row - 1) + (col - 1);
        this.grid[row][col] = true;
        this.openSites += 1;


        if ((row - 1) > 0 && this.grid[row - 1][col]) {
            this.openSites(unionFindPos, unionFindPos - this.gridSize);
        }
        if ((row + 1) <= this.gridSize && this.grid[row + 1][col]) {
            this.openSites(unionFindPos, unionFindPos + this.gridSize);
        }
        if ((col - 1) > 0 && this.grid[row][col - 1]) {
            this.openSites(unionFindPos, unionFindPos - 1);
        }
        if ((col + 1) <= this.gridSize && this.grid[row][col + 1]) {
            this.openSites(unionFindPos, unionFindPos + 1);
        }
    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > this.gridSize || col < 1 || col > this.gridSize) {
            throw new IllegalArgumentException();
        }

        return this.grid[row][col];
    }

    public boolean isFull(int row, int col) {
        if (row < 1 || row > this.gridSize || col < 1 || col > this.gridSize) {
            throw new IllegalArgumentException();
        }

        int unionFindPos = this.gridSize * (row - 1) + (col - 1);
        return (this.isOpen(row, col) &&
                (this.mainUnionFind.find(this.topCell) ==
                        this.mainUnionFind.find(unionFindPos)));
    }

    public int numberOfOpenSites() {
        return this.openSites;
    }

    public boolean percolates() {
        if (this.gridSize == 1) {
            return this.isOpen(1, 1);
        }
        else {
            return (this.backwashUnionFind.find(this.topCell) ==
                    this.backwashUnionFind.find(this.botCell));
        }
    }
}
