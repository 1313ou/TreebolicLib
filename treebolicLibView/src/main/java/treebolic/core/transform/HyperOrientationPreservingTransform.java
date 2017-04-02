package treebolic.core.transform;

import treebolic.core.location.Complex;

/**
 * Orientation-preserving transform
 *
 * @author Bernard Bou
 */
public class HyperOrientationPreservingTransform extends HyperTransform
{
	/**
	 * Construct orientation-preserving hyperbolic transform which translates from-point to to-point while preserving given orientation
	 *
	 * @param from
	 *            from-point
	 * @param to
	 *            to-point
	 * @param thisOrientation
	 *            orientation
	 */
	public HyperOrientationPreservingTransform(final Complex from, final Complex to, final Complex thisOrientation)
	{
		// the HXlat(from, to) translates 'from' to 'to' below but has
		// rotational side-effects
		// we want a transform which maps 'from' to 'to' like HXlat(from, to)
		// but unlike HXlat(from, to) has no rotational effect for
		// thisOrientation (tail preserving)

		// such a result is the composition of t1 and r2 and t2
		// t1 and t2 are translations
		// r2 accounts for offsetting of rotational side-effects for a given
		// point (thisOrientation))
		// the composition of r2 and t2 is the transform t(t2,r2)
		// we are trying to compute r2

		// if we want no orientation-preserving rotation :
		// uncomment line below
		// and comment out the rest

		// super(new HXlat(from, to, true));
		// return;

		// t1
		// theXlat = new HXlat(from).inverse();
		super(new HyperTranslation(from).inverse());

		// thisOrientation points at tail
		final Complex thisHead = new Complex(thisOrientation).neg();

		// t2
		final HyperTranslation t2 = new HyperTranslation(to);

		// map theta by t1
		final Complex thisTheta1 = this.theXlat.map(new Complex(thisHead));

		// map theta by t2 inverse()
		final Complex thisTheta2 = t2.mapinv(new Complex(thisHead));

		// we compute the rotation
		// angle(theta2) - angle(theta1)
		thisTheta2.div(thisTheta1);

		// make rotation to offset computed side-effect rotation
		final HyperRotation r2 = new HyperRotation(thisTheta2);

		// compose
		compose(HyperRotation.compose(r2, t2));
	}
}
