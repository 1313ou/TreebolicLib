package treebolic.model.graph;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import treebolic.model.IEdge;
import treebolic.model.INode;
import treebolic.model.MutableNode;
import treebolic.model.Tree;
import treebolic.model.TreeMutableNode;

/**
 * Graph converter
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class Converter<T extends TreeMutableNode>
{
	/**
	 * Convert graph to tree
	 *
	 * @param graph graph
	 * @return tree
	 */
	@NonNull
	public Tree graphToTree(@NonNull final treebolic.model.graph.Graph graph)
	{
		// determine root node
		GraphNode rootNode;
		final List<GraphNode> rootNodes = graph.getNodesWithZeroDegree();
		if (rootNodes != null)
		{
			if (rootNodes.size() == 1)
			{
				rootNode = rootNodes.get(0);
			}
			else
			{
				throw new RuntimeException("No single root " + rootNodes); //$NON-NLS-1$
			}
		}
		else
		{
			rootNode = graph.getNodeWithMinimumIncomingDegree();
		}
		return graphToTree(graph, rootNode);
	}

	/**
	 * Convert graph to tree
	 *
	 * @param graph    graph
	 * @param rootNode root node
	 * @return tree
	 */
	@NonNull
	@SuppressWarnings({"unchecked"})
	public Tree graphToTree(@NonNull final treebolic.model.graph.Graph graph, @Nullable final GraphNode rootNode)
	{
		// spanning tree
		if (rootNode == null)
		{
			throw new RuntimeException("Null root"); //$NON-NLS-1$
		}

		// spanning tree
		final treebolic.model.graph.Tree spanningTree = graph.makeSpanningTree(rootNode);

		// tree edges
		final Collection<GraphEdge> graphEdges = spanningTree.graph.getEdges();
		for (final GraphEdge graphEdge : graphEdges)
		{
			// tree edge nodes
			final T fromNode = (T) graphEdge.getFrom();
			final MutableNode toNode = (MutableNode) graphEdge.getTo();

			// parent child
			fromNode.addChild(toNode);

			// transfer style
			final IEdge edge = (IEdge) graphEdge.getUserData();
			toNode.setEdgeColor(edge.getColor());
			toNode.setEdgeStyle(edge.getStyle());
			toNode.setEdgeLabel(edge.getLabel());
			toNode.setEdgeImageFile(edge.getImageFile());
		}

		// non-tree edges
		List<IEdge> edges = null;
		for (final GraphEdge graphEdge : graph.getEdges())
		{
			if (graphEdges.contains(graphEdge))
			{
				continue;
			}
			final IEdge edge = (IEdge) graphEdge.getUserData();
			if (edges == null)
			{
				edges = new ArrayList<>();
			}
			edges.add(edge);
		}

		return new Tree((INode) spanningTree.root, edges);
	}
}
