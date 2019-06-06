package treebolic.model;

import android.support.annotation.Nullable;

import java.io.Serializable;

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
	public final Tree tree;

	/**
	 * Settings
	 */
	public final Settings settings;

	/**
	 * Images
	 */
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
