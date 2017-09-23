package treebolic.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Extended Mutable node (mutable tree links, copy constructor)
 *
 * @author Bernard Bou
 */
public class TreeMutableNode extends MutableNode
{
	private static final long serialVersionUID = -1045091316448354198L;

	// C O N S T R U C T O R S

	/**
	 * Constructor
	 *
	 * @param thisParent parent
	 * @param thisId     id
	 */
	public TreeMutableNode(final MutableNode thisParent, final String thisId)
	{
		super(thisParent, thisId);
	}

	/**
	 * Copy constructor (the resulting node node has no tree parent nor tree children)
	 *
	 * @param thatNode node
	 */
	public TreeMutableNode(final INode thatNode)
	{
		super(null, thatNode.getId());
		this.theChildren = null;

		this.theLabel = thatNode.getLabel();
		this.theContent = thatNode.getContent();
		this.theBackColor = thatNode.getBackColor();
		this.theForeColor = thatNode.getForeColor();
		this.theImageFile = thatNode.getImageFile();

		this.theEdgeLabel = thatNode.getEdgeLabel();
		this.theEdgeColor = thatNode.getEdgeColor();
		this.theEdgeStyle = thatNode.getEdgeStyle();
		this.theEdgeImageFile = thatNode.getEdgeImageFile();

		this.theLink = thatNode.getLink();
		this.theTarget = thatNode.getTarget();
		this.theMountPoint = thatNode.getMountPoint();
	}

	// I D

	/**
	 * Set id
	 *
	 * @param thisId id
	 */
	public void setId(final String thisId)
	{
		this.theId = thisId;
	}

	// T R E E . L I N K S

	/**
	 * Set children
	 *
	 * @param theseChildren children
	 */
	@SuppressWarnings("WeakerAccess")
	public void setChildren(final List<INode> theseChildren)
	{
		this.theChildren = theseChildren;
	}

	/**
	 * Insert to parent (handles down link and uplink)
	 */
	public void insertToParent(final TreeMutableNode thisParent, int i)
	{
		List<INode> theseChildren = thisParent.getChildren();
		if (theseChildren == null)
		{
			theseChildren = new ArrayList<>();
			thisParent.setChildren(theseChildren);
		}
		theseChildren.add(i, this);
		setParent(thisParent);
	}

	/**
	 * Add to parent (handles down link and uplink)
	 */
	public void addToParent(final TreeMutableNode thisParent)
	{
		List<INode> theseChildren = thisParent.getChildren();
		if (theseChildren == null)
		{
			theseChildren = new ArrayList<>();
			thisParent.setChildren(theseChildren);
		}
		theseChildren.add(this);
		setParent(thisParent);
	}

	/**
	 * Add to parent as first child (handles down link and uplink)
	 */
	public void prependToParent(final TreeMutableNode thisParent)
	{
		List<INode> theseChildren = thisParent.getChildren();
		if (theseChildren == null)
		{
			theseChildren = new ArrayList<>();
			thisParent.setChildren(theseChildren);
		}
		theseChildren.add(0, this);
		setParent(thisParent);
	}

	/**
	 * Remove from parent (handles down link and uplink)
	 */
	public void removeFromParent()
	{
		final INode thisParent = getParent();
		if (thisParent != null)
		{
			thisParent.getChildren().remove(this);
			setParent(null);
		}
	}
}