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
	 * @param z
	 *            point is hyperbolic space
	 * @return map z to z' with z'=map(z)
	 */
	Complex map(Complex z);
}
