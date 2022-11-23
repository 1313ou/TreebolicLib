/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.core.transform;

import treebolic.annotations.NonNull;
import treebolic.core.location.Complex;

/**
 * Translation
 *
 * @author Bernard Bou
 */
public class HyperTranslation extends Complex
{
	private static final long serialVersionUID = -6442241719154521565L;

	// logically inherits from HTransform (a specific case of transform where
	// theta =(1,0)
	// does hyperbolic translation of z
	// this translation moves center of hyperbolic plane into point p
	// p is outbound
	// this translation moves point ~p to center of circle

	// translation: abs(p) <1

	// C O N S T R U C T O R

	/**
	 * Default constructor (defaulting to null translation)
	 */
	public HyperTranslation()
	{
		super();
	}

	/**
	 * Constructor from point, cartesian representation of translation (relative to 0,0 origin)
	 *
	 * @param p cartesian representation of translation as a point (relative to 0,0 origin)
	 */
	public HyperTranslation(@NonNull final Complex p)
	{
		super(p);
	}

	/**
	 * Constructor from point, cartesian representation of translation (relative to 0,0 origin)
	 *
	 * @param x x-coordinate of cartesian representation of translation (relative to 0,0 origin)
	 * @param y y-coordinate of cartesian representation of translation (relative to 0,0 origin)
	 */
	public HyperTranslation(final double x, final double y)
	{
		super(x, y);
	}

	/**
	 * Constructor of hyperbolic translation which maps 'from' to 0,0 and 0,0 to 'to'
	 *
	 * @param from  source point to be translated to 0,0
	 * @param to    target point 0,0 is to be translated to
	 * @param dummy dummy
	 */
	public HyperTranslation(@NonNull final Complex from, @NonNull final Complex to, @SuppressWarnings({"SameParameterValue", "unused"}) final boolean dummy)
	{
		// make hyperbolic translation which maps 'from' to 0,0 and 0,0 to 'to'
		// but this does NOT imply that t(from) = to
		super(to);
		sub(from);
	}

	// M A K E X L A T

	/**
	 * Construct translation from source point and destination points
	 *
	 * @param from source point
	 * @param to   destination point
	 */
	public HyperTranslation(@NonNull final Complex from, @NonNull final Complex to)
	{
		// make hyperbolic translation which maps 'from' to 'to'
		// t(from) = to
		// p + from
		// ----------- = to
		// 1 + ~p.from
		//
		// p + from = to + ~p.to.from
		// p - ~p.to.from = to - from
		// ~n = |n| / n

		// w1 = to.from
		@NonNull final Complex w1 = new Complex(new Complex(to)).mul(from);
		final double d = 1.0 - w1.abs2();

		// w1= ~(to.from)
		w1.conj();

		// w2 = (to-from)
		@NonNull final Complex w2 = new Complex(to).sub(from);

		// w1 = (to-from).~(to.from)
		w1.mul(w2);

		// ((to-from) + (to-from).~(to.from))/d
		this.re = (w2.re + w1.re) / d;
		// ((to-from) - (to-from).~(to.from))/d
		this.im = (w2.im - w1.im) / d;
	}

	// C O N V E R S I O N

	/**
	 * Convert to cartesian representation of translation as point relative to 0,0
	 *
	 * @return cartesian representation of translation as point relative to 0,0
	 */
	@NonNull
	public Complex toComplex()
	{
		return this;
	}

	// M A P

	/**
	 * Map point by translation expressed as point
	 *
	 * @param z point to be mapped
	 * @param p point expressing translation relative to 0,0
	 * @return this point with mapped values
	 */
	@NonNull
	static public Complex map(@NonNull final Complex z, @NonNull final Complex p)
	{
		// z = (z*theta+p)/(1+(~p)*z)
		// = (z*1+p)/(1+(~p)*z)
		// = (z+p)/(1+(~p)*z)
		@NonNull final Complex nom = new Complex().add(z, p);
		@NonNull final Complex conjp = new Complex().conj(p);
		@NonNull final Complex denom = new Complex().mul(conjp, z).add(Complex.ONE);
		return z.div(nom, denom);
	}

