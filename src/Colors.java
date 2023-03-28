/**
 * The enum with color names.
 */
public enum Colors {
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
