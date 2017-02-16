package treebolic.core.math;

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
	public static final boolean adjustCircle = true;

	/**
	 * Map hyperspace circle to unit circle euclidean space
	 *
	 * @param thisLocation
	 *            location
	 */
	static public void mapToEuclidean(final Location thisLocation)
	{
		MapperToEuclidean.mapToEuclidean(thisLocation.hyper, thisLocation.euclidean);
	}

	/**
	 * Map hyperspace circle to unit circle euclidean space
	 *
	 * @param thisHyperCircle
	 *            hypercircle
	 * @param thisEuclideanCircle
	 *            euclidean circle
	 */
	static public void mapToEuclidean(final HyperCircle thisHyperCircle, final EuclideanCircle thisEuclideanCircle)
	{
		// euclidean radius, assuming center is at origin
		thisEuclideanCircle.radius = Distance.distanceToOrigin_h2e(thisHyperCircle.radius);

		// distance to (0,0)
		if (thisHyperCircle.dist != 0.)
		{
			if (MapperToEuclidean.adjustCircle)
			{
				// map this hypercircle circle HC (center, radius) to euclidean
				// circle EC (origin, eradius)
				// z1 and z2 are the ends of diameter on (0,C) direction
				final Complex z1 = new Complex(thisHyperCircle.center).multiply(thisEuclideanCircle.radius / thisHyperCircle.dist);
				final Complex z2 = new Complex(z1).neg();

				// hyperbolic translation of this diameter
				HyperTranslation.map(z1, thisHyperCircle.center);
				HyperTranslation.map(z2, thisHyperCircle.center);

				// middle of mapped diameter
				z1.add(z2).multiply(0.5);

				// computed center
				thisEuclideanCircle.center.set(z1);
				thisEuclideanCircle.radius = z2.sub(z1).mag();
			}
			else
			{
				thisEuclideanCircle.center.re = thisHyperCircle.center.re;
				thisEuclideanCircle.center.im = thisHyperCircle.center.im;
			}
		}
		else
		{
			// this is (0,0)
			thisEuclideanCircle.center.set(thisHyperCircle.center);
		}

		// flag computation state
		thisHyperCircle.isDirty = false;
	}
}