	/**
	 * Double-map point by translations expressed as points
	 *
	 * @param z  point to be mapped
	 * @param p1 point expressing translation relative to 0,0
	 * @param p2 point expressing translation relative to 0,0
	 * @return this point with mapped values
	 */
	@NonNull
	@SuppressWarnings("UnusedReturnValue")
	static public Complex map2(@NonNull final Complex z, @NonNull final Complex p1, @NonNull final Complex p2)
	{
		return HyperTranslation.map(HyperTranslation.map(z, p1), p2);
	}

	/**
	 * Map point by inverse of translation expressed as point
	 *
	 * @param z point to be mapped
	 * @param p point expressing translation relative to 0,0
	 * @return this point with mapped values (by inverse translation)
	 */
	@NonNull
	@SuppressWarnings("WeakerAccess")
	static public Complex mapinv(@NonNull final Complex z, @NonNull final Complex p)
	{
		@NonNull final Complex pinv = new Complex(p).neg();
		@NonNull final Complex nom = new Complex().add(z, pinv);
		@NonNull final Complex conjpinv = new Complex().conj(pinv);
		@NonNull final Complex denom = new Complex().mul(conjpinv, z).add(Complex.ONE);
		return z.div(nom, denom);
	}

	/**
	 * Map point
	 *
	 * @param z point to be mapped
	 * @return this point with mapped values
	 */
	@NonNull
	public Complex map(@NonNull final Complex z)
	{
		return HyperTranslation.map(z, this);
	}

	/**
	 * Map point by this translation's inverse
	 *
	 * @param z point to be mapped
	 * @return this point with mapped values
	 */
	@NonNull
	public Complex mapinv(@NonNull final Complex z)
	{
		return HyperTranslation.mapinv(z, this);
	}

	// I N V E R S E

	/**
	 * Inverse
	 *
	 * @return this translation with inversed values
	 */
	@NonNull
	public HyperTranslation inverse()
	{
		neg();
		return this;
	}

	/**
	 * Compose 2 translations
	 *
	 * @param t1 translation
	 * @param t2 translation
	 * @return transform resulting from the composition of 2 translations
	 */
	@NonNull
	static public HyperTransform compose(@NonNull final HyperTranslation t1, @NonNull final HyperTranslation t2)
	{
		// t1 is applied first
		// t1 o t2
		// (t1 o t2)(x) = t2(t1(x))
		// ~p=conjugate of p
		// p = (theta2*p1+p2)/(1+theta2*p1*~p2);
		// = (1*p1+p2) /(1+1*p1*~p2);
		// = (p1+p2) /(1+p1*~p2);
		// theta = (theta1*theta2+theta1*~p1*p2) /(1+theta2*p1*~p2);
		// = (1*1+1*~p1*p2)/(1+1*p1*~p2);
		// = (1+~p1*p2) /(1+p1*~p2);
		// = theta1*(theta2+~p1*p2)/(1+theta2*p1*~p2); (factored)
		// = 1*(1+~p1*p2) /(1+1*p1*~p2); (factored)
		// = (1+~p1*p2) /(1+p1*~p2); (factored)

		@NonNull final HyperTransform t = new HyperTransform(t1);

		// denominator : ~p2*p1+1
		@NonNull final Complex denom = new Complex(t2).conj().mul(t1).add(Complex.ONE);

		// translation
		t.xlat.add(t2); // p1+p2
		t.xlat.div(denom); // (p1+p2)/(~p2*p1+1)

		// rotation
		t.rot.set(t1); // p1
		t.rot.conj().mul(t2).add(Complex.ONE); // ~p1*p2+1
		t.rot.div(denom); // (~p1*p2+1) /(~p2*p1+1)
		t.rot.normalize();

		return t;
	}

	// S T R I N G

	@NonNull
	@Override
	public String toString()
	{
		return "xlat=" + super.toString();
	}
}
