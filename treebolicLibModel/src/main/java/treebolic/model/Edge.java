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
	private static final long serialVersionUID = 2466948898564986160L;

	/**
	 * Label
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected String label;

	/**
	 * Edge color
	 */
	@Nullable
	@SuppressWarnings({"WeakerAccess", "InstanceVariableOfConcreteClass"})
	protected Color color;

	/**
	 * Edge style
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected Integer style;

	/**
	 * Edge image filename
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected String imageFile;

	/**
	 * Edge image filename
	 */
	@SuppressWarnings("WeakerAccess")
	protected int imageIndex;

	/**
	 * Constructor
	 */
	@SuppressWarnings("WeakerAccess")
	public EdgeData()
	{
		this.label = null;
		this.color = null;
		this.style = null;
		this.imageFile = null;
		this.imageIndex = -1;
	}

	@Nullable
	public String getLabel()
	{
		return this.label;
	}

	@Nullable
	public Color getColor()
	{
		return this.color;
	}

	@Nullable
	public Integer getStyle()
	{
		return this.style;
	}

	@Nullable
	public String getImageFile()
	{
		return this.imageFile;
	}

	public int getImageIndex()
	{
		return this.imageIndex;
	}

}

/**
 * Extended edge data (internal use)
 *
 * @author Bernard Bou
 */
class XEdgeData extends EdgeData
{
	private static final long serialVersionUID = -3852875983533695072L;

	/**
	 * Edge image
	 */
	@Nullable
	@SuppressWarnings({"WeakerAccess", "InstanceVariableOfConcreteClass"})
	protected Image image;

	@Nullable
	public Image getImage()
	{
		return this.image;
	}

	/**
	 * Set image
	 *
	 * @param image image
	 */
	public void setImage(@Nullable final Image image)
	{
		this.image = image;
	}
}

/**
 * Edge
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class Edge extends XEdgeData implements IEdge
{
	private static final long serialVersionUID = 4067969984015552298L;

	/**
	 * From-node
	 */
	@SuppressWarnings("WeakerAccess")
	protected INode fromNode;

	/**
	 * To-node
	 */
	@SuppressWarnings("WeakerAccess")
	protected INode toNode;

	/**
	 * Construct edge
	 *
	 * @param fromINode from node
	 * @param toINode   to node
	 */
	@SuppressWarnings("WeakerAccess")
	public Edge(final INode fromINode, final INode toINode)
	{
		super();
		this.fromNode = fromINode;
		this.toNode = toINode;
	}

	@Override
	public INode getFrom()
	{
		return this.fromNode;
	}

	@Override
	public INode getTo()
	{
		return this.toNode;
	}

	@NonNull
	@Override
	public String toString()
	{
		return (this.fromNode == null ? "null" : this.fromNode.getLabel()) + "->" + (this.toNode == null ? "null" : this.toNode.getLabel());
	}
}
