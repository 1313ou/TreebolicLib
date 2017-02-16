/**
 * Title : Treebolic
 * Description : Treebolic
 * Version : 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 * <p>
 * Update : Mon Mar 10 00:00:00 CEST 2008
 */
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
	 * @param thisNode
	 *            start node
	 * @param thisCenter
	 *            starting point (hyperbolic circle center)
	 * @param thisHalfWedge
	 *            half wedge allocated to layout
	 * @param thisOrientation
	 *            orientation
	 */
	abstract public void layout(INode thisNode, Complex thisCenter, double thisHalfWedge, double thisOrientation);

	// A C C E S S

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
	 * @param thisExpansion
	 *            expansion factor
	 */
	public void setExpansion(final double thisExpansion)
	{
		this.theNodeDistance = thisExpansion;
		this.theRadius = Distance.distanceToOrigin_e2h(this.theNodeDistance);
	}

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
	 * @param thisSweep
	 *            sweep angle allocated to layout
	 */
	public void setRootSweep(final double thisSweep)
	{
		this.theRootSweep = thisSweep;
	}

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
	 * @param thisSweep
	 *            sweep angle allocated to children
	 */
	public void setChildSweep(final double thisSweep)
	{
		this.theNodeSweep = thisSweep;
		this.theSweepFactor = Math.PI - this.theNodeSweep;
	}

	/**
	 * Set root sweep to default
	 *
	 * @param radial
	 *            true if layout is radial
	 */
	public void setDefaultRootSweep(final boolean radial)
	{
		setRootSweep(radial ? AbstractLayerOut.theDefaultRadialRootSweep : AbstractLayerOut.theDefaultOrientedRootSweep);
	}

	/**
	 * Set child sweep to default
	 *
	 * @param radial
	 *            true if layout is radial
	 */
	public void setDefaultChildSweep(final boolean radial)
	{
		setChildSweep(radial ? AbstractLayerOut.theDefaultRadialChildSweep : AbstractLayerOut.theDefaultOrientedChildSweep);
	}

	// H E L P E R S

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

	/**
	 * Set expansion to default
	 */
	private void setDefaultExpansion()
	{
		setExpansion(AbstractLayerOut.theDefaultExpansion);
	}

	/**
	 * Apply settings
	 *
	 * @param theseSettings
	 *            settings
	 */
	@SuppressWarnings("boxing")
	public void apply(final Settings theseSettings)
	{
		boolean radial = true;
		if (theseSettings.theOrientation != null)
		{
			if (theseSettings.theOrientation.startsWith("n")) //$NON-NLS-1$
			{
				setOrientation(Complex.SOUTH);
				radial = false;
			}
			else if (theseSettings.theOrientation.startsWith("s")) //$NON-NLS-1$
			{
				setOrientation(Complex.NORTH);
				radial = false;
			}
			else if (theseSettings.theOrientation.startsWith("e")) //$NON-NLS-1$
			{
				setOrientation(Complex.EAST);
				radial = false;
			}
			else if (theseSettings.theOrientation.startsWith("w")) //$NON-NLS-1$
			{
				setOrientation(Complex.WEST);
				radial = false;
			}
			else if (theseSettings.theOrientation.startsWith("r")) //$NON-NLS-1$
			{
				setOrientation(Complex.ZERO);
				radial = true;
			}
		}

		// expansion
		setDefaultExpansion();
		if (theseSettings.theExpansion != null && theseSettings.theExpansion > 0.)
		{
			setExpansion(getExpansion() * theseSettings.theExpansion);
		}

		// sweeps
		setDefaultRootSweep(radial);
		setDefaultChildSweep(radial);
		if (theseSettings.theSweep != null && theseSettings.theSweep > 0.)
		{
			setChildSweep(getChildSweep() * theseSettings.theSweep);
		}
	}
}
