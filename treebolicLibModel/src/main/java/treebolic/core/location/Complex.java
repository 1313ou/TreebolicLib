/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.core.location;

import java.io.Serializable;
import java.util.Locale;

import androidx.annotation.NonNull;

/**
 * Complex
 *
 * @author Bernard Bou
 */
public class Complex implements Serializable
{
	private static final long serialVersionUID = 2946336631339380397L;

	// C O N S T A N T S

	/**
	 * Zero
	 */
	static public final Complex ZERO = new Complex(0., 0.);

	/**
	 * One
	 */
	static public final Complex ONE = new Complex(1., 0.);

	/**
	 * North orientation
	 */
	static public final Complex NORTH = new Complex(0., 1.);

	/**
	 * South orientation
	 */
	static public final Complex SOUTH = new Complex(0., -1.);

	/**
	 * East orientation
	 */
	static public final Complex EAST = Complex.ONE;

	/**
	 * West orientation
	 */
	static public final Complex WEST = new Complex(-1., 0.);

	// D A T A

	/**
	 * Real part
	 */
	public double re;

	/**
	 * Imaginary part
	 */
	public double im;

	// C O N S T R U C T O R S

	/**
	 * Default constructor which defaults to (0,0)
	 */
	@SuppressWarnings("EmptyMethod")
	public Complex()
	{
		// this(0.,0.);
	}

	/**
	 * Construct complex from real and imaginary parts
	 *
	 * @param x real part
	 * @param y imaginary part
	 */
	public Complex(final double x, final double y)
	{
		this.re = x;
		this.im = y;
	}

	/**
	 * Copy constructor
	 *
	 * @param z copied complex
	 */
	public Complex(@NonNull final Complex z)
	{
		this.re = z.re;
		this.im = z.im;
	}

	/**
	 * Construct complex from real part, imaginary part defaulting to 0
	 *
	 * @param x real part
	 */
	public Complex(final double x)
	{
		this.re = x;
		this.im = 0.;
	}

	/**
	 * Construct complex from argument and mag re = cos(arg) im = sin(arg)
	 *
	 * @param arg argument a
	 * @param mag magnitude m
	 * @return new complex of this argument a and magnitude m
	 */
	@NonNull
	static public Complex makeFromArgAbs(final double arg, final double mag)
	{
		return new Complex(mag * Math.cos(arg), mag * Math.sin(arg));
	}

	/**
	 * Construct complex of magnitude 1 from argument re = cos(arg) im = sin(arg)
	 *
	 * @param arg argument a
	 * @return new complex of magnitude 1 and argument a
	 */
	@NonNull
	static public Complex makeFromArg(final double arg)
	{
		return new Complex(Math.cos(arg), Math.sin(arg));
	}

	// S E T

	/**
	 * Set complex to value of another complex, no new complex object is created
	 *
	 * @param z complex
	 * @return original complex object, whose value has been set to z
	 */
	@NonNull
	public Complex set(@NonNull final Complex z)
	{
		this.re = z.re;
		this.im = z.im;
		return this;
	}

	/**
	 * Set complex to values of real and imaginary parts no new complex object is created
	 *
	 * @param x real part
	 * @param y imaginary part
	 * @return original complex object, whose value has been set to x,z
	 */
	@NonNull
	@SuppressWarnings("UnusedReturnValue")
	public Complex set(final double x, final double y)
	{
		this.re = x;
		this.im = y;
		return this;
	}

	/**
	 * Reset complex to 0
	 *
	 * @return original complex object, whose value has been set to 0
	 */
	@NonNull
	@SuppressWarnings("UnusedReturnValue")
	public Complex reset()
	{
		this.re = .0;
		this.im = .0;
		return this;
	}

	// E Q U A L S

	/**
	 * Complex equality
	 *
	 * @param z complex
	 * @return true if this complex z0 is equal to complex z
	 */
	public boolean equals(@NonNull final Complex z)
	{
		return this.re == z.re && this.im == z.im;
	}

