package treebolic.model;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
	 * @param parent parent
	 * @param id     id
	 */
	public TreeMutableNode(@SuppressWarnings("SameParameterValue") final INode parent, final String id)
	{
		super(parent, id);
	}

	/**
	 * Constructor
	 *
	 * @param parent     parent
	 * @param id         id
	 * @param label      label
	 * @param imageIndex image index
	 * @param backColor  backcolor
	 * @param foreColor  forecolor
	 */
	public TreeMutableNode(final INode parent, final String id, final String label, final int imageIndex, final Color backColor, final Color foreColor)
	{
		super(parent, id, label, imageIndex, backColor, foreColor);
	}

	/**
	 * Copy constructor (the resulting node node has no tree parent nor tree children)
	 *
	 * @param node node
	 */
	public TreeMutableNode(@NonNull final INode node)
	{
		super(null, node.getId());
		this.children = null;

		this.label = node.getLabel();
		this.content = node.getContent();
		this.backColor = node.getBackColor();
		this.foreColor = node.getForeColor();
		this.imageFile = node.getImageFile();

		this.edgeLabel = node.getEdgeLabel();
		this.edgeColor = node.getEdgeColor();
		this.edgeStyle = node.getEdgeStyle();
		this.edgeImageFile = node.getEdgeImageFile();

		this.link = node.getLink();
		this.target = node.getTarget();
		this.mountPoint = node.getMountPoint();
	}

	// I D

	/**
	 * Set id
	 *
	 * @param id id
	 */
	public void setId(final String id)
	{
		this.id = id;
	}

	// T R E E . L I N K S

	/**
	 * Set children
	 *
	 * @param children children
	 */
	@SuppressWarnings("WeakerAccess")
	public void setChildren(final List<INode> children)
	{
		this.children = children;
	}

	/**
	 * Add child to this node
	 *
	 * @param child child
	 */
	static public void assertNoLink(@NonNull final INode parent, @NonNull final INode child)
	{
		List<INode> children = parent.getChildren();
		if (children != null)
		{
			// assert !children.contains(child);
			if (children.contains(child))
			{
				throw new RuntimeException("parent " + parent.getId() + "] '" + parent.getLabel() + "' already has child " + child.getId() + "] '" + child.getLabel() + "'");
			}
		}

		// assert child.getParent() == null;
		if (child.getParent() != null)
		{
			throw new RuntimeException("child [" + child.getId() + "] '" + child.getLabel() + "' already has parent [" + parent.getId() + "] '" + parent.getLabel() + "'");
		}
	}

	/**
	 * Add child to this node
	 *
	 * @param child child
	 */
	public void addChild(@NonNull final INode child)
	{
		// assertNoLink(this, child);

		List<INode> children = this.getChildren();
		if (children == null)
		{
			children = new ArrayList<>();
			this.setChildren(children);
		}
		children.add(child);
		child.setParent(this);
	}

	/**
	 * Add children to parent
	 *
	 * @param children children nodes
	 */
	public void addChildren(@Nullable final INode... children)
	{
		if (children != null)
		{
			for (INode child : children)
			{
				addChild(child);
			}
		}
	}

	/**
	 * Add children to parent
	 *
	 * @param children children nodes
	 */
	public void addChildren(@Nullable final List<? extends INode> children)
	{
		if (children != null)
		{
			for (INode child : children)
			{
				addChild(child);
			}
		}
	}

	/**
	 * Insert child to this node
	 *
	 * @param child child
	 * @param i     ith position
	 */
	@SuppressWarnings("WeakerAccess")
	public void insertChild(@NonNull final INode child, @SuppressWarnings("SameParameterValue") final int i)
	{
		List<INode> children = this.getChildren();
		if (children == null)
		{
			children = new ArrayList<>();
			this.setChildren(children);
		}
		children.add(i, child);
		child.setParent(this);
	}

	/**
	 * Prepend child to this node
	 *
	 * @param child child
	 */
	public void prependChild(@NonNull final INode child)
	{
		insertChild(child, 0);
	}

	/**
	 * Remove from parent (handles down link and uplink)
	 *
	 * @param child child
	 */
	static public void removeFromParent(@NonNull final INode child)
	{
		remove(child.getParent(), child);
	}

	/**
	 * Remove child from parent (handles down link and uplink)
	 *
	 * @param parent parent
	 * @param child  child
	 */
	@SuppressWarnings("WeakerAccess")
	static public void remove(@Nullable final INode parent, @NonNull final INode child)
	{
		if (parent != null)
		{
			final List<INode> children = parent.getChildren();
			if (children != null)
			{
				children.remove(child);
			}
		}
		child.setParent(null);
	}
}
