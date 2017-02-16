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
package treebolic.model;

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
	public Model(final Tree thisTree, final Settings theseSettings, final Image[] theseImages)
	{
		this.theTree = thisTree;
		this.theSettings = theseSettings;
		this.theImages = theseImages;
	}
}
