package treebolic.model;

import java.io.Serializable;
import java.util.List;

/**
 * Model
 *
 * @author Bernard Bou
 */
public class Tree implements Serializable
{
	private static final long serialVersionUID = 6923143990877138332L;

	// D A T A

	/**
	 * Root node
	 */
	private INode theRoot;

	/**
	 * Edge list
	 */
	private List<IEdge> theEdges;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param thisRoot
	 *            root node
	 * @param theseEdges
	 *            edge list
	 */
	public Tree(final INode thisRoot, final List<IEdge> theseEdges)
	{
		this.theRoot = thisRoot;
		this.theEdges = theseEdges;
	}

	// A C C E S S

	/**
	 * Get root node
	 *
	 * @return root node
	 */
	public INode getRoot()
	{
		return this.theRoot;
	}

	/**
	 * Get edges
	 *
	 * @return edges
	 */
	public List<IEdge> getEdges()
	{
		return this.theEdges;
	}

	/**
	 * Set root node
	 *
	 * @param thisRoot
	 *            node
	 */
	public void setRoot(final INode thisRoot)
	{
		this.theRoot = thisRoot;
	}

	/**
	 * Set edges
	 *
	 * @param theseEdges
	 *            edge list
	 */
	public void setEdges(final List<IEdge> theseEdges)
	{
		this.theEdges = theseEdges;
	}
}
