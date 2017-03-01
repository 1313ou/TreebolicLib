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
 * Graph edge
 *
 * @author Bernard Bou
 */
public class GraphEdge
{
	// U S E R D A T A

	/**
	 * User data
	 */
	private Object theUserData;

	/**
	 * Whether this edge is tree edge (null=undetermined)
	 */
	private final Boolean isTreeEdge;

	// D A T A

	/**
	 * Source node
	 */
	private final GraphNode theFromNode;

	/**
	 * Destination node
	 */
	private final GraphNode theToNode;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param thisFromNode
	 *        source node
	 * @param thisToNode
	 *        destination node
	 */
	public GraphEdge(final GraphNode thisFromNode, final GraphNode thisToNode, final Boolean isTreeEdgeFlag)
	{
		this.theFromNode = thisFromNode;
		this.theToNode = thisToNode;
		this.isTreeEdge = isTreeEdgeFlag;
	}

	/**
	 * Make inverse edge
	 *
	 * @param thatEdge
	 *        edge
	 * @return edge
	 */
	static GraphEdge makeReverseOf(final GraphEdge thatEdge)
	{
		final GraphEdge thisEdge = new GraphEdge(thatEdge.getTo(), thatEdge.getFrom(), thatEdge.getIsTreeEdge());
		thisEdge.setUserData(thatEdge.getUserData());
		return thisEdge;
	}

	/**
	 * Get source node
	 *
	 * @return source node
	 */
	public GraphNode getFrom()
	{
		return this.theFromNode;
	}

	/**
	 * Get destination node
	 *
	 * @return destination node
	 */
	public GraphNode getTo()
	{
		return this.theToNode;
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
		return this.theUserData;
	}

	/**
	 * Set user data
	 *
	 * @param thisUserData
	 *        user data
	 */
	public void setUserData(final Object thisUserData)
	{
		this.theUserData = thisUserData;
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
	 * @param thisNode
	 *        reference node
	 * @return node
	 */
	public GraphNode getOtherNode(final GraphNode thisNode)
	{
		return thisNode.equals(this.theFromNode) ? this.theToNode : this.theFromNode;
	}

	@Override
	public String toString()
	{
		return this.theFromNode.toString() + " -> " + this.theToNode.toString();
	}
}