	// A D D

	/**
	 * Add complex z to this complex
	 *
	 * @param z complex to be added to this complex
	 * @return this complex, whose value has been set to z0+z
	 */
	@NonNull
	public Complex add(@NonNull final Complex z)
	{
		this.re += z.re;
		this.im += z.im;
		return this;
	}

	/**
	 * Set this complex to z1+z2
	 *
	 * @param z1 complex
	 * @param z2 complex
	 * @return this complex, whose value has been set to z1+z2 and whose original value is not used
	 */
	@NonNull
	@SuppressWarnings("WeakerAccess")
	public Complex add(@NonNull final Complex z1, @NonNull final Complex z2)
	{
		this.re = z1.re + z2.re;
		this.im = z1.im + z2.im;
		return this;
	}

	// S U B S T R A C T

	/**
	 * Substract complex z from this complex
	 *
	 * @param z complex
	 * @return this complex, whose value has been set to z0-z
	 */
	@NonNull
	public Complex sub(@NonNull final Complex z)
	{
		this.re -= z.re;
		this.im -= z.im;
		return this;
	}

	/**
	 * Set this complex to z1-z2
	 *
	 * @param z1 complex
	 * @param z2 complex
	 * @return this complex whose value has been set to z1-z2 and whose original value is not used
	 */
	@NonNull
	public Complex sub(@NonNull final Complex z1, @NonNull final Complex z2)
	{
		this.re = z1.re - z2.re;
		this.im = z1.im - z2.im;
		return this;
	}

	// C O N J U G A T E

	/**
	 * Conjugate of this complex
	 *
	 * @return this complex, whose value has been to the conjugate of this complex
	 */
	@NonNull
	public Complex conj()
	{
		this.im = -this.im;
		return this;
	}

	/**
	 * Conjugate of z
	 *
	 * @param z complex
	 * @return this complex, whose value has been set to conjugate of z and whose original value is not used
	 */
	@NonNull
	public Complex conj(@NonNull final Complex z)
	{
		this.re = z.re;
		this.im = -z.im;
		return this;
	}

	// N E G A T I O N

	/**
	 * Negation of this complex
	 *
	 * @return this complex, whose value has been set to the negation of this complex
	 */
	@NonNull
	public Complex neg()
	{
		this.re = -this.re;
		this.im = -this.im;
		return this;
	}

	/**
	 * Negation of complex z
	 *
	 * @param z complex
	 * @return this complex, whose value has been set to the negation of z and whose original value is not used
	 */
	@NonNull
	public Complex neg(@NonNull final Complex z)
	{
		this.re = -z.re;
		this.im = -z.im;
		return this;
	}

	// D I V

	/**
	 * Division of this complex by z
	 *
	 * @param z complex
	 * @return this complex, whose value has been set to the division of this complex by complex z (=z0/z)
	 */
	@NonNull
	public Complex div(@NonNull final Complex z)
	{
		final double d = z.re * z.re + z.im * z.im; // (x2.x2 + y2.y2)
		final double t = (this.re * z.re + this.im * z.im) / d; // (x1.x2 +
		// y1.y2) /
		// (x2.x2 +
		// y2.y2)
		this.im = (this.im * z.re - this.re * z.im) / d; // (y1.x2 - x1*y2) /
		// (x2.x2 + y2.y2)
		this.re = t;
		return this;
	}

	/**
	 * Division
	 *
	 * @param z1 complex
	 * @param z2 complex
	 * @return this complex, whose value has been set to the division of z1 by z2 (=z1/z2) and whose original value is not used
	 */
	@NonNull
	public Complex div(@NonNull final Complex z1, @NonNull final Complex z2)
	{
		final double d = z2.re * z2.re + z2.im * z2.im;
		this.re = (z1.re * z2.re + z1.im * z2.im) / d;
		this.im = (z1.im * z2.re - z1.re * z2.im) / d;
		return this;
	}

