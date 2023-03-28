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