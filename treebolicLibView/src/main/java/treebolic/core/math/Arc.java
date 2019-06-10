package treebolic.core.math;

import androidx.annotation.NonNull;
import treebolic.core.location.Complex;

/**
 * Arc of radius r with center at x,y beginning 'start' and spanning 'angle' angles in radians
 *
 * @author Bernard Bou
 */
public class Arc
{
	// constants

	/**
	 * epsilon
	 */
	static private final double TWOPI = Math.PI * 2;

	/**
	 * epsilon
	 */
	static private final double E = 1.e-4;

	/**
	 * max radius
	 */
	static private final double R = 30.;

	// data

	/**
	 * center x
	 */
	public double x;

	/**
	 * center y
	 */
	public double y;

	/**
	 * radius
	 */
	public final double r;

	/**
	 * start
	 */
	public double start;

	/**
	 * angle
	 */
	public double angle;

	/**
	 * from-end
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@NonNull
	public final Complex from;

	/**
	 * to-end
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@NonNull
	public final Complex to;

	// C O N S T R U C T O R

	/**
	 * Construct segment of hyperbolic circle, which connects z1 and z2
	 *
	 * @param z1 start endpoint
	 * @param z2 end endpoint
	 */
	public Arc(@NonNull final Complex z1, @NonNull final Complex z2)
	{
		// ends
		this.from = z1;
		this.to = z2;

		// z1, z2, enter are aligned -> draw line
		if (intersectOrigin())
		{
			this.r = 0.;
			return;
		}

		// center
		final double s1 = 1. + z1.abs2();
		final double s2 = 1. + z2.abs2();
		final double norm = 1. / (2 * (z1.re * z2.im - z2.re * z1.im));
		final Complex center = new Complex((s1 * z2.im - s2 * z1.im) * norm, -(s1 * z2.re - s2 * z1.re) * norm);

		// radius
		final double radius = Distance.getEuclideanDistance(center, z2);
		if (radius > Arc.R)
		{
			this.r = 0.;
			return;
		}

		// start angle
		final Complex t1 = new Complex(z1).sub(center);
		double startAngle = t1.arg(); // Arg(z1-center);

		// end angle
		final Complex t2 = new Complex(z2).sub(center);
		double endAngle = t2.arg(); // Arg(z2-center);

		// normalize start angle
		if (startAngle < 0.)
		{
			startAngle += Arc.TWOPI;
		}

		// normalize end angle
		if (endAngle < 0.)
		{
			endAngle += Arc.TWOPI;
		}

		// angle extent
		double angleExtent = endAngle - startAngle;

		// normalize
		if (angleExtent > Math.PI)
		{
			angleExtent -= Arc.TWOPI;
		}
		else if (angleExtent < -Math.PI)
		{
			angleExtent += Arc.TWOPI;
		}

		// result
		this.x = center.re;
		this.y = center.im;
		this.r = radius;
		this.start = startAngle;
		this.angle = angleExtent;
	}

	/**
	 * Compute if this arc meets origin
	 *
	 * @return true if this arc meets origin
	 */
	private boolean intersectOrigin()
	{
		final double fromAbs = this.from.abs2();
		final double fromArg = this.from.arg();
		final double toAbs = this.to.abs2();
		final double toArg = this.to.arg();
		return fromAbs < Arc.E || toAbs < Arc.E || fromAbs >= Arc.E && toAbs >= Arc.E && (Math.abs(fromArg - toArg) < Arc.E || Math.abs(fromArg - toArg - Math.PI) < Arc.E || Math.abs(fromArg - toArg + Math.PI) < Arc.E);
	}

	/**
	 * Whether arc will be drawn counterclockwise
	 *
	 * @return whether arc will be drawn counterclockwise
	 */
	public boolean counterclockwise()
	{
		return this.angle < 0;
	}

	// @SuppressWarnings("boxing")
	// @Override
	// public String toString()
	// {
	// return String.format("arc x=%.0f y=%.0f r=%.0f, s=%.1f° %f, e=%.1f° %f ccw=%s", this.x, this.y, this.r, Math.toDegrees(this.start), this.start,
	// Math.toDegrees(this.angle), this.angle, counterclockwise());
	// }
}
