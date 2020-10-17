/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;
    private Stack<Node> iterable;

    private class Node {
        private Point2D key;
        private RectHV rect;
        private Node left;
        private Node right;
        private boolean isVertical;

        public Node(Point2D key, boolean isVertical, Node parent) {
            this.key = key;
            this.isVertical = isVertical;

            int isLeftNode;
            if (parent == null) {
                isLeftNode = 0;
            }
            else {
                if (this.isVertical) {
                    if (this.key.y() - parent.key.y() < 0) {
                        isLeftNode = 1;
                    }
                    else {
                        isLeftNode = -1;
                    }
                }
                else {
                    if (this.key.x() - parent.key.x() < 0) {
                        isLeftNode = 1;
                    }
                    else {
                        isLeftNode = -1;
                    }
                }
            }

            if (parent == null) {
                this.rect = new RectHV(0, 0, 1, 1);
            }
            else {
                if (this.isVertical) {
                    if (isLeftNode > 0) {
                        this.rect = new RectHV(parent.rect.xmin(),
                                               parent.rect.ymin(),
                                               parent.rect.xmax(),
                                               parent.key.y());
                    }
                    else {
                        this.rect = new RectHV(parent.rect.xmin(),
                                               parent.key.y(),
                                               parent.rect.xmax(),
                                               parent.rect.ymax());
                    }
                }
                else {
                    if (isLeftNode > 0) {
                        this.rect = new RectHV(parent.rect.xmin(),
                                               parent.rect.ymin(),
                                               parent.key.x(),
                                               parent.rect.ymax());
                    }
                    else {
                        this.rect = new RectHV(parent.key.x(),
                                               parent.rect.ymin(),
                                               parent.rect.xmax(),
                                               parent.rect.ymax());
                    }
                }
            }
        }
    }

    public KdTree() {
        this.size = 0;
        this.iterable = new Stack<Node>();
    }

    private void put(Point2D key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            this.root = new Node(key, true, null);
            this.size++;
            this.iterable.push(this.root);
        }
        else {
            put(root, root, key);
        }
    }

    private boolean get(Point2D key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        return get(this.root, key);
    }

    private boolean get(Node x, Point2D key) {
        if (x == null) {
            return false;
        }
        if (x.key.compareTo(key) == 0) {
            return true;
        }
        double cmp;
        if (x.isVertical) {
            cmp = key.x() - x.key.x();
        }
        else {
            cmp = key.y() - x.key.y();
        }
        if (cmp < 0) {
            return get(x.left, key);
        }
        else {
            return get(x.right, key);
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return get(p);
    }

    private Node put(Node x, Node parent, Point2D key) {
        if (x == null) {
            this.size++;
            Node newNode = new Node(key, !parent.isVertical, parent);
            this.iterable.push(newNode);
            return newNode;
        }
        double cmp;
        if (x.isVertical) {
            cmp = key.x() - x.key.x();
        }
        else {
            cmp = key.y() - x.key.y();
        }
        if (cmp < 0) {
            x.left = put(x.left, x, key);
        }
        else {
            x.right = put(x.right, x, key);
        }
        return x;
    }

    public boolean isEmpty() {
        return (this.size == 0);
    }

    public int size() {
        return this.size;
    }

    public void insert(Point2D p) {
        if (this.contains(p)) {
            return;
        }
        put(p);
    }

    public void draw() {
        for (Node node : this.iterable) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.key.draw();

            StdDraw.setPenRadius();
            if (node.isVertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.key.x(),
                             node.rect.ymin(),
                             node.key.x(),
                             node.rect.ymax());

            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(),
                             node.key.y(),
                             node.rect.xmax(),
                             node.key.y());
            }
        }
    }

    private void src(Node x, RectHV rect, Stack<Point2D> points) {
        if (x == null) {
            return;
        }

        if (x.left != null && x.left.rect.intersects(rect)) {
            src(x.left, rect, points);
        }
        if (x.right != null && x.right.rect.intersects(rect)) {
            src(x.right, rect, points);
        }

        if (rect.contains(x.key)) {
            points.push(x.key);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Stack<Point2D> points = new Stack<Point2D>();
        src(this.root, rect, points);
        return points;
    }

    private void nearestSearch(Node x, Point2D p, Stack<Point2D> minPoint) {
        if (x == null) {
            return;
        }

        Point2D min = minPoint.peek();
        if (x.key.distanceTo(p) < min.distanceTo(p)) {
            min = x.key;
            minPoint.pop();
            minPoint.push(x.key);
        }

        if (x.left != null && min.distanceTo(p) > x.left.rect.distanceTo(p)) {
            if (x.right != null && min.distanceTo(p) > x.right.rect.distanceTo(p)) {
                if (x.left.rect.contains(p)) {
                    nearestSearch(x.left, p, minPoint);
                    nearestSearch(x.right, p, minPoint);
                }
                else {
                    nearestSearch(x.right, p, minPoint);
                    nearestSearch(x.left, p, minPoint);
                }
            }
            else {
                nearestSearch(x.left, p, minPoint);
            }
        }
        else if (x.right != null && min.distanceTo(p) > x.right.rect.distanceTo(p)) {
            nearestSearch(x.right, p, minPoint);
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null || this.root == null) {
            throw new IllegalArgumentException();
        }
        Point2D min = this.root.key;
        Stack<Point2D> minPoint = new Stack<Point2D>();
        minPoint.push(min);
        nearestSearch(this.root, p, minPoint);
        return minPoint.pop();
    }

    public static void main(String[] args) {
        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        kdtree.draw();
        System.out.println(kdtree.contains(new Point2D(0.5, 0.4)));
        System.out.println(kdtree.contains(new Point2D(0.9, 0.9)));
        System.out.println(kdtree.size);
    }
}
