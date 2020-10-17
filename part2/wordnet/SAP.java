import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph graph;

    public SAP(Digraph G) {
        this.graph = new Digraph(G);
    }


    public int length(int v, int w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.graph, w);

        int min = Integer.MAX_VALUE;
        for (int i = 0; i < this.graph.V(); i++) {
            if ((bfsV.distTo(i) != Integer.MAX_VALUE && bfsW.distTo(i) != Integer.MAX_VALUE)
                    && (bfsV.distTo(i) + bfsW.distTo(i)) < min) {
                min = bfsV.distTo(i) + bfsW.distTo(i);
            }
        }
        if (min == Integer.MAX_VALUE) {
            return -1;
        }
        else {
            return min;
        }
    }

    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.graph, w);

        int min = Integer.MAX_VALUE;
        int ind = -1;
        for (int i = 0; i < this.graph.V(); i++) {
            if ((bfsV.distTo(i) != Integer.MAX_VALUE && bfsW.distTo(i) != Integer.MAX_VALUE)
                    && (bfsV.distTo(i) + bfsW.distTo(i)) < min) {
                min = bfsV.distTo(i) + bfsW.distTo(i);
                ind = i;
            }
        }
        return ind;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        int counter = 0;
        for (Integer num : v) {
            if (num == null) {
                throw new IllegalArgumentException();
            }
            counter++;
        }
        if (counter == 0) {
            return -1;
        }

        counter = 0;
        for (Integer num : w) {
            if (num == null) {
                throw new IllegalArgumentException();
            }
            counter++;
        }
        if (counter == 0) {
            return -1;
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.graph, w);

        int min = Integer.MAX_VALUE;
        for (int i = 0; i < this.graph.V(); i++) {
            if ((bfsV.distTo(i) != Integer.MAX_VALUE && bfsW.distTo(i) != Integer.MAX_VALUE)
                    && (bfsV.distTo(i) + bfsW.distTo(i)) < min) {
                min = bfsV.distTo(i) + bfsW.distTo(i);
            }
        }
        if (min == Integer.MAX_VALUE) {
            return -1;
        }
        else {
            return min;
        }
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        int counter = 0;
        for (Integer num : v) {
            if (num == null) {
                throw new IllegalArgumentException();
            }
            counter++;
        }
        if (counter == 0) {
            return -1;
        }

        counter = 0;
        for (Integer num : w) {
            if (num == null) {
                throw new IllegalArgumentException();
            }
            counter++;
        }
        if (counter == 0) {
            return -1;
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.graph, w);

        int min = Integer.MAX_VALUE;
        int ind = -1;
        for (int i = 0; i < this.graph.V(); i++) {
            if ((bfsV.distTo(i) != Integer.MAX_VALUE && bfsW.distTo(i) != Integer.MAX_VALUE)
                    && (bfsV.distTo(i) + bfsW.distTo(i)) < min) {
                min = bfsV.distTo(i) + bfsW.distTo(i);
                ind = i;
            }
        }
        return ind;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        Stack<Integer> s1 = new Stack<Integer>();
        s1.push(13);
        s1.push(23);
        s1.push(24);
        Stack<Integer> s2 = new Stack<Integer>();
        s2.push(6);
        s2.push(16);
        s2.push(17);
        StdOut.printf("iterable length = %d, iterable ancestor = %d\n", sap.length(s1, s2),
                      sap.ancestor(s1, s2));
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
