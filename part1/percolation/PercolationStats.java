import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] trials;
    private int numTrials;
    private double CONFIDENCE_95 = 1.96;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.numTrials = trials;
        this.trials = new double[trials];
        int x, y;
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                while (true) {
                    x = StdRandom.uniform(1, n + 1);
                    y = StdRandom.uniform(1, n + 1);
                    if (!perc.isOpen(x, y)) {
                        perc.open(x, y);
                        break;
                    }
                }
            }
            this.trials[i] = Double.valueOf(perc.numberOfOpenSites()) / Double.valueOf(n * n);
        }
    }

    public double mean() {
        return StdStats.mean(trials);
    }

    public double stddev() {
        return StdStats.stddev(trials);
    }

    public double confidenceLo() {
        return (this.mean() - CONFIDENCE_95 * (this.stddev() / Math.sqrt(this.numTrials)));
    }

    public double confidenceHi() {
        return (this.mean() + CONFIDENCE_95 * (this.stddev() / Math.sqrt(this.numTrials)));
    }

    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]),
                                                      Integer.parseInt(args[1]));
        System.out.println("mean \t\t\t= " + stats.mean());
        System.out.println("stddev \t\t\t= " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
