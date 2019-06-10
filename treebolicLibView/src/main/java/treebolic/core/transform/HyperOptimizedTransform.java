package treebolic.core.transform;

import androidx.annotation.NonNull;

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
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@NonNull
	private final Complex inverseRot;

	/**
	 * Precomputed p/theta
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@NonNull
	private final Complex xlatOnRot;

	/**
	 * Precomputed ~p
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@NonNull
	private final Complex xlatConj;

	// C O N S T R U C T

	/**
	 * Construct from transform
	 *
	 * @param t transform to optimize
	 */
	public HyperOptimizedTransform(@NonNull final HyperTransform t)
	{
		this.xlatConj = new Complex(t.xlat).conj();
		this.inverseRot = new Complex(t.rot).onediv();
		this.xlatOnRot = new Complex(this.inverseRot).mul(t.xlat);
	}

	// M A P
	// map() does hyperbolic motion:

	@NonNull
	@Override
	public Complex map(@NonNull final Complex z)
	{
		// rigid transformation of the hyperbolic plane
		// z = (z+pontheta)/(invtheta+z*~p)

		/*
		 * Complex num = new Complex(z).add(xlatOnRot); Complex denom = new Complex(z).mul(xlatConj).add(inverseRot); return z.div(num, denom);
		 */
		// z
		final double x = z.re;
		final double y = z.im;
		// z1 = z+pontheta;
		final double numx = x + this.xlatOnRot.re;
		final double numy = y + this.xlatOnRot.im;
		// z2 = z.~p + invtheta
		final double denomx = x * this.xlatConj.re - y * this.xlatConj.im + this.inverseRot.re;
		final double denomy = x * this.xlatConj.im + y * this.xlatConj.re + this.inverseRot.im;
		// z1/z2
		final double d = denomx * denomx + denomy * denomy;
		z.set((numx * denomx + numy * denomy) / d, (numy * denomx - numx * denomy) / d);
		return z;
	}
}
