package treebolic.model.graph;

/**
 * Tree
 *
 * @author Bernard Bou
 */
public class Tree
{
	/**
	 * Tree root
	 */
	public final GraphNode theRoot;

	/**
	 * Graph
	 */
	public final Graph theGraph;

	/**
	 * Constructor
	 *
	 * @param thisGraph
	 *        graph
	 * @param thisRoot
	 *        tree root
	 */
	public Tree(final Graph thisGraph, final GraphNode thisRoot)
	{
		this.theRoot = thisRoot;
		this.theGraph = thisGraph;
	}
}
