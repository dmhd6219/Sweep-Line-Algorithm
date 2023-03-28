/**
 * Point class.
 */
public class Point implements Comparable<Point> {
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
