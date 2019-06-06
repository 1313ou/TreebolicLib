package treebolic.core.math;

import android.support.annotation.NonNull;

import treebolic.core.location.Complex;
import treebolic.core.location.EuclideanCircle;
import treebolic.core.location.HyperCircle;
import treebolic.core.transform.HyperTranslation;
import treebolic.model.Location;

/**
 * Mapper of hypercircle to euclidean circle. This spares us the trouble of drawing circle with hyperbolic sin. Strictly speaking each point in the hyperspace
 * is mapped to the same point in euclidean space. Only the metric changes.
 *
 * @author Bernard Bou
 */
public class MapperToEuclidean
{
	@SuppressWarnings("WeakerAccess")
	public static final boolean adjustCircle = true;

	/**
	 * Map hyperspace circle to unit circle euclidean space
	 *
	 * @param location location
	 */
	static public void mapToEuclidean(@NonNull final Location location)
	{
		MapperToEuclidean.mapToEuclidean(location.hyper, location.euclidean);
	}

	/**
	 * Map hyperspace circle to unit circle euclidean space
	 *
	 * @param hyperCircle     hypercircle
	 * @param euclideanCircle euclidean circle
	 */
	@SuppressWarnings("WeakerAccess")
	static public void mapToEuclidean(@NonNull final HyperCircle hyperCircle, @NonNull final EuclideanCircle euclideanCircle)
	{
		// euclidean radius, assuming center is at origin
		euclideanCircle.radius = Distance.distanceToOrigin_h2e(hyperCircle.radius);

		// distance to (0,0)
		if (hyperCircle.dist != 0.)
		{
			if (MapperToEuclidean.adjustCircle)
			{
				// map this hypercircle circle HC (center, radius) to euclidean
				// circle EC (origin, eradius)
				// z1 and z2 are the ends of diameter on (0,C) direction
				final Complex z1 = new Complex(hyperCircle.center).multiply(euclideanCircle.radius / hyperCircle.dist);
				final Complex z2 = new Complex(z1).neg();

				// hyperbolic translation of this diameter
				HyperTranslation.map(z1, hyperCircle.center);
				HyperTranslation.map(z2, hyperCircle.center);

				// middle of mapped diameter
				z1.add(z2).multiply(0.5);

				// computed center
				euclideanCircle.center.set(z1);
				euclideanCircle.radius = z2.sub(z1).mag();
			}
			else
			{
				euclideanCircle.center.re = hyperCircle.center.re;
				euclideanCircle.center.im = hyperCircle.center.im;
			}
		}
		else
		{
			// this is (0,0)
			euclideanCircle.center.set(hyperCircle.center);
		}

		// flag computation state
		hyperCircle.isDirty = false;
	}
}
