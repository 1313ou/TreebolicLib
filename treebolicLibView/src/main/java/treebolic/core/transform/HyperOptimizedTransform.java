package treebolic.core.transform;

import treebolic.core.location.Complex;

/**
 * Optimized hyperbolic transform, which precomputes some static operations prior to mapping
 *
 * @author Bernard Bou
 */
public class HyperOptimizedTransform implements IHyperTransform
{
	// OPTIMIZED
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

	// D A T A

	/**
	 * Precomputed 1/theta
	 */
	private final Complex theInverseRot;

	/**
	 * Precomputed p/theta
	 */
	private final Complex theXlatOnRot;

	/**
	 * Precomputed ~p
	 */
	private final Complex theXlatConj;

	// C O N S T R U C T

	/**
	 * Construct from transform
	 *
	 * @param t
	 *            transform to optimize
	 */
	public HyperOptimizedTransform(final HyperTransform t)
	{
		this.theXlatConj = new Complex(t.theXlat).conj();
		this.theInverseRot = new Complex(t.theRot).onediv();
		this.theXlatOnRot = new Complex(this.theInverseRot).mul(t.theXlat);
	}

	// M A P
	// map() does hyperbolic motion:

	@Override
	public Complex map(final Complex z)
	{
		// rigid transformation of the hyperbolic plane
		// z = (z+pontheta)/(invtheta+z*~p)

		/*
		 * Complex num = new Complex(z).add(theXlatOnRot); Complex denom = new Complex(z).mul(theXlatConj).add(theInverseRot); return z.div(num, denom);
		 */
		// z
		final double x = z.re;
		final double y = z.im;
		// z1 = z+pontheta;
		final double numx = x + this.theXlatOnRot.re;
		final double numy = y + this.theXlatOnRot.im;
		// z2 = z.~p + invtheta
		final double denomx = x * this.theXlatConj.re - y * this.theXlatConj.im + this.theInverseRot.re;
		final double denomy = x * this.theXlatConj.im + y * this.theXlatConj.re + this.theInverseRot.im;
		// z1/z2
		final double d = denomx * denomx + denomy * denomy;
		z.set((numx * denomx + numy * denomy) / d, (numy * denomx - numx * denomy) / d);
		return z;
	}
}
