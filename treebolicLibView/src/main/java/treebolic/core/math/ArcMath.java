/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.core.math;

import androidx.annotation.NonNull;
import treebolic.glue.Arc2D;
import treebolic.glue.Point2D;

/**
 * Arc Utils
 *
 * @author Bernard Bou
 */
public class ArcMath
{
	@SuppressWarnings("WeakerAccess")
	static final double TWICEPI = 2. * Math.PI;

	static double HALFPI = .5 * Math.PI;

	/**
	 * Get arc eccentricity
	 *
	 * @param arc2D arc
	 * @return eccentricity of arc
	 */
	static private double getEccentricity(@SuppressWarnings("TypeMayBeWeakened") @NonNull final Arc2D arc2D)
	{
		final double a = arc2D.getWidth() / 2.;
		final double b = arc2D.getHeight() / 2.;
		final double d = a > b ? b * b / (a * a) : a * a / (b * b);
		return Math.sqrt(1 - d);
	}

	/**
	 * Get mid-arc point
	 *
	 * @param arc2D arc
	 * @return point at mid-arc
	 */
	@NonNull
	public static Point2D getMidArc(@SuppressWarnings("TypeMayBeWeakened") @NonNull final Arc2D arc2D)
	{
		final double a = arc2D.getWidth() / 2.;
		final double b = arc2D.getHeight() / 2.;
		final double theta = Math.toRadians(-arc2D.getAngleStart()) + Math.toRadians(-arc2D.getAngleExtent() / 2.);
		final double x = a * Math.cos(theta);
		final double y = b * Math.sin(theta);
		return new Point2D(x + arc2D.getCenterX(), y + arc2D.getCenterY());
	}

	/*
	 * Get tangent to this arc at given point
	 *
	 * @param arc2D arc
	 * @param where point on arc
	 * @return tangent of this arc at given point
	 */
	/*
	public static double getTangent0(final Arc2D arc2D, final Point2D where)
	{
		// angle
		final double bissector = ArcMath.getAngleAt(arc2D, where);
		// tangent is perpendicular to radius angle
		return bissector + Math.PI / 2.;
	}
	*/

	/**
	 * Get tangent to this arc at given point (used for text orientation)
	 *
	 * @param arc2D arc
	 * @param where point on arc
	 * @return tangent of this arc at given point
	 */
	public static double getTextTangent(@NonNull final Arc2D arc2D, @SuppressWarnings("TypeMayBeWeakened") @NonNull final Point2D where)
	{
		// center, eccentricity, major axis, minor axis
		final double x = where.getX() - arc2D.getCenterX();
		final double y = where.getY() - arc2D.getCenterY();
		final double e = ArcMath.getEccentricity(arc2D);
		final double a = arc2D.getWidth() / 2.;
		final double b = arc2D.getHeight() / 2.;

		// angles between foci and point
		double angle1;
		double angle2;
		if (a >= b)
		{
			final double ea = e * a;
			angle1 = Math.atan2(y, x - ea);
			angle2 = Math.atan2(y, x + ea);
		}
		else
		{
			final double eb = e * b;
			angle1 = Math.atan2(y - eb, x);
			angle2 = Math.atan2(y + eb, x);
		}
		// tangent
		final double bissector = (angle1 + angle2) / 2.;
		return bissector + Math.PI / 2.;
	}

	/**
	 * Get tangent to arc at given point, the orientation returned is inwards arc
	 *
	 * @param arc2D   arc
	 * @param where   point on arc
	 * @param isStart whether point is start point
	 * @return tangent of this arc at given point
	 */
	public static double getTangent(@NonNull final Arc2D arc2D, @NonNull final Point2D where, final boolean isStart)
	{
		// angle
		double angle = ArcMath.getAngleAt(arc2D, where);

		// tangent is perpendicular to radius angle
		final boolean counterclockwise = arc2D.getCounterclockwise();
		if (counterclockwise)
		{
			angle += isStart ? -Math.PI / 2. : +Math.PI / 2.;
		}
		else
		{
			angle += isStart ? +Math.PI / 2. : -Math.PI / 2.;
		}

		return angle;
	}

	/**
	 * Get radius angle at given point on circle arc
	 *
	 * @param arc2D arc
	 * @param where point on arc
	 * @return angle (of radius) at given point on this Arc2D
	 */
	public static double getCircleAngleAt(@NonNull final Arc2D arc2D, @NonNull final Point2D where)
	{
		// coordinates of point relative to center
		final double x = where.getX() - arc2D.getCenterX();
		final double y = where.getY() - arc2D.getCenterY();
		return Math.atan2(y, x);
	}

	/**
	 * Get radius angle at point on arc
	 *
	 * @param arc2D arc
	 * @param where point on arc where (outgoing) radius orientation is to be computed
	 * @return (outgoing) radius orientation
	 */
	private static double getAngleAt(@NonNull final Arc2D arc2D, @SuppressWarnings("TypeMayBeWeakened") @NonNull final Point2D where)
	{
		// eccentricity, this normally doesn't have to be recomputed : it is dependent on surface dimensions
		final double e = ArcMath.getEccentricity(arc2D);

		// major axis, minor axis
		final double w = arc2D.getWidth() / 2.;
		final double h = arc2D.getHeight() / 2.;

		// coordinates of point relative to center
		final double x = where.getX() - arc2D.getCenterX();
		final double y = where.getY() - arc2D.getCenterY();

		// angles between foci and point
		double angle1;
		double angle2;
		if (w >= h)
		{
			final double ea = e * w;
			angle1 = Math.atan2(y, x - ea);
			angle2 = Math.atan2(y, x + ea);
		}
		else
		{
			final double eb = e * h;
			angle1 = Math.atan2(y - eb, x);
			angle2 = Math.atan2(y + eb, x);
		}

		// bisector
		if (Math.abs(angle1 - angle2) > Math.PI)
		{
			angle2 += ArcMath.TWICEPI;
		}
		return (angle1 + angle2) / 2.;
	}

	/**
	 * Normalize angle
	 *
	 * @param angle angle to normalize
	 * @return normalized angle
	 */
	static double normalizeAngle(final double angle)
	{
		double a = angle;
		while (a <= -Math.PI)
		{
			a += ArcMath.TWICEPI;
		}
		while (a > Math.PI)
		{
			a -= ArcMath.TWICEPI;
		}
		if (Math.abs(a) > Math.PI)
		{
			System.err.print("Normalize error " + a);
		}
		return a;
	}

	/**
	 * Normalize angle diff
	 *
	 * @param firstAngle  angle 1
	 * @param secondAngle angle 2
	 * @return normalized angle difference
	 */
	static double normalizeAngleDiff(final double firstAngle, final double secondAngle)
	{
		double difference = secondAngle - firstAngle;
		while (difference < -Math.PI)
		{
			difference += ArcMath.TWICEPI;
		}
		while (difference > Math.PI)
		{
			difference -= ArcMath.TWICEPI;
		}
		return difference;
	}
}
