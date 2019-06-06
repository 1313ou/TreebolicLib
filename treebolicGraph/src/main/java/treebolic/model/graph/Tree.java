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
	public final GraphNode root;

	/**
	 * Graph
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	public final Graph graph;

	/**
	 * Constructor
	 *
	 * @param graph graph
	 * @param root  tree root
	 */
	public Tree(final Graph graph, final GraphNode root)
	{
		this.root = root;
		this.graph = graph;
	}
}
