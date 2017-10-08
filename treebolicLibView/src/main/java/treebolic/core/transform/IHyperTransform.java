package treebolic.core.transform;

import treebolic.core.location.Complex;

/**
 * Hyperbolic transform interface
 *
 * @author Bernard Bou
 */
public interface IHyperTransform
{
	/**
	 * Map point in hyperbolic space
	 *
	 * @param z point is hyperbolic space
	 * @return map z to z' with z'=map(z)
	 */
	Complex map(Complex z);
}
