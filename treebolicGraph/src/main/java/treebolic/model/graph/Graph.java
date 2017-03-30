package treebolic.model.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Graph
 *
 * @author Bernard Bou
 */
public class Graph
{
	static final boolean dfs = false;

	/**
	 * The set of nodes.
	 */
	protected final Set<GraphNode> theNodes;

	/**
	 * The set of edges
	 */
	protected final Set<GraphEdge> theEdges;

	/**
	 * Constructor
	 */
	protected Graph()
	{
		this.theNodes = new HashSet<>();
		this.theEdges = new HashSet<>();
	}

	/**
	 * Returns a unmodifiable <code>Collection</code> of all nodes of this graph. The result is unmodifiable because any deletions in the collection would leave
	 * the graph in an undefined state. To delete or add a node, the methods in this class must be used.
	 *
	 * @return the nodes of the graph.
	 */
	public Collection<GraphNode> getNodes()
	{
		return Collections.unmodifiableCollection(this.theNodes);
	}

	/**
	 * Returns a unmodifiable <code>Collection</code> of all the edges of this graph. The result is unmodifiable because any deletions in the collection would
	 * leave the graph in an undefined state. To delete or add an edge, the methods in this class must be used.
	 *
	 * @return the edges of the graph.
	 */
	public Collection<GraphEdge> getEdges()
	{
		return Collections.unmodifiableCollection(this.theEdges);
	}

	/**
	 * Gets an unmodifiable collection of all edges adjacent to <code>thisNode</code> that has been marked as tree. This method ignores the direction of the
	 * edges.
	 *
	 * @param thisNode
	 *        node.
	 * @return set of adjacent tree edges.
	 */
	public Collection<GraphEdge> getTreeEdges(final GraphNode thisNode)
	{
		final Set<GraphEdge> theseEdges = new HashSet<>();
		for (final GraphEdge thisEdge : this.theEdges)
		{
			final Boolean isTreeEdge = thisEdge.getIsTreeEdge();
			if (isTreeEdge == null || !isTreeEdge)
			{
				continue;
			}
			if (thisEdge.getFrom().equals(thisNode) || thisEdge.getTo().equals(thisNode))
			{
				theseEdges.add(thisEdge);
			}
		}
		return Collections.unmodifiableCollection(theseEdges);
	}

	/**
	 * Gets an unmodifiable collection of all edges adjacent to <code>thisNode</code> that has been marked as tree. This method ignores the direction of the
	 * edges.
	 *
	 * @param thisNode
	 *        node.
	 * @return set of adjacent tree edges.
	 */
	public Collection<GraphEdge> getNonTreeEdges(final GraphNode thisNode)
	{
		final Set<GraphEdge> theseEdges = new HashSet<>();
		for (final GraphEdge thisEdge : this.theEdges)
		{
			final Boolean isTreeEdge = thisEdge.getIsTreeEdge();
			if (isTreeEdge != null && isTreeEdge)
			{
				continue;
			}
			if (thisEdge.getFrom().equals(thisNode) || thisEdge.getTo().equals(thisNode))
			{
				theseEdges.add(thisEdge);
			}
		}
		return Collections.unmodifiableCollection(theseEdges);
	}

	/**
	 * Gets an unmodifiable collection of all edges adjacent to <code>thisNode</code>. This method ignores the direction of the theEdges.
	 *
	 * @param thisNode
	 *        node.
	 * @return set of adjacent edges.
	 */
	public Collection<GraphEdge> getEdges(final GraphNode thisNode)
	{
		final Set<GraphEdge> theseEdges = new HashSet<>();
		for (final GraphEdge thisEdge : this.theEdges)
			if (thisEdge.getFrom().equals(thisNode) || thisEdge.getTo().equals(thisNode))
			{
				theseEdges.add(thisEdge);
			}
		return Collections.unmodifiableCollection(theseEdges);
	}

	/**
	 * Returns a unmodifiable <code>Map</code> of all edges of this graph. The result is unmodifiable because any deletions in the collection would leave the
	 * graph in an undefined state. To delete or add an edge, the methods in this class must be used.
	 *
	 * @return the edges of the graph.
	 */
	public Map<GraphNode, Collection<GraphEdge>> getNodeToEdgesMap()
	{
		final Map<GraphNode, Collection<GraphEdge>> thisMap = new HashMap<>();
		for (final GraphNode thisNode : this.theNodes)
		{
			thisMap.put(thisNode, getEdges(thisNode));
		}
		return Collections.unmodifiableMap(thisMap);
	}

