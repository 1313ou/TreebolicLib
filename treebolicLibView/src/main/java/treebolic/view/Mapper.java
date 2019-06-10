package treebolic.view;

import androidx.annotation.NonNull;
import treebolic.core.location.Complex;
import treebolic.glue.Point;
import treebolic.model.INode;
import treebolic.model.Location;

/**
 * Mapper of unit circle space to view
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class Mapper
{
	/**
	 * Size of graphics context
	 */
	@SuppressWarnings("WeakerAccess")
	protected int width;

	@SuppressWarnings("WeakerAccess")
	protected int height;

	/**
	 * Map scale factor
	 */
	@SuppressWarnings("WeakerAccess")
	protected float mapScaleFactor = 1;

	/**
	 * Scale x factor
	 */
	@SuppressWarnings("WeakerAccess")
	protected float scaleX = 1F;

	/**
	 * Scale y factor
	 */
	@SuppressWarnings("WeakerAccess")
	protected float scaleY = 1F;

	/**
	 * X shift
	 */
	@SuppressWarnings("WeakerAccess")
	protected float xShift = 0F;

	/**
	 * Y shift
	 */
	@SuppressWarnings("WeakerAccess")
	protected float yShift = 0F;

	/**
	 * Top coordinate
	 */
	@SuppressWarnings("WeakerAccess")
	protected int top;

	/**
	 * Left coordinate
	 */
	@SuppressWarnings("WeakerAccess")
	protected int left;

	// S I Z E

	/**
	 * Get width
	 *
	 * @return width
	 */
	public int getWidth()
	{
		return this.width;
	}

	/**
	 * Get height
	 *
	 * @return width
	 */
	public int getHeight()
	{
		return this.height;
	}

	// A C C E S S

	/**
	 * Set view x-shift
	 *
	 * @param x view x-shift (0,1)
	 */
	public void setXShift(final float x)
	{
		this.xShift = x;
	}

	/**
	 * Set view y-shift
	 *
	 * @param y view y-shift (0,1)
	 */
	public void setYShift(final float y)
	{
		this.yShift = y;
	}

	/**
	 * Get view x-shift
	 *
	 * @return view x-shift
	 */
	public float getXShift()
	{
		return this.xShift;
	}

	/**
	 * Get view y-shift
	 *
	 * @return view y-shift
	 */
	public float getYShift()
	{
		return this.yShift;
	}

	/**
	 * Compute scale from view size
	 */
	@SuppressWarnings("WeakerAccess")
	protected void computeScale()
	{
		this.scaleX = (0.5F + (this.xShift > 0. ? this.xShift : -this.xShift)) * this.width;
		this.scaleY = (0.5F + (this.yShift > 0. ? this.yShift : -this.yShift)) * this.height;
	}

	// M A P . U N I T C I R C L E . T O . V I E W

	/**
	 * Convert unit circle x-coordinate to view x-coordinate
	 *
	 * @param x unit circle x-coordinate
	 * @return view x-coordinate
	 */
	@SuppressWarnings("WeakerAccess")
	protected int xUnitCircleToView(final double x)
	{
		return (int) (this.scaleX * this.mapScaleFactor * x + this.xShift * this.width);
	}

	/**
	 * Convert unit circle y-coordinate to view y-coordinate
	 *
	 * @param y unit circle y-coordinate
	 * @return view y-coordinate
	 */
	@SuppressWarnings("WeakerAccess")
	protected int yUnitCircleToView(final double y)
	{
		return (int) (this.scaleY * this.mapScaleFactor * y + this.yShift * this.width);
	}

	/**
	 * Convert unit circle x-extent to view x-extent
	 *
	 * @param cx unit circle x-extent
	 * @return view x-extent
	 */
	@SuppressWarnings("WeakerAccess")
	protected int wUnitCircleToView(final double cx)
	{
		return (int) (this.scaleX * this.mapScaleFactor * cx);
	}

	/**
	 * Convert unit circle y-extent to view y-extent
	 *
	 * @param cy unit circle y-extent
	 * @return view y-extent
	 */
	@SuppressWarnings("WeakerAccess")
	protected int hUnitCircleToView(final double cy)
	{
		return (int) (this.scaleY * this.mapScaleFactor * cy);
	}

	// M A P . V I E W . T O . U N I T C I R C L E

	/**
	 * Convert view x-coordinate to unit circle x-coordinate
	 *
	 * @param vx view x-coordinate
	 * @return unit circle x-coordinate
	 */
	@SuppressWarnings("WeakerAccess")
	protected double xViewToUnitCircle(final double vx)
	{
		return (vx - this.xShift * this.width) / (this.scaleX * this.mapScaleFactor);
	}

	/**
	 * Convert view y-coordinate to unit circle y-coordinate
	 *
	 * @param vy view y-coordinate
	 * @return unit circle y-coordinate
	 */
	@SuppressWarnings("WeakerAccess")
	protected double yViewToUnitCircle(final double vy)
	{
		return (vy - this.yShift * this.height) / (this.scaleY * this.mapScaleFactor);
	}

	/**
	 * Convert view x-extent (width) to unit circle x-extent
	 *
	 * @param cvx view x-extent
	 * @return unit circle x-extent
	 */
	protected double wViewToUnitCircle(final double cvx)
	{
		return cvx / (this.scaleX * this.mapScaleFactor);
	}

	/**
	 * Convert view y-extent (height) to unit circle y-extent
	 *
	 * @param cvy view y-extent
	 * @return unit circle y-extent
	 */
	protected double hViewToUnitCircle(final double cvy)
	{
		return cvy / (this.scaleY * this.mapScaleFactor);
	}

	// S P A C E . C O N V E R S I O N

	/**
	 * Convert view coordinates to unit circle coordinates
	 *
	 * @param vx     x coordinate in view space
	 * @param vy     y coordinate in view space
	 * @param width  view width
	 * @param height view height
	 * @return point in unit circle
	 */
	@NonNull
	public Complex viewToUnitCircle(final int vx, final int vy, final int width, final int height)
	{
		final Complex p = new Complex(vx, vy);

		// this offsets for translation on cache graphics
		p.set(p.re - width / 2f, p.im - height / 2f);

		// convert
		p.set(xViewToUnitCircle(p.re), yViewToUnitCircle(p.im));

		// adjust
		if (p.abs2() > 1.)
		{
			p.normalize();
			p.multiply(.99);
		}
		return p;
	}

	/**
	 * Get view coordinates of node
	 *
	 * @param node node
	 * @return view coordinate of node
	 */
	@NonNull
	public Point getViewLocation(@NonNull final INode node)
	{
		final Location location = node.getLocation();
		return new Point(xUnitCircleToView(location.euclidean.center.re) - this.left, yUnitCircleToView(location.euclidean.center.im) - this.top);
	}
}
