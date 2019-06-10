package treebolic.model;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.Nullable;
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
	@Nullable
	INode getParent();

	/**
	 * Set tree node parent (used by mounting)
	 *
	 * @param parent tree node parent
	 */
	void setParent(final INode parent);

	/**
	 * Get children nodes
	 *
	 * @return children nodes
	 */
	@Nullable
	List<INode> getChildren();

	// mountpoint

	/**
	 * Get mountpoint
	 *
	 * @return mountpoint data or null if this node is not a mountpoint
	 */
	@Nullable
	MountPoint getMountPoint();

	/**
	 * Set mountpoint
	 *
	 * @param mountPoint mountpoint data
	 */
	void setMountPoint(final MountPoint mountPoint);

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
	 * @param weight node weight
	 */
	void setWeight(final double weight);

	/**
	 * Get children weight
	 *
	 * @return children weight
	 */
	double getChildrenWeight();

	/**
	 * Set children weight
	 *
	 * @param weight children weight
	 */
	void setChildrenWeight(final double weight);

	/**
	 * Get least weight
	 *
	 * @return least weight
	 */
	double getMinWeight();

	/**
	 * Set least weight
	 *
	 * @param weight least weight
	 */
	void setMinWeight(final double weight);

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
	@Nullable
	String getId();

	// display data

	/**
	 * Get node label
	 *
	 * @return node label
	 */
	@Nullable
	String getLabel();

	/**
	 * Get tree edge (to parent) label
	 *
	 * @return tree edge (to parent) label
	 */
	@Nullable
	String getEdgeLabel();

	/**
	 * Set tree edge (to parent) label
	 *
	 * @param label edge (to parent) label
	 */
	void setEdgeLabel(final String label);

	/**
	 * Get node content
	 *
	 * @return node content as string
	 */
	@Nullable
	String getContent();

	// colors

	/**
	 * Get background color
	 *
	 * @return background color
	 */
	@Nullable
	Color getBackColor();

	/**
	 * Get foreground color
	 *
	 * @return foreground color
	 */
	@Nullable
	Color getForeColor();

	/**
	 * Get tree edge color
	 *
	 * @return tree edge color
	 */
	@Nullable
	Color getEdgeColor();

	/**
	 * Set edge color
	 *
	 * @param color edge color
	 */
	void setEdgeColor(final Color color);

	// edge

	/**
	 * Get edge style
	 *
	 * @return edge style
	 */
	@Nullable
	Integer getEdgeStyle();

	/**
	 * Set edge style
	 *
	 * @param style edge style
	 */
	void setEdgeStyle(final Integer style);

	// hyperlink

	/**
	 * Get URL link
	 *
	 * @return URL link
	 */
	@Nullable
	String getLink();

	/**
	 * Get link target frame
	 *
	 * @return link target frame
	 */
	@Nullable
	String getTarget();

	// image locations

	/**
	 * Get node image filename
	 *
	 * @return node image filename
	 */
	@Nullable
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
	@Nullable
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
	 * @param imageIndex image index
	 */
	void setEdgeImageIndex(final int imageIndex);

	// get images

	/**
	 * Get node image
	 *
	 * @return node image
	 */
	@Nullable
	Image getImage();

	/**
	 * Get tree edge image
	 *
	 * @return tree edge image
	 */
	@Nullable
	Image getEdgeImage();

	// set images

	/**
	 * Set image file
	 *
	 * @param image node image
	 */
	void setImage(final Image image);

	/**
	 * Set tree edge image
	 *
	 * @param image tree edge image
	 */
	void setEdgeImage(final Image image);
}