	/**
	 * Returns a spanning tree of the graph. The spanning tree is a new instance of <code>Tree</code>, the nodes of the original graph and the spanning tree are
	 * shared and the edges are either shared or the spanning tree uses the reverse edges. The root of the spanning tree is the node that is returned by
	 * {@link #getNodeWithMinimumIncomingDegree()}, i.e. a node with minimal incoming degree. The spanning tree is directed, i.e. the direction of all the edges
	 * is as it is expected for trees.
	 *
	 * @return A spanning tree of the graph.
	 */
	public Tree makeSpanningTree()
	{
		// root
		final GraphNode thisRoot = getNodeWithMinimumIncomingDegree();
		return makeSpanningTree(thisRoot);
	}

	/**
	 * Make spanning tree
	 *
	 * @param thisRoot
	 *        root to start from
	 * @return spanning tree
	 */
	public synchronized Tree makeSpanningTree(final GraphNode thisRoot)
	{
		// System.err.println("root " + thisRoot);

		// result graph
		final Graph thisSpanningTree = new Graph();
		thisSpanningTree.theNodes.add(thisRoot);

		// populate
		if (Graph.dfs)
		{
			processSpanningTreeDFS(thisSpanningTree, thisRoot);
		}
		else
		{
			processSpanningTreeBFS(thisSpanningTree, thisRoot);
		}

		return new Tree(thisSpanningTree, thisRoot);
	}

	/**
	 * Populate depth-first search
	 *
	 * @param thisSpanningTree
	 *        spanning tree (stub)
	 * @param thisRoot
	 *        starting node
	 */
	private void processSpanningTreeDFS(final Graph thisSpanningTree, final GraphNode thisRoot)
	{
		// check all outgoing nodes, whether they are already in the spanning tree or not. If not, add them.
		for (GraphEdge thisEdge : getTreeEdges(thisRoot))
		{
			// get node at other end of the edge
			final GraphNode thisConnectedNode = thisEdge.getOtherNode(thisRoot);

			// if the spanning tree does not have this node
			if (!thisSpanningTree.theNodes.contains(thisConnectedNode))
			{
				// if the edge is backwards, reverse it
				if (thisConnectedNode == thisEdge.getFrom())
				{
					thisEdge = GraphEdge.makeReverseOf(thisEdge);
				}

				// add node and edge to the spanning tree
				thisSpanningTree.theNodes.add(thisConnectedNode);
				thisSpanningTree.theEdges.add(thisEdge);

				// move down
				processSpanningTreeDFS(thisSpanningTree, thisConnectedNode);
			}
		}

		// check all outgoing nodes, whether they are already in the spanning tree or not. If not, add them.
		for (GraphEdge thisEdge : getNonTreeEdges(thisRoot))
		{
			// get node at other end of the edge
			final GraphNode thisConnectedNode = thisEdge.getOtherNode(thisRoot);

			// if the spanning tree does not have this node
			if (!thisSpanningTree.theNodes.contains(thisConnectedNode))
			{
				// if the edge is backwards, reverse it
				if (thisConnectedNode == thisEdge.getFrom())
				{
					thisEdge = GraphEdge.makeReverseOf(thisEdge);
				}

				// add node and edge to the spanning tree
				thisSpanningTree.theNodes.add(thisConnectedNode);
				thisSpanningTree.theEdges.add(thisEdge);

				// move down
				processSpanningTreeDFS(thisSpanningTree, thisConnectedNode);
			}
		}
	}

