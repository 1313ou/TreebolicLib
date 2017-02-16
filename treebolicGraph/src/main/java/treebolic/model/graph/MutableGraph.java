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
 * Mutable graph
 *
 * @author Bernard Bou
 */
public class MutableGraph extends Graph
{
	/**
	 * Add node.
	 *
	 * @param thisNode
	 *        the node to add.
	 */
	public void add(final GraphNode thisNode)
	{
		this.theNodes.add(thisNode);
	}

	/**
	 * Add edge.
	 *
	 * @param thisEdge
	 *        the edge to add.
	 */
	public void add(final GraphEdge thisEdge)
	{
		this.theEdges.add(thisEdge);
	}
}
