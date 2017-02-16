package treebolic.model.graph;

import treebolic.model.TreeMutableNode;

/**
 * Graph node
 *
 * @author Bernard Bou
 */
public class MutableGraphNode extends TreeMutableNode implements GraphNode
{
	private static final long serialVersionUID = 8850315428753155363L;

	private final int index;

	static private int indexAllocator = 0;

	/**
	 * Constructor
	 *
	 * @param thisId
	 *        id
	 */
	public MutableGraphNode(final String thisId)
	{
		super(null, thisId);
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
