package treebolic.model.graph;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import treebolic.model.IEdge;
import treebolic.model.INode;
import treebolic.model.MutableEdge;
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
	 * @param thisGraph graph
	 * @return tree
	 */
	@NonNull
	public Tree graphToTree(@NonNull final treebolic.model.graph.Graph thisGraph)
	{
		// determine root node
		GraphNode thisRootNode;
		final List<GraphNode> theseRootNodes = thisGraph.getNodesWithZeroDegree();
		if (theseRootNodes != null)
		{
			if (theseRootNodes.size() == 1)
			{
				thisRootNode = theseRootNodes.get(0);
			}
			else
			{
				throw new RuntimeException("No single root " + theseRootNodes); //$NON-NLS-1$
			}
		}
		else
		{
			thisRootNode = thisGraph.getNodeWithMinimumIncomingDegree();
		}
		return graphToTree(thisGraph, thisRootNode);
	}

	/**
	 * Convert graph to tree
	 *
	 * @param thisGraph    graph
	 * @param thisRootNode root node
	 * @return tree
	 */
	@NonNull
	@SuppressWarnings({"unchecked"})
	public Tree graphToTree(@NonNull final treebolic.model.graph.Graph thisGraph, @Nullable final GraphNode thisRootNode)
	{
		// spanning tree
		if (thisRootNode == null)
		{
			throw new RuntimeException("Null root"); //$NON-NLS-1$
		}

		// spanning tree
		final treebolic.model.graph.Tree thisSpanningTree = thisGraph.makeSpanningTree(thisRootNode);

		// tree edges
		final Collection<GraphEdge> theseGraphEdges = thisSpanningTree.theGraph.getEdges();
		for (final GraphEdge thisGraphEdge : theseGraphEdges)
		{
			// tree edge nodes
			final T thisFromNode = (T) thisGraphEdge.getFrom();
			final MutableNode thisToNode = (MutableNode) thisGraphEdge.getTo();

			// parent child
			thisFromNode.addChild(thisToNode);

			// transfer style
			final IEdge thisEdge = (IEdge) thisGraphEdge.getUserData();
			thisToNode.setEdgeColor(thisEdge.getColor());
			thisToNode.setEdgeStyle(thisEdge.getStyle());
			thisToNode.setEdgeLabel(thisEdge.getLabel());
			thisToNode.setEdgeImageFile(thisEdge.getImageFile());
		}

		// non-tree edges
		List<IEdge> theseEdges = null;
		for (final GraphEdge thisGraphEdge : thisGraph.getEdges())
		{
			if (theseGraphEdges.contains(thisGraphEdge))
			{
				continue;
			}
			final MutableEdge thisEdge = (MutableEdge) thisGraphEdge.getUserData();
			if (theseEdges == null)
			{
				theseEdges = new ArrayList<>();
			}
			theseEdges.add(thisEdge);
		}

		return new Tree((INode) thisSpanningTree.theRoot, theseEdges);
	}
}
