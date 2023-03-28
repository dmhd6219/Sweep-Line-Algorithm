/**
 * Segment class.
 */
public record Segment(Point p, Point q) implements Comparable<Segment> {

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
