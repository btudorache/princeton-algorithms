import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private int numSegments;
    private LineSegment[] segm;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (i != j && points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
        this.numSegments = 0;
        this.segm = new LineSegment[100000];
        if (points.length < 4) {
            return;
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        if (Double.compare(points[i].slopeTo(points[j]), points[i].slopeTo(points[k])) == 0 &&
                            Double.compare(points[i].slopeTo(points[k]), points[i].slopeTo(points[l])) == 0) {

                            Point[] fourPoints = { points[i], points[j], points[k], points[l] };
                            Arrays.sort(fourPoints, 0, 4);
                            this.segm[this.numSegments++] = new LineSegment(fourPoints[0],
                                                                            fourPoints[3]);
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return this.numSegments;
    }

    public LineSegment[] segments() {
        LineSegment[] finalSegm = new LineSegment[this.numSegments];
        for (int i = 0; i < this.numSegments; i++) {
            finalSegm[i] = this.segm[i];
        }
        return finalSegm;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
