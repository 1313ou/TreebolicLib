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
	 * @param parent parent
	 * @param id     id
	 */
	@SuppressWarnings("WeakerAccess")
	public MutableNode(final INode parent, final String id)
	{
		super(parent, id);
	}

	/**
	 * Constructor
	 *
	 * @param parent    parent
	 * @param id        id
	 * @param label     label
	 * @param image     image
	 * @param backColor backcolor
	 * @param foreColor forecolor
	 */
	public MutableNode(final INode parent, final String id, final String label, final Image image, final Color backColor, final Color foreColor)
	{
		super(parent, id);
		setLabel(label);
		setBackColor(backColor);
		setForeColor(foreColor);
		setImage(image);
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
	@SuppressWarnings("WeakerAccess")
	public MutableNode(final INode parent, final String id, final String label, final int imageIndex, final Color backColor, final Color foreColor)
	{
		super(parent, id);
		setLabel(label);
		setBackColor(backColor);
		setForeColor(foreColor);
		setImageIndex(imageIndex);
	}

	/**
	 * Set label
	 *
	 * @param label label
	 */
	@SuppressWarnings("WeakerAccess")
	public void setLabel(final String label)
	{
		this.label = label;
	}

	/**
	 * Set content
	 *
	 * @param content content
	 */
	public void setContent(final String content)
	{
		this.content = content;
	}

	/**
	 * Set backcolor
	 *
	 * @param color backcolor
	 */
	@SuppressWarnings("WeakerAccess")
	public void setBackColor(final Color color)
	{
		this.backColor = color;
	}

	/**
	 * Set forecolor
	 *
	 * @param color forecolor
	 */
	@SuppressWarnings("WeakerAccess")
	public void setForeColor(final Color color)
	{
		this.foreColor = color;
	}

	/**
	 * Set node image file
	 *
	 * @param imageFile image file
	 */
	public void setImageFile(final String imageFile)
	{
		this.imageFile = imageFile;
		this.image = null;
	}

	/**
	 * Set node image index
	 *
	 * @param imageIndex image index
	 */
	public void setImageIndex(final int imageIndex)
	{
		this.imageIndex = imageIndex;
		this.image = null;
	}

	/**
	 * Set edge image file
	 *
	 * @param imageFile image file
	 */
	public void setEdgeImageFile(final String imageFile)
	{
		this.edgeImageFile = imageFile;
	}

	/**
	 * Set node link URL
	 *
	 * @param link link URL
	 */
	public void setLink(@SuppressWarnings("SameParameterValue") final String link)
	{
		this.link = link;
	}

	/**
	 * Set node link target frame
	 *
	 * @param target link target frame
	 */
	public void setTarget(final String target)
	{
		this.target = target;
	}
}
