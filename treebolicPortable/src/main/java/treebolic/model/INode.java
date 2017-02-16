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
import java.util.List;

import treebolic.glue.Color;
import treebolic.glue.Image;

/**
 * Node interface
 *
 * @author Bernard Bou
 */
public interface INode extends Serializable
{
	// tree
	/**
	 * Get tree node parent
	 *
	 * @return tree node parent
	 */
	public INode getParent();

	/**
	 * Set tree node parent (used by mounting)
	 *
	 * @param thisParent
	 *            tree node parent
	 */
	public void setParent(final INode thisParent);

	/**
	 * Get children nodes
	 *
	 * @return children nodes
	 */
	public List<INode> getChildren();

	// mountpoint
	/**
	 * Get mountpoint
	 *
	 * @return mountpoint data or null if this node is not a mountpoint
	 */
	public MountPoint getMountPoint();

	/**
	 * Set mountpoint
	 *
	 * @param thisMountPoint
	 *            mountpoint data
	 */
	public void setMountPoint(final MountPoint thisMountPoint);

	// weight
	/**
	 * Get node weight computed as a function of the number of descendant nodes. Can be negative if to be considered preset.
	 *
	 * @return node weight
	 */
	public double getWeight();

	/**
	 * Set node weight (negative values will bypass computations and will be considered preset)
	 *
	 * @param thisWeight
	 *            node weight
	 */
	public void setWeight(final double thisWeight);

	/**
	 * Get children weight
	 *
	 * @return children weight
	 */
	public double getChildrenWeight();

	/**
	 * Set children weight
	 *
	 * @param thisWeight
	 *            children weight
	 */
	public void setChildrenWeight(final double thisWeight);

	/**
	 * Get least weight
	 *
	 * @return least weight
	 */
	public double getMinWeight();

	/**
	 * Set least weight
	 *
	 * @param thisWeight
	 *            least weight
	 */
	public void setMinWeight(final double thisWeight);

	// location
	/**
	 * Get node location
	 *
	 * @return node location
	 */
	public Location getLocation();

	// search/identification data
	/**
	 * Get node id
	 *
	 * @return node id
	 */
	public String getId();

	// display data
	/**
	 * Get node label
	 *
	 * @return node label
	 */
	public String getLabel();

	/**
	 * Get tree edge (to parent) label
	 *
	 * @return tree edge (to parent) label
	 */
	public String getEdgeLabel();

	/**
	 * Set tree edge (to parent) label
	 *
	 * @param thisLabel
	 *            edge (to parent) label
	 */
	public void setEdgeLabel(final String thisLabel);

	/**
	 * Get node content
	 *
	 * @return node content as string
	 */
	public String getContent();

	// colors
	/**
	 * Get background color
	 *
	 * @return background color
	 */
	public Color getBackColor();

	/**
	 * Get foreground color
	 *
	 * @return foreground color
	 */
	public Color getForeColor();

	/**
	 * Get tree edge color
	 *
	 * @return tree edge color
	 */
	public Color getEdgeColor();

	/**
	 * Set edge color
	 *
	 * @param thisColor
	 *            edge color
	 */
	public void setEdgeColor(final Color thisColor);

	// edge

	/**
	 * Get edge style
	 *
	 * @return edge style
	 */
	public Integer getEdgeStyle();

	/**
	 * Set edge style
	 *
	 * @param thisStyle
	 *            edge style
	 */
	public void setEdgeStyle(final Integer thisStyle);

	// hyperlink
	/**
	 * Get URL link
	 *
	 * @return URL link
	 */
	public String getLink();

	/**
	 * Get link target frame
	 *
	 * @return link target frame
	 */
	public String getTarget();

	// image locations

	/**
	 * Get node image filename
	 *
	 * @return node image filename
	 */
	public String getImageFile();

	/**
	 * Get edge image index
	 *
	 * @return edge image index
	 */
	public int getImageIndex();

	/**
	 * Get tree edge image file
	 *
	 * @return tree edge image file
	 */
	public String getEdgeImageFile();

	/**
	 * Get edge image index
	 *
	 * @return edge image index
	 */
	public int getEdgeImageIndex();

	/**
	 * Set edge image index
	 *
	 * @param thisImageIndex
	 *        image index
	 */
	public void setEdgeImageIndex(final int thisImageIndex);

	// get images

	/**
	 * Get node image
	 *
	 * @return node image
	 */
	public Image getImage();

	/**
	 * Get tree edge image
	 *
	 * @return tree edge image
	 */
	public Image getEdgeImage();

	// set images

	/**
	 * Set image file
	 *
	 * @param thisImage
	 *            node image
	 */
	public void setImage(final Image thisImage);

	/**
	 * Set tree edge image
	 *
	 * @param thisImage
	 *            tree edge image
	 */
	public void setEdgeImage(final Image thisImage);
}
