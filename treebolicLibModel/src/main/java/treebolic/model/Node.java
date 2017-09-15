package treebolic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	protected String theId;

	// node data

	/**
	 * Node label
	 */
	protected String theLabel;

	/**
	 * Node content
	 */
	protected String theContent;

	/**
	 * Node URL link
	 */
	protected String theLink;

	/**
	 * Node link target frame
	 */
	protected String theTarget;

	/**
	 * Background color
	 */
	protected Color theBackColor;

	/**
	 * Foreground color
	 */
	protected Color theForeColor;

	/**
	 * Node image filename
	 */
	protected String theImageFile;

	/**
	 * Node image index
	 */
	protected int theImageIndex;

	// tree edge data

	/**
	 * Tree edge label
	 */
	protected String theEdgeLabel;

	/**
	 * Tree edge color
	 */
	protected Color theEdgeColor;

	/**
	 * Tree edge style
	 */
	protected Integer theEdgeStyle;

	/**
	 * Edge image filename
	 */
	protected String theEdgeImageFile;

	/**
	 * Edge image filename
	 */
	protected int theEdgeImageIndex;

	// C O N S T R U C T O R

	/**
	 * Construct node
	 */
	public NodeData()
	{
		this.theId = null;
		this.theLabel = null;
		this.theContent = null;
		this.theBackColor = null;
		this.theForeColor = null;
		this.theImageFile = null;
		this.theImageIndex = -1;
		this.theLink = null;
		this.theTarget = null;

		this.theEdgeLabel = null;
		this.theEdgeColor = null;
		this.theEdgeStyle = null;
		this.theEdgeImageFile = null;
		this.theEdgeImageIndex = -1;
	}

	// I D

	public String getId()
	{
		return this.theId;
	}

	// A C C E S S

	// display
	@Override
	public String toString()
	{
		return this.theLabel;
	}

	public String getLabel()
	{
		return this.theLabel;
	}

	public String getEdgeLabel()
	{
		return this.theEdgeLabel;
	}

	public void setEdgeLabel(final String thisLabel)
	{
		this.theEdgeLabel = thisLabel;
	}

	public String getContent()
	{
		return this.theContent;
	}

	public Color getBackColor()
	{
		return this.theBackColor;
	}

	public Color getForeColor()
	{
		return this.theForeColor;
	}

	public Color getEdgeColor()
	{
		return this.theEdgeColor;
	}

	public void setEdgeColor(final Color thisColor)
	{
		this.theEdgeColor = thisColor;
	}

	public Integer getEdgeStyle()
	{
		return this.theEdgeStyle;
	}

	public void setEdgeStyle(final Integer thisStyle)
	{
		this.theEdgeStyle = thisStyle;
	}

	// link

	public String getLink()
	{
		return this.theLink;
	}

	public String getTarget()
	{
		return this.theTarget;
	}

	// image

	public int getImageIndex()
	{
		return this.theImageIndex;
	}

	public String getImageFile()
	{
		return this.theImageFile;
	}

	public int getEdgeImageIndex()
	{
		return this.theEdgeImageIndex;
	}

	public void setEdgeImageIndex(final int thisImageIndex)
	{
		this.theEdgeImageIndex = thisImageIndex;
	}

	public String getEdgeImageFile()
	{
		return this.theEdgeImageFile;
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
	private transient Location theLocation;

	// images
	/**
	 * Node image
	 */
	protected Image theImage;

	/**
	 * Tree edge image
	 */
	protected Image theEdgeImage;

	// weight
	/**
	 * Node weight
	 */
	private double theWeight;

	/**
	 * Children weight
	 */
	private double theChildrenWeight;

	/**
	 * Least weight
	 */
	private double theMinWeight;

	// mounting

	/**
	 * MountPoint
	 */
	protected MountPoint theMountPoint;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 */
	protected XNodeData()
	{
		this.theLocation = new Location();
		this.theWeight = 0.;
		this.theImage = null;
		this.theEdgeImage = null;
		this.theMountPoint = null;
	}

	// A C C E S S

	// location
	public Location getLocation()
	{
		if (this.theLocation == null)
		{
			this.theLocation = new Location();
		}
		return this.theLocation;
	}

	// weight
	public double getWeight()
	{
		return this.theWeight;
	}

	public void setWeight(final double thisWeight)
	{
		this.theWeight = thisWeight;
	}

	public double getChildrenWeight()
	{
		return this.theChildrenWeight;
	}

	public void setChildrenWeight(final double thisWeight)
	{
		this.theChildrenWeight = thisWeight;
	}

	public double getMinWeight()
	{
		return this.theMinWeight;
	}

	public void setMinWeight(final double thisWeight)
	{
		this.theMinWeight = thisWeight;
	}

	// mountpoint
	public MountPoint getMountPoint()
	{
		return this.theMountPoint;
	}

	/**
	 * Set mountpoint
	 *
	 * @param thisMountPoint mount point
	 */
	public void setMountPoint(final MountPoint thisMountPoint)
	{
		this.theMountPoint = thisMountPoint;
	}

	// images
	public Image getImage()
	{
		return this.theImage;
	}

	public void setImage(final Image thisImage)
	{
		this.theImage = thisImage;
	}

	public Image getEdgeImage()
	{
		return this.theEdgeImage;
	}

	public void setEdgeImage(final Image thisImage)
	{
		this.theEdgeImage = thisImage;
	}
}

/**
 * Node implementation
 *
 * @author Bernard Bou
 */
public class Node extends XNodeData implements INode
{
	private static final long serialVersionUID = 5742412970800366908L;

	// D A T A

	/**
	 * Node children
	 */
	protected List<INode> theChildren;

	/**
	 * Parent node
	 */
	protected INode theParent;

	// C O N S T R U C T O R

	/**
	 * Construct node
	 *
	 * @param thisParent parent node
	 * @param thisId     node id
	 */
	public Node(final INode thisParent, final String thisId)
	{
		super();
		this.theParent = thisParent;
		if (thisParent != null)
		{
			thisParent.getChildren().add(this);
		}
		this.theChildren = new ArrayList<>();

		this.theId = thisId;
	}

	@Override
	public INode getParent()
	{
		return this.theParent;
	}

	@Override
	public void setParent(final INode thisParent)
	{
		this.theParent = thisParent;
	}

	@Override
	public List<INode> getChildren()
	{
		return this.theChildren;
	}
}
