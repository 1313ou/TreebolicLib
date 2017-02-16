/**
 * Title : Treebolic
 * Description : Treebolic
 * Version : 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 *
 * Update : Mon Mar 10 00:00:00 CEST 2008
 */
package treebolic.core.location;

import java.io.Serializable;

/**
 * Hypercircle node location in hyperspace
 *
 * @author Bernard Bou
 */
public class HyperCircle implements Serializable
{
	private static final long serialVersionUID = 483668195607524702L;

	// C O N S T A N T S

	/**
	 * Where border starts (distance to origin)
	 */
	public static final double BORDER = .95;

	// D A T A

	/**
	 * Center as computed by initial layout
	 */
	public final Complex center0;

	/**
	 * Center as mapped by current transform
	 */
	public final Complex center;

	/**
	 * Radius
	 */
	public double radius;

	/**
	 * Distance to (0,0)
	 */
	public double dist;

	// S T A T E

	/**
	 * Computation state (set whenever either center or radius or both change)
	 */
	public boolean isDirty;

	// B O R D E R

	/**
	 * Whether this hypercircle nears border
	 */
	public boolean isBorder;

	// C O N S T R U C T O R

	/**
	 * Construct hypercircle
	 */
	public HyperCircle()
	{
		this.center0 = new Complex();
		this.center = new Complex();
		this.radius = 0.;
		this.isBorder = false;
		this.isDirty = false;
	}

	// S E T

	/**
	 * Set hypercircle data
	 *
	 * @param o
	 *            center
	 * @param r
	 *            radius
	 */
	public void set(final Complex o, final double r)
	{
		this.center0.set(o);
		this.center.set(o);
		this.dist = this.center.mag();
		this.isBorder = this.dist > HyperCircle.BORDER;
		this.radius = r;
		this.isDirty = true;
	}

	/**
	 * Reset to untransformed initial position
	 */
	public void reset()
	{
		this.center.set(this.center0);
		this.dist = this.center.mag();
		this.isBorder = this.dist > HyperCircle.BORDER;
		this.isDirty = true;
	}

	/**
	 * Clone
	 *
	 * @param thisHyperCircle
	 *            hyper circle
	 */
	public void clone(final HyperCircle thisHyperCircle)
	{
		this.center0.set(thisHyperCircle.center0);
		this.center.set(thisHyperCircle.center);
		this.dist = thisHyperCircle.dist;
		this.isBorder = thisHyperCircle.isBorder;
		this.radius = thisHyperCircle.radius;
		this.isDirty = true;
	}

	// S T R I N G

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "o0=" + this.center0 + ", o=" + this.center + ", r=" + this.radius + ", d=" + this.dist; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}
