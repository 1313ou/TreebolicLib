package treebolic.core.transform;

import android.support.annotation.NonNull;

import treebolic.core.location.Complex;

/**
 * Radial orientation-preserving transform (preserves focus-root orientation)
 *
 * @author Bernard Bou
 */
public class HyperRadialOrientationPreservingTransform extends HyperTransform
{
	public HyperRadialOrientationPreservingTransform(@NonNull final Complex from, @NonNull final Complex to, @NonNull final Complex thisRoot)
	{
		// thisRoot->0
		this.theXlat = new HyperTranslation(thisRoot).inverse();

		// from -> from1 -> to
		final Complex from1 = this.theXlat.map(new Complex(from));
		composeXlats(this.theXlat, new HyperTranslation(from1, to));
	}
}
