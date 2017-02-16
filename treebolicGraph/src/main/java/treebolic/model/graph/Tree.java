/**
 * Title : Treebolic
 * Description : Treebolic
 * Version : 3.x
 * Copyright :	(c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 *
 */
package treebolic.model.graph;

/**
 * Tree
 *
 * @author Bernard Bou
 */
public class Tree
{
	/**
	 * Tree root
	 */
	public GraphNode theRoot;

	/**
	 * Graph
	 */
	public Graph theGraph;

	/**
	 * Constructor
	 *
	 * @param thisGraph
	 *        graph
	 * @param thisRoot
	 *        tree root
	 */
	public Tree(final Graph thisGraph, final GraphNode thisRoot)
	{
		this.theRoot = thisRoot;
		this.theGraph = thisGraph;
	}
}
