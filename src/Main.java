// Sviatoslav Sviatkin

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;


/**
 * The Main class.
 *
 * @author Sviatoslav Sviatkin
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
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
                tree.put(segment);

                if (Segment.areIntersect(segment, predecessor)) {
                    System.out.printf("INTERSECTION\n%s\n%s\n", segment, predecessor);
                    return;
                }
                if (Segment.areIntersect(segment, successor)) {
                    System.out.printf("INTERSECTION\n%s\n%s\n", segment, predecessor);
                    return;
                }
            }

            if (point.isRight()) {
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

/**
 * Class with quick sort algorithm.
 *
 * @param <T> the type parameter
 */
class Sort<T extends Comparable<T>> {
    /**
     * Quick sort method.
     *
     * @param arr   the array to sort
     * @param begin the beginning index
     * @param end   the end index
     */
    public void quickSort(T[] arr, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    /**
     * Positions the last component as the pivot.
     * After that, if the value of any element is smaller than the pivot, it is replaced.
     *
     * @param arr   the array to sort
     * @param begin the beginning index
     * @param end   the end index
     * @return position of pivot
     */
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

/**
 * Point class.
 */
class Point implements Comparable<Point> {
    private final long x;
    private final long y;
    private boolean left = false;

    private Segment segment;

    /**
     * Gets {@link Segment} that contains this {@link Point}.
     *
     * @return the segment
     */
    public Segment getSegment() {
        return segment;
    }

    /**
     * Sets {@link Segment} that contains this {@link Point}.
     *
     * @param segment the segment
     */
    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    /**
     * Instantiates a new {@link Point}.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Instantiates a new {@link Point}.
     *
     * @param x    the x coordinate
     * @param y    the y coordinate
     * @param left is this {@link Point} left at {@link Segment}
     */
    Point(long x, long y, boolean left) {
        this.x = x;
        this.y = y;
        this.left = left;
    }

    /**
     * Gets x coordinate.
     *
     * @return the x
     */
    public long getX() {
        return x;
    }

    /**
     * Gets y coordinate.
     *
     * @return the y
     */
    public long getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("%d %d", x, y);
    }

    /**
     * Checks is this {@link Point} left at {@link Segment}.
     *
     * @return {@code true} is this {@link Point} left at {@link Segment}, otherwise {@code false}
     */
    public boolean isLeft() {
        return left;
    }

    /**
     * Checks is this {@link Point} right at {@link Segment}.
     *
     * @return {@code true} is this {@link Point} right at {@link Segment}, otherwise {@code false}
     */
    public boolean isRight() {
        return !isLeft();
    }

    @Override
    public int compareTo(Point other) {
        return Long.compare(getX(), other.getX());
    }


}

/**
 * Segment class.
 */
record Segment(Point p, Point q) implements Comparable<Segment> {

    // To find orientation of ordered triplet (p, q, r).
// The function returns following values
// 0 --> p, q and r are collinear
// 1 --> Clockwise
// 2 --> Counterclockwise

    /**
     * Finds orientation of ordered triplet of {@link Point}s
     *
     * @param p first point
     * @param q second point
     * @param r third point
     * @return the {@link Orientations} orientation
     */
    public static Orientations getOrientation(Point p, Point q, Point r) {
        long orientation = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (orientation == 0) return Orientations.COLLINEAR;

        return (orientation > 0) ? Orientations.CLOCKWISE : Orientations.COUNTERCLOCKWISE;
    }

    /**
     * Checks if two {@link Segment}s are intersecting.
     *
     * @param s1 first segment
     * @param s2 second segment
     * @return {@code true} if segments are intersecting, otherwise {@code false}
     */
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


/**
 * The enum with orientation names.
 */
enum Orientations {
    COLLINEAR,
    CLOCKWISE,
    COUNTERCLOCKWISE
}

/**
 * The enum with color names.
 */
enum Colors {
    RED,
    BLACK;

    /**
     * Gets opposite color.
     *
     * @param color the color
     * @return opposite color
     */
    public static Colors getOpposite(Colors color) {
        return color == RED ? BLACK : RED;
    }
}


/**
 * The Node for {@link RedBlackTree}.
 *
 * @param <T> the parameter of type
 */
class Node<T extends Comparable<T>> {
    /**
     * Value of this {@link Node}.
     */
    T value;
    /**
     * Left child.
     */
    Node<T> left,
    /**
     * Right child.
     */
    right;
    /**
     * Color of this {@link Node}.
     */
    Colors color;

