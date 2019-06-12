/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.model;

import androidx.annotation.NonNull;

/**
 * Extended mutable node (mutable ends, copy constructor)
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class TreeMutableEdge extends MutableEdge
{
	private static final long serialVersionUID = -1812958269993842207L;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param fromNode from node (may be null)
	 * @param toNode   to node (may be null)
	 */
	public TreeMutableEdge(final MutableNode fromNode, final MutableNode toNode)
	{
		super(fromNode, toNode);
	}

	/**
	 * Copy constructor (the resulting edge has no node ends)
	 *
	 * @param edge edge
	 */
	public TreeMutableEdge(@NonNull final IEdge edge)
	{
		super(null, null);
		this.color = edge.getColor();
		this.label = edge.getLabel();
		this.style = edge.getStyle();
		this.imageFile = edge.getImageFile();
	}

	// E N D S

	/**
	 * Set from-node (origin)
	 *
	 * @param fromNode from-node (may be null)
	 */
	public void setFrom(final MutableNode fromNode)
	{
		this.fromNode = fromNode;
	}

	/**
	 * Set to-node (destination)
	 *
	 * @param toNode to-node (may be null)
	 */
	public void setTo(final MutableNode toNode)
	{
		this.toNode = toNode;
	}
}
