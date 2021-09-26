import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }

        ArrayList<Point> copyPoints = new ArrayList<>();
        Collections.addAll(copyPoints, points);
        Collections.sort(copyPoints);

        for (int i = 1; i < points.length; i++) {
            if (copyPoints.get(i).compareTo(copyPoints.get(i - 1)) == 0) {
                throw new IllegalArgumentException();
            }
        }

        this.segments = new ArrayList<>();

        for (int i = 0; i < points.length; i++) {
            Point currPoint = points[i];
            copyPoints.sort(currPoint.slopeOrder());

            List<Point> linePoints = new ArrayList<>();
            linePoints.add(copyPoints.get(0));
            double slope = currPoint.slopeTo(copyPoints.get(0));

            for (int j = 1; j < copyPoints.size(); j++) {
                Point point = copyPoints.get(j);
                double currSlope = currPoint.slopeTo(point);
                if (Double.compare(slope, currSlope) == 0) {
                    linePoints.add(copyPoints.get(j));
                } else  {
                    if (linePoints.size() >= 3) {
                        linePoints.add(currPoint);
                        Collections.sort(linePoints);
                        if (currPoint == linePoints.get(0)) {
                            this.segments.add(new LineSegment(linePoints.get(0), linePoints.get(linePoints.size() - 1)));
                        }
                    }
                    slope = currSlope;
                    linePoints.clear();
                    linePoints.add(point);
                }
            }

            if (linePoints.size() >= 3) {
                linePoints.add(currPoint);
                Collections.sort(linePoints);
                if (currPoint == linePoints.get(0)) {
                    this.segments.add(new LineSegment(linePoints.get(0), linePoints.get(linePoints.size() - 1)));
                }
            }
        }
    }

    public int numberOfSegments() {
        return this.segments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] finalSegm = new LineSegment[numberOfSegments()];
        for (int i = 0; i < numberOfSegments(); i++) {
            finalSegm[i] = this.segments.get(i);
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
        StdOut.println(collinear.numberOfSegments());
        StdDraw.show();
    }
}