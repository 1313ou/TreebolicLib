package treebolic.model;

import treebolic.glue.Color;

/**
 * Mutable edge
 *
 * @author Bernard Bou
 */
public class MutableEdge extends Edge
{
	private static final long serialVersionUID = -8288301829410629431L;

	/**
	 * Construct node
	 *
	 * @param thisFromNode
	 *        from node
	 * @param thisToNode
	 *        to node
	 */
	public MutableEdge(final INode thisFromNode, final INode thisToNode)
	{
		super(thisFromNode, thisToNode);
	}

	/**
	 * Set label
	 *
	 * @param thisLabel
	 *        label
	 */
	public void setLabel(final String thisLabel)
	{
		this.theLabel = thisLabel;
	}

	/**
	 * Set color
	 *
	 * @param thisColor
	 *        color
	 */
	public void setColor(final Color thisColor)
	{
		this.theColor = thisColor;
	}

	/**
	 * Set style
	 *
	 * @param thisStyle
	 *        edge style
	 */
	public void setStyle(final Integer thisStyle)
	{
		this.theStyle = thisStyle;
	}

	/**
	 * Set image filename
	 *
	 * @param thisImageFile
	 *        image filename
	 */
	public void setImageFile(final String thisImageFile)
	{
		this.theImageFile = thisImageFile;
		this.theImage = null;
	}

	/**
	 * Set image index
	 *
	 * @param thisImageIndex
	 *        image index
	 */
	public void setImageIndex(final int thisImageIndex)
	{
		this.theImageIndex = thisImageIndex;
		this.theImage = null;
	}
}
