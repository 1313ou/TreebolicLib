/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.model.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import treebolic.annotations.NonNull;
import treebolic.annotations.Nullable;
import treebolic.model.Tree;
import treebolic.model.*;

/**
 * Graph converter
 *
 * @param <T> type of mutable tree node
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
	public Tree graphToTree(@NonNull final Graph graph)
	{
		// determine root node
		@Nullable GraphNode rootNode;
		@Nullable final List<GraphNode> rootNodes = graph.getNodesWithZeroDegree();
		if (rootNodes != null)
		{
			if (rootNodes.size() == 1)
			{
				rootNode = rootNodes.get(0);
			}
			else
			{
				throw new RuntimeException("No single root " + rootNodes);
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
	public Tree graphToTree(@NonNull final Graph graph, @Nullable final GraphNode rootNode)
	{
		// spanning tree
		if (rootNode == null)
		{
			throw new RuntimeException("Null root");
		}

		// spanning tree
		@NonNull final treebolic.model.graph.Tree spanningTree = graph.makeSpanningTree(rootNode);

		// tree edges
		@NonNull final Collection<GraphEdge> graphEdges = spanningTree.graph.getEdges();
		for (@NonNull final GraphEdge graphEdge : graphEdges)
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
		@Nullable List<IEdge> edges = null;
		for (@NonNull final GraphEdge graphEdge : graph.getEdges())
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
