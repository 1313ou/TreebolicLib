/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.model;

import java.util.List;

import treebolic.annotations.NonNull;
import treebolic.annotations.Nullable;
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
	@SuppressWarnings("WeakerAccess")
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
	public void setChildren(@NonNull final List<INode> children)
	{
		this.children = children;
	}

	/**
	 * Add child to this node
	 *
	 * @param parent parent
	 * @param child  child
	 */
	static public void assertNoLink(@NonNull final INode parent, @NonNull final INode child)
	{
		@Nullable List<INode> children = parent.getChildren();
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
		@NonNull final List<INode> children = this.getChildren();
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
			for (@NonNull INode child : children)
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
	public void addChildren(@Nullable final Iterable<? extends INode> children)
	{
		if (children != null)
		{
			for (@NonNull INode child : children)
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
		@NonNull final List<INode> children = this.getChildren();
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
			@Nullable final List<INode> children = parent.getChildren();
			if (children != null)
			{
				children.remove(child);
			}
		}
		child.setParent(null);
	}
}
