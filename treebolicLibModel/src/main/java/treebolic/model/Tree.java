/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.model;

import java.io.Serializable;
import java.util.List;

/**
 * Model
 *
 * @author Bernard Bou
 */
public class Tree implements Serializable
{
	private static final long serialVersionUID = 6923143990877138332L;

	// D A T A

	/**
	 * Root node
	 */
	private INode root;

	/**
	 * Edge list
	 */
	private List<IEdge> edges;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param root  root node
	 * @param edges edge list
	 */
	public Tree(final INode root, final List<IEdge> edges)
	{
		this.root = root;
		this.edges = edges;
	}

	// A C C E S S

	/**
	 * Get root node
	 *
	 * @return root node
	 */
	public INode getRoot()
	{
		return this.root;
	}

	/**
	 * Get edges
	 *
	 * @return edges
	 */
	public List<IEdge> getEdges()
	{
		return this.edges;
	}

	/**
	 * Set root node
	 *
	 * @param root node
	 */
	public void setRoot(final INode root)
	{
		this.root = root;
	}

	/**
	 * Set edges
	 *
	 * @param edges edge list
	 */
	public void setEdges(final List<IEdge> edges)
	{
		this.edges = edges;
	}
}
