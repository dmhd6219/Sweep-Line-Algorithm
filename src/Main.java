import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) throws IOException {
        AVLTree<Segment> tree = new AVLTree<>();

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bf.readLine());

        ArrayList<Point> points = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int[] nums = Arrays.stream(bf.readLine().split(" ")).mapToInt(x -> Integer.parseInt(x)).toArray();
            Point p = new Point(nums[0], nums[1], true);
            Point q = new Point(nums[2], nums[3]);

            Segment s = new Segment(p, q);
            p.setSegment(s);
            q.setSegment(s);

            points.add(p);
            points.add(q);

        }


        points.sort(new PointComparator());

        for (Point point : points) {
            if (point.isLeft()) {
                tree.insert(point.getSegment());

                if (Segment.intersect(point.getSegment(), tree.getPredecessor(point.getSegment()))) {
                    System.out.println("INTERSECTION");
                    System.out.printf("%s %s\n", point.getSegment(), tree.getPredecessor(point.getSegment()));
                    System.exit(0);
                }
                if (Segment.intersect(point.getSegment(), tree.getSuccessor(point.getSegment()))) {
                    System.out.println("INTERSECTION");
                    System.out.printf("%s %s\n", point.getSegment(), tree.getSuccessor(point.getSegment()));
                    System.exit(0);
                }
            } else {
                if (Segment.intersect(tree.getPredecessor(point.getSegment()),
                        tree.getSuccessor(point.getSegment()))) {
                    System.out.println("INTERSECTION");
                    System.out.printf("%s %s\n", point.getSegment(), tree.getSuccessor(point.getSegment()));
                    System.exit(0);
                }
                tree.delete(tree.root, point.getSegment());
            }
        }

        System.out.println("NO INTERSECTIONS");

    }
}

class PointComparator implements Comparator<Point> {

    @Override
    public int compare(Point o1, Point o2) {
        return o1.getX() > o2.getX() ? -1 : o1.getX() == o2.getX() ? 0 : 1;
    }
}

class Point {
    private int x;
    private int y;
    private boolean left = false;

    private Segment segment;

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Point(int x, int y, boolean left) {
        this.x = x;
        this.y = y;
        this.left = left;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        return String.format("%d %d", x, y);
    }

    public boolean isLeft() {
        return left;
    }
}

class Segment implements Comparable<Segment> {
    private Point p;
    private Point q;

    Segment(Point p, Point q) {
        this.p = p;
        this.q = q;
    }

    public Point getP() {
        return p;
    }

    public Point getQ() {
        return q;
    }

    public static Orientations getOrientation(Point p, Point q, Point r) {
        int orientation = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (orientation == 0) return Orientations.COLLINEAR;

        return (orientation > 0) ? Orientations.CLOCKWISE : Orientations.COUNTERCLOCKWISE;
    }

    public static boolean intersect(Segment s1, Segment s2) {
        System.out.println("a");
        if (s1 == null || s2 == null) {
            return false;
        }

        Orientations o1 = getOrientation(s1.getP(), s1.getQ(), s2.getP());
        Orientations o2 = getOrientation(s1.getP(), s1.getQ(), s2.getQ());
        Orientations o3 = getOrientation(s2.getP(), s2.getQ(), s1.getP());
        Orientations o4 = getOrientation(s2.getP(), s2.getQ(), s1.getQ());

        if (o1 == Orientations.COLLINEAR || o2 == Orientations.COLLINEAR || o3 == Orientations.COLLINEAR ||
                o4 == Orientations.COLLINEAR) {
            return false;
        }

        if (o1 != o2 && o3 != o4) {
            return true;
        }

        int denominator = ((s1.getP().getX() - s1.getQ().getX()) * (s2.getP().getY() - s2.getQ().getY()) -
                (s1.getP().getY() - s1.getQ().getY()) * (s2.getP().getX() - s2.getQ().getX()));


        int x = ((s1.getP().getX() * s1.getQ().getY() - s1.getP().getY() * s1.getQ().getX()) * (s2.getP().getX() -
                s2.getQ().getX()) - (s1.getP().getX() - s1.getQ().getX()) * (s2.getP().getX() * s2.getQ().getY() -
                s2.getP().getY() * s2.getQ().getX())) / denominator;
        int y = ((s1.getP().getX() * s1.getQ().getY() - s1.getP().getY() * s1.getQ().getX()) * (s2.getP().getY() -
                s2.getQ().getY()) - (s1.getP().getY() - s1.getQ().getY()) * (s2.getP().getX() * s2.getQ().getY() -
                s2.getP().getY() * s2.getQ().getX())) / denominator;

        Point p = new Point(x, y);

        if (!onSegment(s1, p) || !onSegment(s2, p)) {
            return false;
        }

        return false;
    }

