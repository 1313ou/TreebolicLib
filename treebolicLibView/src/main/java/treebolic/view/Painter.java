/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.view;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import treebolic.core.location.Complex;
import treebolic.core.math.Arc;
import treebolic.core.math.ArcMath;
import treebolic.core.math.MapperToEuclidean;
import treebolic.glue.Arc2D;
import treebolic.glue.Color;
import treebolic.glue.Image;
import treebolic.glue.Point2D;
import treebolic.glue.Rectangle2D;
import treebolic.model.IEdge;
import treebolic.model.INode;
import treebolic.model.Location;
import treebolic.model.MountPoint;

/**
 * Painter class
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class Painter extends AbstractPainter
{
	// B E H A V I O U R

	/**
	 * Debug flag, bit1=draw circles, bit2=no node label, bit3=no label 0x02 = no node label 0x04 = no label 0x10 = no node fill
	 */
	@SuppressWarnings("WeakerAccess")
	static public final int DEBUG_OUTERCIRCLE = 0x001;

	@SuppressWarnings("WeakerAccess")
	static public final int DEBUG_NODECIRCLE = 0x002;

	@SuppressWarnings("WeakerAccess")
	static public final int DEBUG_NONODE = 0x010;

	@SuppressWarnings("WeakerAccess")
	static public final int DEBUG_NOLABEL = 0x100;

	@SuppressWarnings("WeakerAccess")
	static public final int DEBUG_NOLABELFILL = 0x200;

	@SuppressWarnings("WeakerAccess")
	static public final int DEBUG_NOIMAGE = 0x400;

	@SuppressWarnings({"CanBeFinal", "WeakerAccess"})
	static public int DEBUG = 0; // DEBUG_NOIMAGE // DEBUG_NOLABEL | DEBUG_NOLABELFILL;

	/**
	 * Do not draw curves while moving
	 */
	@SuppressWarnings({"CanBeFinal", "WeakerAccess"})
	static public boolean STRAIGHT_EDGE_WHILE_MOVING = false;

	/**
	 * Text padding
	 */
	@SuppressWarnings("WeakerAccess")
	static public final int TEXT_PADDING = 25;

	/**
	 * Minimum span for terminators (squared)
	 */
	@SuppressWarnings("WeakerAccess")
	static public final int TERMINATOR_MIN_SPAN2 = 50 * 50;

	/**
	 * Edge terminator length
	 */
	@SuppressWarnings("WeakerAccess")
	static public final double TERMINATOR_HEIGHT = 8;

	/**
	 * Edge terminator width
	 */
	@SuppressWarnings("WeakerAccess")
	static public final double TERMINATOR_WIDTH = 5;

	/**
	 * Fraction of label's first line that is allowed to be overlaid by image
	 */
	@SuppressWarnings("WeakerAccess")
	static public final float NODE_LABEL_OVERLAY = 0.2F;

	/**
	 * Min image dimension
	 */
	@SuppressWarnings("WeakerAccess")
	static public final int MIN_IMAGE_DIMENSION = 5;

	// insets
	/**
	 * x padding for node box
	 */
	@SuppressWarnings("WeakerAccess")
	static public final int NODE_HORIZONTAL_PADDING = 4;

	/**
	 * y padding for node box
	 */
	@SuppressWarnings("WeakerAccess")
	static public final int NODE_TOP_PADDING = 1;

	@SuppressWarnings("WeakerAccess")
	static public final int NODE_BOTTOM_PADDING = 1;

	/**
	 * Crop edges where they meet vertex shape
	 */
	@SuppressWarnings("WeakerAccess")
	static public final boolean CROP_EDGES = true;

	// B A C K G R O U N D

	@Override
	public void paintBackground()
	{
		// background color
		if (this.backColor != null)
		{
			this.graphics.drawBackgroundColor(this.backColor, this.left, this.top, this.width, this.height);
		}

		// background image
		if (this.backgroundImage != null)
		{
			this.graphics.drawImage(this.backgroundImage, this.left, this.top, this.width, this.height);
		}
	}

	// D R A W

	@Override
	public void paint(final INode root, @Nullable final List<IEdge> edgeList)
	{
		if (this.zoomFactor != 1F)
		{
			this.graphics.scale(this.zoomFactor, this.zoomPivotX, this.zoomPivotY);
		}

		// boundary circle
		if ((Painter.DEBUG & Painter.DEBUG_OUTERCIRCLE) != 0)
		{
			drawCircle(0., 0., 1., Color.LIGHT_GRAY);
		}

		// compute node data
		computeTree(root);

		// edges
		if (edgeList != null)
		{
			for (final IEdge edge : edgeList)
			{
				drawNonTreeEdge(edge);
			}
		}

		// tree
		drawTree(root);
	}

	/**
	 * Compute tree recursively
	 *
	 * @param node starting node
	 */
	private void computeTree(@Nullable final INode node)
	{
		if (node == null)
		{
			return;
		}

		// hyper circle
		final Location location = node.getLocation();
		if (location.hyper.isDirty)
		{
			MapperToEuclidean.mapToEuclidean(location);
		}

		// node data and attach to node
		node.getLocation().viewData = computeNodeData(node);

		// recurse to compute the children
		final List<INode> children = node.getChildren();
		if (children != null)
		{
			for (final INode child : children)
			{
				computeTree(child);
			}
		}
	}

	/**
	 * Draw tree recursively
	 *
	 * @param node starting node
	 */
	private void drawTree(@Nullable final INode node)
	{
		if (node == null)
		{
			return;
		}

		// edge to parent
		final INode parent = node.getParent();
		if (parent != null)
		{
			// color
			Color color = node.getEdgeColor();
			if (color == null)
			{
				color = this.treeEdgeColor;
			}
			this.graphics.setColor(color);

			// draw
			drawTreeEdge(parent, node);
		}

		// recurse to draw the children
		final List<INode> children = node.getChildren();
		if (children != null)
		{
			for (final INode child : children)
			{
				drawTree(child);
			}
		}

		// debug
		if ((Painter.DEBUG & Painter.DEBUG_NODECIRCLE) != 0)
		{
			drawSpace(node);
		}
		if ((Painter.DEBUG & Painter.DEBUG_NONODE) != 0)
		{
			return;
		}

		// draw node
		final Location location = node.getLocation();
		if (!location.hyper.isBorder)
		{
			drawNode((NodeData) node.getLocation().viewData);
		}
	}

	// D R A W . N O D E

	/**
	 * Class collecting drawing data
	 *
	 * @author Bernard Bou
	 */
	static private class NodeData
	{
		/**
		 * Node space
		 */
		@SuppressWarnings({"WeakerAccess", "InstanceVariableOfConcreteClass"})
		@Nullable
		public Rectangle2D space;

		/**
		 * Node label box
		 */
		@SuppressWarnings({"WeakerAccess", "InstanceVariableOfConcreteClass"})
		public Rectangle2D box;

		/**
		 * Node label lines
		 */
		@SuppressWarnings("WeakerAccess")
		@Nullable
		public String[] labelLines;

		/**
		 * Node label lines' width
		 */
		@SuppressWarnings("WeakerAccess")
		public int[] labelLinesW;

		/**
		 * Node label width
		 */
		@SuppressWarnings("WeakerAccess")
		public int labelW;

		/**
		 * Node label x coordinate
		 */
		@SuppressWarnings("WeakerAccess")
		public int labelX;

		/**
		 * Node label y coordinate
		 */
		@SuppressWarnings("WeakerAccess")
		public int labelY;

		/**
		 * Node image
		 */
		@SuppressWarnings({"WeakerAccess", "InstanceVariableOfConcreteClass"})
		@Nullable
		public Image image;

		/**
		 * Node image x-coordinate
		 */
		@SuppressWarnings("WeakerAccess")
		public int imageX;

		/**
		 * Node image y-coordinate
		 */
		@SuppressWarnings("WeakerAccess")
		public int imageY;

		/**
		 * Node image width
		 */
		@SuppressWarnings("WeakerAccess")
		public int imageWidth;

		/**
		 * Node image height
		 */
		@SuppressWarnings("WeakerAccess")
		public int imageHeight;

		/**
		 * Node backcolor
		 */
		@SuppressWarnings({"WeakerAccess", "InstanceVariableOfConcreteClass"})
		@Nullable
		public Color backColor;

		/**
		 * Node forecolor
		 */
		@SuppressWarnings({"WeakerAccess", "InstanceVariableOfConcreteClass"})
		@Nullable
		public Color foreColor;

		/**
		 * Node text size
		 */
		@SuppressWarnings("WeakerAccess")
		public float textSize;

		/**
		 * Is mountable
		 */
		@SuppressWarnings("WeakerAccess")
		@Nullable
		public Boolean isMountable;
	}

	/**
	 * Draw node
	 *
	 * @param nodeData computed node data
	 */
	private void drawNode(@Nullable final NodeData nodeData)
	{
		if (nodeData == null)
		{
			return;
		}

		// box
		if (nodeData.box != null)
		{
			// rectangle
			final int x = (int) nodeData.box.getX();
			final int y = (int) nodeData.box.getY();
			final int w = (int) nodeData.box.getWidth();
			final int h = (int) nodeData.box.getHeight();
			final int rx = 10;
			final int ry = 10;

			// fill
			if ((Painter.DEBUG & Painter.DEBUG_NOLABELFILL) == 0)
			{
				this.graphics.setColor(nodeData.backColor);
				this.graphics.fillRoundRectangle(x, y, w, h, rx, ry);
			}

			// foreground color
			this.graphics.setColor(nodeData.foreColor);

			// outline
			if (this.border)
			{
				this.graphics.drawRoundRectangle(x, y, w, h, rx, ry);
			}

			// mount clue
			if (nodeData.isMountable != null)
			{
				final int w0 = 3;
				final int h0 = 2 * w0;
				final int hm = 2;
				final int x0 = x + w + 5;
				final int x1 = x0 - w0;
				final int x2 = x0 + w0;
				final int y0 = y + hm;
				final int[] xs = {x1, x0, x2};
				if (nodeData.isMountable)
				{
					final int hr = h - 2 * hm;
					final int y2 = y0 + hr;
					final int y1 = y2 - h0;
					final int[] ys = {y1, y2, y1};
					this.graphics.fillPolygon(xs, ys, 3);
				}
				else
				{
					final int y1 = y0 + h0;
					final int[] ys = {y1, y0, y1};
					this.graphics.fillPolygon(xs, ys, 3);
				}
			}
		}

		// image
		if (nodeData.image != null && (Painter.DEBUG & Painter.DEBUG_NOIMAGE) == 0)
		{
			if (nodeData.imageWidth >= Painter.MIN_IMAGE_DIMENSION && nodeData.imageHeight >= Painter.MIN_IMAGE_DIMENSION)
			{
				if (this.downscaleImages)
				{
					this.graphics.drawImage(nodeData.image, nodeData.imageX, nodeData.imageY, nodeData.imageWidth, nodeData.imageHeight);
				}
				else
				{
					this.graphics.drawImage(nodeData.image, nodeData.imageX, nodeData.imageY);
				}
			}
		}

		// label
		if ((Painter.DEBUG & Painter.DEBUG_NOLABEL) != 0)
		{
			return;
		}
		if (nodeData.labelLines != null)
		{
			drawLabel(nodeData);
		}
	}

	/**
	 * Compute node data
	 *
	 * @param node node
	 * @return node data
	 */
	@Nullable
	@SuppressWarnings({"boxing"})
	private NodeData computeNodeData(@NonNull final INode node)
	{
		final NodeData nodeData = new NodeData();

		// hyper circle
		final Location location = node.getLocation();
		if (location.hyper.isBorder)
		{
			return null;
		}

		// text size
		final float textSize = hyperdistanceToSize(location.hyper.dist);
		nodeData.textSize = textSize;
		this.graphics.setTextSize(textSize);

		// color
		nodeData.backColor = node.getBackColor();
		if (nodeData.backColor == null)
		{
			nodeData.backColor = this.nodeBackColor;
		}
		nodeData.foreColor = node.getForeColor();
		if (nodeData.foreColor == null)
		{
			nodeData.foreColor = this.nodeForeColor;
		}

		// center
		final int xnode = xUnitCircleToView(location.euclidean.center.re);
		final int ynode = yUnitCircleToView(location.euclidean.center.im);

		// node space actual diameter
		final int rnode = wUnitCircleToView(location.euclidean.radius);
		final int dnode = 2 * rnode;

		// image
		nodeData.image = node.getImage();
		nodeData.imageWidth = 0;
		nodeData.imageHeight = 0;
		if (nodeData.image == null)
		{
			nodeData.image = this.defaultNodeImage;
		}
		if (nodeData.image != null)
		{
			// scale down as per hyperbolic distance
			assert this.imageDownscaler != null;
			final int imageScaleIndex = Math.min(this.imageDownscaler.length - 1, (int) Math.round(location.hyper.dist * this.imageDownscaler.length));
			double imageScale = this.downscaleImages ? this.imageDownscaler[imageScaleIndex] : 1.F;
			if (imageScale == -1F)
			{
				imageScale = 1.F - location.hyper.dist;
			}
			imageScale *= this.imageScaleFactor;
			nodeData.imageWidth = (int) (imageScale * nodeData.image.getWidth());
			nodeData.imageHeight = (int) (imageScale * nodeData.image.getHeight());
		}

		// string
		nodeData.labelLines = makeLabel(node);
		if (nodeData.labelLines == null)
		{
			// no label
			if (nodeData.image != null)
			{
				final int hi = nodeData.imageHeight;
				final int xi = xnode - nodeData.imageWidth / 2;
				final int yi = ynode - hi / 2;

				nodeData.imageX = xi;
				nodeData.imageY = yi;
			}
			return nodeData;
		}

		// label dimensions
		nodeData.labelW = labelWidth(nodeData);
		if (nodeData.labelW > dnode && this.ellipsize)
		{
			// ellipsize label
			nodeData.labelW = ellipsizeLabel(nodeData, dnode);
		}
		final int htext = labelHeight(nodeData);

		// box computation
		final int wbox = nodeData.labelW + 2 * Painter.NODE_HORIZONTAL_PADDING;
		final int hbox = Painter.NODE_TOP_PADDING + htext + this.graphics.getDescent() + Painter.NODE_BOTTOM_PADDING;
		final int xbox = xnode - nodeData.labelW / 2 - Painter.NODE_HORIZONTAL_PADDING;
		int ybox;

		// image computation
		if (nodeData.image == null)
		{
			ybox = ynode - htext / 2 - Painter.NODE_TOP_PADDING;
			nodeData.space = null;
		}
		else
		{
			// image is horizontally centered on node's focus point
			nodeData.imageX = xnode - nodeData.imageWidth / 2;

			// compute combined height of image and label (minus overlay of label)
			final int overlap = (int) (this.graphics.getAscent() * Painter.NODE_LABEL_OVERLAY);
			final int hcombined = nodeData.imageHeight + hbox - overlap;
			final int hcombined2 = (int) (hcombined / 2F);

			// combination is centered on node's focus

			// 1) box is placed on node's focus, pushed down to bottom of combination (half height of image-label combination) and raised by its height
			ybox = ynode + hcombined2 - hbox;

			// 2) image data is placed on node's focus and raised to top of combination (half height of image-label combination)
			nodeData.imageY = ynode - hcombined2;

			// space
			int xspace = xbox;
			int wspace = wbox;
			if (nodeData.imageWidth > wbox)
			{
				xspace = nodeData.imageX;
				wspace = nodeData.imageWidth;
			}
			nodeData.space = new Rectangle2D(xspace, nodeData.imageY, wspace, hcombined);
		}

		// label box
		nodeData.box = new Rectangle2D(xbox, ybox, wbox, hbox);

		// label
		nodeData.labelX = xbox + Painter.NODE_HORIZONTAL_PADDING;
		nodeData.labelY = ybox + Painter.NODE_TOP_PADDING + this.graphics.getAscent();

		// is mountable
		final MountPoint mountPoint = node.getMountPoint();
		//noinspection InstanceofConcreteClass
		nodeData.isMountable = mountPoint == null ? null : mountPoint instanceof MountPoint.Mounting;

		return nodeData;
	}

	// D R A W . E D G E

	/**
	 * Draw tree edge, from parent to child
	 *
	 * @param parent from-node
	 * @param node   to-node
	 */
	private void drawTreeEdge(@NonNull final INode parent, @NonNull final INode node)
	{
		// style
		final Integer style0 = node.getEdgeStyle();
		final int style = mergeStyles(this.treeEdgeStyle, style0);
		if ((style & IEdge.HIDDEN) != 0) // defined and hidden
		{
			return;
		}

		// hyper circles
		final Location from = parent.getLocation();
		final Location to = node.getLocation();

		// space
		final NodeData fromData = (NodeData) parent.getLocation().viewData;
		final NodeData toData = (NodeData) node.getLocation().viewData;
		Rectangle2D fromSpace = null;
		if (fromData != null)
		{
			fromSpace = fromData.space != null ? fromData.space : fromData.box;
		}
		Rectangle2D toSpace = null;
		if (toData != null)
		{
			toSpace = toData.space != null ? toData.space : toData.box;
		}

		// do not draw edge if boxes intersect
		if (Painter.boxesIntersect(fromSpace, toSpace))
		{
			return;
		}

		// image
		Image image = node.getEdgeImage();
		float imageScale = 1.F;
		if (image == null)
		{
			image = this.defaultTreeEdgeImage;
		}
		if (image != null)
		{
			final Location location = node.getLocation();
			assert this.imageDownscaler != null;
			final int imageScaleIndex = Math.min(this.imageDownscaler.length - 1, (int) Math.round(location.hyper.dist * this.imageDownscaler.length));
			imageScale = this.downscaleImages ? this.imageDownscaler[imageScaleIndex] : 1.F;
			if (imageScale == -1F)
			{
				imageScale = 1.F - (float) location.hyper.dist;
			}
			imageScale *= this.imageScaleFactor;
		}

		// draw
		final String label = node.getEdgeLabel();
		final boolean isBorder = from.hyper.isBorder;
		drawEdge(from.euclidean.center, to.euclidean.center, label, image, imageScale, style, fromSpace, toSpace, isBorder);
	}

	/**
	 * Draw non-tree edge
	 *
	 * @param edge edge
	 */
	private void drawNonTreeEdge(@NonNull final IEdge edge)
	{
		// style
		final Integer style0 = edge.getStyle();
		final int style = mergeStyles(this.edgeStyle, style0);
		if ((style & IEdge.HIDDEN) != 0) // defined and hidden
		{
			return;
		}

		// nodes
		INode fromNode = edge.getFrom();
		INode toNode = edge.getTo();
		if (fromNode == null || toNode == null)
		{
			return;
		}
		fromNode = MountPoint.follow(fromNode, false, true);
		toNode = MountPoint.follow(toNode, false, true);

		// hyper circles
		final Location fromLocation = fromNode.getLocation();
		final Location toLocation = toNode.getLocation();
		// if (fromLocation.hyper.isBorder || toLocation.hyper.isBorder)
		// return;

		if (fromLocation.hyper.isDirty)
		{
			MapperToEuclidean.mapToEuclidean(fromLocation);
		}
		if (toLocation.hyper.isDirty)
		{
			MapperToEuclidean.mapToEuclidean(toLocation);
		}

		// space
		final NodeData fromData = (NodeData) fromNode.getLocation().viewData;
		final NodeData toData = (NodeData) toNode.getLocation().viewData;
		Rectangle2D fromSpace = null;
		if (fromData != null)
		{
			fromSpace = fromData.space != null ? fromData.space : fromData.box;
		}
		Rectangle2D toSpace = null;
		if (toData != null)
		{
			toSpace = toData.space != null ? toData.space : toData.box;
		}

		// do not draw edge if boxes intersect
		if (Painter.boxesIntersect(fromSpace, toSpace))
		{
			return;
		}

		// label
		final String label = edge.getLabel();
		if (label != null)
		{
			// text size
			final float textSize = hyperdistanceToSize(toLocation.hyper.dist);
			this.graphics.setTextSize(textSize);
		}

		// draw
		Color color = edge.getColor();
		if (color == null)
		{
			color = this.edgeColor;
		}
		this.graphics.setColor(color);

		// image
		Image image = edge.getImage();
		if (image == null)
		{
			image = this.defaultEdgeImage;
		}

		// image scaling
		float imageScale = 1.F;
		if (image != null && this.downscaleImages)
		{
			// font size
			assert this.imageDownscaler != null;
			final int scaleIdx = Math.min(this.imageDownscaler.length - 1, (int) Math.round(toLocation.hyper.dist * this.imageDownscaler.length));
			imageScale = this.imageDownscaler[scaleIdx];
			if (imageScale == -1F)
			{
				imageScale = 1.F - (float) toLocation.hyper.dist;
			}
			imageScale *= this.imageScaleFactor;
		}

		// draw arc
		drawEdge(fromLocation.euclidean.center, toLocation.euclidean.center, label, image, imageScale, style, fromSpace, toSpace, false);
	}

	/**
	 * Draw edge from z1 to z2
	 *
	 * @param z1         from-end
	 * @param z2         to-end
	 * @param label      arc label
	 * @param image      arc image
	 * @param imageScale image scale
	 * @param style      code for edge style
	 * @param fromSpace  from-node space
	 * @param toSpace    to-node space
	 * @param isBorder   true if arc neighbours border
	 */
	private void drawEdge(@NonNull final Complex z1, @NonNull final Complex z2, final String label, final Image image, final float imageScale, final int style, final Rectangle2D fromSpace, final Rectangle2D toSpace, final boolean isBorder)
	{
		if (Painter.STRAIGHT_EDGE_WHILE_MOVING && this.isDragging || !this.arcEdges || (style & IEdge.LINE) != 0)
		{
			drawLine(z1, z2, label, image, imageScale, style, fromSpace, toSpace, isBorder);
		}
		else
		{
			drawArc(z1, z2, label, image, imageScale, style, fromSpace, toSpace, isBorder);
		}
	}

	/**
	 * Get font size from hyperdistance of node
	 *
	 * @param hyperDistance hyperdistance of node
	 * @return font size
	 */
	private float hyperdistanceToSize(final double hyperDistance)
	{
		assert this.fontDownscaler != null;
		final int bucket = Math.min(this.fontDownscaler.length - 1, (int) Math.round(hyperDistance * this.fontDownscaler.length));
		return this.fontSize * this.fontSizeFactor * this.fontScaleFactor * this.fontDownscaler[bucket];
	}

	// D R A W . A R C

	/**
	 * Draw geodesic arc from z1 to z2 which models line from z1 to z2
	 *
	 * @param z1         from-end
	 * @param z2         to-end
	 * @param label0     arc label
	 * @param image      arc image
	 * @param imageScale image scale
	 * @param style      style
	 * @param fromSpace  from-node space
	 * @param toSpace    to-node space
	 * @param isBorder   true if arc neighbours border
	 */
	private void drawArc(@NonNull final Complex z1, @NonNull final Complex z2, final String label0, @Nullable final Image image, final float imageScale, final int style, final Rectangle2D fromSpace, final Rectangle2D toSpace, final boolean isBorder)
	{
		final Arc arc = new Arc(z1, z2);

		String label = label0;

		// if(r == 0.) it is segment of line
		if (arc.r == 0.)
		{
			Point2D from = new Point2D(xUnitCircleToView(arc.from.re), yUnitCircleToView(arc.from.im));
			Point2D to = new Point2D(xUnitCircleToView(arc.to.re), yUnitCircleToView(arc.to.im));

			// adjust to anchors
			Point2D fromAnchor = null;
			if (!isBorder)
			{
				fromAnchor = getIntersection(fromSpace, to, from);
			}
			//noinspection UnusedAssignment
			Point2D toAnchor = null;
			toAnchor = getIntersection(toSpace, from, to);

			// adjust line ends
			if (fromAnchor != null)
			{
				from = fromAnchor;
			}
			if (toAnchor != null)
			{
				to = toAnchor;
			}

			// line
			drawLine(from, to, style);

			// image
			if (image != null && !isBorder)
			{
				final Point2D midPoint = Painter.getMidPoint(from, to);
				drawImage(image, midPoint, imageScale);
			}

			// ends
			drawEdgeEnds(from, to, null, style);

			// label
			if (!isBorder && label != null && !label.isEmpty())
			{
				// fit
				label = mangleString(label, from, to);
				if (label == null)
				{
					return;
				}

				drawText(label, from, to);
			}
		}
		else
		{
			final Arc2D arc2D = toArc2D(arc);

			// adjust to anchors
			Point2D fromAnchor = null;
			if (!isBorder)
			{
				fromAnchor = getIntersection(fromSpace, arc2D);
			}
			if (fromAnchor == null)
			{
				fromAnchor = arc2D.getStartPoint();
			}

			//noinspection UnusedAssignment
			Point2D toAnchor = null;
			toAnchor = getIntersection(toSpace, arc2D);
			if (toAnchor == null)
			{
				toAnchor = arc2D.getEndPoint();
			}

			// drawPoint(arc2D.getStartPoint(), Color.MAGENTA);
			// drawPoint(arc2D.getEndPoint(), Color.MAGENTA);
			// drawPoint(fromAnchor, Color.YELLOW);
			// drawPoint(toAnchor, Color.YELLOW);

			// adjust arc ends to anchors
			if (arc2D.getAngleExtent() >= 0.)
			{
				arc2D.setAngles(fromAnchor, toAnchor);
			}
			else
			{
				arc2D.setAngles(toAnchor, fromAnchor);
			}
			// drawPoint(arc2D.getStartPoint(), Color.GREEN);
			// drawPoint(arc2D.getEndPoint(), Color.GREEN);

			// draw
			drawArc(arc2D, fromAnchor, toAnchor, style);

			// image
			if (image != null)
			{
				drawImage(image, ArcMath.getMidArc(arc2D), imageScale);
			}

			// ends
			drawEdgeEnds(fromAnchor, toAnchor, arc2D, style);

			// draw edge label
			if (!isBorder && label != null && !label.isEmpty())
			{
				// fit
				label = mangleString(label, fromAnchor, toAnchor);
				if (label == null)
				{
					return;
				}

				// mid arc
				final Point2D midArc = ArcMath.getMidArc(arc2D);

				// tangent
				final double tangent = ArcMath.getTextTangent(arc2D, midArc);

				// draw text
				drawText(label, midArc, tangent);
			}
		}
	}

	/**
	 * Draw arc2D
	 *
	 * @param arc2D      arc
	 * @param fromAnchor from-anchor
	 * @param toAnchor   to-anchor
	 */
	private void drawArc(@NonNull final Arc2D arc2D, final Point2D fromAnchor, final Point2D toAnchor, final int style)
	{
		final int x = (int) arc2D.x;
		final int y = (int) arc2D.y;
		final int w = (int) arc2D.width;
		final int h = (int) arc2D.height;
		final float start = (float) arc2D.start;
		final float extent = (float) arc2D.extent;

		if ((style & (IEdge.STROKEMASK | IEdge.STROKEWIDTHMASK)) != 0)
		{
			// stroke
			int stroke = (style & IEdge.STROKEMASK);
			int strokeValue;
			switch (stroke)
			{
				case IEdge.DASH:
					strokeValue = treebolic.glue.iface.Graphics.DASH;
					break;
				case IEdge.DOT:
					strokeValue = treebolic.glue.iface.Graphics.DOT;
					break;
				case IEdge.SOLID:
				default:
					strokeValue = treebolic.glue.iface.Graphics.SOLID;
					break;
			}

			// width
			int width = (style & IEdge.STROKEWIDTHMASK) >> IEdge.STROKEWIDTHSHIFT;
			this.graphics.pushStroke();
			this.graphics.setStroke(strokeValue, width);
			this.graphics.drawArc(x, y, w, h, start, extent);
			this.graphics.popStroke();
			return;
		}
		this.graphics.drawArc(x, y, w, h, start, extent);
	}

	/**
	 * Convert Arc to Arc2D
	 *
	 * @param arc arc
	 * @return arc
	 */
	@NonNull
	private Arc2D toArc2D(@NonNull final Arc arc)
	{
		final Arc2D arc2D = new Arc2D();

		// frame
		final double x0 = xUnitCircleToView(arc.x);
		final double y0 = yUnitCircleToView(arc.y);
		final double x = xUnitCircleToView(arc.x - arc.r);
		final double y = yUnitCircleToView(arc.y - arc.r);
		arc2D.setFrameFromCenter(x0, y0, x, y);

		// start (reversing top/down)
		double start = Math.toDegrees(-arc.start);
		if (start < 0.)
		{
			start += 360.;
		}
		arc2D.setAngleStart(start);

		// extent (reversing top/down)
		final double extent = Math.toDegrees(-arc.angle);
		arc2D.setAngleExtent(extent);

		// rotation direction later lost in normalizations
		arc2D.setCounterclockwise(arc.counterclockwise());

		return arc2D;
	}

	// D R A W . L I N E

	/**
	 * Draw line
	 *
	 * @param z1         from
	 * @param z2         to
	 * @param label0     label
	 * @param image      image
	 * @param imageScale image scale
	 * @param style      style
	 * @param fromSpace  from-node space
	 * @param toSpace    to-node-space
	 * @param isBorder   true if is nearing border
	 */
	private void drawLine(@NonNull final Complex z1, @NonNull final Complex z2, final String label0, @Nullable final Image image, final float imageScale, final int style, final Rectangle2D fromSpace, final Rectangle2D toSpace, final boolean isBorder)
	{
		Point2D from = new Point2D(xUnitCircleToView(z1.re), yUnitCircleToView(z1.im));
		Point2D to = new Point2D(xUnitCircleToView(z2.re), yUnitCircleToView(z2.im));

		// adjust to anchors
		Point2D fromAnchor = null;
		if (!isBorder)
		{
			fromAnchor = getIntersection(fromSpace, to, from);
		}
		//noinspection UnusedAssignment
		Point2D toAnchor = null;
		toAnchor = getIntersection(toSpace, from, to);

		// adjust line ends
		if (fromAnchor != null)
		{
			from = fromAnchor;
		}
		if (toAnchor != null)
		{
			to = toAnchor;
		}

		// line
		drawLine(from, to, style);

		// image
		if (image != null && !isBorder)
		{
			final Point2D midPoint = Painter.getMidPoint(from, to);
			drawImage(image, midPoint, imageScale);
		}

		// ends
		drawEdgeEnds(from, to, null, style);

		// label
		String label = label0;
		if (!isBorder && label != null && !label.isEmpty())
		{
			// fit
			label = mangleString(label, from, to);
			if (label == null)
			{
				return;
			}

			drawText(label, from, to);
		}
	}

	/**
	 * Draw line from p1 to p2
	 *
	 * @param fromPoint from-point
	 * @param toPoint   to-point
	 */
	private void drawLine(@NonNull final Point2D fromPoint, @NonNull final Point2D toPoint, final int style)
	{
		if ((style & (IEdge.STROKEMASK | IEdge.STROKEWIDTHMASK)) != 0)
		{
			// stroke
			int strokeValue;
			int stroke = (style & IEdge.STROKEMASK);
			switch (stroke)
			{
				case IEdge.DASH:
					strokeValue = treebolic.glue.iface.Graphics.DASH;
					break;
				case IEdge.DOT:
					strokeValue = treebolic.glue.iface.Graphics.DOT;
					break;
				case IEdge.SOLID:
				default:
					strokeValue = treebolic.glue.iface.Graphics.SOLID;
					break;
			}

			// width
			int width = (style & IEdge.STROKEWIDTHMASK) >> IEdge.STROKEWIDTHSHIFT;
			this.graphics.pushStroke();
			this.graphics.setStroke(strokeValue, width);
			this.graphics.drawLine((int) fromPoint.getX(), (int) fromPoint.getY(), (int) toPoint.getX(), (int) toPoint.getY());
			this.graphics.popStroke();
			return;
		}
		this.graphics.drawLine((int) fromPoint.getX(), (int) fromPoint.getY(), (int) toPoint.getX(), (int) toPoint.getY());
	}

	// D R A W . P O I N T

	// @formatter:off
	//	/**
	//	 * Draw point
	//	 *
	//	 * @param x
	//	 *        x
	//	 * @param y
	//	 *        y
	//	 * @param color
	//	 *        color
	//	 */
	//	private void drawPoint(double x, double y, final Color color)
	//	{
	//		int d = 10;
	//		int width = 3;
	//		this.graphics.pushStroke();
	//		this.graphics.setStroke(treebolic.glue.iface.Graphics.SOLID, width);
	//		final Color color0 = this.graphics.getColor();
	//		this.graphics.setColor(color);
	//		this.graphics.drawLine((int) x - d, (int) y, (int) x + d, (int) y);
	//		this.graphics.drawLine((int) x, (int) y - d, (int) x, (int) y + d);
	//		this.graphics.setColor(color0);
	//		this.graphics.popStroke();
	//	}
	// @formatter:on

	// D R A W . T E X T

	/**
	 * Draw text
	 *
	 * @param str          string to be drawn
	 * @param where        where to put text (centered on this point)
	 * @param orientation0 text orientation
	 */
	private void drawText(@NonNull final String str, @NonNull final Point2D where, final double orientation0)
	{
		double orientation = orientation0;

		// save current transform
		this.graphics.pushMatrix();

		// font metrics
		final int width = this.graphics.stringWidth(str);

		// text orientation
		final boolean reverse = orientation > Math.PI / 2.;
		//noinspection UnusedAssignment
		int yShift = 0;
		if (reverse)
		{
			orientation += Math.PI;
			yShift = -this.graphics.getAscent();
		}
		else
		{
			yShift = this.graphics.getDescent();
		}

		// translate to center of text
		this.graphics.rotate((float) orientation, (float) where.getX(), (float) where.getY());
		this.graphics.drawString(str, -width / 2, -yShift);

		// restore transform
		this.graphics.popMatrix();
	}

	/**
	 * Draw edge text from (x1,y1) to (x2,y2)
	 *
	 * @param str  text
	 * @param from from-point
	 * @param to   to-point
	 */
	private void drawText(@NonNull final String str, @NonNull final Point2D from, @NonNull final Point2D to)
	{
		final double x1 = from.getX();
		final double y1 = from.getY();
		final double x2 = to.getX();
		final double y2 = to.getY();

		// orientation
		double orientation = Math.atan2(y1 - y2, x1 - x2);
		if (orientation < 0)
		{
			orientation += Math.PI;
		}

		// where
		final Point2D where = new Point2D((x2 + x1) / 2., (y2 + y1) / 2.);

		// draw
		drawText(str, where, orientation);
	}

	/**
	 * Mangle string to fit in
	 *
	 * @param str0 string
	 * @param from from-point
	 * @param to   to-point
	 * @return mangled string or null
	 */
	private String mangleString(final String str0, @NonNull final Point2D from, @NonNull final Point2D to)
	{
		String str = str0;
		final double cx = to.getX() - from.getX();
		final double cy = to.getY() - from.getY();
		final int span = (int) Math.sqrt(cx * cx + cy * cy) - Painter.TEXT_PADDING;

		// label font size
		final int w = this.graphics.stringWidth(str);

		// ellipsize label
		if (w > span)
		{
			// compute average character width
			final int wunit = this.graphics.stringWidth(AVERAGE_CHAR);
			if (wunit > 0) // avoid 0 div
			{

				// compute trailing dots width
				final int wdots = this.graphics.stringWidth(ELLIPSIS);

				// compute number of characters that fit before dots
				int nChars = (span - wdots) / wunit;

				// ensure at least one
				if (nChars < 1)
				{
					nChars = 1;
				}

				// perform truncation if we actually ellipsize
				final int len = str.length();
				if (len > nChars)
				{
					str = str.substring(0, nChars) + ELLIPSIS;
				}
			}
		}
		return str;
	}

	// D R A W . I M A G E

	/**
	 * Draw image at point (scaled)
	 *
	 * @param image      image
	 * @param where      location
	 * @param imageScale image scale
	 */
	private void drawImage(@Nullable final Image image, @NonNull final Point2D where, final float imageScale)
	{
		if (image != null)
		{
			if (imageScale == 1.F || !this.downscaleImages)
			{
				final int w = image.getWidth();
				final int h = image.getHeight();
				if (w < Painter.MIN_IMAGE_DIMENSION || h < Painter.MIN_IMAGE_DIMENSION)
				{
					return;
				}
				final int x = (int) (where.getX() - w / 2);
				final int y = (int) (where.getY() - h / 2);
				this.graphics.drawImage(image, x, y);
			}
			else
			{
				final int w = (int) (imageScale * image.getWidth());
				final int h = (int) (imageScale * image.getHeight());
				if (w < Painter.MIN_IMAGE_DIMENSION || h < Painter.MIN_IMAGE_DIMENSION)
				{
					return;
				}
				final int x = (int) (where.getX() - w / 2);
				final int y = (int) where.getY() - h / 2;
				this.graphics.drawImage(image, x, y, w, h);
			}
		}
	}

	// D R A W . E D G E E N D S

	/**
	 * Draw edge ends
	 *
	 * @param from  from-end
	 * @param to    to-end
	 * @param arc2D arc
	 * @param style style code
	 */
	private void drawEdgeEnds(@NonNull final Point2D from, @NonNull final Point2D to, @Nullable final Arc2D arc2D, final int style)
	{
		if (style == 0)
		{
			return;
		}

		int strokeWidth = (style & IEdge.STROKEWIDTHMASK) >> IEdge.STROKEWIDTHSHIFT;
		double terminatorHeight = Painter.TERMINATOR_HEIGHT + strokeWidth;
		double terminatorWidth = Painter.TERMINATOR_WIDTH + strokeWidth;

		final double x1 = from.getX();
		final double y1 = from.getY();
		final double x2 = to.getX();
		final double y2 = to.getY();
		final double cx = x2 - x1;
		final double cy = y2 - y1;

		// do nothing if too near
		final double span2 = cx * cx + cy * cy;
		if (span2 < Painter.TERMINATOR_MIN_SPAN2)
		{
			return;
		}

		double sx1;
		double sy1;
		double dx1;
		double dy1;
		double sx2;
		double sy2;
		double dx2;
		double dy2;
		if (arc2D == null)
		{
			final double orientation = Math.atan2(y2 - y1, x2 - x1);
			sx1 = terminatorHeight * Math.cos(orientation);
			sy1 = terminatorHeight * Math.sin(orientation);
			dx1 = terminatorWidth * Math.cos(orientation + Math.PI / 2);
			dy1 = terminatorWidth * Math.sin(orientation + Math.PI / 2);
			sx2 = -sx1;
			sy2 = -sy1;
			dx2 = dx1;
			dy2 = dy1;
		}
		else
		{
			final double orientation1 = ArcMath.getTangent(arc2D, from, true);
			final double orientation2 = ArcMath.getTangent(arc2D, to, false);

			// {
			// this.graphics.setColor(Color.GREEN);
			// drawOrientation(x1, y1, orientation1);
			// this.graphics.setColor(Color.MAGENTA);
			// drawOrientation(x2, y2, orientation2);
			// }

			sx1 = terminatorHeight * Math.cos(orientation1);
			sy1 = terminatorHeight * Math.sin(orientation1);
			dx1 = terminatorWidth * Math.cos(orientation1 + Math.PI / 2);
			dy1 = terminatorWidth * Math.sin(orientation1 + Math.PI / 2);

			sx2 = terminatorHeight * Math.cos(orientation2);
			sy2 = terminatorHeight * Math.sin(orientation2);
			dx2 = terminatorWidth * Math.cos(orientation2 + Math.PI / 2);
			dy2 = terminatorWidth * Math.sin(orientation2 + Math.PI / 2);
		}

		// from
		int endStyle = style >> IEdge.FROMSHIFT & IEdge.SHAPEMASK;
		switch (endStyle)
		{
			case IEdge.TRIANGLE:
			{
				// t
				final int[] x = {(int) x1, (int) (x1 + sx1 + dx1), (int) (x1 + sx1 - dx1)};
				final int[] y = {(int) y1, (int) (y1 + sy1 + dy1), (int) (y1 + sy1 - dy1)};
				if ((style & IEdge.FROMFILL) != 0)
				{
					this.graphics.fillPolygon(x, y, x.length);
				}
				else
				{
					final Color color = this.graphics.getColor();
					this.graphics.setColor(this.backColor);
					this.graphics.fillPolygon(x, y, x.length);
					this.graphics.setColor(color);
					this.graphics.drawPolygon(x, y, x.length);
				}
				break;
			}

			case IEdge.CIRCLE:
			{
				// c
				final double x = x1 + sx1 / 2 - terminatorHeight / 2;
				final double y = y1 + sy1 / 2 - terminatorHeight / 2;
				if ((style & IEdge.FROMFILL) != 0)
				{
					this.graphics.fillOval((int) x, (int) y, (int) terminatorHeight, (int) terminatorHeight);
				}
				else
				{
					final Color color = this.graphics.getColor();
					this.graphics.setColor(this.backColor);
					this.graphics.fillOval((int) x, (int) y, (int) terminatorHeight, (int) terminatorHeight);
					this.graphics.setColor(color);
					this.graphics.drawOval((int) x, (int) y, (int) terminatorHeight, (int) terminatorHeight);
				}
				break;
			}

			case IEdge.DIAMOND:
			{
				// d
				final int[] x = {(int) x1, (int) (x1 + sx1 + dx1), (int) (x1 + sx1 + sx1), (int) (x1 + sx1 - dx1)};
				final int[] y = {(int) y1, (int) (y1 + sy1 + dy1), (int) (y1 + sy1 + sy1), (int) (y1 + sy1 - dy1)};
				if ((style & IEdge.FROMFILL) != 0)
				{
					this.graphics.fillPolygon(x, y, x.length);
				}
				else
				{
					final Color color = this.graphics.getColor();
					this.graphics.setColor(this.backColor);
					this.graphics.fillPolygon(x, y, x.length);
					this.graphics.setColor(color);
					this.graphics.drawPolygon(x, y, x.length);
				}
				break;
			}

			case IEdge.ARROW:
			{
				// a
				final int[] x = {(int) (x1 + sx1 + dx1), (int) x1, (int) (x1 + sx1 - dx1)};
				final int[] y = {(int) (y1 + sy1 + dy1), (int) y1, (int) (y1 + sy1 - dy1)};
				this.graphics.drawPolyline(x, y, x.length);
				break;
			}

			case IEdge.HOOK:
			{
				// h
				final int[] x = {(int) (x1 + sx1 + dx1), (int) x1};
				final int[] y = {(int) (y1 + sy1 + dy1), (int) y1};
				this.graphics.drawPolyline(x, y, x.length);
				break;
			}

			default:
				break;
		}

		// to
		endStyle = style >> IEdge.TOSHIFT & IEdge.SHAPEMASK;
		switch (endStyle)
		{
			case IEdge.TRIANGLE:
			{
				// t
				final int[] x = {(int) x2, (int) (x2 + sx2 + dx2), (int) (x2 + sx2 - dx2)};
				final int[] y = {(int) y2, (int) (y2 + sy2 + dy2), (int) (y2 + sy2 - dy2)};
				if ((style & IEdge.TOFILL) != 0)
				{
					this.graphics.fillPolygon(x, y, x.length);
				}
				else
				{
					final Color color = this.graphics.getColor();
					this.graphics.setColor(this.backColor);
					this.graphics.fillPolygon(x, y, x.length);
					this.graphics.setColor(color);
					this.graphics.drawPolygon(x, y, x.length);
				}
				break;
			}

			case IEdge.CIRCLE:
			{
				// c
				final double x = x2 + sx2 / 2 - terminatorHeight / 2;
				final double y = y2 + sy2 / 2 - terminatorHeight / 2;
				if ((style & IEdge.TOFILL) != 0)
				{
					this.graphics.fillOval((int) x, (int) y, (int) terminatorHeight, (int) terminatorHeight);
				}
				else
				{
					final Color color = this.graphics.getColor();
					this.graphics.setColor(this.backColor);
					this.graphics.fillOval((int) x, (int) y, (int) terminatorHeight, (int) terminatorHeight);
					this.graphics.setColor(color);
					this.graphics.drawOval((int) x, (int) y, (int) terminatorHeight, (int) terminatorHeight);
				}
				break;
			}

			case IEdge.DIAMOND:
			{
				// d
				final int[] x = {(int) x2, (int) (x2 + sx2 + dx2), (int) (x2 + sx2 + sx2), (int) (x2 + sx2 - dx2)};
				final int[] y = {(int) y2, (int) (y2 + sy2 + dy2), (int) (y2 + sy2 + sy2), (int) (y2 + sy2 - dy2)};
				if ((style & IEdge.TOFILL) != 0)
				{
					this.graphics.fillPolygon(x, y, x.length);
				}
				else
				{
					final Color color = this.graphics.getColor();
					this.graphics.setColor(this.backColor);
					this.graphics.fillPolygon(x, y, x.length);
					this.graphics.setColor(color);
					this.graphics.drawPolygon(x, y, x.length);
				}
				break;
			}

			case IEdge.ARROW:
			{
				// a
				final int[] x = {(int) (x2 + sx2 + dx2), (int) x2, (int) (x2 + sx2 - dx2)};
				final int[] y = {(int) (y2 + sy2 + dy2), (int) y2, (int) (y2 + sy2 - dy2)};
				this.graphics.drawPolyline(x, y, x.length);
				break;
			}

			case IEdge.HOOK:
			{
				// h
				final int[] x = {(int) (x2 + sx2 + dx2), (int) x2};
				final int[] y = {(int) (y2 + sy2 + dy2), (int) y2};
				this.graphics.drawPolyline(x, y, x.length);
				break;
			}

			default:
				break;
		}
	}

	// D R A W . D E B U G

	/**
	 * Draw node's space
	 *
	 * @param node node
	 */
	private void drawSpace(@NonNull final INode node)
	{
		// color
		Color backColor = node.getBackColor();
		if (backColor == null)
		{
			backColor = this.nodeBackColor;
		}
		// draw
		final Location location = node.getLocation();
		drawCircle(location.euclidean.center.re, location.euclidean.center.im, location.euclidean.radius, backColor);

		// draw center
		final int x = xUnitCircleToView(location.euclidean.center.re);
		final int y = yUnitCircleToView(location.euclidean.center.im);
		final int left = x - 5;
		final int right = x + 5;
		final int top = y - 5;
		final int bottom = y + 5;
		this.graphics.drawLine(left, y, right, y);
		this.graphics.drawLine(x, top, x, bottom);
	}

	/**
	 * Draw circle
	 *
	 * @param x     center x-coordinate
	 * @param y     center y-coordinate
	 * @param r     radius
	 * @param color color
	 */
	private void drawCircle(final double x, final double y, final double r, @Nullable final Color color)
	{
		this.graphics.setColor(color);
		this.graphics.drawOval(xUnitCircleToView(x - r), yUnitCircleToView(y - r), wUnitCircleToView(2 * r), hUnitCircleToView(2 * r));
	}

	// @formatter:off
	/*
	private void drawOrientation(final double x1, double y1, double theta)
	{
		double x2 = x1 + Math.cos(theta) * 50;
		double y2 = y1 + Math.sin(theta) * 50;
		this.graphics.drawLine((int) x1, (int) y1, (int) (x2), (int) (y2));
	}
	*/
	// @formatter:on

	// H E L P E R S

	// s t y l e

	/**
	 * Merge styles
	 *
	 * @param defaultStyle default style
	 * @param style0       local style
	 * @return style
	 */
	static private int mergeStyles(int defaultStyle, @Nullable final Integer style0)
	{
		if (style0 == null)
		{
			return defaultStyle;
		}
		int style = style0;
		int result = defaultStyle;
		if ((style & IEdge.HIDDENDEF) != 0)
		{
			result &= ~IEdge.HIDDEN;
			result |= style & IEdge.HIDDEN;
		}
		if ((style & IEdge.LINEDEF) != 0)
		{
			result &= ~IEdge.LINE;
			result |= style & IEdge.LINE;
		}
		if ((style & IEdge.STROKEDEF) != 0)
		{
			result &= ~IEdge.STROKEMASK;
			result |= style & IEdge.STROKEMASK;
		}
		if ((style & IEdge.STROKEWIDTHDEF) != 0)
		{
			result &= ~IEdge.STROKEWIDTHMASK;
			result |= style & IEdge.STROKEWIDTHMASK;
		}
		if ((style & IEdge.FROMDEF) != 0)
		{
			result &= ~IEdge.FROMMASK;
			result |= style & IEdge.FROMMASK;
		}
		if ((style & IEdge.TODEF) != 0)
		{
			result &= ~IEdge.TOMASK;
			result |= style & IEdge.TOMASK;
		}
		return result;
	}

	// m i d - p o i n t

	/**
	 * Get middle
	 *
	 * @param x1 from point
	 * @param x2 to mpoint
	 * @return middle
	 */
	@NonNull
	private static Point2D getMidPoint(@NonNull final Point2D x1, @NonNull final Point2D x2)
	{
		final double x = (x1.getX() + x2.getX()) / 2.0;
		final double y = (x1.getY() + x2.getY()) / 2.0;
		return new Point2D(x, y);
	}

	// i n t e r s e c t i o n

	/**
	 * Get intersection between line and rectangle
	 *
	 * @param rect rectangle
	 * @param from from-point on line
	 * @param to   to-point on line
	 * @return intersection point
	 */
	@Nullable
	static private Point2D getIntersection(@SuppressWarnings("TypeMayBeWeakened") @Nullable final Rectangle2D rect, @NonNull final Point2D from, @NonNull final Point2D to)
	{
		if (!CROP_EDGES)
		{
			return null;
		}

		if (rect == null)
		{
			return null;
		}

		final int code = rect.outcode(from);

		// rectangle
		final double x0 = rect.getCenterX();
		final double y0 = rect.getCenterY();
		final double x = rect.getX();
		final double y = rect.getY();
		final double w = rect.getWidth();
		final double h = rect.getHeight();

		// points on top, bottom, left, right
		//noinspection UnnecessaryLocalVariable
		final double ty = y;
		final double by = y + h;
		//noinspection UnnecessaryLocalVariable
		final double lx = x;
		final double rx = x + w;

		// line
		final double tanAlpha = (to.getY() - from.getY()) / (to.getX() - from.getX());

		if ((code & Rectangle2D.OUT_LEFT) != 0)
		{
			// compute left intersection
			final double ly = (lx - x0) * tanAlpha + y0;
			if (ly >= ty && ly <= by)
			{
				// drawPoint(lx, ly, Color.GREEN);
				return new Point2D(lx, ly);
			}
		}
		if ((code & Rectangle2D.OUT_RIGHT) != 0)
		{
			// compute right intersection
			final double ry = (rx - x0) * tanAlpha + y0;
			if (ry >= ty && ry <= by)
			{
				// drawPoint(rx, ry, Color.YELLOW);
				return new Point2D(rx, ry);
			}
		}
		if ((code & Rectangle2D.OUT_BOTTOM) != 0)
		{
			// compute bottom intersection
			final double bx = (by - y0) / tanAlpha + x0;
			if (bx >= lx && bx <= rx)
			{
				// drawPoint(bx, by, Color.ORANGE);
				return new Point2D(bx, by);
			}
		}
		if ((code & Rectangle2D.OUT_TOP) != 0)
		{
			// compute top intersection
			final double tx = (ty - y0) / tanAlpha + x0;
			if (tx >= lx && tx <= rx)
			{
				// drawPoint(tx, ty, Color.RED);
				return new Point2D(tx, ty);
			}
		}
		return null;
	}

	/**
	 * Get intersection between arc and rectangle
	 *
	 * @param rect rectangle
	 * @param arc  arc
	 * @return intersection point
	 */
	@Nullable
	static private Point2D getIntersection(@SuppressWarnings("TypeMayBeWeakened") @Nullable final Rectangle2D rect, @NonNull final Arc2D arc)
	{
		if (!CROP_EDGES)
		{
			return null;
		}

		if (rect == null)
		{
			return null;
		}

		// rectangle
		final double x = rect.getCenterX();
		final double y = rect.getCenterY();
		final double w = rect.getWidth() / 2.;
		final double h = rect.getHeight() / 2.;

		// arc
		final double ax = arc.getCenterX();
		final double ay = arc.getCenterY();
		final double a = arc.getWidth() / 2.;
		final double b = arc.getHeight() / 2.;
		final double a2 = a * a;
		final double b2 = b * b;

		// rectangle center relative to arc center
		final double drx = x - ax;
		final double dry = y - ay;
		final boolean isSouth = dry < 0.;
		final boolean isEast = drx < 0.;

		// rectangle top, bottom, left, right relative to arc center
		final double drl = drx - w;
		final double drr = drx + w;
		final double drt = dry - h;
		final double drb = dry + h;

		// points on top, bottom, left, right
		final double ty = dry - h;
		final double by = dry + h;
		final double lx = drx - w;
		final double rx = drx + w;
		double k;

		// compute left intersection
		k = lx * lx / a2;
		if (k <= 1.)
		{
			double ly = Math.sqrt((1. - k) * b2);
			if (isSouth)
			{
				ly = -ly;
			}
			// if other coordinate is on rectangle
			if (ly >= drt && ly <= drb)
			{
				if (Painter.isOnArc(lx, ly, arc))
				{
					// drawPoint(lx + ax, ly + ay, Color.BLUE);
					return new Point2D(lx + ax, ly + ay);
				}
			}
		}

		// compute right intersection
		k = rx * rx / a2;
		if (k <= 1.)
		{
			double ry = Math.sqrt((1. - k) * b2);
			if (isSouth)
			{
				ry = -ry;
			}
			// if other coordinate is on rectangle
			if (ry >= drt && ry <= drb)
			{
				if (Painter.isOnArc(rx, ry, arc))
				{
					// drawPoint(rx + ax, ry + ay, Color.CYAN);
					return new Point2D(rx + ax, ry + ay);
				}
			}
		}

		// compute bottom intersection
		k = by * by / b2;
		if (k <= 1.)
		{
			double bx = Math.sqrt((1. - k) * a2);
			if (isEast)
			{
				bx = -bx;
			}
			// if other coordinate is on rectangle
			if (bx >= drl && bx <= drr)
			{
				if (Painter.isOnArc(bx, by, arc))
				{
					// drawPoint(bx + ax, by + ay, Color.PINK);
					return new Point2D(bx + ax, by + ay);
				}
			}
		}

		// compute top intersection
		k = ty * ty / b2;
		if (k <= 1.)
		{
			double tx = Math.sqrt((1. - k) * a2);
			if (isEast)
			{
				tx = -tx;
			}
			// if other coordinate is on rectangle
			if (tx >= drl && tx <= drr)
			{
				if (Painter.isOnArc(tx, ty, arc))
				{
					// drawPoint(tx + ax, ty + ay, Color.MAGENTA);
					return new Point2D(tx + ax, ty + ay);
				}
			}
		}
		return null;
	}

	/**
	 * Get whether spaces or boxes intersect (boxes have to be inflated by one pixel)
	 *
	 * @param rect1 rect1
	 * @param rect2 rect2
	 * @return true if nodes' rects intersect
	 */
	private static boolean boxesIntersect(@Nullable final Rectangle2D rect1, @Nullable final Rectangle2D rect2)
	{
		if (rect1 != null && rect2 != null)
		{
			// inflate
			@SuppressWarnings("TypeMayBeWeakened") final Rectangle2D fromRect2 = new Rectangle2D();
			fromRect2.setFrame(rect1.getMinX() - 1, rect1.getMinY() - 1, (int) rect1.getWidth() + 2, (int) rect1.getHeight() + 2);
			final Rectangle2D toRect2 = new Rectangle2D();
			toRect2.setFrame(rect2.getMinX() - 1, rect2.getMinY() - 1, (int) rect2.getWidth() + 2, (int) rect2.getHeight() + 2);

			// intersection test
			return fromRect2.intersects(toRect2);
		}
		return false;
	}

	// i s o n a r c

	/**
	 * Test whether point is on arc
	 *
	 * @param dx  x-offset from arc center
	 * @param dy  y-offset from arc center
	 * @param arc arc
	 * @return whether the point is on arc
	 */
	private static boolean isOnArc(final double dx, final double dy, @NonNull final Arc2D arc)
	{
		final double a = Painter.pointToAngle(dx, dy, arc);
		return arc.containsAngle(Math.toDegrees(a));
	}

	// c o n v e r s i o n

	/**
	 * Compute angle for point
	 *
	 * @param dx  x-offset from arc center
	 * @param dy  y-offset from arc center
	 * @param arc arc
	 * @return computed angle
	 */
	private static double pointToAngle(final double dx, final double dy, @NonNull final Arc2D arc)
	{
		return Math.atan2(-dy * arc.getWidth(), dx * arc.getHeight());
	}

	// l a b e l   h a n d l i n g

	/**
	 * Make text into lines
	 *
	 * @param node node
	 * @return lines
	 */
	@Nullable
	private String[] makeLabel(@NonNull final INode node)
	{
		final String label = node.toString();
		if (label.isEmpty())
		{
			return null;
		}
		if (this.labelMaxLines == 1)
		{
			return new String[]{label.replaceAll("\\n", " ")};
		}
		// multiline
		final String[] result = label.split("\\n", this.labelMaxLines);
		if (this.labelMaxLines > 1 && result.length == this.labelMaxLines)
		{
			result[result.length - 1] = ELLIPSIS;
		}
		return result;
	}

	static private final String ELLIPSIS = "…"; // ellipsis … ⋯

	static private final String AVERAGE_CHAR = "x";

	/**
	 * Ellipsize text in label
	 *
	 * @param nodeData      node data
	 * @param nodeSpaceDiam node space diameter
	 * @return new text width
	 */
	private int ellipsizeLabel(@NonNull final NodeData nodeData, final int nodeSpaceDiam)
	{
		// compute average character width
		int wunit = this.graphics.stringWidth(AVERAGE_CHAR);

		// compute trailing dots width
		int wdots = this.graphics.stringWidth(ELLIPSIS);

		// compute number of characters that fit before dots
		int nChars = (nodeSpaceDiam - wdots) / wunit;

		// ensure at least one
		if (nChars < 1)
		{
			nChars = 1;
		}

		// truncation
		assert nodeData.labelLines != null;
		int len = nodeData.labelLines[0].length();
		if (len > nChars)
		{
			// truncate
			nodeData.labelLines[0] = nodeData.labelLines[0].substring(0, nChars) + ELLIPSIS;

			// recompute label width
			nodeData.labelLinesW[0] = this.graphics.stringWidth(nodeData.labelLines[0]);
		}
		int w = nodeData.labelLinesW[0];

		// extra lines
		if (nodeData.labelLines.length > 1)
		{
			this.graphics.setTextSize(nodeData.textSize * this.labelExtraLineFactor);

			// compute average character width
			wunit = this.graphics.stringWidth(AVERAGE_CHAR);

			// compute trailing dots width
			wdots = this.graphics.stringWidth(ELLIPSIS);

			// compute number of characters that fit before dots
			nChars = (w - wdots) / wunit;

			// ensure at least one
			if (nChars < 1)
			{
				nChars = 1;
			}

			for (int i = 1; i < nodeData.labelLines.length; i++)
			{
				len = nodeData.labelLines[i].length();
				if (len > nChars)
				{
					// truncate
					nodeData.labelLines[i] = nodeData.labelLines[i].substring(0, nChars) + ELLIPSIS;
					nodeData.labelLinesW[i] = this.graphics.stringWidth(nodeData.labelLines[i]);
				}
			}
			this.graphics.setTextSize(nodeData.textSize);
		}
		return w;
	}

	/**
	 * Compute label's width
	 *
	 * @param nodeData node data
	 * @return label's width
	 */
	private int labelWidth(@NonNull final NodeData nodeData)
	{
		assert nodeData.labelLines != null;
		int n = nodeData.labelLines.length;
		nodeData.labelLinesW = new int[n];
		nodeData.labelLinesW[0] = this.graphics.stringWidth(nodeData.labelLines[0]);
		int w = nodeData.labelLinesW[0];

		if (nodeData.labelLines.length > 1)
		{
			this.graphics.setTextSize(nodeData.textSize * this.labelExtraLineFactor);
			for (int i = 1; i < n; i++)
			{
				nodeData.labelLinesW[i] = this.graphics.stringWidth(nodeData.labelLines[i]);
				if (nodeData.labelLinesW[i] > w)
				{
					w = nodeData.labelLinesW[i];
				}
			}
			this.graphics.setTextSize(nodeData.textSize);
		}
		return w;
	}

	/**
	 * Compute label's height
	 *
	 * @param nodeData node data
	 * @return label's height
	 */
	private int labelHeight(@NonNull final NodeData nodeData)
	{
		int h = this.graphics.getAscent(); // +fm.getDescent();
		assert nodeData.labelLines != null;
		if (nodeData.labelLines.length > 1)
		{
			this.graphics.setTextSize(nodeData.textSize * this.labelExtraLineFactor);
			h += this.graphics.getAscent() * (nodeData.labelLines.length - 1);
			this.graphics.setTextSize(nodeData.textSize);
		}
		return h;
	}

	/**
	 * Draw lines in label
	 *
	 * @param nodeData node data
	 */
	private void drawLabel(@NonNull final NodeData nodeData)
	{
		this.graphics.setTextSize(nodeData.textSize);
		int dx = (nodeData.labelW - nodeData.labelLinesW[0]) / 2;
		assert nodeData.labelLines != null;
		this.graphics.drawString(nodeData.labelLines[0], nodeData.labelX + dx, nodeData.labelY);

		if (nodeData.labelLines.length > 1)
		{
			this.graphics.setTextSize(nodeData.textSize * this.labelExtraLineFactor);
			int h = this.graphics.getAscent();
			int dy = h;
			for (int i = 1; i < nodeData.labelLines.length; i++)
			{
				dx = (nodeData.labelW - nodeData.labelLinesW[i]) / 2;
				this.graphics.drawString(nodeData.labelLines[i], nodeData.labelX + dx, nodeData.labelY + dy);
				dy += h;
			}
			this.graphics.setTextSize(nodeData.textSize);
		}
	}
}
