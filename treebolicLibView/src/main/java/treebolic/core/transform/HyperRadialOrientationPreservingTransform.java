/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.core.transform;

import treebolic.annotations.NonNull;
import treebolic.core.location.Complex;

/**
 * Radial orientation-preserving transform (preserves focus-root orientation)
 *
 * @author Bernard Bou
 */
public class HyperRadialOrientationPreservingTransform extends HyperTransform
{
	/**
	 * @param from from point
	 * @param to   to point
	 * @param root root point
	 */
	public HyperRadialOrientationPreservingTransform(@NonNull final Complex from, @NonNull final Complex to, @NonNull final Complex root)
	{
		// root->0
		this.xlat = new HyperTranslation(root).inverse();

		// from -> from1 -> to
		@NonNull final Complex from1 = this.xlat.map(new Complex(from));
		composeXlats(this.xlat, new HyperTranslation(from1, to));
	}
}
