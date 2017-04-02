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
	INode getParent();

	/**
	 * Set tree node parent (used by mounting)
	 *
	 * @param thisParent
	 *            tree node parent
	 */
	void setParent(final INode thisParent);

	/**
	 * Get children nodes
	 *
	 * @return children nodes
	 */
	List<INode> getChildren();

	// mountpoint
	/**
	 * Get mountpoint
	 *
	 * @return mountpoint data or null if this node is not a mountpoint
	 */
	MountPoint getMountPoint();

	/**
	 * Set mountpoint
	 *
	 * @param thisMountPoint
	 *            mountpoint data
	 */
	void setMountPoint(final MountPoint thisMountPoint);

	// weight
	/**
	 * Get node weight computed as a function of the number of descendant nodes. Can be negative if to be considered preset.
	 *
	 * @return node weight
	 */
	double getWeight();

	/**
	 * Set node weight (negative values will bypass computations and will be considered preset)
	 *
	 * @param thisWeight
	 *            node weight
	 */
	void setWeight(final double thisWeight);

	/**
	 * Get children weight
	 *
	 * @return children weight
	 */
	double getChildrenWeight();

	/**
	 * Set children weight
	 *
	 * @param thisWeight
	 *            children weight
	 */
	void setChildrenWeight(final double thisWeight);

	/**
	 * Get least weight
	 *
	 * @return least weight
	 */
	double getMinWeight();

	/**
	 * Set least weight
	 *
	 * @param thisWeight
	 *            least weight
	 */
	void setMinWeight(final double thisWeight);

	// location
	/**
	 * Get node location
	 *
	 * @return node location
	 */
	Location getLocation();

	// search/identification data
	/**
	 * Get node id
	 *
	 * @return node id
	 */
	String getId();

	// display data
	/**
	 * Get node label
	 *
	 * @return node label
	 */
	String getLabel();

	/**
	 * Get tree edge (to parent) label
	 *
	 * @return tree edge (to parent) label
	 */
	String getEdgeLabel();

	/**
	 * Set tree edge (to parent) label
	 *
	 * @param thisLabel
	 *            edge (to parent) label
	 */
	void setEdgeLabel(final String thisLabel);

	/**
	 * Get node content
	 *
	 * @return node content as string
	 */
	String getContent();

	// colors
	/**
	 * Get background color
	 *
	 * @return background color
	 */
	Color getBackColor();

	/**
	 * Get foreground color
	 *
	 * @return foreground color
	 */
	Color getForeColor();

	/**
	 * Get tree edge color
	 *
	 * @return tree edge color
	 */
	Color getEdgeColor();

	/**
	 * Set edge color
	 *
	 * @param thisColor
	 *            edge color
	 */
	void setEdgeColor(final Color thisColor);

	// edge

	/**
	 * Get edge style
	 *
	 * @return edge style
	 */
	Integer getEdgeStyle();

	/**
	 * Set edge style
	 *
	 * @param thisStyle
	 *            edge style
	 */
	void setEdgeStyle(final Integer thisStyle);

	// hyperlink
	/**
	 * Get URL link
	 *
	 * @return URL link
	 */
	String getLink();

	/**
	 * Get link target frame
	 *
	 * @return link target frame
	 */
	String getTarget();

	// image locations

	/**
	 * Get node image filename
	 *
	 * @return node image filename
	 */
	String getImageFile();

	/**
	 * Get edge image index
	 *
	 * @return edge image index
	 */
	int getImageIndex();

	/**
	 * Get tree edge image file
	 *
	 * @return tree edge image file
	 */
	String getEdgeImageFile();

	/**
	 * Get edge image index
	 *
	 * @return edge image index
	 */
	int getEdgeImageIndex();

	/**
	 * Set edge image index
	 *
	 * @param thisImageIndex
	 *        image index
	 */
	void setEdgeImageIndex(final int thisImageIndex);

	// get images

	/**
	 * Get node image
	 *
	 * @return node image
	 */
	Image getImage();

	/**
	 * Get tree edge image
	 *
	 * @return tree edge image
	 */
	Image getEdgeImage();

	// set images

	/**
	 * Set image file
	 *
	 * @param thisImage
	 *            node image
	 */
	void setImage(final Image thisImage);

	/**
	 * Set tree edge image
	 *
	 * @param thisImage
	 *            tree edge image
	 */
	void setEdgeImage(final Image thisImage);
}
