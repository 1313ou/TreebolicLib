/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.model;

import treebolic.glue.Color;

/**
 * Mutable edge
 *
 * @author Bernard Bou
 */
public class MutableEdge extends Edge
{
	private static final long serialVersionUID = -8288301829410629431L;

	/**
	 * Construct node
	 *
	 * @param fromNode from node
	 * @param toNode   to node
	 */
	@SuppressWarnings("WeakerAccess")
	public MutableEdge(final INode fromNode, final INode toNode)
	{
		super(fromNode, toNode);
	}

	/**
	 * Set label
	 *
	 * @param label label
	 */
	public void setLabel(final String label)
	{
		this.label = label;
	}

	/**
	 * Set color
	 *
	 * @param color color
	 */
	public void setColor(final Color color)
	{
		this.color = color;
	}

	/**
	 * Set style
	 *
	 * @param style edge style
	 */
	public void setStyle(final Integer style)
	{
		this.style = style;
	}

	/**
	 * Set image filename
	 *
	 * @param imageFile image filename
	 */
	public void setImageFile(final String imageFile)
	{
		this.imageFile = imageFile;
		this.image = null;
	}

	/**
	 * Set image index
	 *
	 * @param imageIndex image index
	 */
	public void setImageIndex(final int imageIndex)
	{
		this.imageIndex = imageIndex;
		this.image = null;
	}
}
