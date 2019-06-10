package treebolic.core.transform;

import androidx.annotation.NonNull;
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
	 * @param from        from-point
	 * @param to          to-point
	 * @param orientation orientation
	 */
	public HyperOrientationPreservingTransform(@NonNull final Complex from, @NonNull final Complex to, @NonNull final Complex orientation)
	{
		// the HXlat(from, to) translates 'from' to 'to' below but has
		// rotational side-effects
		// we want a transform which maps 'from' to 'to' like HXlat(from, to)
		// but unlike HXlat(from, to) has no rotational effect for
		// orientation (tail preserving)

		// such a result is the composition of t1 and r2 and t2
		// t1 and t2 are translations
		// r2 accounts for offsetting of rotational side-effects for a given
		// point (orientation))
		// the composition of r2 and t2 is the transform t(t2,r2)
		// we are trying to compute r2

		// if we want no orientation-preserving rotation :
		// uncomment line below
		// and comment out the rest

		// super(new HXlat(from, to, true));
		// return;

		// t1
		// xlat = new HXlat(from).inverse();
		super(new HyperTranslation(from).inverse());

		// orientation points at tail
		final Complex head = new Complex(orientation).neg();

		// t2
		final HyperTranslation t2 = new HyperTranslation(to);

		// map theta by t1
		final Complex theta1 = this.xlat.map(new Complex(head));

		// map theta by t2 inverse()
		final Complex theta2 = t2.mapinv(new Complex(head));

		// we compute the rotation
		// angle(theta2) - angle(theta1)
		theta2.div(theta1);

		// make rotation to offset computed side-effect rotation
		final HyperRotation r2 = new HyperRotation(theta2);

		// compose
		compose(HyperRotation.compose(r2, t2));
	}
}
