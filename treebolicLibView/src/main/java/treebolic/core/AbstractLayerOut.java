package treebolic.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import treebolic.core.location.Complex;
import treebolic.core.math.Distance;
import treebolic.model.INode;
import treebolic.model.Settings;

/**
 * Layout agent
 *
 * @author Bernard Bou
 */
public abstract class AbstractLayerOut
{
	// D A T A

	// default values

	/**
	 * Default orientation
	 */
	@SuppressWarnings("WeakerAccess")
	static protected final Complex DEFAULT_ORIENTATION = Complex.ZERO;

	/**
	 * Default expansion
	 */
	@SuppressWarnings("WeakerAccess")
	static protected final double DEFAULT_EXPANSION = .3;

	/**
	 * Default root radial sweep
	 */
	@SuppressWarnings("WeakerAccess")
	static protected final double DEFAULT_RADIAL_ROOT_SWEEP = Math.PI;

	/**
	 * Default oriented (non-radial) root sweep
	 */
	static private final double DEFAULT_ORIENTED_ROOT_SWEEP = Math.PI / 2.;

	/**
	 * Default radial child sweep
	 */
	static private final double DEFAULT_RADIAL_CHILD_SWEEP = Math.PI / 2.;

	/**
	 * Default oriented (non-radial) child sweep
	 */
	static private final double DEFAULT_ORIENTED_CHILD_SWEEP = Math.PI / 2.;

	// settings

	/**
	 * Root orientation
	 */
	@SuppressWarnings("WeakerAccess")
	protected Complex theRootOrientation;

	/**
	 * Root sweep
	 */
	@SuppressWarnings("WeakerAccess")
	protected double theRootSweep;

	/**
	 * Node distance
	 */
	@SuppressWarnings("WeakerAccess")
	protected double theNodeDistance;

	/**
	 * Sweep factor
	 */
	@SuppressWarnings("WeakerAccess")
	protected double theSweepFactor;

	/**
	 * Radius
	 */
	@SuppressWarnings("WeakerAccess")
	protected double theRadius;

	/**
	 * Node sweep
	 */
	@SuppressWarnings("WeakerAccess")
	protected double theNodeSweep;

	/**
	 * Expansion from settings
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected Float theSettingsExpansion;

	/**
	 * Sweep from settings
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected Float theSettingsSweep;

	/**
	 * Whether node space is allocated clockwise
	 */
	@SuppressWarnings("WeakerAccess")
	protected boolean clockwise;

	// C O N S T R U C T O R

	/**
	 * Contructor
	 */
	@SuppressWarnings("WeakerAccess")
	public AbstractLayerOut()
	{
		// default radial
		final boolean isRadial = setDefaultOrientation();
		setDefaultExpansion();
		setDefaultRootSweep(isRadial);
		setDefaultChildSweep(isRadial);
		this.clockwise = false;
	}

	// O P E R A T I O N

	/**
	 * Do layout
	 */
	abstract public void layout(INode thisNode);

	/**
	 * Do layout
	 *
	 * @param thisNode        start node
	 * @param thisCenter      starting point (hyperbolic circle center)
	 * @param thisHalfWedge   half wedge allocated to layout
	 * @param thisOrientation orientation
	 */
	abstract public void layout(INode thisNode, Complex thisCenter, double thisHalfWedge, double thisOrientation);

	// A C C E S S

	// ORIENTATION

	/**
	 * Get layout orientation
	 *
	 * @return orientation
	 */
	public Complex getOrientation()
	{
		return this.theRootOrientation;
	}

	/**
	 * Set layout orientation
	 *
	 * @param thisOrientation orientation
	 */
	public void setOrientation(final Complex thisOrientation)
	{
		this.theRootOrientation = thisOrientation;
		this.clockwise = !(thisOrientation == Complex.NORTH || thisOrientation == Complex.EAST);
	}

	/**
	 * Set orientation to default
	 *
	 * @return true if default orientation is radial
	 */
	private boolean setDefaultOrientation()
	{
		setOrientation(AbstractLayerOut.DEFAULT_ORIENTATION);
		return AbstractLayerOut.DEFAULT_ORIENTATION == Complex.ZERO;
	}

	// EXPANSION

	/**
	 * Get expansion factor
	 *
	 * @return expansion factor
	 */
	public double getExpansion()
	{
		return this.theNodeDistance;
	}

	/**
	 * Set expansion factor
	 *
	 * @param thisExpansion expansion factor
	 */
	public void setExpansion(final double thisExpansion)
	{
		this.theNodeDistance = thisExpansion;
		this.theRadius = Distance.distanceToOrigin_e2h(this.theNodeDistance);
	}

