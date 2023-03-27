import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;


public class Main {
    public static void main(String[] args) throws IOException {
        RedBlackTree<Segment> tree = new RedBlackTree<>();

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bf.readLine());

        Point[] points = new Point[n * 2];

        for (int i = 0; i < n * 2; i += 2) {
            long[] nums = Arrays.stream(bf.readLine().split(" ")).mapToLong(x -> Long.parseLong(x)).toArray();
            Point p = new Point(nums[0], nums[1], true);
            Point q = new Point(nums[2], nums[3]);

            Segment s = new Segment(p, q);
            p.setSegment(s);
            q.setSegment(s);

            points[i] = p;
            points[i + 1] = q;

        }

        quickSort(points, 0, n * 2 - 1);


        for (Point point : points) {
            if (point.isLeft()) {
                tree.insert(point.getSegment());

                if (Segment.intersect(point.getSegment(), tree.getPredecessor(point.getSegment()))) {
                    System.out.println("INTERSECTION");
                    System.out.printf("%s\n%s\n", point.getSegment(), tree.getPredecessor(point.getSegment()));
                    System.exit(0);
                }
                if (Segment.intersect(
                        point.getSegment(),
                        tree.getSuccessor(
                                point.getSegment()))) {
                    System.out.println("INTERSECTION");
                    System.out.printf("%s\n%s\n", point.getSegment(), tree.getSuccessor(point.getSegment()));
                    System.exit(0);
                }
            } else {
                if (Segment.intersect(tree.getPredecessor(point.getSegment()),
                        tree.getSuccessor(point.getSegment()))) {
                    System.out.println("INTERSECTION");
                    System.out.printf("%s\n%s\n", tree.getPredecessor(point.getSegment()), tree.getSuccessor(point.getSegment()));
                    System.exit(0);
                }
                tree.delete(point.getSegment());
            }
        }

