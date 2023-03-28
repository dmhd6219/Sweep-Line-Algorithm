/**
 * The Node for {@link RedBlackTree}.
 *
 * @param <T> the parameter of type
 */
public class Node<T extends Comparable<T>> {
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
