package treebolic.glue;

/**
 * Point2D
 *
 * @author Bernard Bou
 */
public class Point2D extends android.graphics.PointF implements treebolic.glue.iface.Point2D
{
	/**
	 * Constructor
	 *
	 * @param x0 x
	 * @param y0 y
	 */
	public Point2D(final double x0, final double y0)
	{
		super((float) x0, (float) y0);
	}

	@Override
	public double getX()
	{
		return this.x;
	}

	@Override
	public double getY()
	{
		return this.y;
	}
}
