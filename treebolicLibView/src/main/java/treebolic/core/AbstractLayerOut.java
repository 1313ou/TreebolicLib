package treebolic.core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
	@SuppressWarnings({"WeakerAccess", "InstanceVariableOfConcreteClass"})
	protected Complex rootOrientation;

	/**
	 * Root sweep
	 */
	@SuppressWarnings("WeakerAccess")
	protected double rootSweep;

	/**
	 * Node distance
	 */
	@SuppressWarnings("WeakerAccess")
	protected double nodeDistance;

	/**
	 * Sweep factor
	 */
	@SuppressWarnings("WeakerAccess")
	protected double sweepFactor;

	/**
	 * Radius
	 */
	@SuppressWarnings("WeakerAccess")
	protected double radius;

	/**
	 * Node sweep
	 */
	@SuppressWarnings("WeakerAccess")
	protected double nodeSweep;

	/**
	 * Expansion from settings
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected Float settingsExpansion;

	/**
	 * Sweep from settings
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected Float settingsSweep;

	/**
	 * Whether node space is allocated clockwise
	 */
	@SuppressWarnings("WeakerAccess")
	protected boolean clockwise;

	// C O N S T R U C T O R

	/**
	 * Constructor
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
	abstract public void layout(INode node);

	/**
	 * Do layout
	 *
	 * @param node        start node
	 * @param center      starting point (hyperbolic circle center)
	 * @param halfWedge   half wedge allocated to layout
	 * @param orientation orientation
	 */
	abstract public void layout(INode node, Complex center, double halfWedge, double orientation);

	// A C C E S S

	// ORIENTATION

	/**
	 * Get layout orientation
	 *
	 * @return orientation
	 */
	public Complex getOrientation()
	{
		return this.rootOrientation;
	}

	/**
	 * Set layout orientation
	 *
	 * @param orientation orientation
	 */
	public void setOrientation(final Complex orientation)
	{
		this.rootOrientation = orientation;
		this.clockwise = !(orientation == Complex.NORTH || orientation == Complex.EAST);
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
		return this.nodeDistance;
	}

	/**
	 * Set expansion factor
	 *
	 * @param expansion expansion factor
	 */
	public void setExpansion(final double expansion)
	{
		this.nodeDistance = expansion;
		this.radius = Distance.distanceToOrigin_e2h(this.nodeDistance);
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
		setDefaultSettingsExpansion(this.settingsExpansion);
	}

	/**
	 * Set settings default expansion factor to parameter factor and apply it (used by apply)
	 *
	 * @param expansion expansion
	 */
	private void setDefaultSettingsExpansion(@Nullable final Float expansion)
	{
		setDefaultExpansion();
		if (expansion != null && expansion > 0.)
		{
			this.settingsExpansion = expansion; // store value of factor
			setExpansion(getExpansion() * expansion);
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
		return this.rootSweep;
	}

	/**
	 * Get root sweep angle allocated to layout
	 *
	 * @param sweep sweep angle allocated to layout
	 */
	@SuppressWarnings("WeakerAccess")
	public void setRootSweep(final double sweep)
	{
		this.rootSweep = sweep;
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
		return this.nodeSweep;
	}

	/**
	 * Set sweep angle allocated to children
	 *
	 * @param sweep sweep angle allocated to children
	 */
	public void setChildSweep(final double sweep)
	{
		this.nodeSweep = sweep;
		this.sweepFactor = Math.PI - this.nodeSweep;
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
		setDefaultSettingsSweep(getOrientation() == Complex.ZERO, this.settingsSweep);
	}

	/**
	 * Set settings default sweep factor to parameter factor and apply it (used by apply)
	 *
	 * @param radial radial
	 * @param sweep  sweep
	 */
	private void setDefaultSettingsSweep(boolean radial, @Nullable final Float sweep)
	{
		setDefaultRootSweep(radial);
		setDefaultChildSweep(radial);
		if (sweep != null && sweep > 0.)
		{
			this.settingsSweep = sweep;
			setChildSweep(getChildSweep() * sweep);
		}
	}

	// H E L P E R S

	/**
	 * Apply settings
	 *
	 * @param settings settings
	 */
	public void apply(@NonNull final Settings settings)
	{
		boolean radial = true;
		if (settings.orientation != null)
		{
			if (settings.orientation.startsWith("n"))
			{
				setOrientation(Complex.SOUTH);
				radial = false;
			}
			else if (settings.orientation.startsWith("s"))
			{
				setOrientation(Complex.NORTH);
				radial = false;
			}
			else if (settings.orientation.startsWith("e"))
			{
				setOrientation(Complex.EAST);
				radial = false;
			}
			else if (settings.orientation.startsWith("w"))
			{
				setOrientation(Complex.WEST);
				radial = false;
			}
			else if (settings.orientation.startsWith("r"))
			{
				setOrientation(Complex.ZERO);
				radial = true;
			}
		}

		// expansion
		setDefaultSettingsExpansion(settings.expansion);

		// sweep
		setDefaultSettingsSweep(radial, settings.sweep);
	}
}