    /**
     * Instantiates a new {@link Node}.
     *
     * @param value the value of this {@link Node}
     */
    public Node(T value) {
        this.value = value;
        this.color = Colors.RED;
    }

    /**
     * Changes color of this {@link Node} and its children to opposite.
     */
    public void changeColor() {
        this.color = Colors.getOpposite(this.color);
        this.left.color = Colors.getOpposite(this.left.color);
        this.right.color = Colors.getOpposite(this.right.color);

    }
}

/**
 * RedBlackTree class.
 *
 * @param <T> the parameter of type
 */
class RedBlackTree<T extends Comparable<T>> {
    private Node<T> root;


    /**
     * Puts value into this {@link RedBlackTree}.
     * Time complexity (worst case) - O(log n).
     *
     * @param value the value
     */
    public void put(T value) {
        root = put(root, value);
        root.color = Colors.BLACK;
    }

    /**
     * Puts value into this {@link RedBlackTree}.
     *
     * @param root  root {@link Node} of subtree to put
     * @param value the value
     * @return {@link Node} that contains this value
     */
    private Node<T> put(Node<T> root, T value) {
        if (root == null) {
            return new Node<>(value);
        }

        if (value.compareTo(root.value) > 0) {
            root.right = put(root.right, value);
        }

        if (value.compareTo(root.value) < 0) {
            root.left = put(root.left, value);
        }

        if (value.compareTo(root.value) == 0) {
            root.value = value;
        }

        if (isRed(root.right) && isBlackOrNull(root.left)) {
            root = leftRotation(root);
        }
        if (isRed(root.left) && isRed(root.left.left)) {
            root = rightRotation(root);
        }
        if (isRed(root.left) && isRed(root.right)) {
            root.changeColor();
        }

        return root;
    }

    /**
     * Checks if this {@link Node} is Red.
     *
     * @param node {@link Node} to check.
     * @return {@code true} if {@link Node} is Red, otherwise {@code false}
     */
    private boolean isRed(Node<T> node) {
        if (node == null) {
            return false;
        }
        return node.color == Colors.RED;
    }

    /**
     * Checks if this {@link Node} is Black or Null.
     *
     * @param node {@link Node} to check.
     * @return {@code true} if {@link Node} is Black or Null, otherwise {@code false}
     */
    private boolean isBlackOrNull(Node<T> node) {
        return !isRed(node);
    }

    /**
     * Converts a left-leaning link to a right-leaning link
     *
     * @param root root {@link Node} of subtree to rotate
     * @return new root {@link Node}
     */
    private Node<T> leftRotation(Node<T> root) {
        if (root == null) {
            return null;
        }
        Node<T> node = root.right;
        root.right = node.left;

        node.left = root;
        node.color = root.color;
        root.color = Colors.RED;
        return node;
    }

    /**
     * Converts a right-leaning link to a left-leaning link
     *
     * @param root root {@link Node} of subtree to rotate
     * @return new root {@link Node}
     */
    private Node<T> rightRotation(Node<T> root) {
        if (root == null) {
            return null;
        }
        Node<T> node = root.left;
        root.left = node.right;

        node.right = root;
        node.color = root.color;
        root.color = Colors.RED;
        return node;
    }


    /**
     * Deletes value from this {@link RedBlackTree}.
     * Time complexity (worst case) - O(log n).
     *
     * @param value value to delete
     */
    public void delete(T value) {
        if (root == null) {
            return;
        }

        root = deleteNode(root, value);
        if (root != null) {
            root.color = Colors.BLACK;
        }
    }

    /**
     * Deletes value from this {@link RedBlackTree}.
     *
     * @param root  root {@link Node} of subtree where {@link Node} will be deleted
     * @param value value to delete
     * @return new root {@link Node}
     */
    private Node<T> deleteNode(Node<T> root, T value) {
        if (root == null) {
            return null;
        }
        if (value.compareTo(root.value) < 0) {
            if (root.left != null) {
                if (isBlackOrNull(root.left) && isBlackOrNull(root.left.left)) {
                    root = moveRedLeft(root);
                }
                root.left = deleteNode(root.left, value);
            }
            return rebalance(root);
        }

        if (isRed(root.left)) {
            root = rightRotation(root);
        }
        if (value.compareTo(root.value) == 0 && root.right == null) {
            return null;
        }

        if (root.right != null) {
            if (isBlackOrNull(root.right) && isBlackOrNull(root.right.left)) {
                root = moveRedRight(root);
            }
            if (value.compareTo(root.value) == 0) {
                Node<T> minRBNode = getMinimum(root.right);
                root.value = minRBNode.value;
                root.right = deleteMinimum(root.right);
            } else {
                root.right = deleteNode(root.right, value);
            }
        }

        return rebalance(root);
    }