	/**
	 * Populate breadth-first search
	 *
	 * @param thisSpanningTree
	 *        spanning tree (stub)
	 * @param thisRoot
	 *        starting node
	 */
	private void processSpanningTreeBFS(final Graph thisSpanningTree, final GraphNode thisRoot)
	{
		// bag
		final Set<GraphNode> thisBag = new HashSet<>();
		thisBag.add(thisRoot);

		while (!thisBag.isEmpty())
		{
			// pick node from the bag (and remove it)
			final GraphNode thisNode = thisBag.iterator().next();
			thisBag.remove(thisNode);

			// follow each edge starting from this node
			for (GraphEdge thisEdge : getTreeEdges(thisNode))
			{
				// get node at other end of the edge
				final GraphNode thisConnectedNode = thisEdge.getOtherNode(thisNode);

				// if the spanning tree does not have this node
				if (!thisSpanningTree.theNodes.contains(thisConnectedNode))
				{
					// if the edge is backwards, reverse it
					if (thisConnectedNode == thisEdge.getFrom())
					{
						thisEdge = GraphEdge.makeReverseOf(thisEdge);
					}

					// add connected node and edge to spanning tree
					thisSpanningTree.theNodes.add(thisConnectedNode);
					thisSpanningTree.theEdges.add(thisEdge);

					// add this node to bag
					thisBag.add(thisConnectedNode);
				}
			}

			// follow each non edge starting from this node
			for (GraphEdge thisEdge : getNonTreeEdges(thisNode))
			{
				// get node at other end of the edge
				final GraphNode thisConnectedNode = thisEdge.getOtherNode(thisNode);

				// if the spanning tree does not have this node
				if (!thisSpanningTree.theNodes.contains(thisConnectedNode))
				{
					// if the edge is backwards, reverse it
					if (thisConnectedNode == thisEdge.getFrom())
					{
						thisEdge = GraphEdge.makeReverseOf(thisEdge);
					}

					// add connected node and edge to spanning tree
					thisSpanningTree.theNodes.add(thisConnectedNode);
					thisSpanningTree.theEdges.add(thisEdge);

					// add this node to bag
					thisBag.add(thisConnectedNode);
				}
			}
		}
	}

	// D E G R E E S

	/**
	 * Determines a node with the minimal incoming degree. Often there are several such nodes, this method returns any of these, it is not guaranteed that two
	 * separate calls return the same node.
	 *
	 * @return A node with the minimal incoming degree.
	 */
	public GraphNode getNodeWithMinimumIncomingDegree()
	{
		// the node with the smallest incoming degree so far
		GraphNode thisResult = null;

		// the current incoming degree
		int thisMinimumDegree = -1;

		// for each node
		for (final GraphNode thisNode : getNodes())
		{
			// compute incoming degree for this node
			int thisDegree = 0;
			for (final GraphEdge thisEdge : getEdges(thisNode))
				if (thisEdge.getTo().equals(thisNode))
				{
					thisDegree++;
				}

			if (thisMinimumDegree < 0 || thisMinimumDegree > thisDegree)
			{
				// this node is either the first node or better than all before
				thisResult = thisNode;
				thisMinimumDegree = thisDegree;
			}

			// the degree can not be smaller than 0, we can stop here
			if (thisMinimumDegree == 0)
			{
				break;
			}
		}
		return thisResult;
	}

	/**
	 * Determines a node with the minimal incoming degree. Often there are several such nodes, this method returns any of these, it is not guaranteed that two
	 * separate calls return the same node.
	 *
	 * @return A node with the minimal incoming degree.
	 */
	public List<GraphNode> getNodesWithZeroDegree()
	{
		// the nodes with zero incoming degree
		List<GraphNode> thisResult = null;

		// for each node
		for (final GraphNode thisNode : getNodes())
		{
			// compute incoming degree for this node
			boolean zeroDegree = true;
			for (final GraphEdge thisEdge : getEdges(thisNode))
				if (thisEdge.getTo().equals(thisNode))
				{
					zeroDegree = false;
					break;
				}

			// the degree can not be smaller than 0, we can stop here
			if (zeroDegree)
			{
				if (thisResult == null)
				{
					thisResult = new ArrayList<>();
				}
				thisResult.add(thisNode);
			}
		}
		return thisResult;
	}

	// S T R I N G

	@Override
	public String toString()
	{
		final StringBuilder thisBuilder = new StringBuilder();
		thisBuilder.append("Nodes :\n");
		for (final GraphNode thisNode : getNodes())
		{
			thisBuilder.append(thisNode.toString());
			thisBuilder.append("\n");
		}
		thisBuilder.append("Edges :\n");
		for (final GraphEdge thisEdge : getEdges())
		{
			thisBuilder.append(thisEdge.toString());
			thisBuilder.append("\n");
		}
		return thisBuilder.toString();
	}
}
