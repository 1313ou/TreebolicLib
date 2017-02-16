package treebolic.glue.iface;

/**
 * Glue interface for Rectangle2D
 *
 * @author Bernard Bou
 */
public interface Rectangle2D<P, R>
{
	// public Rectangle2D(final double x, final double y, final double w, final double h);

	// public Rectangle2D();

	/**
	 * Set rectangle frame
	 *
	 * @param x
	 *            left
	 * @param y
	 *            top
	 * @param width
	 *            width
	 * @param height
	 *            height
	 */
	public void setFrame(double x, double y, double width, double height);

	/**
	 * Left
	 *
	 * @return left
	 */
	public double getX();

	/**
	 * Top
	 *
	 * @return top
	 */
	public double getY();

	/**
	 * Width
	 *
	 * @return width
	 */
	public double getWidth();

	/**
	 * Height
	 *
	 * @return height
	 */
	public double getHeight();

	/**
	 * X-Center
	 *
	 * @return x-center
	 */
	public double getCenterX();

	/**
	 * Y-Center
	 *
	 * @return y-center
	 */
	public double getCenterY();

	/**
	 * X-Min
	 *
	 * @return x-min
	 */
	public double getMinX();

	/**
	 * Y-Min
	 *
	 * @return y-min
	 */
	public double getMinY();

	/**
	 * Whether rectangle intersects with other rectangle
	 *
	 * @param rect
	 *            other rectangle
	 * @return true if they intersect
	 */
	public boolean intersects(final R rect);

	public static final int OUT_BOTTOM = 1;

	public static final int OUT_LEFT = 2;

	public static final int OUT_RIGHT = 4;

	public static final int OUT_TOP = 8;

	/**
	 * Locate point relative to rectangle
	 *
	 * @param point
	 *            point
	 * @return code (above constants)
	 */
	public int outcode(final P point);
}
