package treebolic.model;

import treebolic.glue.Color;
import treebolic.glue.Image;

/**
 * Mutable node
 *
 * @author Bernard Bou
 */
public class MutableNode extends Node
{
	private static final long serialVersionUID = 114104713585352390L;

	/**
	 * Constructor
	 *
	 * @param thisParent
	 *        parent
	 * @param thisId
	 *        id
	 */
	public MutableNode(final INode thisParent, final String thisId)
	{
		super(thisParent, thisId);
	}

	/**
	 * Constructor
	 *
	 * @param thisParent
	 *        parent
	 * @param thisId
	 *        id
	 * @param thisLabel
	 *        label
	 * @param thisImage
	 *        image
	 * @param thisBackColor
	 *        backcolor
	 * @param thisForeColor
	 *        forecolor
	 */
	public MutableNode(final INode thisParent, final String thisId, final String thisLabel, final Image thisImage, final Color thisBackColor, final Color thisForeColor)
	{
		super(thisParent, thisId);
		setLabel(thisLabel);
		setBackColor(thisBackColor);
		setForeColor(thisForeColor);
		setImage(thisImage);
	}

	/**
	 * Constructor
	 *
	 * @param thisParent
	 *        parent
	 * @param thisId
	 *        id
	 * @param thisLabel
	 *        label
	 * @param thisImageIndex
	 *        image index
	 * @param thisBackColor
	 *        backcolor
	 * @param thisForeColor
	 *        forecolor
	 */
	public MutableNode(final INode thisParent, final String thisId, final String thisLabel, final int thisImageIndex, final Color thisBackColor, final Color thisForeColor)
	{
		super(thisParent, thisId);
		setLabel(thisLabel);
		setBackColor(thisBackColor);
		setForeColor(thisForeColor);
		setImageIndex(thisImageIndex);
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
	 * Set content
	 *
	 * @param thisContent
	 *        content
	 */
	public void setContent(final String thisContent)
	{
		this.theContent = thisContent;
	}

	/**
	 * Set backcolor
	 *
	 * @param thisColor
	 *        backcolor
	 */
	public void setBackColor(final Color thisColor)
	{
		this.theBackColor = thisColor;
	}

	/**
	 * Set forecolor
	 *
	 * @param thisColor
	 *        forecolor
	 */
	public void setForeColor(final Color thisColor)
	{
		this.theForeColor = thisColor;
	}

	/**
	 * Set node image file
	 *
	 * @param thisImageFile
	 *        image file
	 */
	public void setImageFile(final String thisImageFile)
	{
		this.theImageFile = thisImageFile;
		this.theImage = null;
	}

	/**
	 * Set node image index
	 *
	 * @param thisImageIndex
	 *        image index
	 */
	public void setImageIndex(final int thisImageIndex)
	{
		this.theImageIndex = thisImageIndex;
		this.theImage = null;
	}

	/**
	 * Set edge image file
	 *
	 * @param thisImageFile
	 *        image file
	 */
	public void setEdgeImageFile(final String thisImageFile)
	{
		this.theEdgeImageFile = thisImageFile;
	}

	/**
	 * Set node link URL
	 *
	 * @param thisLink
	 *        link URL
	 */
	public void setLink(final String thisLink)
	{
		this.theLink = thisLink;
	}

	/**
	 * Set node link target frame
	 *
	 * @param thisTarget
	 *        link target frame
	 */
	public void setTarget(final String thisTarget)
	{
		this.theTarget = thisTarget;
	}
}
