package treebolic.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

import treebolic.glue.Color;
import treebolic.glue.Image;

/**
 * Edge data
 *
 * @author Bernard Bou
 */
class EdgeData implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 2466948898564986160L;

	/**
	 * Label
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected String theLabel;

	/**
	 * Edge color
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected Color theColor;

	/**
	 * Edge style
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected Integer theStyle;

	/**
	 * Edge image filename
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected String theImageFile;

	/**
	 * Edge image filename
	 */
	@SuppressWarnings("WeakerAccess")
	protected int theImageIndex;

	/**
	 * Constructor
	 */
	@SuppressWarnings("WeakerAccess")
	public EdgeData()
	{
		this.theLabel = null;
		this.theColor = null;
		this.theStyle = null;
		this.theImageFile = null;
		this.theImageIndex = -1;
	}

	@Nullable
	public String getLabel()
	{
		return this.theLabel;
	}

	@Nullable
	public Color getColor()
	{
		return this.theColor;
	}

	@Nullable
	public Integer getStyle()
	{
		return this.theStyle;
	}

	@Nullable
	public String getImageFile()
	{
		return this.theImageFile;
	}

	public int getImageIndex()
	{
		return this.theImageIndex;
	}

}

/**
 * Extended edge data (internal use)
 *
 * @author Bernard Bou
 */
class XEdgeData extends EdgeData
{
	private static final long serialVersionUID = -3852875983533695071L;

	/**
	 * Edge image
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected Image theImage;

	@Nullable
	public Image getImage()
	{
		return this.theImage;
	}

	/**
	 * Set image
	 *
	 * @param thisImage image
	 */
	public void setImage(final Image thisImage)
	{
		this.theImage = thisImage;
	}
}

/**
 * Edge
 *
 * @author Bernard Bou
 */
public class Edge extends XEdgeData implements IEdge
{
	private static final long serialVersionUID = 4067969984015552298L;

	/**
	 * From-node
	 */
	@SuppressWarnings("WeakerAccess")
	protected INode theFromNode;

	/**
	 * To-node
	 */
	@SuppressWarnings("WeakerAccess")
	protected INode theToNode;

	/**
	 * Construct edge
	 *
	 * @param thisFromINode from node
	 * @param thisToINode   to node
	 */
	@SuppressWarnings("WeakerAccess")
	public Edge(final INode thisFromINode, final INode thisToINode)
	{
		super();
		this.theFromNode = thisFromINode;
		this.theToNode = thisToINode;
	}

	@Override
	public INode getFrom()
	{
		return this.theFromNode;
	}

	@Override
	public INode getTo()
	{
		return this.theToNode;
	}

	@NonNull
	@Override
	public String toString()
	{
		return (this.theFromNode == null ? "null" : this.theFromNode.getLabel()) + "->" + (this.theToNode == null ? "null" : this.theToNode.getLabel());
	}
}
