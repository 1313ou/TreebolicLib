/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.model;

import java.io.Serializable;

import treebolic.annotations.Nullable;
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
	 *
	 * @param tree     tree
	 * @param settings settings
	 */
	public Model(final Tree tree, final Settings settings)
	{
		this.tree = tree;
		this.settings = settings;
		this.images = null;
	}

	/**
	 * Constructor
	 *
	 * @param tree     tree
	 * @param settings settings
	 * @param images   images
	 */
	public Model(final Tree tree, final Settings settings, @Nullable final Image[] images)
	{
		this.tree = tree;
		this.settings = settings;
		this.images = images;
	}
}
