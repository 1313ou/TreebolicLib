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
	public final Tree theTree;

	/**
	 * Settings
	 */
	public final Settings theSettings;

	/**
	 * Images
	 */
	@Nullable
	public final Image[] theImages;

	/**
	 * Constructor
	 */
	public Model(final Tree thisTree, final Settings theseSettings)
	{
		this.theTree = thisTree;
		this.theSettings = theseSettings;
		this.theImages = null;
	}

	/**
	 * Constructor
	 */
	public Model(final Tree thisTree, final Settings theseSettings, @Nullable final Image[] theseImages)
	{
		this.theTree = thisTree;
		this.theSettings = theseSettings;
		this.theImages = theseImages;
	}
}
