/**
 * RedBlackTree class.
 *
 * @param <T> the parameter of type
 */
public class RedBlackTree<T extends Comparable<T>> {
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
     * Restores Red-Black tree invariant.
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
