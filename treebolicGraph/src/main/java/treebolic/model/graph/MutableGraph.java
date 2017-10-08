package treebolic.model.graph;

/**
 * Mutable graph
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class MutableGraph extends Graph
{
	/**
	 * Add node.
	 *
	 * @param thisNode the node to add.
	 */
	public void add(final GraphNode thisNode)
	{
		this.theNodes.add(thisNode);
	}

	/**
	 * Add edge.
	 *
	 * @param thisEdge the edge to add.
	 */
	public void add(final GraphEdge thisEdge)
	{
		this.theEdges.add(thisEdge);
	}
}
