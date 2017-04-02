/**
 *
 */
package treebolic.core.math;

import treebolic.glue.Arc2D;
import treebolic.glue.Point2D;

/**
 * Arc Utils
 *
 * @author Bernard Bou
 */
public class ArcMath
{
	static final double TWICEPI = 2. * Math.PI;

	static double HALFPI = .5 * Math.PI;

	/**
	 * Get arc eccentricity
	 *
	 * @param thisArc2D arc
	 * @return eccentricity of arc
	 */
	static private double getEccentricity(final Arc2D thisArc2D)
	{
		final double a = thisArc2D.getWidth() / 2.;
		final double b = thisArc2D.getHeight() / 2.;
		final double d = a > b ? b * b / (a * a) : a * a / (b * b);
		return Math.sqrt(1 - d);
	}

	/**
	 * Get mid-arc point
	 *
	 * @param thisArc2D arc
	 * @return point at mid-arc
	 */
	public static Point2D getMidArc(final Arc2D thisArc2D)
	{
		final double a = thisArc2D.getWidth() / 2.;
		final double b = thisArc2D.getHeight() / 2.;
		final double theta = Math.toRadians(-thisArc2D.getAngleStart()) + Math.toRadians(-thisArc2D.getAngleExtent() / 2.);
		final double x = a * Math.cos(theta);
		final double y = b * Math.sin(theta);
		return new Point2D(x + thisArc2D.getCenterX(), y + thisArc2D.getCenterY());
	}

	/**
	 * Get tangent to this arc at given point
	 *
	 * @param thisArc2D
	 *            arc
	 * @param thisWhere
	 *            point on arc
	 * @return tangent of this arc at given point
	 */
	/*
	 * public static double getTangent0(final Arc2D thisArc2D, final Point2D thisWhere) { // angle final double thisBissector = ArcMath.getAngleAt(thisArc2D,
	 * thisWhere); // tangent is perpendicular to radius angle return thisBissector + Math.PI / 2.; }
	 */

	/**
	 * Get tangent to this arc at given point (used for text orientation)
	 *
	 * @param thisArc2D arc
	 * @param thisWhere point on arc
	 * @return tangent of this arc at given point
	 */
	public static double getTextTangent(final Arc2D thisArc2D, final Point2D thisWhere)
	{
		// center, eccentricity, major axis, minor axis
		final double x = thisWhere.getX() - thisArc2D.getCenterX();
		final double y = thisWhere.getY() - thisArc2D.getCenterY();
		final double e = ArcMath.getEccentricity(thisArc2D);
		final double a = thisArc2D.getWidth() / 2.;
		final double b = thisArc2D.getHeight() / 2.;

		// angles between foci and point
		double thisAngle1;
		double thisAngle2;
		if (a >= b)
		{
			final double ea = e * a;
			thisAngle1 = Math.atan2(y, x - ea);
			thisAngle2 = Math.atan2(y, x + ea);
		}
		else
		{
			final double eb = e * b;
			thisAngle1 = Math.atan2(y - eb, x);
			thisAngle2 = Math.atan2(y + eb, x);
		}
		// tangent
		final double thisBissector = (thisAngle1 + thisAngle2) / 2.;
		return thisBissector + Math.PI / 2.;
	}

	/**
	 * Get tangent to arc at given point, the orientation returned is inwards arc
	 *
	 * @param thisArc2D arc
	 * @param thisWhere point on arc
	 * @param isStart   whether point is start point
	 * @return tangent of this arc at given point
	 */
	public static double getTangent(final Arc2D thisArc2D, final Point2D thisWhere, final boolean isStart)
	{
		// angle
		double thisAngle = ArcMath.getAngleAt(thisArc2D, thisWhere);

		// tangent is perpendicular to radius angle
		final boolean counterclockwise = thisArc2D.getCounterclockwise();
		if (counterclockwise)
		{
			thisAngle += isStart ? -Math.PI / 2. : +Math.PI / 2.;
		}
		else
		{
			thisAngle += isStart ? +Math.PI / 2. : -Math.PI / 2.;
		}

		return thisAngle;
	}

	/**
	 * Get radius angle at given point on circle arc
	 *
	 * @param thisArc2D arc
	 * @param thisWhere point on arc
	 * @return angle (of radius) at given point on this Arc2D
	 */
	public static double getCircleAngleAt(final Arc2D thisArc2D, final Point2D thisWhere)
	{
		// coordinates of point relative to center
		final double x = thisWhere.getX() - thisArc2D.getCenterX();
		final double y = thisWhere.getY() - thisArc2D.getCenterY();
		return Math.atan2(y, x);
	}

	/**
	 * Get radius angle at point on arc
	 *
	 * @param thisArc2D arc
	 * @param thisWhere point on arc where (outgoing) radius orientation is to be computed
	 * @return (outgoing) radius orientation
	 */
	private static double getAngleAt(final Arc2D thisArc2D, final Point2D thisWhere)
	{
		// eccentricity, this normally doesn't have to be recomputed : it is dependent on surface dimensions
		final double e = ArcMath.getEccentricity(thisArc2D);

		// major axis, minor axis
		final double w = thisArc2D.getWidth() / 2.;
		final double h = thisArc2D.getHeight() / 2.;

		// coordinates of point relative to center
		final double x = thisWhere.getX() - thisArc2D.getCenterX();
		final double y = thisWhere.getY() - thisArc2D.getCenterY();

		// angles between foci and point
		double thisAngle1;
		double thisAngle2;
		if (w >= h)
		{
			final double ea = e * w;
			thisAngle1 = Math.atan2(y, x - ea);
			thisAngle2 = Math.atan2(y, x + ea);
		}
		else
		{
			final double eb = e * h;
			thisAngle1 = Math.atan2(y - eb, x);
			thisAngle2 = Math.atan2(y + eb, x);
		}

		// bisector
		if (Math.abs(thisAngle1 - thisAngle2) > Math.PI)
		{
			thisAngle2 += ArcMath.TWICEPI;
		}
		return (thisAngle1 + thisAngle2) / 2.;
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
