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
            long[] nums = Arrays.stream(bf.readLine().split(" ")).mapToLong(Long::parseLong).toArray();
            Point p = new Point(nums[0], nums[1], true);
            Point q = new Point(nums[2], nums[3]);

            Segment s = new Segment(p, q);
            p.setSegment(s);
            q.setSegment(s);

            points[i] = p;
            points[i + 1] = q;

        }

        Sort<Point> sort = new Sort<>();
        sort.quickSort(points, 0, 2 * n - 1);


        for (Point point : points) {
            Segment segment = point.getSegment();
            Segment predecessor = tree.getPredecessor(segment);
            Segment successor = tree.getSuccessor(segment);

            if (point.isLeft()) {
                tree.insert(segment);

                if (Segment.areIntersect(segment, predecessor)) {
                    System.out.printf("INTERSECTION\n%s\n%s\n", segment, predecessor);
                    return;
                }
                if (Segment.areIntersect(segment, successor)) {
                    System.out.printf("INTERSECTION\n%s\n%s\n", segment, predecessor);
                    return;
                }
            }

            if (!point.isLeft()) {
                if (Segment.areIntersect(predecessor, successor)) {
                    System.out.printf("INTERSECTION\n%s\n%s\n", segment, predecessor);
                    return;
                }
                tree.delete(segment);
            }
        }

        System.out.println("NO INTERSECTIONS");

    }


}

class Sort<T extends Comparable<T>> {
    public void quickSort(T[] arr, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    private int partition(T[] arr, int begin, int end) {
        T pivot = arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (arr[j].compareTo(pivot) > 0) {
                i++;

                T swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }

        T swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

}

class Point implements Comparable<Point> {
    private final long x;
    private final long y;
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
        return Long.compare(getX(), o.getX());
    }
}

record Segment(Point p, Point q) implements Comparable<Segment> {

    public static Orientations getOrientation(Point p, Point q, Point r) {
        long orientation = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (orientation == 0) return Orientations.COLLINEAR;

        return (orientation > 0) ? Orientations.CLOCKWISE : Orientations.COUNTERCLOCKWISE;
    }

    public static boolean areIntersect(Segment s1, Segment s2) {
        if (s1 == null || s2 == null) {
            return false;
        }

        Orientations o1 = getOrientation(s1.p(), s1.q(), s2.p());
        Orientations o2 = getOrientation(s1.p(), s1.q(), s2.q());
        Orientations o3 = getOrientation(s2.p(), s2.q(), s1.p());
        Orientations o4 = getOrientation(s2.p(), s2.q(), s1.q());

        if (o1 == Orientations.COLLINEAR || o2 == Orientations.COLLINEAR || o3 == Orientations.COLLINEAR || o4 == Orientations.COLLINEAR) {
            return false;
        }

        return o1 != o2 && o3 != o4;
    }

    @Override
    public int compareTo(Segment other) {
        return Long.compare(p.getX(), other.p().getX());
    }

    @Override
    public String toString() {
        return String.format("%s %s", p, q);
    }
}


enum Orientations {
    COLLINEAR, CLOCKWISE, COUNTERCLOCKWISE
}

enum Colors {
    RED, BLACK;

    public static Colors getOpposite(Colors color) {
        return color == RED ? BLACK : RED;
    }
}

class Node<T> {
    T value;
    Node<T> left, right;
    Colors color;

    public Node(T value) {
        this.value = value;
        this.color = Colors.RED;
    }

    public void changeColor(){
        this.color = Colors.getOpposite(this.color);
        this.left.color = Colors.getOpposite(this.left.color);
        this.right.color = Colors.getOpposite(this.right.color);

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
            return new Node<>(value);
        }
        if (value.compareTo(node.value) < 0) {
            node.left = insert(node.left, value);
        } else if (value.compareTo(node.value) > 0) {
            node.right = insert(node.right, value);
        } else {
            node.value = value;
        }

        if (isRed(node.right) && isBlack(node.left)) {
            node = leftRotation(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rightRotation(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            node.changeColor();
        }

        return node;
    }

    private boolean isRed(Node<T> node) {
        if (node == null) {
            return false;
        }
        return node.color == Colors.RED;
    }

    private boolean isBlack(Node<T> node) {
        return !isRed(node);
    }

    private Node<T> leftRotation(Node<T> root) {
        Node<T> node = root.right;
        root.right = node.left;

        node.left = root;
        node.color = root.color;
        root.color = Colors.RED;
        return node;
    }

    private Node<T> rightRotation(Node<T> root) {
        Node<T> node = root.left;
        root.left = node.right;

        node.right = root;
        node.color = root.color;
        root.color = Colors.RED;
        return node;
    }


    public void delete(T value) {
        if (root == null) {
            return;
        }

        root = deleteNode(root, value);
        if (root != null) {
            root.color = Colors.BLACK;
        }
    }

    private Node<T> deleteNode(Node<T> node, T value) {
        if (value.compareTo(node.value) < 0) {
            if (node.left != null) {
                if (isBlack(node.left) && isBlack(node.left.left)) {
                    node = moveRedLeft(node);
                }
                node.left = deleteNode(node.left, value);
            }
            return fixUp(node);
        }

        if (isRed(node.left)) {
            node = rightRotation(node);
        }
        if (value.compareTo(node.value) == 0 && node.right == null) {
            return null;
        }

        if (node.right != null) {
            if (isBlack(node.right) && isBlack(node.right.left)) {
                node = moveRedRight(node);
            }
            if (value.compareTo(node.value) == 0) {
                Node<T> minRBNode = findMin(node.right);
                node.value = minRBNode.value;
                node.right = deleteMin(node.right);
            } else {
                node.right = deleteNode(node.right, value);
            }
        }

        return fixUp(node);
    }

    private Node<T> moveRedLeft(Node<T> node) {
        node.changeColor();
        if (isRed(node.right.left)) {
            node.right = rightRotation(node.right);
            node = leftRotation(node);
            node.changeColor();
        }
        return node;
    }

    private Node<T> deleteMin(Node<T> node) {
        if (node.left == null) {
            return null;
        }
        if (isBlack(node.left) && isBlack(node.left.left)) {
            node = moveRedLeft(node);
        }
        node.left = deleteMin(node.left);
        return fixUp(node);
    }

    private Node<T> moveRedRight(Node<T> node) {
        node.changeColor();
        if (isRed(node.left.left)) {
            node = rightRotation(node);
            node.changeColor();
        }
        return node;
    }

    private Node<T> findMin(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node<T> fixUp(Node<T> node) {
        if (isRed(node.right)) {
            node = leftRotation(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rightRotation(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            node.changeColor();
        }
        return node;
    }

    T getPredecessor(T value) {
        Node<T> current = root;
        Node<T> predecessor = null;

        while (current != null) {
            predecessor = current;
            if (value.compareTo(current.value) <= 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return predecessor == null ? null : predecessor.value;
    }

    T getSuccessor(T value) {
        Node<T> current = root;
        Node<T> successor = null;

        while (current != null) {
            if (value.compareTo(current.value) <= 0) {
                if (current.left != null && current.left.value == value) {
                    successor = current;
                } else if (current.value == value && current.right != null) {
                    successor = current.right;
                }
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return successor == null ? null : successor.value;
    }
}