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
	 * @param x      left
	 * @param y      top
	 * @param width  width
	 * @param height height
	 */
	void setFrame(double x, double y, double width, double height);

	/**
	 * Left
	 *
	 * @return left
	 */
	double getX();

	/**
	 * Top
	 *
	 * @return top
	 */
	double getY();

	/**
	 * Width
	 *
	 * @return width
	 */
	double getWidth();

	/**
	 * Height
	 *
	 * @return height
	 */
	double getHeight();

	/**
	 * X-Center
	 *
	 * @return x-center
	 */
	double getCenterX();

	/**
	 * Y-Center
	 *
	 * @return y-center
	 */
	double getCenterY();

	/**
	 * X-Min
	 *
	 * @return x-min
	 */
	double getMinX();

	/**
	 * Y-Min
	 *
	 * @return y-min
	 */
	double getMinY();

	/**
	 * Whether rectangle intersects with other rectangle
	 *
	 * @param rect other rectangle
	 * @return true if they intersect
	 */
	boolean intersects(final R rect);

	int OUT_BOTTOM = 1;

	int OUT_LEFT = 2;

	int OUT_RIGHT = 4;

	int OUT_TOP = 8;

	/**
	 * Locate point relative to rectangle
	 *
	 * @param point point
	 * @return code (above constants)
	 */
	int outcode(final P point);
}