    public static boolean onSegment(Segment s, Point r) {
        if (s.getQ().getX() <= Math.max(s.getP().getX(), r.getX()) && s.getQ().getX() >= Math.min(s.getP().getX(), r.getX()) &&
                s.getQ().getY() <= Math.max(s.getP().getY(), r.getY()) && s.getQ().getY() >= Math.min(s.getP().getY(), r.getY()))
            return true;

        return false;
    }

    @Override
    public int compareTo(Segment other) {
        return (int) (p.getX() - other.p.getX());
    }

    @Override
    public String toString(){
        return String.format("%s %s", p, q);
    }
}

class Node<T extends Comparable<T>> {
    T data;
    Node<T> left;
    Node<T> right;

    int height;

    public Node(T data) {
        this.data = data;
    }
}

class AVLTree<T extends Comparable<T>> {
    Node<T> root;

    private int height(Node<T> node) {
        if (node == null) {
            return 0;
        }
        if (node.left == null && node.right == null) {
            return 1;
        } else if (node.left == null) {
            return height(node.right) + 1;
        } else if (node.right == null) {
            return height(node.left) + 1;
        }

        return Math.max(height(node.right) + 1, height(node.left) + 1);
    }


    private int balanceFactor(Node node) {
        return height(node.right) - height(node.left);
    }

    private Node<T> rotateRight(Node<T> node) {
        Node<T> leftChild = node.left;

        node.left = leftChild.right;
        leftChild.right = node;

        return leftChild;
    }

    private Node<T> rotateLeft(Node<T> node) {
        Node<T> rightChild = node.right;

        node.right = rightChild.left;
        rightChild.left = node;

        return rightChild;
    }


    private Node<T> rebalance(Node<T> node) {
        int balanceFactor = balanceFactor(node);

        if (balanceFactor < -1) {
            if (balanceFactor(node.left) <= 0) {
            } else {
                node.left = rotateLeft(node.left);
            }
            node = rotateRight(node);
        }

        if (balanceFactor > 1) {
            if (balanceFactor(node.right) < 0) {
                node.right = rotateRight(node.right);
            }
            node = rotateLeft(node);
        }

        return node;
    }

    Node insert(Node<T> rootNode, T value) {
        if (rootNode == null) {
            rootNode = new Node<>(value);
            return rootNode;
        }

        if (value.compareTo(rootNode.data) < 0) {
            rootNode.left = insert(rootNode.left, value);
        } else {
            rootNode.right = insert(rootNode.right, value);
        }


        rootNode = rebalance(rootNode);

        return rootNode;
    }

    Node insert(T value) {
        Node<T> rootNode = this.root;
        if (rootNode == null) {
            rootNode = new Node<>(value);
            this.root = rootNode;
            return rootNode;
        }

        if (value.compareTo(rootNode.data) < 0) {
            rootNode.left = insert(rootNode.left, value);
        } else {
            rootNode.right = insert(rootNode.right, value);
        }


        this.root = rebalance(rootNode);

        return rootNode;
    }

    Node delete(Node<T> rootNode, T value) {
        if (rootNode == null) {
            return null;
        }
        if (rootNode.data.compareTo(value) > 0) {
            rootNode.left = delete(rootNode.left, value);
        } else if (rootNode.data.compareTo(value) < 0) {
            rootNode.right = delete(rootNode.right, value);
        } else {
            if (rootNode.left == null || rootNode.right == null) {
                rootNode = (rootNode.left == null) ? rootNode.right : rootNode.left;
            } else {
                Node<T> mostLeftChild = findMin(rootNode.right);
                rootNode.data = mostLeftChild.data;
                rootNode.right = delete(rootNode.right, rootNode.data);
            }
        }
        if (rootNode != null) {
            rootNode = rebalance(rootNode);
        }
        return rootNode;
    }

    public T getSuccessor(T value) {
        Node<T> node = root;
        Node<T> successor = null;

        while (node != null) {
            if (node.data.compareTo(value) > 0) {
                successor = node;
                node = node.left;

            } else if (node.data.compareTo(value) < 0) {
                node = node.right;

            } else {
                if (node.right != null) {
                    successor = findMin(node.right);
                }
            }
        }

        return successor == null ? null : successor.data;
    }

    public T getPredecessor(T elem) {
        Node<T> node = root;
        Node<T> predecessor = null;

        while (node != null) {
            if (elem.compareTo(node.data) <= 0) {
                node = node.left;
            } else {
                predecessor = node;
                node = node.right;
            }
        }
        return predecessor == null ? null : predecessor.data;

    }

    private Node<T> findMin(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

}

enum Orientations {
    COLLINEAR, CLOCKWISE, COUNTERCLOCKWISE;
}