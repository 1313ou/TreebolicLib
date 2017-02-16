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
 * How hyperbolic circles are mapped to Unit Circle
 *
 * @author Bernard Bou
 */
public class EuclideanCircle implements Serializable
{
	private static final long serialVersionUID = 8973089409227338200L;

	// D A T A

	/**
	 * Center in (Euclidean space) unit circle
	 */
	public final Complex center;

	/**
	 * Radius in (Euclidean space) unit circle
	 */
	public double radius;

	// C O N S T R U C T O R

	/**
	 * Construct
	 */
	public EuclideanCircle()
	{
		this.center = new Complex();
		this.radius = 0.;
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
		return "eo=" + this.center + ", er=" + this.radius;  //$NON-NLS-1$//$NON-NLS-2$
	}
}
