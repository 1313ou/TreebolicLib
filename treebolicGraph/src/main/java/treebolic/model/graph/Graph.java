package treebolic.model.graph;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
	@SuppressWarnings("WeakerAccess")
	static final boolean dfs = false;

	/**
	 * The set of nodes.
	 */
	@NonNull
	@SuppressWarnings("WeakerAccess")
	protected final Set<GraphNode> nodes;

	/**
	 * The set of edges
	 */
	@NonNull
	@SuppressWarnings("WeakerAccess")
	protected final Set<GraphEdge> edges;

	/**
	 * Constructor
	 */
	@SuppressWarnings("WeakerAccess")
	protected Graph()
	{
		this.nodes = new HashSet<>();
		this.edges = new HashSet<>();
	}

	/**
	 * Returns a unmodifiable {@code Collection} of all nodes of this graph. The result is unmodifiable because any deletions in the collection would leave
	 * the graph in an undefined state. To delete or add a node, the methods in this class must be used.
	 *
	 * @return the nodes of the graph.
	 */
	@SuppressWarnings("WeakerAccess")
	public Collection<GraphNode> getNodes()
	{
		return Collections.unmodifiableCollection(this.nodes);
	}

	/**
	 * Returns a unmodifiable {@code Collection} of all the edges of this graph. The result is unmodifiable because any deletions in the collection would
	 * leave the graph in an undefined state. To delete or add an edge, the methods in this class must be used.
	 *
	 * @return the edges of the graph.
	 */
	public Collection<GraphEdge> getEdges()
	{
		return Collections.unmodifiableCollection(this.edges);
	}

	/**
	 * Gets an unmodifiable collection of all edges adjacent to {@code node} that has been marked as tree. This method ignores the direction of the
	 * edges.
	 *
	 * @param node node.
	 * @return set of adjacent tree edges.
	 */
	@SuppressWarnings("WeakerAccess")
	public Collection<GraphEdge> getTreeEdges(final GraphNode node)
	{
		final Collection<GraphEdge> edgeSet = new HashSet<>();
		for (final GraphEdge edge : this.edges)
		{
			final Boolean isTreeEdge = edge.getIsTreeEdge();
			if (isTreeEdge == null || !isTreeEdge)
			{
				continue;
			}
			if (edge.getFrom().equals(node) || edge.getTo().equals(node))
			{
				edgeSet.add(edge);
			}
		}
		return Collections.unmodifiableCollection(edgeSet);
	}

	/**
	 * Gets an unmodifiable collection of all edges adjacent to {@code node} that has been marked as tree. This method ignores the direction of the
	 * edges.
	 *
	 * @param node node.
	 * @return set of adjacent tree edges.
	 */
	@SuppressWarnings("WeakerAccess")
	public Collection<GraphEdge> getNonTreeEdges(final GraphNode node)
	{
		final Collection<GraphEdge> edgeSet = new HashSet<>();
		for (final GraphEdge edge : this.edges)
		{
			final Boolean isTreeEdge = edge.getIsTreeEdge();
			if (isTreeEdge != null && isTreeEdge)
			{
				continue;
			}
			if (edge.getFrom().equals(node) || edge.getTo().equals(node))
			{
				edgeSet.add(edge);
			}
		}
		return Collections.unmodifiableCollection(edgeSet);
	}

	/**
	 * Gets an unmodifiable collection of all edges adjacent to {@code node}. This method ignores the direction of the edges.
	 *
	 * @param node node.
	 * @return set of adjacent edges.
	 */
	@SuppressWarnings("WeakerAccess")
	public Collection<GraphEdge> getEdges(final GraphNode node)
	{
		final Collection<GraphEdge> edgeSet = new HashSet<>();
		for (final GraphEdge edge : this.edges)
		{
			if (edge.getFrom().equals(node) || edge.getTo().equals(node))
			{
				edgeSet.add(edge);
			}
		}
		return Collections.unmodifiableCollection(edgeSet);
	}

	/**
	 * Returns a unmodifiable {@code Map} of all edges of this graph. The result is unmodifiable because any deletions in the collection would leave the
	 * graph in an undefined state. To delete or add an edge, the methods in this class must be used.
	 *
	 * @return the edges of the graph.
	 */
	public Map<GraphNode, Collection<GraphEdge>> getNodeToEdgesMap()
	{
		final Map<GraphNode, Collection<GraphEdge>> map = new HashMap<>();
		for (final GraphNode node : this.nodes)
		{
			map.put(node, getEdges(node));
		}
		return Collections.unmodifiableMap(map);
	}

	/**
	 * Returns a spanning tree of the graph. The spanning tree is a new instance of {@code Tree}, the nodes of the original graph and the spanning tree are
	 * shared and the edges are either shared or the spanning tree uses the reverse edges. The root of the spanning tree is the node that is returned by
	 * {@link #getNodeWithMinimumIncomingDegree()}, i.e. a node with minimal incoming degree. The spanning tree is directed, i.e. the direction of all the edges
	 * is as it is expected for trees.
	 *
	 * @return A spanning tree of the graph.
	 */
	@NonNull
	public Tree makeSpanningTree()
	{
		// root
		final GraphNode root = getNodeWithMinimumIncomingDegree();
		assert root != null;
		return makeSpanningTree(root);
	}

	/**
	 * Make spanning tree
	 *
	 * @param root root to start from
	 * @return spanning tree
	 */
	@NonNull
	public synchronized Tree makeSpanningTree(@NonNull final GraphNode root)
	{
		// System.err.println("root " + root);

		// result graph
		final Graph spanningTree = new Graph();
		spanningTree.nodes.add(root);

		// populate
		if (Graph.dfs)
		{
			processSpanningTreeDFS(spanningTree, root);
		}
		else
		{
			processSpanningTreeBFS(spanningTree, root);
		}

		return new Tree(spanningTree, root);
	}

	/**
	 * Populate depth-first search
	 *
	 * @param spanningTree spanning tree (stub)
	 * @param root         starting node
	 */
	private void processSpanningTreeDFS(@NonNull final Graph spanningTree, @NonNull final GraphNode root)
	{
		// check all outgoing nodes, whether they are already in the spanning tree or not. If not, add them.
		for (GraphEdge edge : getTreeEdges(root))
		{
			// get node at other end of the edge
			final GraphNode connectedNode = edge.getOtherNode(root);

			// if the spanning tree does not have this node
			if (!spanningTree.nodes.contains(connectedNode))
			{
				// if the edge is backwards, reverse it
				if (connectedNode == edge.getFrom())
				{
					edge = GraphEdge.makeReverseOf(edge);
				}

				// add node and edge to the spanning tree
				spanningTree.nodes.add(connectedNode);
				spanningTree.edges.add(edge);

				// move down
				processSpanningTreeDFS(spanningTree, connectedNode);
			}
		}

		// check all outgoing nodes, whether they are already in the spanning tree or not. If not, add them.
		for (GraphEdge edge : getNonTreeEdges(root))
		{
			// get node at other end of the edge
			final GraphNode connectedNode = edge.getOtherNode(root);

			// if the spanning tree does not have this node
			if (!spanningTree.nodes.contains(connectedNode))
			{
				// if the edge is backwards, reverse it
				if (connectedNode == edge.getFrom())
				{
					edge = GraphEdge.makeReverseOf(edge);
				}

				// add node and edge to the spanning tree
				spanningTree.nodes.add(connectedNode);
				spanningTree.edges.add(edge);

				// move down
				processSpanningTreeDFS(spanningTree, connectedNode);
			}
		}
	}

	/**
	 * Populate breadth-first search
	 *
	 * @param spanningTree spanning tree (stub)
	 * @param root         starting node
	 */
	private void processSpanningTreeBFS(@NonNull final Graph spanningTree, final GraphNode root)
	{
		// bag
		final Collection<GraphNode> bag = new HashSet<>();
		bag.add(root);

		while (!bag.isEmpty())
		{
			// pick node from the bag (and remove it)
			final GraphNode node = bag.iterator().next();
			bag.remove(node);

			// follow each edge starting from this node
			for (GraphEdge edge : getTreeEdges(node))
			{
				// get node at other end of the edge
				final GraphNode connectedNode = edge.getOtherNode(node);

				// if the spanning tree does not have this node
				if (!spanningTree.nodes.contains(connectedNode))
				{
					// if the edge is backwards, reverse it
					if (connectedNode == edge.getFrom())
					{
						edge = GraphEdge.makeReverseOf(edge);
					}

					// add connected node and edge to spanning tree
					spanningTree.nodes.add(connectedNode);
					spanningTree.edges.add(edge);

					// add this node to bag
					bag.add(connectedNode);
				}
			}

			// follow each non edge starting from this node
			for (GraphEdge edge : getNonTreeEdges(node))
			{
				// get node at other end of the edge
				final GraphNode connectedNode = edge.getOtherNode(node);

				// if the spanning tree does not have this node
				if (!spanningTree.nodes.contains(connectedNode))
				{
					// if the edge is backwards, reverse it
					if (connectedNode == edge.getFrom())
					{
						edge = GraphEdge.makeReverseOf(edge);
					}

					// add connected node and edge to spanning tree
					spanningTree.nodes.add(connectedNode);
					spanningTree.edges.add(edge);

					// add this node to bag
					bag.add(connectedNode);
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
	@Nullable
	public GraphNode getNodeWithMinimumIncomingDegree()
	{
		// the node with the smallest incoming degree so far
		GraphNode result = null;

		// the current incoming degree
		int minimumDegree = -1;

		// for each node
		for (final GraphNode node : getNodes())
		{
			// compute incoming degree for this node
			int degree = 0;
			for (final GraphEdge edge : getEdges(node))
			{
				if (edge.getTo().equals(node))
				{
					degree++;
				}
			}

			if (minimumDegree < 0 || minimumDegree > degree)
			{
				// this node is either the first node or better than all before
				result = node;
				minimumDegree = degree;
			}

			// the degree can not be smaller than 0, we can stop here
			if (minimumDegree == 0)
			{
				break;
			}
		}
		return result;
	}

	/**
	 * Determines a node with the minimal incoming degree. Often there are several such nodes, this method returns any of these, it is not guaranteed that two
	 * separate calls return the same node.
	 *
	 * @return A node with the minimal incoming degree.
	 */
	@Nullable
	public List<GraphNode> getNodesWithZeroDegree()
	{
		// the nodes with zero incoming degree
		List<GraphNode> result = null;

		// for each node
		for (final GraphNode node : getNodes())
		{
			// compute incoming degree for this node
			boolean zeroDegree = true;
			for (final GraphEdge edge : getEdges(node))
			{
				if (edge.getTo().equals(node))
				{
					zeroDegree = false;
					break;
				}
			}

			// the degree can not be smaller than 0, we can stop here
			if (zeroDegree)
			{
				if (result == null)
				{
					result = new ArrayList<>();
				}
				result.add(node);
			}
		}
		return result;
	}

	// S T R I N G

	@NonNull
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("Nodes :\n");
		for (final GraphNode node : getNodes())
		{
			sb.append(node.toString());
			sb.append("\n");
		}
		sb.append("Edges :\n");
		for (final GraphEdge edge : getEdges())
		{
			sb.append(edge.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}
