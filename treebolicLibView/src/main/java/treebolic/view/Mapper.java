package treebolic.view;

import android.support.annotation.NonNull;

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
	protected int theWidth;

	@SuppressWarnings("WeakerAccess")
	protected int theHeight;

	/**
	 * Map scale factor
	 */
	@SuppressWarnings("WeakerAccess")
	protected float theMapScaleFactor = 1;

	/**
	 * Scale x factor
	 */
	@SuppressWarnings("WeakerAccess")
	protected float theScaleX = 1F;

	/**
	 * Scale y factor
	 */
	@SuppressWarnings("WeakerAccess")
	protected float theScaleY = 1F;

	/**
	 * X shift
	 */
	@SuppressWarnings("WeakerAccess")
	protected float theXShift = 0F;

	/**
	 * Y shift
	 */
	@SuppressWarnings("WeakerAccess")
	protected float theYShift = 0F;

	/**
	 * Top coordinate
	 */
	@SuppressWarnings("WeakerAccess")
	protected int theTop;

	/**
	 * Left coordinate
	 */
	@SuppressWarnings("WeakerAccess")
	protected int theLeft;

	// S I Z E

	/**
	 * Get width
	 *
	 * @return width
	 */
	public int getWidth()
	{
		return this.theWidth;
	}

	/**
	 * Get height
	 *
	 * @return width
	 */
	public int getHeight()
	{
		return this.theHeight;
	}

	// A C C E S S

	/**
	 * Set view x-shift
	 *
	 * @param thisX view x-shift (0,1)
	 */
	public void setXShift(final float thisX)
	{
		this.theXShift = thisX;
	}

	/**
	 * Set view y-shift
	 *
	 * @param thisY view y-shift (0,1)
	 */
	public void setYShift(final float thisY)
	{
		this.theYShift = thisY;
	}

	/**
	 * Get view x-shift
	 *
	 * @return view x-shift
	 */
	public float getXShift()
	{
		return this.theXShift;
	}

	/**
	 * Get view y-shift
	 *
	 * @return view y-shift
	 */
	public float getYShift()
	{
		return this.theYShift;
	}

	/**
	 * Compute scale from view size
	 */
	@SuppressWarnings("WeakerAccess")
	protected void computeScale()
	{
		this.theScaleX = (0.5F + (this.theXShift > 0. ? this.theXShift : -this.theXShift)) * this.theWidth;
		this.theScaleY = (0.5F + (this.theYShift > 0. ? this.theYShift : -this.theYShift)) * this.theHeight;
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
		return (int) (this.theScaleX * this.theMapScaleFactor * x + this.theXShift * this.theWidth);
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
		return (int) (this.theScaleY * this.theMapScaleFactor * y + this.theYShift * this.theWidth);
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
		return (int) (this.theScaleX * this.theMapScaleFactor * cx);
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
		return (int) (this.theScaleY * this.theMapScaleFactor * cy);
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
		return (vx - this.theXShift * this.theWidth) / (this.theScaleX * this.theMapScaleFactor);
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
		return (vy - this.theYShift * this.theHeight) / (this.theScaleY * this.theMapScaleFactor);
	}

	/**
	 * Convert view x-extent (width) to unit circle x-extent
	 *
	 * @param cvx view x-extent
	 * @return unit circle x-extent
	 */
	protected double wViewToUnitCircle(final double cvx)
	{
		return cvx / (this.theScaleX * this.theMapScaleFactor);
	}

	/**
	 * Convert view y-extent (height) to unit circle y-extent
	 *
	 * @param cvy view y-extent
	 * @return unit circle y-extent
	 */
	protected double hViewToUnitCircle(final double cvy)
	{
		return cvy / (this.theScaleY * this.theMapScaleFactor);
	}

	// S P A C E . C O N V E R S I O N

	/**
	 * Convert view coordinates to unit circle coordinates
	 *
	 * @param vx         x coordinate in view space
	 * @param vy         y coordinate in view space
	 * @param thisWidth  view width
	 * @param thisHeight view height
	 * @return point in unit circle
	 */
	@NonNull
	public Complex viewToUnitCircle(final int vx, final int vy, final int thisWidth, final int thisHeight)
	{
		final Complex p = new Complex(vx, vy);

		// this offsets for translation on cache graphics
		p.set(p.re - thisWidth / 2, p.im - thisHeight / 2);

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
	 * @param thisNode node
	 * @return view coordinate of node
	 */
	@NonNull
	public Point getViewLocation(@NonNull final INode thisNode)
	{
		final Location thisLocation = thisNode.getLocation();
		return new Point(xUnitCircleToView(thisLocation.euclidean.center.re) - this.theLeft, yUnitCircleToView(thisLocation.euclidean.center.im) - this.theTop);
	}
}
