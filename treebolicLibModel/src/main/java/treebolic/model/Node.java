/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import treebolic.glue.Color;
import treebolic.glue.Image;

/**
 * Node
 *
 * @author Bernard Bou
 */
class NodeData implements Serializable
{
	private static final long serialVersionUID = -5631809010026385842L;

	// D A T A

	// id

	/**
	 * Node id
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected String id;

	// node data

	/**
	 * Node label
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected String label;

	/**
	 * Node content
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected String content;

	/**
	 * Node URL link
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected String link;

	/**
	 * Node link target frame
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected String target;

	/**
	 * Background color
	 */
	@Nullable
	@SuppressWarnings({"WeakerAccess", "InstanceVariableOfConcreteClass"})
	protected Color backColor;

	/**
	 * Foreground color
	 */
	@Nullable
	@SuppressWarnings({"WeakerAccess", "InstanceVariableOfConcreteClass"})
	protected Color foreColor;

	/**
	 * Node image filename
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected String imageFile;

	/**
	 * Node image index
	 */
	@SuppressWarnings("WeakerAccess")
	protected int imageIndex;

	// tree edge data

	/**
	 * Tree edge label
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected String edgeLabel;

	/**
	 * Tree edge color
	 */
	@Nullable
	@SuppressWarnings({"WeakerAccess", "InstanceVariableOfConcreteClass"})
	protected Color edgeColor;

	/**
	 * Tree edge style
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected Integer edgeStyle;

	/**
	 * Edge image filename
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected String edgeImageFile;

	/**
	 * Edge image filename
	 */
	@SuppressWarnings("WeakerAccess")
	protected int edgeImageIndex;

	// C O N S T R U C T O R

	/**
	 * Construct node
	 */
	@SuppressWarnings("WeakerAccess")
	public NodeData()
	{
		this.id = null;
		this.label = null;
		this.content = null;
		this.backColor = null;
		this.foreColor = null;
		this.imageFile = null;
		this.imageIndex = -1;
		this.link = null;
		this.target = null;

		this.edgeLabel = null;
		this.edgeColor = null;
		this.edgeStyle = null;
		this.edgeImageFile = null;
		this.edgeImageIndex = -1;
	}

	// I D

	@Nullable
	public String getId()
	{
		return this.id;
	}

	// A C C E S S

	// display
	@NonNull
	@Override
	public String toString()
	{
		return this.label != null ? this.label : "";
	}

	@Nullable
	public String toWritableString()
	{
		return this.label != null ? this.label : super.toString();
	}

	@Nullable
	public String getLabel()
	{
		return this.label;
	}

	@Nullable
	public String getEdgeLabel()
	{
		return this.edgeLabel;
	}

	public void setEdgeLabel(@Nullable final String label)
	{
		this.edgeLabel = label;
	}

	@Nullable
	public String getContent()
	{
		return this.content;
	}

	@Nullable
	public Color getBackColor()
	{
		return this.backColor;
	}

	@Nullable
	public Color getForeColor()
	{
		return this.foreColor;
	}

	@Nullable
	public Color getEdgeColor()
	{
		return this.edgeColor;
	}

	public void setEdgeColor(@Nullable final Color color)
	{
		this.edgeColor = color;
	}

	@Nullable
	public Integer getEdgeStyle()
	{
		return this.edgeStyle;
	}

	public void setEdgeStyle(@Nullable final Integer style)
	{
		this.edgeStyle = style;
	}

	// link

	@Nullable
	public String getLink()
	{
		return this.link;
	}

	@Nullable
	public String getTarget()
	{
		return this.target;
	}

	// image

	public int getImageIndex()
	{
		return this.imageIndex;
	}

	@Nullable
	public String getImageFile()
	{
		return this.imageFile;
	}

	public int getEdgeImageIndex()
	{
		return this.edgeImageIndex;
	}

