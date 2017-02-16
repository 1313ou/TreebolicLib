package treebolic.glue.iface;

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
	 * @param x
	 *            x-center
	 * @param y
	 *            y-center
	 * @param x1
	 *            x-corner
	 * @param y1
	 *            y-corner
	 */
	public void setFrameFromCenter(final double x, final double y, final double x1, final double y1);

	/**
	 * Adjust angles from ends (while preserving enclosing rectangle)
	 *
	 * @param from
	 *            from end
	 * @param to
	 *            to end
	 */
	public void setAngles(final P from, final P to);

	/**
	 * Get x-center
	 *
	 * @return x-center
	 */
	public double getCenterX();

	/**
	 * Get y-center
	 *
	 * @return y-center
	 */
	public double getCenterY();

	/**
	 * Get width
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
	 * Get start point
	 *
	 * @return start point
	 */
	public P getStartPoint();

	/**
	 * Get end point
	 *
	 * @return end point
	 */
	public P getEndPoint();

	/**
	 * Get angle extent
	 *
	 * @return angle extent
	 */
	public double getAngleExtent();

	/**
	 * Set angle extent
	 *
	 * @param extent
	 *            angle extent
	 */
	public void setAngleExtent(final double extent);

	/**
	 * Get angle start
	 *
	 * @return angle start
	 */
	public double getAngleStart();

	/**
	 * Set angle start
	 *
	 * @param start
	 *            angle start
	 */
	public void setAngleStart(final double start);

	/**
	 * Get direction
	 *
	 * @return counterclockwise flag
	 */
	public boolean getCounterclockwise();

	/**
	 * Set direction
	 *
	 * @param flag
	 *            counterclockwise flag
	 */
	public void setCounterclockwise(boolean flag);

	/**
	 * Whether arc contains angle
	 *
	 * @param angle
	 *            angle
	 * @return true if arc contains angle
	 */
	public boolean containsAngle(final double angle);
}
