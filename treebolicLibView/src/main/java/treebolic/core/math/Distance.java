/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.core.math;

import androidx.annotation.NonNull;
import treebolic.core.location.Complex;

/**
 * Distance utilities
 *
 * @author Bernard Bou
 */
public class Distance
{
	// E U C L I D I A N . D I S T A N C E

	/**
	 * Get Euclidean distance
	 *
	 * @param z1 point
	 * @param z2 point
	 * @return Euclidean distance between z1 and z2
	 */
	static public double getEuclideanDistance(@NonNull final Complex z1, @NonNull final Complex z2)
	{
		final double dx = z1.re - z2.re;
		final double dy = z1.im - z2.im;
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Get squared Euclidean distance
	 *
	 * @param z1 point
	 * @param z2 point
	 * @return squared Euclidean distance between z1 and z2
	 */
	static public double getEuclideanDistanceSquared(@NonNull final Complex z1, @NonNull final Complex z2)
	{
		final double dx = z1.re - z2.re;
		final double dy = z1.im - z2.im;
		return dx * dx + dy * dy;
	}

	// H Y P E R B O L I C . D I S T A N C E

	/**
	 * Distance between 2 points
	 *
	 * @param z1 point1
	 * @param z2 point2
	 * @return Hyperbolic distance between two points
	 */
	static public double getHyperDistance(@NonNull final Complex z1, @NonNull final Complex z2)
	{
		// 2*atanh((z1-z2)/(1-z1*~z2));
		// 2*atanh(z) if distance from 0,0 origin
		final Complex denom = new Complex().conj(z2).mul(z1).neg().add(Complex.ONE);
		final Complex z = new Complex().sub(z1, z2).div(denom);
		return 2. * XMath.atanh(z.mag());
	}

	// C O N V E R S I O N

	/**
	 * Map Euclidean distance to origin to Hyperbolic distance to origin
	 *
	 * @param re Euclidean distance to origin
	 * @return Hyperbolic distance to origin
	 */
	static public double distanceToOrigin_e2h(final double re)
	{
		return 2. * XMath.atanh(re);
		// return (re+re) / (1.0 + re*re);
	}

	/**
	 * Map Hyperbolic distance to origin to Euclidean distance to origin
	 *
	 * @param rh Hyperbolic distance to origin
	 * @return Euclidean distance to origin
	 */
	static public double distanceToOrigin_h2e(final double rh)
	{
		return XMath.tanh(rh * 0.5);
		// return rh / (1.0 + Math.sqrt( 1.0 - rh*rh ));
	}
}
