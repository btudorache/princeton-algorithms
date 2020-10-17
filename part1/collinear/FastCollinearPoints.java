import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private int numSegments;
    private LineSegment[] segm;

    public FastCollinearPoints(Point[] points) {
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
        this.segm = new LineSegment[1000000];
        if (points.length < 4) {
            return;
        }
        for (int i = 0; i < points.length; i++) {
            Point[] copyArray = new Point[points.length - 1];
            int ind = 0;
            for (int j = 0; j < points.length; j++) {
                if (points[i].compareTo(points[j]) != 0) {
                    copyArray[ind++] = points[j];
                }
            }
            Arrays.sort(copyArray, 0, copyArray.length, points[i].slopeOrder());
            int start = 0;
            int cnt = 0;
            for (int j = 0; j < copyArray.length; j++) {
                if (Double.compare(points[i].slopeTo(copyArray[start]),
                                   points[i].slopeTo(copyArray[j])) != 0 && cnt >= 3) {
                    Arrays.sort(copyArray, start, j);
                    if (points[i].compareTo(copyArray[start]) < 0) {
                        this.segm[this.numSegments++] = new LineSegment(points[i],
                                                                        copyArray[j - 1]);
                    }
                    start = j;
                    cnt = 1;
                }
                else if (Double.compare(points[i].slopeTo(copyArray[start]),
                                        points[i].slopeTo(copyArray[j])) != 0) {
                    start = j;
                    cnt = 1;
                }
                else if (Double.compare(points[i].slopeTo(copyArray[start]),
                                        points[i].slopeTo(copyArray[j])) == 0) {
                    if (cnt >= 3 && j == copyArray.length - 1) {
                        Arrays.sort(copyArray, start, j + 1);
                        if (points[i].compareTo(copyArray[start]) < 0) {
                            this.segm[this.numSegments++] = new LineSegment(points[i],
                                                                            copyArray[j]);
                        }
                    }
                    else {
                        cnt++;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
