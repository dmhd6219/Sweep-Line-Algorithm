import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bf.readLine());

        ArrayList<Segment> segments = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int[] nums = Arrays.stream(bf.readLine().split(" ")).mapToInt(x -> Integer.parseInt(x)).toArray();
            Point p = new Point(nums[0], nums[1]);
            Point q = new Point(nums[2], nums[3]);

            segments.add(new Segment(q, p));
        }


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
    T value;
    Node left;
    Node right;

    Node(T value) {
        this.value = value;
    }

    Node<T> copy() {
        Node ans = new Node(this.value);
        ans.left = this.left;
        ans.right = right;
        return ans;
    }

    int height() {
        if (this.left == null && this.right == null) {
            return 1;
        } else if (this.left == null) {
            return this.right.height() + 1;
        } else if (this.right == null) {
            return this.left.height() + 1;
        }

        return Math.max(this.left.height() + 1, this.right.height() + 1);
    }
}

class AVLTree<T extends Comparable<T>> {
    Node<T> root;

    public int getBalance(Node<T> root) {
        return (root == null) ? 0 : root.right.height() - root.left.height();
    }

    public void rightRotate(Node<T> root) {
        Node<T> tempRoot = root.copy();
        root = root.left;

        tempRoot.left = root.right;
        root.right = tempRoot;
    }

    public void leftRotate(Node<T> root) {
        Node<T> tempRoot = root.copy();
        root = root.right;

        tempRoot.right = root.left;
        root.left = tempRoot;
    }

    public void rebalance(Node root) {
        int balance = getBalance(root);

        if (balance > 1) {
            if ((root.right.right.height()) > (root.right.left.height())) {
                leftRotate(root);
            } else {
                rightRotate(root.right);
                leftRotate(root);
            }
        } else if (balance < -1) {
            if ((root.left.left.height()) > (root.left.right.height()))
                rightRotate(root);
            else {
                leftRotate(root.left);
                rightRotate(root);
            }
        }
    }

    public void insert(Node<T> root, T value) {
        if (value == null) {
            throw new RuntimeException("Null value");
        }

        if (value.compareTo(root.value) < 1) {
            if (root.left == null) {
                root.left = new Node(value);
            }
            else {
                insert(root.left, value);
            }

        } else {
            if (root.right == null) {
                root.right = new Node(value);
            }
            else {
                insert(root.right, value);
            }
        }

    }

    Node<T> find(Node<T> root, T value){
        if (value == null) {
            throw new RuntimeException("Null value");
        }

        if (value.compareTo(root.value) < 1) {
            if (root.left == null) {
                return null;
            }
            else {
                if (root.value == value){
                    return root;
                }
                else {
                    find(root.left, value);
                }
            }

        } else {
            if (root.right == null) {
                return null;
            }
            else {
                if (root.value == value){
                    return root;
                }
                else {
                    find(root.right, value);
                }
            }
        }
        return null;
    }


}