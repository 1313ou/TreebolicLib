package treebolic.core.transform;

import treebolic.core.location.Complex;

/**
 * Radial orientation-preserving transform (preserves focus-root orientation)
 *
 * @author Bernard Bou
 */
public class HyperRadialOrientationPreservingTransform extends HyperTransform
{
	public HyperRadialOrientationPreservingTransform(final Complex from, final Complex to, final Complex thisRoot)
	{
		// thisRoot->0
		this.theXlat = new HyperTranslation(thisRoot).inverse();

		// from -> from1 -> to
		final Complex from1 = this.theXlat.map(new Complex(from));
		composeXlats(this.theXlat, new HyperTranslation(from1, to));
	}
}
