import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
//        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
//        int n = Integer.parseInt(bf.readLine());
//
//        ArrayList<Segment> segments = new ArrayList<>();

//        for (int i = 0; i < n; i++) {
//            int[] nums = Arrays.stream(bf.readLine().split(" ")).mapToInt(x -> Integer.parseInt(x)).toArray();
//            Point p = new Point(nums[0], nums[1]);
//            Point q = new Point(nums[2], nums[3]);
//
//            segments.add(new Segment(q, p));
//        }

        AVLTree<Integer> tree = new AVLTree<>();
        tree.insert(1);
        tree.insert(4);
        tree.insert(3);
        tree.insert(6);
        tree.insert(5);
        tree.insert(2);
        tree.insert(7);


    }
}
    class Point {
        private int x;
        private int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    class Segment {
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

        public static Point intersect(Segment s1, Segment s2) {
            int x = ((s1.getP().getX() * s1.getQ().getY() - s1.getP().getY() * s1.getQ().getX()) * (s2.getP().getX() - s2.getQ().getX()) - (s1.getP().getX() - s1.getQ().getX()) * (s2.getP().getX() * s2.getQ().getY() - s2.getP().getY() * s2.getQ().getX())) / ((s1.getP().getX() - s1.getQ().getX()) * (s2.getP().getY() - s2.getQ().getY()) - (s1.getP().getY() - s1.getQ().getY()) * (s2.getP().getX() - s2.getQ().getX()));
            int y = ((s1.getP().getX() * s1.getQ().getY() - s1.getP().getY() * s1.getQ().getX()) * (s2.getP().getY() - s2.getQ().getY()) - (s1.getP().getY() - s1.getQ().getY()) * (s2.getP().getX() * s2.getQ().getY() - s2.getP().getY() * s2.getQ().getX())) / ((s1.getP().getX() - s1.getQ().getX()) * (s2.getP().getY() - s2.getQ().getY()) - (s1.getP().getY() - s1.getQ().getY()) * (s2.getP().getX() - s2.getQ().getX()));
            return null;
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
            return node != null ? node.height : -1;
        }

        private void updateHeight(Node<T> node) {
            int leftChildHeight = height(node.left);
            int rightChildHeight = height(node.right);
            node.height = Math.max(leftChildHeight, rightChildHeight) + 1;
        }

        private int balanceFactor(Node node) {
            return height(node.right) - height(node.left);
        }

        private Node<T> rotateRight(Node<T> node) {
            Node<T> leftChild = node.left;

            node.left = leftChild.right;
            leftChild.right = node;

            updateHeight(node);
            updateHeight(leftChild);

            return leftChild;
        }

        private Node<T> rotateLeft(Node<T> node) {
            Node<T> rightChild = node.right;

            node.right = rightChild.left;
            rightChild.left = node;

            updateHeight(node);
            updateHeight(rightChild);

            return rightChild;
        }


        private Node<T> rebalance(Node<T> node) {
            int balanceFactor = balanceFactor(node);

            // Left-heavy?
            if (balanceFactor < -1) {
                if (balanceFactor(node.left) <= 0) {    // Case 1
                    // Rotate right
                    node = rotateRight(node);
                } else {                                // Case 2
                    // Rotate left-right
                    node.left = rotateLeft(node.left);
                    node = rotateRight(node);
                }
            }

            // Right-heavy?
            if (balanceFactor > 1) {
                if (balanceFactor(node.right) >= 0) {    // Case 3
                    // Rotate left
                    node = rotateLeft(node);
                } else {                                 // Case 4
                    // Rotate right-left
                    node.right = rotateRight(node.right);
                    node = rotateLeft(node);
                }
            }

            return node;
        }

        Node insert(Node<T> rootNode, T value) {
            if (rootNode == null) {
                rootNode = new Node<>(value);
                return rootNode;
            }

            // Otherwise, traverse the tree to the left or right depending on the key
            if (value.compareTo(rootNode.data) < 0) {
                rootNode.left = insert(rootNode.left, value);
            } else if (value.compareTo(rootNode.data) > 0) {
                rootNode.right = insert(rootNode.right, value);
            } else {
                throw new IllegalArgumentException("AVL Tree already contains a node with this key");
            }

            updateHeight(rootNode);

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

            // Otherwise, traverse the tree to the left or right depending on the key
            if (value.compareTo(rootNode.data) <= -1) {
                rootNode.left = insert(rootNode.left, value);
            } else if (value.compareTo(rootNode.data) >= 1) {
                rootNode.right = insert(rootNode.right, value);
            } else {
                throw new IllegalArgumentException("AVL Tree already contains a node with this key");
            }

            updateHeight(rootNode);

            this.root = rebalance(rootNode);

            return rootNode;
        }

}