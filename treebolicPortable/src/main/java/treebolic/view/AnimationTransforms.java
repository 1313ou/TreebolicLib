/**
 * Title : Treebolic
 * Description : Treebolic
 * Version : 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 *
 * Update : Mon Mar 10 00:00:00 CEST 2008
 */
package treebolic.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import treebolic.core.Transformer;
import treebolic.core.location.Complex;
import treebolic.core.math.Distance;
import treebolic.core.transform.HyperTransform;
import treebolic.core.transform.HyperTranslation;

/**
 * Animation implements sequence of transforms in animation of tree
 *
 * @author Bernard Bou
 */
public class AnimationTransforms
{
	/**
	 * Sequence of transforms
	 */
	public final List<HyperTransform> theTransforms;

	/**
	 * Behaviour
	 */
	static private final boolean finalTransformOnly = false;

	/**
	 * Constructor
	 *
	 * @param theseTransforms
	 *            list of transforms
	 */
	protected AnimationTransforms(final List<HyperTransform> theseTransforms)
	{
		this.theTransforms = theseTransforms;
	}

	/**
	 * Constructor
	 *
	 * @param thisFrom
	 *            source point
	 * @param thisTo
	 *            target point
	 * @param thisTransformer
	 *            transform generator
	 * @param thisOrientation
	 *            orientation
	 * @param theseSteps
	 *            number of steps
	 */
	static public AnimationTransforms make(final Complex thisFrom, final Complex thisTo, final Transformer thisTransformer, final Complex thisOrientation,
			final int theseSteps)
	{
		final List<HyperTransform> theseTransforms = AnimationTransforms.finalTransformOnly ? AnimationTransforms.makeTransform1(thisFrom, thisTo,
				thisTransformer, thisOrientation) : AnimationTransforms.makeTransforms(thisFrom, thisTo, thisTransformer, thisOrientation, theseSteps);
		return new AnimationTransforms(theseTransforms);
	}

	/**
	 * Make sequence of transforms
	 *
	 * @param thisFrom
	 *            source point
	 * @param thisTo
	 *            target point
	 * @param thisTransformer
	 *            transform generator
	 * @param thisOrientation
	 *            orientation
	 * @param thoseSteps
	 *            number of steps
	 * @return transforms or null
	 */
	static private List<HyperTransform> makeTransforms(final Complex thisFrom, final Complex thisTo, final Transformer thisTransformer,
			final Complex thisOrientation, final int thoseSteps)
	{
		int theseSteps = thoseSteps;
		final HyperTransform thisCurrentTransform = thisTransformer.getTransform();
		final double thisHDist = Distance.getHyperDistance(thisFrom, thisTo);
		if (thisHDist != 0.)
		{
			// steps
			if (theseSteps == 0)
			{
				theseSteps = (int) (thisHDist * 3);
			}

			// vector
			final List<HyperTransform> theseTransforms = new ArrayList<>(theseSteps);

			// final transform
			final HyperTransform thisFinalTransform = thisTransformer.makeTransform(thisFrom, thisTo, thisOrientation);
			final HyperTransform thisFinalTransformInverse = new HyperTransform(thisFinalTransform).inverse();

			// this point will eventually transform to (0,0)
			final Complex z0 = thisFinalTransformInverse.map(new Complex(Complex.ZERO));
			final HyperTranslation thisXlat = new HyperTranslation(z0);

			// distance = arc diameter
			final double thisDist = thisXlat.mag();
			// double thisRadius = thisDist / 2.;

			// normalize z0
			final Complex theta = new Complex(z0).divide(thisDist);

			// iterate and make middle transforms
			for (int i = 1; i < theseSteps; ++i)
			{
				// middle transform
				HyperTransform thisiTransform;
				if (thisDist != 0.)
				{
					// progress 1/n 2/n ... i/n ... n/n
					final double thisProgress = (double) i / (double) theseSteps;

					// i distance
					final double thisiDist = Distance.distanceToOrigin_h2e(Distance.distanceToOrigin_e2h(thisDist) * thisProgress);

					// linear
					final Complex z = new Complex(thisiDist, 0.);

					// rotate z by theta
					z.mul(theta);

					// make z->0 transform
					thisiTransform = thisTransformer.makeTransform(z, Complex.ZERO, thisOrientation);
				}
				else
				{
					thisiTransform = thisFinalTransform;
				}

				// add to sequence
				theseTransforms.add(new HyperTransform(thisCurrentTransform).compose(thisiTransform));
			}

			// last in sequence
			theseTransforms.add(new HyperTransform(thisCurrentTransform).compose(thisFinalTransform));
			return theseTransforms;
		}
		return null;
	}

	/**
	 * Make animation with one transform only
	 *
	 * @param thisFrom
	 *            source point
	 * @param thisTo
	 *            source point
	 * @param thisTransformer
	 *            transform generator
	 * @param thisOrientation
	 *            orientation
	 * @return sequence of one transform
	 */
	static private Vector<HyperTransform> makeTransform1(final Complex thisFrom, final Complex thisTo, final Transformer thisTransformer,
			final Complex thisOrientation)
	{
		final HyperTransform thisCurrentTransform = thisTransformer.getTransform();

		// final transform
		final HyperTransform thisFinalTransform = thisTransformer.makeTransform(thisFrom, thisTo, thisOrientation);

		// vector
		final Vector<HyperTransform> theseTransforms = new Vector<>(1);
		theseTransforms.addElement(new HyperTransform(thisCurrentTransform).compose(thisFinalTransform));
		return theseTransforms;
	}
}
