/**
 * Title : Treebolic
 * Description : Treebolic
 * Version : 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 *
 * Update : Mon Mar 10 00:00:00 CEST 2008
 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getId()
	 */
	public String getId()
	{
		return this.theId;
	}

	// A C C E S S

	// display
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return this.theLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getLabel()
	 */
	public String getLabel()
	{
		return this.theLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getEdgeLabel()
	 */
	public String getEdgeLabel()
	{
		return this.theEdgeLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#setEdgeLabel()
	 */
	public void setEdgeLabel(final String thisLabel)
	{
		this.theEdgeLabel = thisLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getContent()
	 */
	public String getContent()
	{
		return this.theContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getBackColor()
	 */
	public Color getBackColor()
	{
		return this.theBackColor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getForeColor()
	 */
	public Color getForeColor()
	{
		return this.theForeColor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getEdgeColor()
	 */
	public Color getEdgeColor()
	{
		return this.theEdgeColor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#setEdgeColor()
	 */
	public void setEdgeColor(final Color thisColor)
	{
		this.theEdgeColor = thisColor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getEdgeStyle()
	 */
	public Integer getEdgeStyle()
	{
		return this.theEdgeStyle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getEdgeStyle()
	 */
	public void setEdgeStyle(final Integer thisStyle)
	{
		this.theEdgeStyle = thisStyle;
	}

	// link

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getLink()
	 */
	public String getLink()
	{
		return this.theLink;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getTarget()
	 */
	public String getTarget()
	{
		return this.theTarget;
	}

	// image

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getImageIndex()
	 */
	public int getImageIndex()
	{
		return this.theImageIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getImageFile()
	 */
	public String getImageFile()
	{
		return this.theImageFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getEdgeImageIndex()
	 */
	public int getEdgeImageIndex()
	{
		return this.theEdgeImageIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#setEdgeImageIndex()
	 */
	public void setEdgeImageIndex(final int thisImageIndex)
	{
		this.theEdgeImageIndex = thisImageIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getEdgeImageFile()
	 */
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

	// mouting
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
	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getLocation()
	 */
	public Location getLocation()
	{
		if (this.theLocation == null)
		{
			this.theLocation = new Location();
		}
		return this.theLocation;
	}

	// weight
	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getWeight()
	 */
	public double getWeight()
	{
		return this.theWeight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#setWeight(double)
	 */
	public void setWeight(final double thisWeight)
	{
		this.theWeight = thisWeight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getChildrenWeight()
	 */
	public double getChildrenWeight()
	{
		return this.theChildrenWeight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#setChildrenWeight(double)
	 */
	public void setChildrenWeight(final double thisWeight)
	{
		this.theChildrenWeight = thisWeight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getMinWeight()
	 */
	public double getMinWeight()
	{
		return this.theMinWeight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#setMinWeight(double)
	 */
	public void setMinWeight(final double thisWeight)
	{
		this.theMinWeight = thisWeight;
	}

	// mountpoint
	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getMountPoint()
	 */
	public MountPoint getMountPoint()
	{
		return this.theMountPoint;
	}

	/**
	 * Set mountpoint
	 *
	 * @param thisMountPoint
	 */
	public void setMountPoint(final MountPoint thisMountPoint)
	{
		this.theMountPoint = thisMountPoint;
	}

	// images
	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getImage()
	 */
	public Image getImage()
	{
		return this.theImage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#setImage()
	 */
	public void setImage(final Image thisImage)
	{
		this.theImage = thisImage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getEdgeImage()
	 */
	public Image getEdgeImage()
	{
		return this.theEdgeImage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#setEdgeImage(Image)
	 */
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
	 * @param thisParent
	 *            parent node
	 * @param thisId
	 *            node id
	 */
	public Node(final INode thisParent, final String thisId)
	{
		super();
		this.theParent = thisParent;
		if (thisParent != null)
		{
			thisParent.getChildren().add(this);
		}
		this.theChildren = new ArrayList<INode>();

		this.theId = thisId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#getParent()
	 */
	@Override
	public INode getParent()
	{
		return this.theParent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#setParent(treebolic.model.INode)
	 */
	@Override
	public void setParent(final INode thisParent)
	{
		this.theParent = thisParent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.model.INode#removeTChild(treebolic.model.INode)
	 */
	@Override
	public List<INode> getChildren()
	{
		return this.theChildren;
	}
}
