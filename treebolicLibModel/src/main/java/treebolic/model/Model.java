/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.model;

import java.io.Serializable;

import androidx.annotation.Nullable;
import treebolic.glue.Image;

/**
 * Data model
 *
 * @author Bernard Bou
 */
public class Model implements Serializable
{
	private static final long serialVersionUID = -1032694309277028305L;

	/**
	 * Tree
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	public final Tree tree;

	/**
	 * Settings
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	public final Settings settings;

	/**
	 * Images
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	public final Image[] images;

	/**
	 * Constructor
	 */
	public Model(final Tree tree, final Settings settings)
	{
		this.tree = tree;
		this.settings = settings;
		this.images = null;
	}

	/**
	 * Constructor
	 */
	public Model(final Tree tree, final Settings settings, @Nullable final Image[] images)
	{
		this.tree = tree;
		this.settings = settings;
		this.images = images;
	}
}