    /**
     * Gets minimum value from this {@link RedBlackTree}
     * <p>
     * Time complexity (worst case) - O(log n).
     *
     * @param node root {@link Node} of subtree where to search minimum value
     * @return {@link Node} with minimum value
     */
    private Node<T> getMinimum(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Deletes minimum value from this {@link RedBlackTree}
     *
     * @param node root {@link Node} of subtree where to search minimum value
     * @return new root {@link Node}
     */
    private Node<T> deleteMinimum(Node<T> node) {
        if (node == null || node.left == null) {
            return null;
        }
        if (isBlackOrNull(node.left) && isBlackOrNull(node.left.left)) {
            node = moveRedLeft(node);
        }
        node.left = deleteMinimum(node.left);
        return rebalance(node);
    }

    /**
     * Make node.left or one of its children Red
     * in the case that {@link Node} is Red and node.left and node.left.left are both Black.
     *
     * @param node {@link Node} to change
     * @return changed node
     */
    private Node<T> moveRedLeft(Node<T> node) {
        if (node == null) {
            return null;
        }
        node.changeColor();
        if (isRed(node.right.left)) {
            node.right = rightRotation(node.right);
            node = leftRotation(node);
            node.changeColor();
        }
        return node;
    }

    /**
     * Make node.right or one of its children Red
     * in the case that {@link Node} is Red and both node.right and node.right.left are both Black.
     *
     * @param node {@link Node} to change
     * @return changed node
     */
    private Node<T> moveRedRight(Node<T> node) {
        if (node == null) {
            return null;
        }
        node.changeColor();
        if (isRed(node.left.left)) {
            node = rightRotation(node);
            node.changeColor();
        }
        return node;
    }

    /**
     * Restores Red-Black tree invariant
     *
     * @param node root {@link Node}
     * @return new root {@link Node}
     */
    private Node<T> rebalance(Node<T> node) {
        if (node == null) {
            return null;
        }
        if (isBlackOrNull(node.left) && isRed(node.right)) {
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

    /**
     * Gets predecessor.
     * Time complexity (worst case) - O(log n).
     *
     * @param value the value
     * @return the predecessor
     */
    public T getPredecessor(T value) {
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

    /**
     * Gets successor.
     * Time complexity (worst case) - O(log n).
     *
     * @param value the value
     * @return the successor
     */
    public T getSuccessor(T value) {
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

/*
Task 2.1.2
    It's true that several industries, including computer graphics, robotics, geographic information systems,
    and computer vision, face the challenge of detecting the intersection of two line segments.
    Here are some additional applications of line segment intersection:

        1) For the purpose of detecting collisions between objects in a scene, line segment intersection is used in
        computer games and simulations. It is possible to tell if two things are in contact or if they are colliding by
        examining whether the line segments that represent the two objects intersect.

        2) It is used in pattern identification in computer vision and image processing.
        It is feasible to utilize line segment intersection algorithms to find characteristics like corners,
        intersections, and areas with similar patterns by modeling edges and contours as line segments.

        3) In additive manufacturing, 3D models are divided into layers that may be printed one at a time using line
        segment intersection. Line segment intersection algorithms can be used to split a 3D model into layers and
        produce the toolpaths needed by the 3D printer to construct the actual object by describing the model's
        surfaces as line segments.

Task 2.1.3
    It involves grouping the line segments according to their x-coordinate and processing each one in sequence.
    As each line segment is taken into account, a vertical line known as the sweep line sweeps across the plane.
    The method keeps track of the line segments that cross the sweep line in a data structure.

    The algorithm adds new line segments to the data structure as the sweep line crosses the plane and deletes old line
    segments that are no longer intersecting the sweep line. The technique checks for intersection with a segment's
    neighbors in the data structure whenever a new segment is added. The algorithm announces the discovery of an
    intersection as a solution to the issue.

Task 2.1.4
    * Worst O(n^2), average O(nlog(n))
    * In-place
    * No, it is not stable
 */