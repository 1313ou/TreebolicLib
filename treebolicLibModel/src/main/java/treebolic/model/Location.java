/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.model;

import java.io.Serializable;

import treebolic.annotations.NonNull;
import treebolic.annotations.Nullable;
import treebolic.core.location.EuclideanCircle;
import treebolic.core.location.HyperCircle;

/**
 * Node location in hyperspace and unit circle mapping
 *
 * @author Bernard Bou
 */
public class Location implements Serializable
{
	private static final long serialVersionUID = 7642657695999901145L;

	// H Y P E R S P A C E

	/**
	 * Hyper location
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@NonNull
	public final HyperCircle hyper;

	// E U C L I D I A N . U N I T C I R C L E

	/**
	 * Unit circle location
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@NonNull
	public final EuclideanCircle euclidean;

	// V I E W D A T A

	/**
	 * Opaque view rendering data
	 */
	@Nullable
	public Object viewData;

	// C O N S T R U C T O R

	/**
	 * Construct
	 */
	public Location()
	{
		this.hyper = new HyperCircle();
		this.euclidean = new EuclideanCircle();
	}
}