	/**
	 * Inverse of this complex
	 *
	 * @return this complex, as the result of inverting its original value (=1/z0)
	 */
	@NonNull
	public Complex onediv()
	{
		// 1/z
		final double d = this.re * this.re + this.im * this.im; // (x.x + y.y)
		this.re = this.re / d;
		this.im = -this.im / d; // -y / (x.x + y.y)
		return this;
	}

	// M U L T I P L Y

	/**
	 * Multiplication of this complex by z
	 *
	 * @param z multiplier
	 * @return this complex, whose value has been multiplied by z (=z0.z)
	 */
	@NonNull
	public Complex mul(@NonNull final Complex z)
	{
		final double t = this.re * z.im + this.im * z.re; // x1.y2 + x2.y1
		this.re = this.re * z.re - this.im * z.im; // x1.x2 - y1.y2
		this.im = t;
		return this;
	}

	/**
	 * Multiplication
	 *
	 * @param a complex
	 * @param b complex
	 * @return this complex as the result of a.b and whose original value not used
	 */
	@NonNull
	@SuppressWarnings("WeakerAccess")
	public Complex mul(@NonNull final Complex a, @NonNull final Complex b)
	{
		this.re = a.re * b.re - a.im * b.im;
		this.im = a.re * b.im + a.im * b.re;
		return this;
	}

	// M U L T I P L Y / D I V I D E . B Y . D O U B L E

	/**
	 * Multiply complex by double
	 *
	 * @param z complex
	 * @param m multiplicator
	 * @return this complex, as the result of multiplying z by m
	 */
	@NonNull
	public Complex multiply(@NonNull final Complex z, final double m)
	{
		this.im = z.im * m;
		this.re = z.re * m;
		return this;
	}

	/**
	 * Multiply this complex by double
	 *
	 * @param m multiplicator
	 * @return this complex, multiplied by m
	 */
	@NonNull
	public Complex multiply(final double m)
	{
		this.im *= m;
		this.re *= m;
		return this;
	}

	/**
	 * Divide complex by double
	 *
	 * @param z complex
	 * @param d divisor
	 * @return this complex, as the result of dividing z by d
	 */
	@NonNull
	public Complex divide(@NonNull final Complex z, final double d)
	{
		this.im = z.im / d;
		this.re = z.re / d;
		return this;
	}

	/**
	 * Divide this complex by double
	 *
	 * @param d divisor
	 * @return this complex, divided by d
	 */
	@NonNull
	public Complex divide(final double d)
	{
		this.im /= d;
		this.re /= d;
		return this;
	}

	// A R G U M E N T / A N G L E
	// re = abs().cos(arg)
	// im = abs().sin(arg)

	/**
	 * Argument
	 *
	 * @return argument of complex
	 */
	public double arg()
	{
		return Math.atan2(this.im, this.re);
	}

	// M A G N I T U D E / N O R M / R O
	// re = mag().cos(arg)
	// im = mag().sin(arg)

	/**
	 * Magnitude
	 *
	 * @return magnitude
	 */
	public double mag()
	{
		return Math.sqrt(this.re * this.re + this.im * this.im);
	}

	/**
	 * Squared magnitude
	 *
	 * @return squared magnitude
	 */
	public double abs2()
	{
		return this.re * this.re + this.im * this.im;
	}

	// N O R M A L I Z E . T O . M A G N I T U D E . O N E . C O M P L E X

	/**
	 * Map to magnitude one complex
	 *
	 * @return magnitude 1 complex with same argument
	 */
	@NonNull
	public Complex normalize()
	{
		final double r = mag();
		this.re /= r;
		this.im /= r;
		return this;
	}

	// S T R I N G

	@NonNull
	@SuppressWarnings("boxing")
	@Override
	public String toString()
	{
		return String.format(Locale.US, "(%.3f,%.3f)", this.re, this.im);
	}
}
