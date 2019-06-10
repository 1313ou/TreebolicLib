package treebolic.glue.iface;

import androidx.annotation.NonNull;

/**
 * Glue interface for Arc2D
 *
 * @author Bernard Bou
 */
public interface Arc2D<P>
{
	/**
	 * Set frame from center and one corner
	 *
	 * @param x  x-center
	 * @param y  y-center
	 * @param x1 x-corner
	 * @param y1 y-corner
	 */
	void setFrameFromCenter(final double x, final double y, final double x1, final double y1);

	/**
	 * Adjust angles from ends (while preserving enclosing rectangle)
	 *
	 * @param from from end
	 * @param to   to end
	 */
	void setAngles(final P from, final P to);

	/**
	 * Get x-center
	 *
	 * @return x-center
	 */
	double getCenterX();

	/**
	 * Get y-center
	 *
	 * @return y-center
	 */
	double getCenterY();

	/**
	 * Get width
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
	 * Get start point
	 *
	 * @return start point
	 */
	@NonNull
	P getStartPoint();

	/**
	 * Get end point
	 *
	 * @return end point
	 */
	@NonNull
	P getEndPoint();

	/**
	 * Get angle extent
	 *
	 * @return angle extent
	 */
	double getAngleExtent();

	/**
	 * Set angle extent
	 *
	 * @param extent angle extent
	 */
	void setAngleExtent(final double extent);

	/**
	 * Get angle start
	 *
	 * @return angle start
	 */
	double getAngleStart();

	/**
	 * Set angle start
	 *
	 * @param start angle start
	 */
	void setAngleStart(final double start);

	/**
	 * Get direction
	 *
	 * @return counterclockwise flag
	 */
	boolean getCounterclockwise();

	/**
	 * Set direction
	 *
	 * @param flag counterclockwise flag
	 */
	void setCounterclockwise(boolean flag);

	/**
	 * Whether arc contains angle
	 *
	 * @param angle angle
	 * @return true if arc contains angle
	 */
	boolean containsAngle(final double angle);
}
