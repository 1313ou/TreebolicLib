/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.model;

import treebolic.annotations.NonNull;

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
	public TreeMutableEdge(final INode fromNode, final INode toNode)
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
	public void setFrom(final INode fromNode)
	{
		this.fromNode = fromNode;
	}

	/**
	 * Set to-node (destination)
	 *
	 * @param toNode to-node (may be null)
	 */
	public void setTo(final INode toNode)
	{
		this.toNode = toNode;
	}
}