        System.out.println("NO INTERSECTIONS");

    }

    public static void quickSort(Point arr[], int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    private static int partition(Point arr[], int begin, int end) {
        Point pivot = arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (arr[j].compareTo(pivot) > 0) {
                i++;

                Point swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }

        Point swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

}


class Point implements Comparable<Point> {
    private long x;
    private long y;
    private boolean left = false;

    private Segment segment;

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

    Point(long x, long y, boolean left) {
        this.x = x;
        this.y = y;
        this.left = left;
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public String toString() {
        return String.format("%d %d", x, y);
    }

    public boolean isLeft() {
        return left;
    }

    @Override
    public int compareTo(Point o) {
        return getX() > o.getX() ? 1 : getX() == o.getX() ? 0 : -1;
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
        long orientation = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (orientation == 0) return Orientations.COLLINEAR;

        return (orientation > 0) ? Orientations.CLOCKWISE : Orientations.COUNTERCLOCKWISE;
    }

    public static boolean intersect(Segment s1, Segment s2) {
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

        long denominator = ((s1.getP().getX() - s1.getQ().getX()) * (s2.getP().getY() - s2.getQ().getY()) -
                (s1.getP().getY() - s1.getQ().getY()) * (s2.getP().getX() - s2.getQ().getX()));

        if (denominator == 0) {
            return false;
        }


        long x = ((s1.getP().getX() * s1.getQ().getY() - s1.getP().getY() * s1.getQ().getX()) * (s2.getP().getX() -
                s2.getQ().getX()) - (s1.getP().getX() - s1.getQ().getX()) * (s2.getP().getX() * s2.getQ().getY() -
                s2.getP().getY() * s2.getQ().getX())) / denominator;
        long y = ((s1.getP().getX() * s1.getQ().getY() - s1.getP().getY() * s1.getQ().getX()) * (s2.getP().getY() -
                s2.getQ().getY()) - (s1.getP().getY() - s1.getQ().getY()) * (s2.getP().getX() * s2.getQ().getY() -
                s2.getP().getY() * s2.getQ().getX())) / denominator;

        Point p = new Point(x, y);

        if (!onSegment(s1, p) || !onSegment(s2, p)) {
            return false;
        }

        return false;
    }

    public static boolean onSegment(Segment s, Point r) {
        if (s.getQ().getX() <= Math.max(s.getP().getX(), r.getX()) && s.getQ().getX() >=
                Math.min(s.getP().getX(), r.getX()) && s.getQ().getY() <=
                Math.max(s.getP().getY(), r.getY()) && s.getQ().getY() >= Math.min(s.getP().getY(), r.getY()))
            return true;

        return false;
    }

    @Override
    public int compareTo(Segment other) {
        return (int) (p.getX() - other.p.getX());
    }

    @Override
    public String toString() {
        return String.format("%s %s", p, q);
    }
}

enum Orientations {
    COLLINEAR, CLOCKWISE, COUNTERCLOCKWISE;
}

enum Colors {
    RED, BLACK;

    public static Colors getOpposite(Colors color) {
        return color == RED ? BLACK : RED;
    }


}

class Node<T extends Comparable<T>> {
    T value;
    Node left, right;
    Colors color;

    public Node(T value) {
        this.value = value;
        this.color = Colors.RED;
    }
}

class RedBlackTree<T extends Comparable<T>> {
    private Node<T> root;


    public void insert(T value) {
        root = insert(root, value);
        root.color = Colors.BLACK;
    }

    private Node<T> insert(Node<T> node, T value) {
        if (node == null) {
            return new Node(value);
        }
        if (value.compareTo(node.value) < 0) {
            node.left = insert(node.left, value);
        } else if (value.compareTo(node.value) > 0) {
            node.right = insert(node.right, value);
        } else {
            node.value = value;
        }

        if (isRed(node.right) && !isRed(node.left)) {
            return rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            return rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
            return node;
        }

        return node;
    }

    private boolean isRed(Node node) {
        if (node == null) {
            return false;
        }
        return node.color == Colors.RED;
    }

    private Node<T> rotateLeft(Node node) {
        Node x = node.right;
        node.right = x.left;
        x.left = node;
        x.color = node.color;
        node.color = Colors.RED;
        return x;
    }

    private Node<T> rotateRight(Node node) {
        Node x = node.left;
        node.left = x.right;
        x.right = node;
        x.color = node.color;
        node.color = Colors.RED;
        return x;
    }

    private Node<T> flipColors(Node node) {
        node.color = Colors.getOpposite(node.color);
        node.left.color = Colors.getOpposite(node.left.color);
        node.right.color = Colors.getOpposite(node.right.color);
        return node;
    }

    public void delete(T value) {
        if (root == null) {
            return;
        }

        root = delete(root, value);
        if (root != null) {
            root.color = Colors.BLACK;
        }
    }

    private Node<T> delete(Node<T> node, T value) {
        if (value.compareTo(node.value) < 0) {
            if (node.left != null) {
                if (!isRed(node.left) && !isRed(node.left.left)) {
                    node = moveRedLeft(node);
                }
                node.left = delete(node.left, value);
            }
        } else {
            if (isRed(node.left)) {
                node = rotateRight(node);
            }
            if (value.compareTo(node.value) == 0 && node.right == null) {
                return null;
            }
            if (node.right != null) {
                if (!isRed(node.right) && !isRed(node.right.left)) {
                    node = moveRedRight(node);
                }
                if (value.compareTo(node.value) == 0) {
                    Node<T> minNode = findMin(node.right);
                    node.value = minNode.value;
                    node.right = deleteMin(node.right);
                } else {
                    node.right = delete(node.right, value);
                }
            }
        }
        return fixUp(node);
    }

    private Node<T> moveRedLeft(Node node) {
        flipColors(node);
        if (isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private Node<T> deleteMin(Node node) {
        if (node.left == null) {
            return null;
        }
        if (!isRed(node.left) && !isRed(node.left.left)) {
            node = moveRedLeft(node);
        }
        node.left = deleteMin(node.left);
        return fixUp(node);
    }

    private Node<T> moveRedRight(Node node) {
        flipColors(node);
        if (isRed(node.left.left)) {
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    private Node<T> findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node<T> fixUp(Node node) {
        if (isRed(node.right)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }
        return node;
    }

    public T getPredecessor(T elem) {
        Node<T> x = root;
        Node<T> getPredecessor = null;
        while (x != null) {
            if (elem.compareTo(x.value) <= 0) {
                x = x.left;
            } else {
                getPredecessor = x;
                x = x.right;
            }
        }
        if (getPredecessor == null) {
            return null;
        } else {
            return getPredecessor.value;
        }
    }

    public T getSuccessor(T elem) {
        Node<T> Node = root;
        Node<T> getSuccessor = null;
        while (Node != null) {
            if (Node.value.compareTo(elem) > 0) {
                getSuccessor = Node;
                Node = Node.left;
            } else if (Node.value.compareTo(elem) < 0) {
                Node = Node.right;
            } else {
                if (Node.right != null) {
                    getSuccessor = findMin(Node.right);
                }
                break;
            }
        }
        return getSuccessor == null ? null : getSuccessor.value;
    }
}