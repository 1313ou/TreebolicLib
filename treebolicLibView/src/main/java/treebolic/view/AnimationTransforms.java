/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import androidx.annotation.NonNull;
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
	public final List<HyperTransform> transforms;

	/**
	 * Behaviour
	 */
	static private final boolean FINAL_TRANSFORM_ONLY = false;

	/**
	 * Constructor
	 *
	 * @param transforms list of transforms
	 */
	@SuppressWarnings("WeakerAccess")
	protected AnimationTransforms(final List<HyperTransform> transforms)
	{
		this.transforms = transforms;
	}

	/**
	 * Constructor
	 *
	 * @param from        source point
	 * @param to          target point
	 * @param transformer transform generator
	 * @param orientation orientation
	 * @param steps       number of steps
	 */
	static public AnimationTransforms make(@NonNull final Complex from, @NonNull final Complex to, @NonNull final Transformer transformer, @NonNull final Complex orientation, @SuppressWarnings("SameParameterValue") final int steps)
	{
		final List<HyperTransform> transforms = AnimationTransforms.FINAL_TRANSFORM_ONLY ? AnimationTransforms.makeTransform1(from, to, transformer, orientation) : AnimationTransforms.makeTransforms(from, to, transformer, orientation, steps);
		return new AnimationTransforms(transforms);
	}

	/**
	 * Make sequence of transforms
	 *
	 * @param from         source point
	 * @param to           target point
	 * @param transformer  transform generator
	 * @param oOrientation orientation
	 * @param steps0       number of steps
	 * @return transforms or null
	 */
	static private List<HyperTransform> makeTransforms(@NonNull final Complex from, @NonNull final Complex to, @NonNull final Transformer transformer, @NonNull final Complex oOrientation, final int steps0)
	{
		int steps = steps0;
		final HyperTransform currentTransform = transformer.getTransform();
		final double hDist = Distance.getHyperDistance(from, to);
		if (hDist != 0.)
		{
			// steps
			if (steps == 0)
			{
				steps = (int) (hDist * 3);
			}

			// vector
			final List<HyperTransform> transforms = new ArrayList<>(steps);

			// final transform
			final HyperTransform finalTransform = transformer.makeTransform(from, to, oOrientation);
			final HyperTransform finalTransformInverse = new HyperTransform(finalTransform).inverse();

			// this point will eventually transform to (0,0)
			final Complex z0 = finalTransformInverse.map(new Complex(Complex.ZERO));
			final HyperTranslation xlat = new HyperTranslation(z0);

			// distance = arc diameter
			final double dist = xlat.mag();
			// double radius = dist / 2.;

			// normalize z0
			final Complex theta = new Complex(z0).divide(dist);

			// iterate and make middle transforms
			for (int i = 1; i < steps; ++i)
			{
				// middle transform
				HyperTransform transform;
				if (dist != 0.)
				{
					// progress 1/n 2/n ... i/n ... n/n
					final double progress = (double) i / (double) steps;

					// i distance
					final double di = Distance.distanceToOrigin_h2e(Distance.distanceToOrigin_e2h(dist) * progress);

					// linear
					final Complex z = new Complex(di, 0.);

					// rotate z by theta
					z.mul(theta);

					// make z->0 transform
					transform = transformer.makeTransform(z, Complex.ZERO, oOrientation);
				}
				else
				{
					transform = finalTransform;
				}

				// add to sequence
				transforms.add(new HyperTransform(currentTransform).compose(transform));
			}

			// last in sequence
			transforms.add(new HyperTransform(currentTransform).compose(finalTransform));
			return transforms;
		}
		return null;
	}

	/**
	 * Make animation with one transform only
	 *
	 * @param from        source point
	 * @param to          source point
	 * @param transformer transform generator
	 * @param orientation orientation
	 * @return sequence of one transform
	 */
	@NonNull
	static private List<HyperTransform> makeTransform1(@NonNull final Complex from, @NonNull final Complex to, @NonNull final Transformer transformer, @NonNull final Complex orientation)
	{
		final HyperTransform currentTransform = transformer.getTransform();

		// final transform
		final HyperTransform finalTransform = transformer.makeTransform(from, to, orientation);

		// vector
		final Vector<HyperTransform> transforms = new Vector<>(1);
		transforms.addElement(new HyperTransform(currentTransform).compose(finalTransform));
		return transforms;
	}
}
