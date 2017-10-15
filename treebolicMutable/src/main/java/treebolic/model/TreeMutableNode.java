package treebolic.model;

import java.util.ArrayList;
import java.util.List;

import treebolic.glue.Color;

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
	public TreeMutableNode(final INode thisParent, final String thisId)
	{
		super(thisParent, thisId);
	}

	/**
	 * Constructor
	 *
	 * @param thisParent parent
	 * @param thisId id
	 * @param thisLabel label
	 * @param thisImageIndex image index
	 * @param thisBackColor backcolor
	 * @param thisForeColor forecolor
	 */
	public TreeMutableNode(final INode thisParent, final String thisId, final String thisLabel, final int thisImageIndex, final Color thisBackColor, final Color thisForeColor)
	{
		super(thisParent, thisId, thisLabel, thisImageIndex, thisBackColor, thisForeColor);
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
	 * Add child to this node
	 *
	 * @param thisChild child
	 */
	static public void assertNoLink(final INode thisParent, final INode thisChild)
	{
		List<INode> theseChildren = thisParent.getChildren();
		if (theseChildren != null)
		{
			// assert !theseChildren.contains(thisChild);
			if (theseChildren.contains(thisChild))
			{
				throw new RuntimeException("parent " + thisParent.getId() + "] '" + thisParent.getLabel() + "' already has child " + thisChild.getId() + "] '" + thisChild.getLabel() + "'");
			}
		}

		// assert thisChild.getParent() == null;
		if (thisChild.getParent() != null)
		{
			throw new RuntimeException("child [" + thisChild.getId() + "] '" + thisChild.getLabel() + "' already has parent [" + thisParent.getId() + "] '" + thisParent.getLabel() + "'");
		}
	}

	/**
	 * Add child to this node
	 *
	 * @param thisChild child
	 */
	public void addChild(final INode thisChild)
	{
		// assertNoLink(this, thisChild);

		List<INode> theseChildren = this.getChildren();
		if (theseChildren == null)
		{
			theseChildren = new ArrayList<>();
			this.setChildren(theseChildren);
		}
		theseChildren.add(thisChild);
		thisChild.setParent(this);
	}

	/**
	 * Add children to parent
	 *
	 * @param theseChildren children nodes
	 */
	public void addChildren(final INode... theseChildren)
	{
		if (theseChildren != null)
		{
			for (INode thisChild : theseChildren)
			{
				addChild(thisChild);
			}
		}
	}

	/**
	 * Add children to parent
	 *
	 * @param theseChildren children nodes
	 */
	public void addChildren(final List<? extends INode> theseChildren)
	{
		if (theseChildren != null)
		{
			for (INode thisChild : theseChildren)
			{
				addChild(thisChild);
			}
		}
	}

	/**
	 * Insert child to this node
	 *
	 * @param thisChild child
	 * @param i         ith position
	 */
	@SuppressWarnings("WeakerAccess")
	public void insertChild(final INode thisChild, @SuppressWarnings("SameParameterValue") final int i)
	{
		List<INode> theseChildren = this.getChildren();
		if (theseChildren == null)
		{
			theseChildren = new ArrayList<>();
			this.setChildren(theseChildren);
		}
		theseChildren.add(i, thisChild);
		thisChild.setParent(this);
	}

	/**
	 * Prepend child to this node
	 *
	 * @param thisChild child
	 */
	public void prependChild(final INode thisChild)
	{
		insertChild(thisChild, 0);
	}

	/**
	 * Remove from parent (handles down link and uplink)
	 *
	 * @param thisChild child
	 */
	static public void removeFromParent(final INode thisChild)
	{
		remove(thisChild.getParent(), thisChild);
	}

	/**
	 * Remove child from parent (handles down link and uplink)
	 *
	 * @param thisParent parent
	 * @param thisChild  child
	 */
	@SuppressWarnings("WeakerAccess")
	static public void remove(final INode thisParent, final INode thisChild)
	{
		if (thisParent != null)
		{
			final List<INode> theseChildren = thisParent.getChildren();
			if (theseChildren != null)
			{
				theseChildren.remove(thisChild);
			}
		}
		thisChild.setParent(null);
	}
}
