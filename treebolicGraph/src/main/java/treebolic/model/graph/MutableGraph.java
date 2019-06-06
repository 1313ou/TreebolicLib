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
	 * @param node the node to add.
	 */
	public void add(final GraphNode node)
	{
		this.nodes.add(node);
	}

	/**
	 * Add edge.
	 *
	 * @param edge the edge to add.
	 */
	public void add(final GraphEdge edge)
	{
		this.edges.add(edge);
	}
}
