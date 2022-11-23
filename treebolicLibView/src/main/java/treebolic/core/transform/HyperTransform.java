/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.core.transform;

import treebolic.annotations.NonNull;
import treebolic.core.location.Complex;

/**
 * Hyperbolic transform implementation
 *
 * @author Bernard Bou
 */
public class HyperTransform implements IHyperTransform
{
	// C O N S T A N T

	/**
	 * Null transform
	 */
	static public final HyperTransform NULLTRANSFORM = new HyperTransform(0., 0., 1., 0.);

	// D A T A

	/**
	 * Translation component, with abs(xlat) &lt; 1
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	public HyperTranslation xlat;

	/**
	 * Rotation component of transform, with abs(rot) == 1
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	public HyperRotation rot;

	// C O N S T R U C T

	/**
	 * Default constructor that makes null transform
	 */
	public HyperTransform()
	{
		this.xlat = new HyperTranslation(0., 0.);
		this.rot = new HyperRotation(1., 0.);
	}

	/**
	 * Constructor from translation vector and rotation angle
	 *
	 * @param p translation vector
	 * @param r rotation angle
	 */
	public HyperTransform(@NonNull final Complex p, @NonNull final Complex r)
	{
		this.xlat = new HyperTranslation(p);
		this.rot = new HyperRotation(r);
	}

	/**
	 * Constructor from translation, null rotation
	 *
	 * @param p translation vector
	 */
	public HyperTransform(@NonNull final HyperTranslation p)
	{
		this.xlat = new HyperTranslation(p);
		this.rot = new HyperRotation(1., 0.);
	}

	/**
	 * Constructor from rotation, null translation
	 *
	 * @param r rotation angle
	 */
	public HyperTransform(final HyperRotation r)
	{
		this.xlat = new HyperTranslation(0., 0.);
		this.rot = r;
	}

	/**
	 * Constructor from translation and rotation
	 *
	 * @param p translation
	 * @param r rotation
	 */
	public HyperTransform(final HyperTranslation p, final HyperRotation r)
	{
		// == set(HRotation.compose(r, p));
		this.xlat = p;
		this.rot = r;
	}

	/**
	 * Constructor for transform expressed in cartesian mode
	 *
	 * @param px x coordinate for translation vector expressed in cartesian mode (relative to 0,0)
	 * @param py y coordinate for translation vector expressed in cartesian mode (relative to 0,0)
	 * @param rx x coordinate for rotation expressed by point (relative to 0,0 and x-axis)
	 * @param ry y coordinate for rotation expressed by point (relative to 0,0 and x-axis)
	 */
	@SuppressWarnings("WeakerAccess")
	public HyperTransform(@SuppressWarnings("SameParameterValue") final double px, @SuppressWarnings("SameParameterValue") final double py, @SuppressWarnings("SameParameterValue") final double rx, @SuppressWarnings("SameParameterValue") final double ry)
	{
		this.xlat = new HyperTranslation(px, py);
		this.rot = new HyperRotation(rx, ry);
	}

	/**
	 * Copy constructor
	 *
	 * @param t source transform
	 */
	public HyperTransform(@NonNull final HyperTransform t)
	{
		this.xlat = new HyperTranslation(t.xlat);
		this.rot = new HyperRotation(t.rot);
	}

	/**
	 * Copy transform
	 *
	 * @param t source transform
	 * @return this transform with value set to t
	 */
	@NonNull
	@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
	public HyperTransform set(@NonNull final HyperTransform t)
	{
		this.xlat.set(t.xlat);
		this.rot.set(t.rot);
		return this;
	}

	/**
	 * Construct transform as composition of 2 transforms
	 *
	 * @param t1 transform
	 * @param t2 transform
	 */
	public HyperTransform(@NonNull final HyperTranslation t1, @NonNull final HyperTranslation t2)
	{
		composeXlats(t1, t2);
	}

	// I N V E R S E

	/**
	 * Inverse
	 *
	 * @return this transform with inverse value
	 */
	@NonNull
	public HyperTransform inverse()
	{
		this.rot.inverse();
		this.xlat.mul(this.rot).neg();
		return this;
	}

	// M A P
	// map() does hyperbolic motion:
	// rotation on t.theta over center and translation over t.xlat;

	@NonNull
	@Override
	public Complex map(@NonNull final Complex z)
	{
		// <BOUTHIER>
		// rigid transformation of the hyperbolic plane
		// z = (z*theta+p)/(1+(~p)*z*theta)
		// </BOUTHIER>

		@NonNull final Complex num = new Complex(z).mul(this.rot).add(this.xlat);
		@NonNull final Complex denom = new Complex(this.xlat).conj().mul(z).mul(this.rot).add(Complex.ONE);
		return z.div(num, denom);

		// OPTIMIZED
		// see HTOptimizedTransform which does just this
		// factor out theta (1/theta = invtheta)
		// z = (z*theta+p)/(1+(~p)*theta*z)
		// z = theta(z+p*invtheta)/theta(invtheta+z*~p)
		// z = (z+p*invtheta)/(invtheta+z*~p)
		// invtheta can be precomputed
		// p*invtheta can be precomputed (pontheta)
		// z = (z+pontheta)/(invtheta+z*~p)
		// at runtime it boils down to :
		// -2 complex additions
		// -1 complex multiplication
		// -1 complex division
	}

	// C O M P O S E

	/**
	 * Compose this transform with other transform
	 *
	 * @param t2 transform
	 * @return this transform as the result of composing the initial transform with t2
	 */
	@NonNull
	public HyperTransform compose(@NonNull final HyperTransform t2)
	{
		// t1=this transform
		// this transform is applied first
		// t1 o t2
		// (t1 o t2)(x) = t2(t1(x))
		// ~p=conjugate of p
		// p = (theta2*p1+p2) /(1+theta2*p1*~p2);
		// theta = (theta1*theta2+theta1*~p1*p2) /(1+theta2*p1*~p2);
		// = theta1*(theta2+~p1*p2) /(1+theta2*p1*~p2); (factored)

		// denominator : theta2*p1*~p2+1
		// ~p2 * p1 * theta2 + 1
		@NonNull final Complex denom = new Complex(t2.xlat).conj().mul(this.xlat).mul(t2.rot).add(Complex.ONE);

		// translation
		// p1 * theta2 + p2
		@NonNull final Complex nom1 = new Complex(this.xlat).mul(t2.rot).add(t2.xlat); // (theta2*p1)+p2

		// rotation
		// ~p1 * p2 + theta2 * theta1
		@NonNull final Complex nom2 = new Complex(this.xlat).conj().mul(t2.xlat).add(t2.rot).mul(this.rot); // theta1*(theta2+~p1*p2)

		// make
		this.xlat.div(nom1, denom);
		this.rot.div(nom2, denom);
		this.rot.normalize();

		return this;
	}

	/**
	 * Compose 2 translations
	 *
	 * @param t1 translation
	 * @param t2 translation
	 * @return composition of t1 and t2
	 */
	@NonNull
	@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
	public HyperTransform composeXlats(@NonNull final HyperTranslation t1, @NonNull final HyperTranslation t2)
	{
		set(HyperTranslation.compose(t1, t2));
		return this;
	}

	// S T R I N G

	@NonNull
	@Override
	public String toString()
	{
		return "p=" + this.xlat.toString() + " theta=" + this.rot.toString();
	}
}
