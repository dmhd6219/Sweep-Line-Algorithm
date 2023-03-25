import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

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

class Node {
    int key;
    int height;
    Node left;
    Node right;

    Node(int key){
        this.key = key;
    }
}

class AVLTree {

    private Node rootNode;

    void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    int height(Node n) {
        return n == null ? -1 : n.height;
    }

    int getBalance(Node n) {
        return (n == null) ? 0 : height(n.right) - height(n.left);
    }

    Node rotateRight(Node y) {
        Node x = y.left;
        Node z = x.right;
        x.right = y;
        y.left = z;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    Node rotateLeft(Node y) {
        Node x = y.right;
        Node z = x.left;
        x.left = y;
        y.right = z;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    Node rebalance(Node z) {
        updateHeight(z);
        int balance = getBalance(z);
        if (balance > 1) {
            if (height(z.right.right) > height(z.right.left)) {
                z = rotateLeft(z);
            } else {
                z.right = rotateRight(z.right);
                z = rotateLeft(z);
            }
        } else if (balance < -1) {
            if (height(z.left.left) > height(z.left.right))
                z = rotateRight(z);
            else {
                z.left = rotateLeft(z.left);
                z = rotateRight(z);
            }
        }
        return z;
    }

    Node insert(Node node, int key) {
        if (node == null) {
            return new Node(key);
        } else if (node.key > key) {
            node.left = insert(node.left, key);
        } else if (node.key < key) {
            node.right = insert(node.right, key);
        } else {
            throw new RuntimeException("duplicate Key!");
        }
        return rebalance(node);
    }

}