	public void setEdgeImageIndex(final int imageIndex)
	{
		this.edgeImageIndex = imageIndex;
	}

	@Nullable
	public String getEdgeImageFile()
	{
		return this.edgeImageFile;
	}
}

/**
 * XNodeData node
 *
 * @author Bernard Bou
 */
class XNodeData extends NodeData
{
	private static final long serialVersionUID = 8459156134568648138L;

	// D A T A

	// location
	/**
	 * Node location
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	private transient Location location;

	// images
	/**
	 * Node image
	 */
	@Nullable
	@SuppressWarnings({"WeakerAccess", "InstanceVariableOfConcreteClass"})
	protected Image image;

	/**
	 * Tree edge image
	 */
	@Nullable
	@SuppressWarnings({"WeakerAccess", "InstanceVariableOfConcreteClass"})
	protected Image edgeImage;

	// weight
	/**
	 * Node weight
	 */
	private double weight;

	/**
	 * Children weight
	 */
	private double childrenWeight;

	/**
	 * Least weight
	 */
	private double minWeight;

	// mounting

	/**
	 * MountPoint
	 */
	@Nullable
	@SuppressWarnings({"WeakerAccess", "InstanceVariableOfConcreteClass"})
	protected MountPoint mountPoint;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 */
	@SuppressWarnings("WeakerAccess")
	protected XNodeData()
	{
		this.location = new Location();
		this.weight = 0.;
		this.image = null;
		this.edgeImage = null;
		this.mountPoint = null;
	}

	// A C C E S S

	// location
	public Location getLocation()
	{
		if (this.location == null)
		{
			this.location = new Location();
		}
		return this.location;
	}

	// weight
	public double getWeight()
	{
		return this.weight;
	}

	public void setWeight(final double weight)
	{
		this.weight = weight;
	}

	public double getChildrenWeight()
	{
		return this.childrenWeight;
	}

	public void setChildrenWeight(final double weight)
	{
		this.childrenWeight = weight;
	}

	public double getMinWeight()
	{
		return this.minWeight;
	}

	public void setMinWeight(final double weight)
	{
		this.minWeight = weight;
	}

	// mountpoint
	@Nullable
	public MountPoint getMountPoint()
	{
		return this.mountPoint;
	}

	/**
	 * Set mountpoint
	 *
	 * @param mountPoint mount point
	 */
	public void setMountPoint(@Nullable final MountPoint mountPoint)
	{
		this.mountPoint = mountPoint;
	}

	// images
	@Nullable
	public Image getImage()
	{
		return this.image;
	}

	@SuppressWarnings("WeakerAccess")
	public void setImage(@Nullable final Image image)
	{
		this.image = image;
	}

	@Nullable
	public Image getEdgeImage()
	{
		return this.edgeImage;
	}

	public void setEdgeImage(@Nullable final Image image)
	{
		this.edgeImage = image;
	}
}

/**
 * Node implementation
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class Node extends XNodeData implements INode
{
	private static final long serialVersionUID = 5742412970800366908L;

	// D A T A

	/**
	 * Node children
	 */
	@NonNull
	@SuppressWarnings("WeakerAccess")
	protected List<INode> children;

	/**
	 * Parent node
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected INode parent;

	// C O N S T R U C T O R

	/**
	 * Construct node
	 *
	 * @param parent parent node
	 * @param id     node id
	 */
	@SuppressWarnings("WeakerAccess")
	public Node(@Nullable final INode parent, final String id)
	{
		super();
		this.children = new ArrayList<>();
		this.id = id;
		if (parent != null)
		{
			this.parent = parent;
			final List<INode> children = parent.getChildren();
			assert children != null;
			children.add(this);
		}
	}

	@Nullable
	@Override
	public INode getParent()
	{
		return this.parent;
	}

	@Override
	public void setParent(@Nullable final INode parent)
	{
		this.parent = parent;
	}

	@NonNull
	@Override
	public List<INode> getChildren()
	{
		return this.children;
	}
}
