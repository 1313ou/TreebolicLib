/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.core.transform;

import treebolic.annotations.NonNull;
import treebolic.core.location.Complex;

/**
 * Rotation in hyperbolic space
 *
 * @author Bernard Bou
 */
public class HyperRotation extends Complex
{
	private static final long serialVersionUID = -7077490742779857511L;

	// C O N S T R U C T O R

	/**
	 * Default constructor
	 */
	public HyperRotation()
	{
		super();
	}

	/**
	 * Constructor from rotation expressed in cartesian value relative to (0,0) origin and x-axis
	 *
	 * @param z rotation expressed as cartesian value
	 */
	public HyperRotation(@NonNull final Complex z)
	{
		super(z);
	}

	/**
	 * Constructor from rotation expressed in cartesian value relative to (0,0) origin and x-axis
	 *
	 * @param x x-coordinate of point expressing rotation expressed as cartesian value
	 * @param y y-coordinate of point expressing rotation expressed as cartesian value
	 */
	public HyperRotation(final double x, final double y)
	{
		super(x, y);
	}

	// C O N V E R S I O N

	/**
	 * Convert to complex expressing rotation as cartesian value relative to (0,0) origin and x-axis
	 *
	 * @return return complex expressing in cartesian value
	 */
	@NonNull
	public Complex toComplex()
	{
		return this;
	}

	// M A P

	/**
	 * Rotate point
	 *
	 * @param z     input point
	 * @param angle rotation angle
	 * @return this point with rotated values
	 */
	@NonNull
	@SuppressWarnings("WeakerAccess")
	static public Complex map(@NonNull final Complex z, @NonNull final Complex angle)
	{
		// z = (z*theta+p)/(1+(~p)*z)
		// = (z*theta+0)/(1+(~0)*z)
		// = z*theta
		return z.mul(angle);
	}

	/**
	 * Rotate point
	 *
	 * @param z point
	 * @return rotated point
	 */
	@NonNull
	public Complex map(@NonNull final Complex z)
	{
		return HyperRotation.map(z, this);
	}

	// C O M P O S E

	/**
	 * Compose rotation with translation
	 *
	 * @param r1 rotation
	 * @param t2 translation
	 * @return transform
	 */
	@NonNull
	static public HyperTransform compose(final HyperRotation r1, final HyperTranslation t2)
	{
		// t1.p = p1 = ~p1 = (0,0)
		// t2.theta = theta2 = (1,0)
		// t1 applied first
		// t1 o t2
		// (t1 o t2)(x) = t2(t1(x))
		// ~p=conjugate of p
		// p = (theta2*p1+p2) /(1+theta2*p1*~p2);
		// = (1*0+p2) /(1+1*0*~p2);
		// = (p2) /(1);
		// = p2
		// theta = (theta1*theta2+theta1*~p1*p2) /(1+theta2*p1*~p2);
		// = (theta1*1+theta1*0*p2) /(1+1*0*~p2);
		// = (theta1) /(1);
		// = theta1*(1+0*p2) /(1+theta2*p1*~p2); (factored)
		// = theta1*(1) /(1+1*0*~p2); (factored)
		// = theta1 (factored)

		return new HyperTransform(t2, r1);
	}

	// R E V E R S E

	/**
	 * Reverse
	 *
	 * @return this rotation with reversed values
	 */
	@NonNull
	@SuppressWarnings("UnusedReturnValue")
	public HyperRotation inverse()
	{
		conj();
		return this;
	}
}
