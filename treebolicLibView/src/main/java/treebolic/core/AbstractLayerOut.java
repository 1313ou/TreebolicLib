package treebolic.core;

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
	static protected final Complex theDefaultOrientation = Complex.ZERO;

	/**
	 * Default expansion
	 */
	static protected final double theDefaultExpansion = .3;

	/**
	 * Default root radial sweep
	 */
	static protected final double theDefaultRadialRootSweep = Math.PI;

	/**
	 * Default oriented (non-radial) root sweep
	 */
	static private final double theDefaultOrientedRootSweep = Math.PI / 2.;

	/**
	 * Default radial child sweep
	 */
	static private final double theDefaultRadialChildSweep = Math.PI / 2.;

	/**
	 * Default oriented (non-radial) child sweep
	 */
	static private final double theDefaultOrientedChildSweep = Math.PI / 2.;

	// settings

	/**
	 * Root orientation
	 */
	protected Complex theRootOrientation;

	/**
	 * Root sweep
	 */
	protected double theRootSweep;

	/**
	 * Node distance
	 */
	protected double theNodeDistance;

	/**
	 * Sweep factor
	 */
	protected double theSweepFactor;

	/**
	 * Radius
	 */
	protected double theRadius;

	/**
	 * Node sweep
	 */
	protected double theNodeSweep;

	/**
	 * Expansion from settings
	 */
	protected Float theSettingsExpansion;

	/**
	 * Sweep from settings
	 */
	protected Float theSettingsSweep;

	/**
	 * Whether node space is allocated clockwise
	 */
	protected boolean clockwise;

	// C O N S T R U C T O R

	/**
	 * Contructor
	 */
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
		setOrientation(AbstractLayerOut.theDefaultOrientation);
		return AbstractLayerOut.theDefaultOrientation == Complex.ZERO;
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
		setExpansion(AbstractLayerOut.theDefaultExpansion);
	}

	/**
	 * Set expansion to settings default
	 */
	public void setDefaultSettingsExpansion()
	{
		setDefaultSettingsExpansion(this.theSettingsExpansion);
	}

	/**
	 * Set expansion to settings default
	 * 
	 * @param thisExpansion expansion
	 */
	private void setDefaultSettingsExpansion(final Float thisExpansion)
	{
		setDefaultExpansion();
		if (thisExpansion != null && thisExpansion > 0.)
		{
			this.theSettingsExpansion = thisExpansion;
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
		setRootSweep(radial ? AbstractLayerOut.theDefaultRadialRootSweep : AbstractLayerOut.theDefaultOrientedRootSweep);
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
		setChildSweep(radial ? AbstractLayerOut.theDefaultRadialChildSweep : AbstractLayerOut.theDefaultOrientedChildSweep);
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
	 * Set expansion to settings default
	 */
	public void setDefaultSettingsSweep()
	{
		setDefaultSettingsSweep(getOrientation() == Complex.ZERO, this.theSettingsSweep);
	}

	/**
	 * Set expansion to settings default
	 * 
	 * @param radial 	radial
	 * @param thisSweep sweep
	 */
	private void setDefaultSettingsSweep(boolean radial, final Float thisSweep)
	{
		setDefaultRootSweep(radial);
		setDefaultChildSweep(radial);
		if (thisSweep != null && thisSweep > 0.)
		{
			setChildSweep(getChildSweep() * thisSweep);
	}
	}

	// H E L P E R S

	/**
	 * Apply settings
	 *
	 * @param theseSettings settings
	 */
	@SuppressWarnings("boxing")
	public void apply(final Settings theseSettings)
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
