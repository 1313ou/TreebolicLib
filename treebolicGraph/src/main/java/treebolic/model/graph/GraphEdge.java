package treebolic.model.graph;

import androidx.annotation.NonNull;

/**
 * Graph edge
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class GraphEdge
{
	// U S E R D A T A

	/**
	 * User data
	 */
	private Object userData;

	/**
	 * Whether this edge is tree edge (null=undetermined)
	 */
	private final Boolean isTreeEdge;

	// D A T A

	/**
	 * Source node
	 */
	private final GraphNode fromNode;

	/**
	 * Destination node
	 */
	private final GraphNode toNode;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param fromNode source node
	 * @param toNode   destination node
	 */
	public GraphEdge(final GraphNode fromNode, final GraphNode toNode, final Boolean isTreeEdgeFlag)
	{
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.isTreeEdge = isTreeEdgeFlag;
	}

	/**
	 * Make inverse edge
	 *
	 * @param edge0 edge
	 * @return edge
	 */
	@NonNull
	static GraphEdge makeReverseOf(@NonNull final GraphEdge edge0)
	{
		final GraphEdge edge = new GraphEdge(edge0.getTo(), edge0.getFrom(), edge0.getIsTreeEdge());
		edge.setUserData(edge0.getUserData());
		return edge;
	}

	/**
	 * Get source node
	 *
	 * @return source node
	 */
	public GraphNode getFrom()
	{
		return this.fromNode;
	}

	/**
	 * Get destination node
	 *
	 * @return destination node
	 */
	public GraphNode getTo()
	{
		return this.toNode;
	}

	/**
	 * Whether this edge is tree edge (null=undetermined)
	 */
	public Boolean getIsTreeEdge()
	{
		return this.isTreeEdge;
	}

	/**
	 * Get user data
	 *
	 * @return user data
	 */
	public Object getUserData()
	{
		return this.userData;
	}

	/**
	 * Set user data
	 *
	 * @param userData user data
	 */
	@SuppressWarnings("WeakerAccess")
	public void setUserData(final Object userData)
	{
		this.userData = userData;
	}

	/**
	 * Whether this edge is tree edge (null=undetermined)
	 *
	 * @return Boolean (null=undetermined)
	 */
	public Boolean get()
	{
		return this.isTreeEdge;
	}

	/**
	 * Get node other than
	 *
	 * @param node reference node
	 * @return node
	 */
	public GraphNode getOtherNode(@NonNull final GraphNode node)
	{
		return node.equals(this.fromNode) ? this.toNode : this.fromNode;
	}

	@NonNull
	@Override
	public String toString()
	{
		return this.fromNode.toString() + " -> " + this.toNode.toString();
	}
}