	/**
	 * Set expansion to default
	 */
	public void setDefaultExpansion()
	{
		setExpansion(AbstractLayerOut.DEFAULT_EXPANSION);
	}

	/**
	 * Set expansion to settings default (called to set to value applied in settings)
	 */
	public void setDefaultSettingsExpansion()
	{
		setDefaultSettingsExpansion(this.theSettingsExpansion);
	}

	/**
	 * Set settings default expansion factor to parameter factor and apply it (used by apply)
	 *
	 * @param thisExpansion expansion
	 */
	private void setDefaultSettingsExpansion(@Nullable final Float thisExpansion)
	{
		setDefaultExpansion();
		if (thisExpansion != null && thisExpansion > 0.)
		{
			this.theSettingsExpansion = thisExpansion; // store value of factor
			setExpansion(getExpansion() * thisExpansion);
		}
	}

	// ROOT SWEEP

	/**
	 * Set root sweep angle allocated to layout
	 *
	 * @return sweep angle allocated to layout
	 */
	public double getRootSweep()
	{
		return this.theRootSweep;
	}

	/**
	 * Get root sweep angle allocated to layout
	 *
	 * @param thisSweep sweep angle allocated to layout
	 */
	@SuppressWarnings("WeakerAccess")
	public void setRootSweep(final double thisSweep)
	{
		this.theRootSweep = thisSweep;
	}

	/**
	 * Set root sweep to default
	 *
	 * @param radial true if layout is radial
	 */
	public void setDefaultRootSweep(final boolean radial)
	{
		setRootSweep(radial ? AbstractLayerOut.DEFAULT_RADIAL_ROOT_SWEEP : AbstractLayerOut.DEFAULT_ORIENTED_ROOT_SWEEP);
	}

	// CHILD SWEEP

	/**
	 * Get sweep angle allocated to children
	 *
	 * @return sweep angle allocated to children
	 */
	public double getChildSweep()
	{
		return this.theNodeSweep;
	}

	/**
	 * Set sweep angle allocated to children
	 *
	 * @param thisSweep sweep angle allocated to children
	 */
	public void setChildSweep(final double thisSweep)
	{
		this.theNodeSweep = thisSweep;
		this.theSweepFactor = Math.PI - this.theNodeSweep;
	}

	/**
	 * Set child sweep to default
	 *
	 * @param radial true if layout is radial
	 */
	public void setDefaultChildSweep(final boolean radial)
	{
		setChildSweep(radial ? AbstractLayerOut.DEFAULT_RADIAL_CHILD_SWEEP : AbstractLayerOut.DEFAULT_ORIENTED_CHILD_SWEEP);
	}

	/**
	 * Set child sweep to default
	 */
	public void setDefaultChildSweep()
	{
		setDefaultChildSweep(getOrientation() == Complex.ZERO);
	}

	// SWEEP

	/**
	 * Set sweep to settings default
	 */
	public void setDefaultSettingsSweep()
	{
		setDefaultSettingsSweep(getOrientation() == Complex.ZERO, this.theSettingsSweep);
	}

	/**
	 * Set settings default sweep factor to parameter factor and apply it (used by apply)
	 *
	 * @param radial    radial
	 * @param thisSweep sweep
	 */
	private void setDefaultSettingsSweep(boolean radial, @Nullable final Float thisSweep)
	{
		setDefaultRootSweep(radial);
		setDefaultChildSweep(radial);
		if (thisSweep != null && thisSweep > 0.)
		{
			this.theSettingsSweep = thisSweep;
			setChildSweep(getChildSweep() * thisSweep);
		}
	}

	// H E L P E R S

	/**
	 * Apply settings
	 *
	 * @param theseSettings settings
	 */
	public void apply(@NonNull final Settings theseSettings)
	{
		boolean radial = true;
		if (theseSettings.theOrientation != null)
		{
			if (theseSettings.theOrientation.startsWith("n"))
			{
				setOrientation(Complex.SOUTH);
				radial = false;
			}
			else if (theseSettings.theOrientation.startsWith("s"))
			{
				setOrientation(Complex.NORTH);
				radial = false;
			}
			else if (theseSettings.theOrientation.startsWith("e"))
			{
				setOrientation(Complex.EAST);
				radial = false;
			}
			else if (theseSettings.theOrientation.startsWith("w"))
			{
				setOrientation(Complex.WEST);
				radial = false;
			}
			else if (theseSettings.theOrientation.startsWith("r"))
			{
				setOrientation(Complex.ZERO);
				radial = true;
			}
		}

		// expansion
		setDefaultSettingsExpansion(theseSettings.theExpansion);

		// sweep
		setDefaultSettingsSweep(radial, theseSettings.theSweep);
	}
}
