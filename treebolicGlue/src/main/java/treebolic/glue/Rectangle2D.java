package treebolic.glue;

import android.graphics.RectF;
import androidx.annotation.NonNull;

/**
 * Rectangle2D
 *
 * @author Bernard Bou
 */
public class Rectangle2D extends RectF implements treebolic.glue.iface.Rectangle2D<Point2D, Rectangle2D>
{
	/**
	 * Make rectangle from topleft and size
	 *
	 * @param left0   left
	 * @param top0    top
	 * @param width0  width
	 * @param height0 height
	 * @return rectangle
	 */
	public static RectF makeRect(final double left0, final double top0, final double width0, final double height0)
	{
		double left = left0;
		double top = top0;
		double right = left + width0;
		double bottom = top + height0;

		if (left > right)
		{
			left = right;
			right = left0;
		}
		if (top > bottom)
		{
			top = bottom;
			bottom = top0;
		}
		return new RectF((float) left, (float) top, (float) right, (float) bottom);
	}

	/**
	 * Constructor from topleft and size
	 *
	 * @param x left
	 * @param y top
	 * @param w width
	 * @param h height
	 */
	public Rectangle2D(final int x, final int y, final int w, final int h)
	{
		super(Rectangle2D.makeRect(x, y, w, h));
	}

	/**
	 * Constructor of empty rectangle
	 */
	public Rectangle2D()
	{
		super();
	}

	@Override
	public void setFrame(final double x, final double y, final double width, final double height)
	{
		super.set(Rectangle2D.makeRect(x, y, width, height));
	}

	@Override
	public double getX()
	{
		return this.left;
	}

	@Override
	public double getY()
	{
		return this.top;
	}

	@Override
	public double getWidth()
	{
		return super.width();
	}

	@Override
	public double getHeight()
	{
		return super.height();
	}

	@Override
	public double getMinX()
	{
		return (int) this.left;
	}

	@Override
	public double getMinY()
	{
		return (int) this.top;
	}

	@Override
	public double getCenterX()
	{
		return super.centerX();
	}

	@Override
	public double getCenterY()
	{
		return super.centerY();
	}

	@Override
	public boolean intersects(@NonNull final Rectangle2D rect)
	{
		return super.intersects(rect.left, rect.top, rect.right, rect.bottom);
	}

	// Result code for outcode()

	public static final int OUT_BOTTOM = 1;

	public static final int OUT_LEFT = 2;

	public static final int OUT_RIGHT = 4;

	public static final int OUT_TOP = 8;

	@Override
	public int outcode(@NonNull final Point2D point)
	{
		// Determines where the specified coordinates lie with respect to this Rectangle.
		// This method computes a binary OR of the appropriate mask values indicating, for each side of this Rectangle,
		// whether or not the specified coordinates are on the same side of the edge as the rest of this Rectangle.
		final double x = point.getX();
		final double y = point.getY();
		int out = 0;
		if (getWidth() <= 0)
		{
			out |= Rectangle2D.OUT_LEFT | Rectangle2D.OUT_RIGHT;
		}
		else if (x < this.left)
		{
			out |= Rectangle2D.OUT_LEFT;
		}
		else if (x > this.right)
		{
			out |= Rectangle2D.OUT_RIGHT;
		}
		if (getHeight() <= 0)
		{
			out |= Rectangle2D.OUT_TOP | Rectangle2D.OUT_BOTTOM;
		}
		else if (y < this.top)
		{
			out |= Rectangle2D.OUT_TOP;
		}
		else if (y > this.bottom)
		{
			out |= Rectangle2D.OUT_BOTTOM;
		}
		return out;
	}
}
