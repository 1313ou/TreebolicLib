package treebolic.core.transform;

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
	 * Translation component, with abs(theXlat) < 1
	 */
	public HyperTranslation theXlat;

	/**
	 * Rotation component of transform, with abs(theRot) == 1
	 */
	public HyperRotation theRot;

	// C O N S T R U C T

	/**
	 * Default constructor that makes null transform
	 */
	public HyperTransform()
	{
		this.theXlat = new HyperTranslation(0., 0.);
		this.theRot = new HyperRotation(1., 0.);
	}

	/**
	 * Constructor from translation vector and rotation angle
	 *
	 * @param p
	 *            translation vector
	 * @param r
	 *            rotation angle
	 */
	public HyperTransform(final Complex p, final Complex r)
	{
		this.theXlat = new HyperTranslation(p);
		this.theRot = new HyperRotation(r);
	}

	/**
	 * Constructor from translation, null rotation
	 *
	 * @param p
	 *            translation vector
	 */
	public HyperTransform(final HyperTranslation p)
	{
		this.theXlat = new HyperTranslation(p);
		this.theRot = new HyperRotation(1., 0.);
	}

	/**
	 * Constructor from rotation, null translation
	 *
	 * @param r
	 *            rotation angle
	 */
	public HyperTransform(final HyperRotation r)
	{
		this.theXlat = new HyperTranslation(0., 0.);
		this.theRot = r;
	}

	/**
	 * Constructor from translation and rotation
	 *
	 * @param p
	 *            translation
	 * @param r
	 *            rotation
	 */
	public HyperTransform(final HyperTranslation p, final HyperRotation r)
	{
		// == set(HRotation.compose(r, p));
		this.theXlat = p;
		this.theRot = r;
	}

	/**
	 * Constructor for transform expressed in cartesian mode
	 *
	 * @param px
	 *            x coordinate for translation vector expressed in cartesian mode (relative to 0,0)
	 * @param py
	 *            y coordinate for translation vector expressed in cartesian mode (relative to 0,0)
	 * @param rx
	 *            x coordinate for rotation expressed by point (relative to 0,0 and x-axis)
	 * @param ry
	 *            y coordinate for rotation expressed by point (relative to 0,0 and x-axis)
	 */
	public HyperTransform(@SuppressWarnings("SameParameterValue") final double px, @SuppressWarnings("SameParameterValue") final double py, @SuppressWarnings("SameParameterValue") final double rx, @SuppressWarnings("SameParameterValue") final double ry)
	{
		this.theXlat = new HyperTranslation(px, py);
		this.theRot = new HyperRotation(rx, ry);
	}

	/**
	 * Copy constructor
	 *
	 * @param t
	 *            source transform
	 */
	public HyperTransform(final HyperTransform t)
	{
		this.theXlat = new HyperTranslation(t.theXlat);
		this.theRot = new HyperRotation(t.theRot);
	}

	/**
	 * Copy transform
	 *
	 * @param t
	 *            source transform
	 * @return this transform with value set to t
	 */
	@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
	public HyperTransform set(final HyperTransform t)
	{
		this.theXlat.set(t.theXlat);
		this.theRot.set(t.theRot);
		return this;
	}

	/**
	 * Construct transform as composition of 2 transforms
	 *
	 * @param t1
	 *            transform
	 * @param t2
	 *            transform
	 */
	public HyperTransform(final HyperTranslation t1, final HyperTranslation t2)
	{
		composeXlats(t1, t2);
	}

	// I N V E R S E

	/**
	 * Inverse
	 *
	 * @return this transform with inverse value
	 */
	public HyperTransform inverse()
	{
		this.theRot.inverse();
		this.theXlat.mul(this.theRot).neg();
		return this;
	}

	// M A P
	// map() does hyperbolic motion:
	// rotation on t.theta over center and translation over t.theXlat;

	@Override
	public Complex map(final Complex z)
	{
		// <BOUTHIER>
		// rigid transformation of the hyperbolic plane
		// z = (z*theta+p)/(1+(~p)*z*theta)
		// </BOUTHIER>

		final Complex num = new Complex(z).mul(this.theRot).add(this.theXlat);
		final Complex denom = new Complex(this.theXlat).conj().mul(z).mul(this.theRot).add(Complex.ONE);
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
	 * @param t2
	 *            transform
	 * @return this transform as the result of composing the initial transform with t2
	 */
	public HyperTransform compose(final HyperTransform t2)
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
		final Complex denom = new Complex(t2.theXlat).conj().mul(this.theXlat).mul(t2.theRot).add(Complex.ONE);

		// translation
		// p1 * theta2 + p2
		final Complex nom1 = new Complex(this.theXlat).mul(t2.theRot).add(t2.theXlat); // (theta2*p1)+p2

		// rotation
		// ~p1 * p2 + theta2 * theta1
		final Complex nom2 = new Complex(this.theXlat).conj().mul(t2.theXlat).add(t2.theRot).mul(this.theRot); // theta1*(theta2+~p1*p2)

		// make
		this.theXlat.div(nom1, denom);
		this.theRot.div(nom2, denom);
		this.theRot.normalize();

		return this;
	}

	/**
	 * Compose 2 translations
	 *
	 * @param t1
	 *            translation
	 * @param t2
	 *            translation
	 * @return composition of t1 and t2
	 */
	@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
	public HyperTransform composeXlats(final HyperTranslation t1, final HyperTranslation t2)
	{
		set(HyperTranslation.compose(t1, t2));
		return this;
	}

	// S T R I N G

	@Override
	public String toString()
	{
		return "p=" + this.theXlat.toString() + " theta=" + this.theRot.toString();
	}
}
