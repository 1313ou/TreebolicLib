package treebolic.model;

/**
 * Extended mutable node (mutable ends, copy constructor)
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class TreeMutableEdge extends MutableEdge
{
	private static final long serialVersionUID = -1812958269993842207L;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param thisFrom from node (may be null)
	 * @param thisTo   to node (may be null)
	 */
	public TreeMutableEdge(final MutableNode thisFrom, final MutableNode thisTo)
	{
		super(thisFrom, thisTo);
	}

	/**
	 * Copy constructor (the resulting edge has no node ends)
	 *
	 * @param thatEdge edge
	 */
	public TreeMutableEdge(final IEdge thatEdge)
	{
		super(null, null);
		this.theColor = thatEdge.getColor();
		this.theLabel = thatEdge.getLabel();
		this.theStyle = thatEdge.getStyle();
		this.theImageFile = thatEdge.getImageFile();
	}

	// E N D S

	/**
	 * Set from-node (origin)
	 *
	 * @param thisFromNode from-node (may be null)
	 */
	public void setFrom(final MutableNode thisFromNode)
	{
		this.theFromNode = thisFromNode;
	}

	/**
	 * Set to-node (destination)
	 *
	 * @param thisToNode to-node (may be null)
	 */
	public void setTo(final MutableNode thisToNode)
	{
		this.theToNode = thisToNode;
	}
}
