/*
 * Copyright (c) 2019-2023. Bernard Bou
 */

package treebolic.glue;

import androidx.annotation.NonNull;

/**
 * Arc2D
 *
 * @author Bernard Bou
 * @noinspection WeakerAccess
 */
public class Arc2D implements treebolic.glue.iface.Arc2D<Point2D>
{
	/**
	 * Left
	 *
	 * @noinspection WeakerAccess
	 */
	public double x;

	/**
	 * Top
	 *
	 * @noinspection WeakerAccess
	 */
	public double y;

	/**
	 * Width
	 *
	 * @noinspection WeakerAccess
	 */
	public double width;

	/**
	 * Height
	 *
	 * @noinspection WeakerAccess
	 */
	public double height;

	/**
	 * Angle start
	 *
	 * @noinspection WeakerAccess
	 */
	public double start;

	/**
	 * Angle extent
	 *
	 * @noinspection WeakerAccess
	 */
	public double extent;

	/**
	 * Whether angle is counterclockwise
	 */
	@SuppressWarnings("WeakerAccess")
	public boolean counterclockwise;

	@Override
	public double getX()
	{
		return x;
	}

	@Override
	public double getY()
	{
		return y;
	}

	@Override
	public double getHeight()
	{
		return this.height;
	}

	@Override
	public double getWidth()
	{
		return this.width;
	}

	@Override
	public double getCenterX()
	{
		return this.x + this.width / 2.0;
	}

	@Override
	public double getCenterY()
	{
		return this.y + this.height / 2.0;
	}

	@NonNull
	@Override
	public Point2D getStartPoint()
	{
		final double angle = Math.toRadians(-this.start);
		final double xs = this.x + (Math.cos(angle) * 0.5 + 0.5) * this.width;
		final double ys = this.y + (Math.sin(angle) * 0.5 + 0.5) * this.height;
		return new Point2D(xs, ys);
	}

	@NonNull
	@Override
	public Point2D getEndPoint()
	{
		final double angle = Math.toRadians(-this.start - this.extent);
		final double xe = this.x + (Math.cos(angle) * 0.5 + 0.5) * this.width;
		final double ye = this.y + (Math.sin(angle) * 0.5 + 0.5) * this.height;
		return new Point2D(xe, ye);
	}

	@Override
	public void setAngles(@NonNull final Point2D from, @NonNull final Point2D to)
	{
		final double x1 = from.x;
		final double y1 = from.y;
		final double x2 = to.x;
		final double y2 = to.y;
		final double x0 = getCenterX();
		final double y0 = getCenterY();
		final double w = this.width;
		final double h = this.height;
		// Note: reversing the Y equations negates the angle to adjust
		// for the upside down coordinate system.
		// Also, we should bias atans by the height and width of the oval.
		final double ang1 = Math.atan2(w * (y0 - y1), h * (x1 - x0));
		double ang2 = Math.atan2(w * (y0 - y2), h * (x2 - x0));
		ang2 -= ang1;
		if (ang2 <= 0.0)
		{
			ang2 += Math.PI * 2.0;
		}
		setAngleStart(Math.toDegrees(ang1));
		setAngleExtent(Math.toDegrees(ang2));
	}

	@Override
	public double getAngleStart()
	{
		return this.start;
	}

	@Override
	public void setAngleStart(final double start0)
	{
		this.start = start0;
	}

	@Override
	public double getAngleExtent()
	{
		return this.extent;
	}

	@Override
	public void setAngleExtent(final double extent0)
	{
		this.extent = extent0;
	}

	@Override
	public boolean containsAngle(final double angle0)
	{
		double angle = angle0;
		double angExt = getAngleExtent();
		final boolean backwards = angExt < 0.0;
		if (backwards)
		{
			angExt = -angExt;
		}
		if (angExt >= 360.0)
		{
			return true;
		}
		angle = Arc2D.normalizeDegrees(angle) - Arc2D.normalizeDegrees(getAngleStart());
		if (backwards)
		{
			angle = -angle;
		}
		if (angle < 0.0)
		{
			angle += 360.0;
		}

		return angle >= 0.0 && angle < angExt;
	}

	@Override
	public void setFrameFromCenter(final double centerX, final double centerY, final double cornerX, final double cornerY)
	{
		final double w2 = Math.abs(cornerX - centerX);
		final double h2 = Math.abs(cornerY - centerY);
		this.x = centerX - w2;
		this.y = centerY - h2;
		this.width = w2 * 2.0;
		this.height = h2 * 2.0;
	}

	@Override
	public void setCounterclockwise(final boolean flag)
	{
		this.counterclockwise = flag;
	}

	@Override
	public boolean getCounterclockwise()
	{
		return this.counterclockwise;
	}

	/*
	 * Normalizes the specified angle into the range -180 to 180.
	 */
	@SuppressWarnings("WeakerAccess")
	static double normalizeDegrees(final double angle0)
	{
		double angle = angle0;
		if (angle > 180.0)
		{
			if (angle <= 180.0 + 360.0)
			{
				angle = angle - 360.0;
			}
			else
			{
				angle = Math.IEEEremainder(angle, 360.0);
				// IEEEremainder can return -180 here for some input values...
				if (angle == -180.0)
				{
					angle = 180.0;
				}
			}
		}
		else if (angle <= -180.0)
		{
			if (angle > -180.0 - 360.0)
			{
				angle = angle + 360.0;
			}
			else
			{
				angle = Math.IEEEremainder(angle, 360.0);
				// IEEEremainder can return -180 here for some input values...
				if (angle == -180.0)
				{
					angle = 180.0;
				}
			}
		}
		return angle;
	}
}
