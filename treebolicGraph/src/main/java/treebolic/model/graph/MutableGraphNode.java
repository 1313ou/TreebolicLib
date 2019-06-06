package treebolic.model.graph;

import treebolic.model.TreeMutableNode;

/**
 * Graph node
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class MutableGraphNode extends TreeMutableNode implements GraphNode
{
	private static final long serialVersionUID = 8850315428753155363L;

	private final int index;

	static private int indexAllocator = 0;

	/**
	 * Constructor
	 *
	 * @param id id
	 */
	public MutableGraphNode(final String id)
	{
		super(null, id);
		this.index = MutableGraphNode.indexAllocator++;
	}

	/**
	 * Get the index
	 *
	 * @return index
	 */
	public int getIndex()
	{
		return this.index;
	}
}
