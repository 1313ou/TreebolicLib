/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.core.location;

import java.io.Serializable;

import treebolic.annotations.NonNull;

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
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@NonNull
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

	@NonNull
	@Override
	public String toString()
	{
		return "eo=" + this.center + ", er=" + this.radius;
	}
}
