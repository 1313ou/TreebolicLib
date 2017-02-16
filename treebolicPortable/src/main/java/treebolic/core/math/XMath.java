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
package treebolic.core.math;

/**
 * Encapsulation of some hyperbolic trigonometry functions
 *
 * @author Bernard Bou
 */
public class XMath
{
	// H Y P E R B O L I C . T R I G O N O M E T R Y

	/**
	 * Hyperbolic tangence
	 *
	 * @param x0
	 *            input
	 * @return hyperbolic tangence of x
	 */
	static public double tanh(final double x0)
	{
		double x = x0;
		x = Math.exp(x);
		x *= x;
		return (x - 1) / (x + 1);
	}

	/**
	 * Hyperbolic arc tangence
	 *
	 * @param x
	 *            input
	 * @return hyperbolic atan
	 */
	static public double atanh(final double x)
	{
		return 0.5 * Math.log((1 + x) / (1 - x));
	}
}
