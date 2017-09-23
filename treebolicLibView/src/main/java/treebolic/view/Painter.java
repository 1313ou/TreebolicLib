package treebolic.view;

import java.util.List;

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
		this.theGraphics.drawBackgroundColor(this.theBackColor, this.theLeft, this.theTop, this.theWidth, this.theHeight);

		// background image
		if (this.theBackgroundImage != null)
		{
			this.theGraphics.drawImage(this.theBackgroundImage, this.theLeft, this.theTop, this.theWidth, this.theHeight);
		}
	}

	// D R A W

	@Override
	public void paint(final INode thisRoot, final List<IEdge> thisEdgeList)
	{
		if (this.theZoomFactor != 1F)
		{
			this.theGraphics.scale(this.theZoomFactor, this.theZoomPivotX, this.theZoomPivotY);
		}

		// boundary circle
		//noinspection PointlessBitwiseExpression,ConstantConditions
		if ((Painter.DEBUG & Painter.DEBUG_OUTERCIRCLE) != 0)
		{
			drawCircle(0., 0., 1., Color.LIGHT_GRAY);
		}

		// compute node data
		computeTree(thisRoot);

		// edges
		if (thisEdgeList != null)
		{
			for (final IEdge thisEdge : thisEdgeList)
			{
				drawNonTreeEdge(thisEdge);
			}
		}

		// tree
		drawTree(thisRoot);
	}

	/**
	 * Compute tree recursively
	 *
	 * @param thisNode starting node
	 */
	private void computeTree(final INode thisNode)
	{
		if (thisNode == null)
		{
			return;
		}

		// hyper circle
		final Location thisLocation = thisNode.getLocation();
		if (thisLocation.hyper.isDirty)
		{
			MapperToEuclidean.mapToEuclidean(thisLocation);
		}

		// node data and attach to node
		thisNode.getLocation().theViewData = computeNodeData(thisNode);

		// recurse to compute the children
		final List<INode> theseChildren = thisNode.getChildren();
		if (theseChildren != null)
		{
			for (final INode thisChild : theseChildren)
			{
				computeTree(thisChild);
			}
		}
	}

	/**
	 * Draw tree recursively
	 *
	 * @param thisNode starting node
	 */
	private void drawTree(final INode thisNode)
	{
		if (thisNode == null)
		{
			return;
		}

		// edge to parent
		final INode thisParent = thisNode.getParent();
		if (thisParent != null)
		{
			// color
			Color thisColor = thisNode.getEdgeColor();
			if (thisColor == null)
			{
				thisColor = this.theTreeEdgeColor;
			}
			this.theGraphics.setColor(thisColor);

			// draw
			drawTreeEdge(thisParent, thisNode);
		}

		// recurse to draw the children
		final List<INode> theseChildren = thisNode.getChildren();
		if (theseChildren != null)
		{
			for (final INode thisChild : theseChildren)
			{
				drawTree(thisChild);
			}
		}

		// debug
		//noinspection PointlessBitwiseExpression,ConstantConditions
		if ((Painter.DEBUG & Painter.DEBUG_NODECIRCLE) != 0)
		{
			drawSpace(thisNode);
		}
		//noinspection PointlessBitwiseExpression,ConstantConditions
		if ((Painter.DEBUG & Painter.DEBUG_NONODE) != 0)
		{
			return;
		}

		// draw node
		final Location thisLocation = thisNode.getLocation();
		if (!thisLocation.hyper.isBorder)
		{
			drawNode((NodeData) thisNode.getLocation().theViewData);
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
		public Rectangle2D theSpace;

		/**
		 * Node label box
		 */
		public Rectangle2D theBox;

		/**
		 * Node label lines
		 */
		public String[] theLabelLines;

		/**
		 * Node label lines' width
		 */
		public int[] theLabelLinesW;

		/**
		 * Node label width
		 */
		public int theLabelW;

		/**
		 * Node label x coordinate
		 */
		public int theLabelX;

		/**
		 * Node label y coordinate
		 */
		public int theLabelY;

		/**
		 * Node image
		 */
		public Image theImage;

		/**
		 * Node image x-coordinate
		 */
		public int theImageX;

		/**
		 * Node image y-coordinate
		 */
		public int theImageY;

		/**
		 * Node image width
		 */
		public int theImageWidth;

		/**
		 * Node image height
		 */
		public int theImageHeight;

		/**
		 * Node backcolor
		 */
		public Color theBackColor;

		/**
		 * Node forecolor
		 */
		public Color theForeColor;

		/**
		 * Node text size
		 */
		public float theTextSize;

		/**
		 * Is mountable
		 */
		public Boolean isMountable;
	}

	/**
	 * Draw node
	 *
	 * @param thisNodeData computed node data
	 */
	@SuppressWarnings("boxing")
	private void drawNode(final NodeData thisNodeData)
	{
		if (thisNodeData == null)
		{
			return;
		}

		// box
		if (thisNodeData.theBox != null)
		{
			// rectangle
			final int x = (int) thisNodeData.theBox.getX();
			final int y = (int) thisNodeData.theBox.getY();
			final int w = (int) thisNodeData.theBox.getWidth();
			final int h = (int) thisNodeData.theBox.getHeight();
			final int rx = 10;
			final int ry = 10;

			// fill
			//noinspection PointlessBitwiseExpression,ConstantConditions
			if ((Painter.DEBUG & Painter.DEBUG_NOLABELFILL) == 0)
			{
				this.theGraphics.setColor(thisNodeData.theBackColor);
				this.theGraphics.fillRoundRectangle(x, y, w, h, rx, ry);
			}

			// foreground color
			this.theGraphics.setColor(thisNodeData.theForeColor);

			// outline
			if (this.border)
			{
				this.theGraphics.drawRoundRectangle(x, y, w, h, rx, ry);
			}

			// mount clue
			if (thisNodeData.isMountable != null)
			{
				final int w0 = 3;
				final int h0 = 2 * w0;
				final int hm = 2;
				final int x0 = x + w + 5;
				final int x1 = x0 - w0;
				final int x2 = x0 + w0;
				final int y0 = y + hm;
				final int xs[] = {x1, x0, x2};
				if (thisNodeData.isMountable)
				{
					final int hr = h - 2 * hm;
					final int y2 = y0 + hr;
					final int y1 = y2 - h0;
					final int ys[] = {y1, y2, y1};
					this.theGraphics.fillPolygon(xs, ys, 3);
				}
				else
				{
					final int y1 = y0 + h0;
					final int ys[] = {y1, y0, y1};
					this.theGraphics.fillPolygon(xs, ys, 3);
				}
			}
		}

		// image
		//noinspection PointlessBitwiseExpression,ConstantConditions
		if (thisNodeData.theImage != null && (Painter.DEBUG & Painter.DEBUG_NOIMAGE) == 0)
		{
			if (thisNodeData.theImageWidth >= Painter.MIN_IMAGE_DIMENSION && thisNodeData.theImageHeight >= Painter.MIN_IMAGE_DIMENSION)
			{
				if (this.downscaleImages)
				{
					this.theGraphics.drawImage(thisNodeData.theImage, thisNodeData.theImageX, thisNodeData.theImageY, thisNodeData.theImageWidth, thisNodeData.theImageHeight);
				}
				else
				{
					this.theGraphics.drawImage(thisNodeData.theImage, thisNodeData.theImageX, thisNodeData.theImageY);
				}
			}
		}

		// label
		//noinspection PointlessBitwiseExpression,ConstantConditions
		if ((Painter.DEBUG & Painter.DEBUG_NOLABEL) != 0)
		{
			return;
		}
		if (thisNodeData.theLabelLines != null)
		{
			drawLabel(thisNodeData);
		}
	}

	/**
	 * Compute node data
	 *
	 * @param thisNode node
	 * @return node data
	 */
	@SuppressWarnings({"synthetic-access", "boxing"})
	private NodeData computeNodeData(final INode thisNode)
	{
		final NodeData thisNodeData = new NodeData();

		// hyper circle
		final Location thisLocation = thisNode.getLocation();
		if (thisLocation.hyper.isBorder)
		{
			return null;
		}

		// text size
		final float thisTextSize = hyperdistanceToSize(thisLocation.hyper.dist);
		thisNodeData.theTextSize = thisTextSize;
		this.theGraphics.setTextSize(thisTextSize);

		// color
		thisNodeData.theBackColor = thisNode.getBackColor();
		if (thisNodeData.theBackColor == null)
		{
			thisNodeData.theBackColor = this.theNodeBackColor;
		}
		thisNodeData.theForeColor = thisNode.getForeColor();
		if (thisNodeData.theForeColor == null)
		{
			thisNodeData.theForeColor = this.theNodeForeColor;
		}

		// center
		final int xnode = xUnitCircleToView(thisLocation.euclidean.center.re);
		final int ynode = yUnitCircleToView(thisLocation.euclidean.center.im);

		// node space actual diameter
		final int rnode = wUnitCircleToView(thisLocation.euclidean.radius);
		final int dnode = 2 * rnode;

		// image
		thisNodeData.theImage = thisNode.getImage();
		thisNodeData.theImageWidth = 0;
		thisNodeData.theImageHeight = 0;
		if (thisNodeData.theImage == null)
		{
			thisNodeData.theImage = this.theDefaultNodeImage;
		}
		if (thisNodeData.theImage != null)
		{
			// scale down as per hyperbolic distance
			final int thisImageScaleIndex = Math.min(this.theImageDownscaler.length - 1, (int) Math.round(thisLocation.hyper.dist * this.theImageDownscaler.length));
			double thisImageScale = this.downscaleImages ? this.theImageDownscaler[thisImageScaleIndex] : 1.F;
			if (thisImageScale == -1F)
			{
				thisImageScale = 1.F - thisLocation.hyper.dist;
			}
			thisImageScale *= this.theImageScaleFactor;
			thisNodeData.theImageWidth = (int) (thisImageScale * thisNodeData.theImage.getWidth());
			thisNodeData.theImageHeight = (int) (thisImageScale * thisNodeData.theImage.getHeight());
		}

		// string
		thisNodeData.theLabelLines = makeLabel(thisNode);
		if (thisNodeData.theLabelLines == null)
		{
			// no label
			if (thisNodeData.theImage != null)
			{
				final int hi = thisNodeData.theImageHeight;
				final int xi = xnode - thisNodeData.theImageWidth / 2;
				final int yi = ynode - hi / 2;

				thisNodeData.theImageX = xi;
				thisNodeData.theImageY = yi;
			}
			return thisNodeData;
		}

		// label dimensions
		thisNodeData.theLabelW = labelWidth(thisNodeData);
		if (thisNodeData.theLabelW > dnode && this.ellipsize)
		{
			// ellipsize label
			thisNodeData.theLabelW = ellipsizeLabel(thisNodeData, dnode);
		}
		final int htext = labelHeight(thisNodeData);

		// box computation
		final int wbox = thisNodeData.theLabelW + 2 * Painter.NODE_HORIZONTAL_PADDING;
		final int hbox = Painter.NODE_TOP_PADDING + htext + this.theGraphics.getDescent() + Painter.NODE_BOTTOM_PADDING;
		final int xbox = xnode - thisNodeData.theLabelW / 2 - Painter.NODE_HORIZONTAL_PADDING;
		int ybox;

		// image computation
		if (thisNodeData.theImage == null)
		{
			ybox = ynode - htext / 2 - Painter.NODE_TOP_PADDING;
			thisNodeData.theSpace = null;
		}
		else
		{
			// image is horizontally centered on node's focus point
			thisNodeData.theImageX = xnode - thisNodeData.theImageWidth / 2;

			// compute combined height of image and label (minus overlay of label)
			final int overlap = (int) (this.theGraphics.getAscent() * Painter.NODE_LABEL_OVERLAY);
			final int hcombined = thisNodeData.theImageHeight + hbox - overlap;
			final int hcombined2 = (int) (hcombined / 2F);

			// combination is centered on node's focus

			// 1) box is placed on node's focus, pushed down to bottom of combination (half height of image-label combination) and raised by its height
			ybox = ynode + hcombined2 - hbox;

			// 2) image data is placed on node's focus and raised to top of combination (half height of image-label combination)
			thisNodeData.theImageY = ynode - hcombined2;

			// space
			int xspace = xbox;
			int wspace = wbox;
			if (thisNodeData.theImageWidth > wbox)
			{
				xspace = thisNodeData.theImageX;
				wspace = thisNodeData.theImageWidth;
			}
			thisNodeData.theSpace = new Rectangle2D(xspace, thisNodeData.theImageY, wspace, hcombined);
		}

		// label box
		thisNodeData.theBox = new Rectangle2D(xbox, ybox, wbox, hbox);

		// label
		thisNodeData.theLabelX = xbox + Painter.NODE_HORIZONTAL_PADDING;
		thisNodeData.theLabelY = ybox + Painter.NODE_TOP_PADDING + this.theGraphics.getAscent();

		// is mountable
		final MountPoint thisMountPoint = thisNode.getMountPoint();
		thisNodeData.isMountable = thisMountPoint == null ? null : thisMountPoint instanceof MountPoint.Mounting;

		return thisNodeData;
	}

	// D R A W . E D G E

	/**
	 * Draw tree edge, from parent to child
	 *
	 * @param thisParent from-node
	 * @param thisNode   to-node
	 */
	private void drawTreeEdge(final INode thisParent, final INode thisNode)
	{
		// style
		final Integer thatStyle = thisNode.getEdgeStyle();
		final int thisStyle = mergeStyles(this.theTreeEdgeStyle, thatStyle);
		if ((thisStyle & IEdge.HIDDEN) != 0) // defined and hidden
		{
			return;
		}

		// hyper circles
		final Location thisFrom = thisParent.getLocation();
		final Location thisTo = thisNode.getLocation();

		// space
		final NodeData thisFromData = (NodeData) thisParent.getLocation().theViewData;
		final NodeData thisToData = (NodeData) thisNode.getLocation().theViewData;
		Rectangle2D thisFromSpace = null;
		if (thisFromData != null)
		{
			thisFromSpace = thisFromData.theSpace != null ? thisFromData.theSpace : thisFromData.theBox;
		}
		Rectangle2D thisToSpace = null;
		if (thisToData != null)
		{
			thisToSpace = thisToData.theSpace != null ? thisToData.theSpace : thisToData.theBox;
		}

		// do not draw edge if boxes intersect
		if (Painter.boxesIntersect(thisFromSpace, thisToSpace))
		{
			return;
		}

		// image
		Image thisImage = thisNode.getEdgeImage();
		float thisImageScale = 1.F;
		if (thisImage == null)
		{
			thisImage = this.theDefaultTreeEdgeImage;
		}
		if (thisImage != null)
		{
			final Location thisLocation = thisNode.getLocation();
			final int thisImageScaleIndex = Math.min(this.theImageDownscaler.length - 1, (int) Math.round(thisLocation.hyper.dist * this.theImageDownscaler.length));
			thisImageScale = this.downscaleImages ? this.theImageDownscaler[thisImageScaleIndex] : 1.F;
			if (thisImageScale == -1F)
			{
				thisImageScale = 1.F - (float) thisLocation.hyper.dist;
			}
			thisImageScale *= this.theImageScaleFactor;
		}

		// draw
		final String thisLabel = thisNode.getEdgeLabel();
		final boolean isBorder = thisFrom.hyper.isBorder;
		drawEdge(thisFrom.euclidean.center, thisTo.euclidean.center, thisLabel, thisImage, thisImageScale, thisStyle, thisFromSpace, thisToSpace, isBorder);
	}

	/**
	 * Draw non-tree edge
	 *
	 * @param thisEdge edge
	 */
	private void drawNonTreeEdge(final IEdge thisEdge)
	{
		// style
		final Integer thatStyle = thisEdge.getStyle();
		final int thisStyle = mergeStyles(this.theEdgeStyle, thatStyle);
		if ((thisStyle & IEdge.HIDDEN) != 0) // defined and hidden
		{
			return;
		}

		// nodes
		INode thisFromNode = thisEdge.getFrom();
		INode thisToNode = thisEdge.getTo();
		if (thisFromNode == null || thisToNode == null)
		{
			return;
		}
		thisFromNode = MountPoint.follow(thisFromNode, false, true);
		thisToNode = MountPoint.follow(thisToNode, false, true);

		// hyper circles
		final Location thisFromLocation = thisFromNode.getLocation();
		final Location thisToLocation = thisToNode.getLocation();
		// if (thisFromLocation.hyper.isBorder || thisToLocation.hyper.isBorder)
		// return;

		if (thisFromLocation.hyper.isDirty)
		{
			MapperToEuclidean.mapToEuclidean(thisFromLocation);
		}
		if (thisToLocation.hyper.isDirty)
		{
			MapperToEuclidean.mapToEuclidean(thisToLocation);
		}

		// space
		final NodeData thisFromData = (NodeData) thisFromNode.getLocation().theViewData;
		final NodeData thisToData = (NodeData) thisToNode.getLocation().theViewData;
		Rectangle2D thisFromSpace = null;
		if (thisFromData != null)
		{
			thisFromSpace = thisFromData.theSpace != null ? thisFromData.theSpace : thisFromData.theBox;
		}
		Rectangle2D thisToSpace = null;
		if (thisToData != null)
		{
			thisToSpace = thisToData.theSpace != null ? thisToData.theSpace : thisToData.theBox;
		}

		// do not draw edge if boxes intersect
		if (Painter.boxesIntersect(thisFromSpace, thisToSpace))
		{
			return;
		}

		// label
		final String thisLabel = thisEdge.getLabel();
		if (thisLabel != null)
		{
			// text size
			final float thisTextSize = hyperdistanceToSize(thisToLocation.hyper.dist);
			this.theGraphics.setTextSize(thisTextSize);
		}

		// draw
		Color thisColor = thisEdge.getColor();
		if (thisColor == null)
		{
			thisColor = this.theEdgeColor;
		}
		this.theGraphics.setColor(thisColor);

		// image
		Image thisImage = thisEdge.getImage();
		if (thisImage == null)
		{
			thisImage = this.theDefaultEdgeImage;
		}

		// image scaling
		float thisImageScale = 1.F;
		if (thisImage != null && this.downscaleImages)
		{
			// font size
			final int thisScaleIdx = Math.min(this.theImageDownscaler.length - 1, (int) Math.round(thisToLocation.hyper.dist * this.theImageDownscaler.length));
			thisImageScale = this.theImageDownscaler[thisScaleIdx];
			if (thisImageScale == -1F)
			{
				thisImageScale = 1.F - (float) thisToLocation.hyper.dist;
			}
			thisImageScale *= this.theImageScaleFactor;
		}

		// draw arc
		drawEdge(thisFromLocation.euclidean.center, thisToLocation.euclidean.center, thisLabel, thisImage, thisImageScale, thisStyle, thisFromSpace, thisToSpace, false);
	}

	/**
	 * Draw edge from z1 to z2
	 *
	 * @param z1             from-end
	 * @param z2             to-end
	 * @param thisLabel      arc label
	 * @param thisImage      arc image
	 * @param thisImageScale image scale
	 * @param thisStyle      code for edge style
	 * @param thisFromSpace  from-node space
	 * @param thisToSpace    to-node space
	 * @param isBorder       true if arc neighbours border
	 */
	private void drawEdge(final Complex z1, final Complex z2, final String thisLabel, final Image thisImage, final float thisImageScale, final int thisStyle, final Rectangle2D thisFromSpace, final Rectangle2D thisToSpace, final boolean isBorder)
	{
		if (Painter.STRAIGHT_EDGE_WHILE_MOVING && this.isDragging || !this.arcEdges || (thisStyle & IEdge.LINE) != 0)
		{
			drawLine(z1, z2, thisLabel, thisImage, thisImageScale, thisStyle, thisFromSpace, thisToSpace, isBorder);
		}
		else
		{
			drawArc(z1, z2, thisLabel, thisImage, thisImageScale, thisStyle, thisFromSpace, thisToSpace, isBorder);
		}
	}

	/**
	 * Get font size from hyperdistance of node
	 *
	 * @param thisHyperDistance hyperdistance of node
	 * @return font size
	 */
	private float hyperdistanceToSize(final double thisHyperDistance)
	{
		final int thisBucket = Math.min(this.theFontDownscaler.length - 1, (int) Math.round(thisHyperDistance * this.theFontDownscaler.length));
		return this.theFontSize * this.theFontScaleFactor * this.theFontDownscaler[thisBucket];
	}

	// D R A W . A R C

	/**
	 * Draw geodesic arc from z1 to z2 which models line from z1 to z2
	 *
	 * @param z1             from-end
	 * @param z2             to-end
	 * @param thatLabel      arc label
	 * @param thisImage      arc image
	 * @param thisImageScale image scale
	 * @param thisStyle      style
	 * @param thisFromSpace  from-node space
	 * @param thisToSpace    to-node space
	 * @param isBorder       true if arc neighbours border
	 */
	private void drawArc(final Complex z1, final Complex z2, final String thatLabel, final Image thisImage, final float thisImageScale, final int thisStyle, final Rectangle2D thisFromSpace, final Rectangle2D thisToSpace, final boolean isBorder)
	{
		final Arc thisArc = new Arc(z1, z2);

		String thisLabel = thatLabel;

		// if(r == 0.) it is segment of line
		if (thisArc.r == 0.)
		{
			Point2D thisFrom = new Point2D(xUnitCircleToView(thisArc.from.re), yUnitCircleToView(thisArc.from.im));
			Point2D thisTo = new Point2D(xUnitCircleToView(thisArc.to.re), yUnitCircleToView(thisArc.to.im));

			// adjust to anchors
			Point2D thisFromAnchor = null;
			if (!isBorder)
			{
				thisFromAnchor = getIntersection(thisFromSpace, thisTo, thisFrom);
			}
			//noinspection UnusedAssignment
			Point2D thisToAnchor = null;
			thisToAnchor = getIntersection(thisToSpace, thisFrom, thisTo);

			// adjust line ends
			if (thisFromAnchor != null)
			{
				thisFrom = thisFromAnchor;
			}
			if (thisToAnchor != null)
			{
				thisTo = thisToAnchor;
			}

			// line
			drawLine(thisFrom, thisTo, thisStyle);

			// image
			if (thisImage != null && !isBorder)
			{
				final Point2D thisMidPoint = Painter.getMidPoint(thisFrom, thisTo);
				drawImage(thisImage, thisMidPoint, thisImageScale);
			}

			// ends
			drawEdgeEnds(thisFrom, thisTo, null, thisStyle);

			// label
			if (!isBorder && thisLabel != null && !thisLabel.isEmpty())
			{
				// fit
				thisLabel = mangleString(thisLabel, thisFrom, thisTo);
				if (thisLabel == null)
				{
					return;
				}

				drawText(thisLabel, thisFrom, thisTo);
			}
		}
		else
		{
			final Arc2D thisArc2D = toArc2D(thisArc);

			// adjust to anchors
			Point2D thisFromAnchor = null;
			if (!isBorder)
			{
				thisFromAnchor = getIntersection(thisFromSpace, thisArc2D);
			}
			if (thisFromAnchor == null)
			{
				thisFromAnchor = thisArc2D.getStartPoint();
			}

			//noinspection UnusedAssignment
			Point2D thisToAnchor = null;
			thisToAnchor = getIntersection(thisToSpace, thisArc2D);
			if (thisToAnchor == null)
			{
				thisToAnchor = thisArc2D.getEndPoint();
			}

			// drawPoint(thisArc2D.getStartPoint(), Color.MAGENTA);
			// drawPoint(thisArc2D.getEndPoint(), Color.MAGENTA);
			// drawPoint(thisFromAnchor, Color.YELLOW);
			// drawPoint(thisToAnchor, Color.YELLOW);

			// adjust arc ends to anchors
			if (thisArc2D.getAngleExtent() >= 0.)
			{
				thisArc2D.setAngles(thisFromAnchor, thisToAnchor);
			}
			else
			{
				thisArc2D.setAngles(thisToAnchor, thisFromAnchor);
			}
			// drawPoint(thisArc2D.getStartPoint(), Color.GREEN);
			// drawPoint(thisArc2D.getEndPoint(), Color.GREEN);

			// draw
			drawArc(thisArc2D, thisFromAnchor, thisToAnchor, thisStyle);

			// image
			if (thisImage != null)
			{
				drawImage(thisImage, ArcMath.getMidArc(thisArc2D), thisImageScale);
			}

			// ends
			drawEdgeEnds(thisFromAnchor, thisToAnchor, thisArc2D, thisStyle);

			// draw edge label
			if (!isBorder && thisLabel != null && !thisLabel.isEmpty())
			{
				// fit
				thisLabel = mangleString(thisLabel, thisFromAnchor, thisToAnchor);
				if (thisLabel == null)
				{
					return;
				}

				// mid arc
				final Point2D thisMidArc = ArcMath.getMidArc(thisArc2D);

				// tangent
				final double thisTangent = ArcMath.getTextTangent(thisArc2D, thisMidArc);

				// draw text
				drawText(thisLabel, thisMidArc, thisTangent);
			}
		}
	}

	/**
	 * Draw arc2D
	 *
	 * @param thisArc2D      arc
	 * @param thisFromAnchor from-anchor
	 * @param thisToAnchor   to-anchor
	 */
	private void drawArc(final Arc2D thisArc2D, @SuppressWarnings("UnusedParameters") final Point2D thisFromAnchor, @SuppressWarnings("UnusedParameters") final Point2D thisToAnchor, final int thisStyle)
	{
		final int x = (int) thisArc2D.x;
		final int y = (int) thisArc2D.y;
		final int w = (int) thisArc2D.width;
		final int h = (int) thisArc2D.height;
		final float start = (float) thisArc2D.start;
		final float extent = (float) thisArc2D.extent;

		if ((thisStyle & (IEdge.STROKEMASK | IEdge.STROKEWIDTHMASK)) != 0)
		{
			int thisStrokeValue;
			int thisWidth = (thisStyle & IEdge.STROKEWIDTHMASK) >> IEdge.STROKEWIDTHSHIFT;
			int thisStroke = (thisStyle & IEdge.STROKEMASK);
			switch (thisStroke)
			{
				case IEdge.DASH:
					thisStrokeValue = treebolic.glue.iface.Graphics.DASH;
					break;
				case IEdge.DOT:
					thisStrokeValue = treebolic.glue.iface.Graphics.DOT;
					break;
				case IEdge.SOLID:
				default:
					thisStrokeValue = treebolic.glue.iface.Graphics.SOLID;
					break;
			}

			if (thisWidth != 0)
			{
				this.theGraphics.pushStroke();
				this.theGraphics.setStroke(thisStrokeValue, thisWidth);
				this.theGraphics.drawArc(x, y, w, h, start, extent);
				this.theGraphics.popStroke();
				return;
			}
		}
		this.theGraphics.drawArc(x, y, w, h, start, extent);
	}

	/**
	 * Convert Arc to Arc2D
	 *
	 * @param thisArc arc
	 * @return arc
	 */
	private Arc2D toArc2D(final Arc thisArc)
	{
		final Arc2D thisArc2D = new Arc2D();

		// frame
		final double x0 = xUnitCircleToView(thisArc.x);
		final double y0 = yUnitCircleToView(thisArc.y);
		final double x = xUnitCircleToView(thisArc.x - thisArc.r);
		final double y = yUnitCircleToView(thisArc.y - thisArc.r);
		thisArc2D.setFrameFromCenter(x0, y0, x, y);

		// start (reversing top/down)
		double thisStart = Math.toDegrees(-thisArc.start);
		if (thisStart < 0.)
		{
			thisStart += 360.;
		}
		thisArc2D.setAngleStart(thisStart);

		// extent (reversing top/down)
		final double thisExtent = Math.toDegrees(-thisArc.angle);
		thisArc2D.setAngleExtent(thisExtent);

		// rotation direction later lost in normalizations
		thisArc2D.setCounterclockwise(thisArc.counterclockwise());

		return thisArc2D;
	}

	// D R A W . L I N E

	/**
	 * Draw line
	 *
	 * @param z1             from
	 * @param z2             to
	 * @param thatLabel      label
	 * @param thisImage      image
	 * @param thisImageScale image scale
	 * @param thisStyle      style
	 * @param thisFromSpace  from-node space
	 * @param thisToSpace    to-node-space
	 * @param isBorder       true if is nearing border
	 */
	private void drawLine(final Complex z1, final Complex z2, final String thatLabel, final Image thisImage, final float thisImageScale, final int thisStyle, final Rectangle2D thisFromSpace, final Rectangle2D thisToSpace, final boolean isBorder)
	{
		Point2D thisFrom = new Point2D(xUnitCircleToView(z1.re), yUnitCircleToView(z1.im));
		Point2D thisTo = new Point2D(xUnitCircleToView(z2.re), yUnitCircleToView(z2.im));

		// adjust to anchors
		Point2D thisFromAnchor = null;
		if (!isBorder)
		{
			thisFromAnchor = getIntersection(thisFromSpace, thisTo, thisFrom);
		}
		//noinspection UnusedAssignment
		Point2D thisToAnchor = null;
		thisToAnchor = getIntersection(thisToSpace, thisFrom, thisTo);

		// adjust line ends
		if (thisFromAnchor != null)
		{
			thisFrom = thisFromAnchor;
		}
		if (thisToAnchor != null)
		{
			thisTo = thisToAnchor;
		}

		// line
		drawLine(thisFrom, thisTo, thisStyle);

		// image
		if (thisImage != null && !isBorder)
		{
			final Point2D thisMidPoint = Painter.getMidPoint(thisFrom, thisTo);
			drawImage(thisImage, thisMidPoint, thisImageScale);
		}

		// ends
		drawEdgeEnds(thisFrom, thisTo, null, thisStyle);

		// label
		String thisLabel = thatLabel;
		if (!isBorder && thisLabel != null && !thisLabel.isEmpty())
		{
			// fit
			thisLabel = mangleString(thisLabel, thisFrom, thisTo);
			if (thisLabel == null)
			{
				return;
			}

			drawText(thisLabel, thisFrom, thisTo);
		}
	}

	/**
	 * Draw line from p1 to p2
	 *
	 * @param thisFromPoint from-point
	 * @param thisToPoint   to-point
	 */
	private void drawLine(final Point2D thisFromPoint, final Point2D thisToPoint, final int thisStyle)
	{
		if ((thisStyle & (IEdge.STROKEMASK | IEdge.STROKEWIDTHMASK)) != 0)
		{
			int thisStrokeValue;
			int thisWidth = (thisStyle & IEdge.STROKEWIDTHMASK) >> IEdge.STROKEWIDTHSHIFT;
			int thisStroke = (thisStyle & IEdge.STROKEMASK);
			switch (thisStroke)
			{
				case IEdge.DASH:
					thisStrokeValue = treebolic.glue.iface.Graphics.DASH;
					break;
				case IEdge.DOT:
					thisStrokeValue = treebolic.glue.iface.Graphics.DOT;
					break;
				case IEdge.SOLID:
				default:
					thisStrokeValue = treebolic.glue.iface.Graphics.SOLID;
					break;
			}

			if (thisWidth != 0)
			{
				this.theGraphics.pushStroke();
				this.theGraphics.setStroke(thisStrokeValue, thisWidth);
				this.theGraphics.drawLine((int) thisFromPoint.getX(), (int) thisFromPoint.getY(), (int) thisToPoint.getX(), (int) thisToPoint.getY());
				this.theGraphics.popStroke();
				return;
			}
		}
		this.theGraphics.drawLine((int) thisFromPoint.getX(), (int) thisFromPoint.getY(), (int) thisToPoint.getX(), (int) thisToPoint.getY());
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
	//	 * @param thisColor
	//	 *        color
	//	 */
	//	private void drawPoint(double x, double y, final Color thisColor)
	//	{
	//		int d = 10;
	//		int thisWidth = 3;
	//		this.theGraphics.pushStroke();
	//		this.theGraphics.setStroke(treebolic.glue.iface.Graphics.SOLID, thisWidth);
	//		final Color thatColor = this.theGraphics.getColor();
	//		this.theGraphics.setColor(thisColor);
	//		this.theGraphics.drawLine((int) x - d, (int) y, (int) x + d, (int) y);
	//		this.theGraphics.drawLine((int) x, (int) y - d, (int) x, (int) y + d);
	//		this.theGraphics.setColor(thatColor);
	//		this.theGraphics.popStroke();
	//	}
	// @formatter:on

	// D R A W . T E X T

	/**
	 * Draw text
	 *
	 * @param thisString      string to be drawn
	 * @param thisWhere       where to put text (centered on this point)
	 * @param thatOrientation text orientation
	 */
	private void drawText(final String thisString, final Point2D thisWhere, final double thatOrientation)
	{
		double thisOrientation = thatOrientation;

		// save current transform
		this.theGraphics.pushMatrix();

		// font metrics
		final int thisWidth = this.theGraphics.stringWidth(thisString);

		// text orientation
		final boolean reverse = thisOrientation > Math.PI / 2.;
		//noinspection UnusedAssignment
		int thisYShift = 0;
		if (reverse)
		{
			thisOrientation += Math.PI;
			thisYShift = -this.theGraphics.getAscent();
		}
		else
		{
			thisYShift = this.theGraphics.getDescent();
		}

		// translate to center of text
		this.theGraphics.rotate((float) thisOrientation, (float) thisWhere.getX(), (float) thisWhere.getY());
		this.theGraphics.drawString(thisString, -thisWidth / 2, -thisYShift);

		// restore transform
		this.theGraphics.popMatrix();
	}

	/**
	 * Draw edge text from (x1,y1) to (x2,y2)
	 *
	 * @param thisString text
	 * @param thisFrom   from-point
	 * @param thisTo     to-point
	 */
	private void drawText(final String thisString, final Point2D thisFrom, final Point2D thisTo)
	{
		final double x1 = thisFrom.getX();
		final double y1 = thisFrom.getY();
		final double x2 = thisTo.getX();
		final double y2 = thisTo.getY();

		// orientation
		double thisOrientation = Math.atan2(y1 - y2, x1 - x2);
		if (thisOrientation < 0)
		{
			thisOrientation += Math.PI;
		}

		// where
		final Point2D thisWhere = new Point2D((x2 + x1) / 2., (y2 + y1) / 2.);

		// draw
		drawText(thisString, thisWhere, thisOrientation);
	}

	/**
	 * Mangle string to fit in
	 *
	 * @param thatString string
	 * @param thisFrom   from-point
	 * @param thisTo     to-point
	 * @return mangled string or null
	 */
	private String mangleString(final String thatString, final Point2D thisFrom, final Point2D thisTo)
	{
		String thisString = thatString;
		final double cx = thisTo.getX() - thisFrom.getX();
		final double cy = thisTo.getY() - thisFrom.getY();
		final int span = (int) Math.sqrt(cx * cx + cy * cy) - Painter.TEXT_PADDING;

		// label font size
		final int w = this.theGraphics.stringWidth(thisString);

		// ellipsize label
		if (w > span)
		{
			// compute average character width
			final int wunit = this.theGraphics.stringWidth("x");

			// compute trailing dots width
			final int wdots = this.theGraphics.stringWidth("...");

			// compute number of characters that fit before dots
			int thisNChars = (span - wdots) / wunit;

			// ensure at least one
			if (thisNChars < 1)
			{
				thisNChars = 1;
			}

			// perform truncation if we actually ellipsize
			final int thisLen = thisString.length();
			if (thisLen > thisNChars)
			{
				thisString = thisString.substring(0, thisNChars) + "..."; // …⋯
			}
		}
		return thisString;
	}

	// D R A W . I M A G E

	/**
	 * Draw image at point (scaled)
	 *
	 * @param thisImage      image
	 * @param where          location
	 * @param thisImageScale image scale
	 */
	private void drawImage(final Image thisImage, final Point2D where, final float thisImageScale)
	{
		if (thisImage != null)
		{
			if (thisImageScale == 1.F || !this.downscaleImages)
			{
				final int w = thisImage.getWidth();
				final int h = thisImage.getHeight();
				if (w < Painter.MIN_IMAGE_DIMENSION || h < Painter.MIN_IMAGE_DIMENSION)
				{
					return;
				}
				final int x = (int) (where.getX() - w / 2);
				final int y = (int) (where.getY() - h / 2);
				this.theGraphics.drawImage(thisImage, x, y);
			}
			else
			{
				final int w = (int) (thisImageScale * thisImage.getWidth());
				final int h = (int) (thisImageScale * thisImage.getHeight());
				if (w < Painter.MIN_IMAGE_DIMENSION || h < Painter.MIN_IMAGE_DIMENSION)
				{
					return;
				}
				final int x = (int) (where.getX() - w / 2);
				final int y = (int) where.getY() - h / 2;
				this.theGraphics.drawImage(thisImage, x, y, w, h);
			}
		}
	}

	// D R A W . E D G E E N D S

	/**
	 * Draw edge ends
	 *
	 * @param thisFrom  from-end
	 * @param thisTo    to-end
	 * @param thisArc2D arc
	 * @param thisStyle style code
	 */
	private void drawEdgeEnds(final Point2D thisFrom, final Point2D thisTo, final Arc2D thisArc2D, final int thisStyle)
	{
		if (thisStyle == 0)
		{
			return;
		}

		int thisStrokeWidth = (thisStyle & IEdge.STROKEWIDTHMASK) >> IEdge.STROKEWIDTHSHIFT;
		double thisTerminatorHeight = Painter.TERMINATOR_HEIGHT + thisStrokeWidth;
		double thisTerminatorWidth = Painter.TERMINATOR_WIDTH + thisStrokeWidth;

		final double x1 = thisFrom.getX();
		final double y1 = thisFrom.getY();
		final double x2 = thisTo.getX();
		final double y2 = thisTo.getY();
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
		if (thisArc2D == null)
		{
			final double thisOrientation = Math.atan2(y2 - y1, x2 - x1);
			sx1 = thisTerminatorHeight * Math.cos(thisOrientation);
			sy1 = thisTerminatorHeight * Math.sin(thisOrientation);
			dx1 = thisTerminatorWidth * Math.cos(thisOrientation + Math.PI / 2);
			dy1 = thisTerminatorWidth * Math.sin(thisOrientation + Math.PI / 2);
			sx2 = -sx1;
			sy2 = -sy1;
			dx2 = dx1;
			dy2 = dy1;
		}
		else
		{
			final double thisOrientation1 = ArcMath.getTangent(thisArc2D, thisFrom, true);
			final double thisOrientation2 = ArcMath.getTangent(thisArc2D, thisTo, false);

			// {
			// this.theGraphics.setColor(Color.GREEN);
			// drawOrientation(x1, y1, thisOrientation1);
			// this.theGraphics.setColor(Color.MAGENTA);
			// drawOrientation(x2, y2, thisOrientation2);
			// }

			sx1 = thisTerminatorHeight * Math.cos(thisOrientation1);
			sy1 = thisTerminatorHeight * Math.sin(thisOrientation1);
			dx1 = thisTerminatorWidth * Math.cos(thisOrientation1 + Math.PI / 2);
			dy1 = thisTerminatorWidth * Math.sin(thisOrientation1 + Math.PI / 2);

			sx2 = thisTerminatorHeight * Math.cos(thisOrientation2);
			sy2 = thisTerminatorHeight * Math.sin(thisOrientation2);
			dx2 = thisTerminatorWidth * Math.cos(thisOrientation2 + Math.PI / 2);
			dy2 = thisTerminatorWidth * Math.sin(thisOrientation2 + Math.PI / 2);
		}

		// from
		int thisEndStyle = thisStyle >> IEdge.FROMSHIFT & IEdge.SHAPEMASK;
		switch (thisEndStyle)
		{
			case IEdge.TRIANGLE:
			{
				// t
				final int x[] = {(int) x1, (int) (x1 + sx1 + dx1), (int) (x1 + sx1 - dx1)};
				final int y[] = {(int) y1, (int) (y1 + sy1 + dy1), (int) (y1 + sy1 - dy1)};
				if ((thisStyle & IEdge.FROMFILL) != 0)
				{
					this.theGraphics.fillPolygon(x, y, x.length);
				}
				else
				{
					final Color thatColor = this.theGraphics.getColor();
					this.theGraphics.setColor(this.theBackColor);
					this.theGraphics.fillPolygon(x, y, x.length);
					this.theGraphics.setColor(thatColor);
					this.theGraphics.drawPolygon(x, y, x.length);
				}
				break;
			}

			case IEdge.CIRCLE:
			{
				// c
				final double x = x1 + sx1 / 2 - thisTerminatorHeight / 2;
				final double y = y1 + sy1 / 2 - thisTerminatorHeight / 2;
				if ((thisStyle & IEdge.FROMFILL) != 0)
				{
					this.theGraphics.fillOval((int) x, (int) y, (int) thisTerminatorHeight, (int) thisTerminatorHeight);
				}
				else
				{
					final Color thatColor = this.theGraphics.getColor();
					this.theGraphics.setColor(this.theBackColor);
					this.theGraphics.fillOval((int) x, (int) y, (int) thisTerminatorHeight, (int) thisTerminatorHeight);
					this.theGraphics.setColor(thatColor);
					this.theGraphics.drawOval((int) x, (int) y, (int) thisTerminatorHeight, (int) thisTerminatorHeight);
				}
				break;
			}

			case IEdge.DIAMOND:
			{
				// d
				final int x[] = {(int) x1, (int) (x1 + sx1 + dx1), (int) (x1 + sx1 + sx1), (int) (x1 + sx1 - dx1)};
				final int y[] = {(int) y1, (int) (y1 + sy1 + dy1), (int) (y1 + sy1 + sy1), (int) (y1 + sy1 - dy1)};
				if ((thisStyle & IEdge.FROMFILL) != 0)
				{
					this.theGraphics.fillPolygon(x, y, x.length);
				}
				else
				{
					final Color thatColor = this.theGraphics.getColor();
					this.theGraphics.setColor(this.theBackColor);
					this.theGraphics.fillPolygon(x, y, x.length);
					this.theGraphics.setColor(thatColor);
					this.theGraphics.drawPolygon(x, y, x.length);
				}
				break;
			}

			case IEdge.ARROW:
			{
				// a
				final int x[] = {(int) (x1 + sx1 + dx1), (int) x1, (int) (x1 + sx1 - dx1)};
				final int y[] = {(int) (y1 + sy1 + dy1), (int) y1, (int) (y1 + sy1 - dy1)};
				this.theGraphics.drawPolyline(x, y, x.length);
				break;
			}

			case IEdge.HOOK:
			{
				// h
				final int x[] = {(int) (x1 + sx1 + dx1), (int) x1};
				final int y[] = {(int) (y1 + sy1 + dy1), (int) y1};
				this.theGraphics.drawPolyline(x, y, x.length);
				break;
			}

			default:
				break;
		}

		// to
		thisEndStyle = thisStyle >> IEdge.TOSHIFT & IEdge.SHAPEMASK;
		switch (thisEndStyle)
		{
			case IEdge.TRIANGLE:
			{
				// t
				final int x[] = {(int) x2, (int) (x2 + sx2 + dx2), (int) (x2 + sx2 - dx2)};
				final int y[] = {(int) y2, (int) (y2 + sy2 + dy2), (int) (y2 + sy2 - dy2)};
				if ((thisStyle & IEdge.TOFILL) != 0)
				{
					this.theGraphics.fillPolygon(x, y, x.length);
				}
				else
				{
					final Color thatColor = this.theGraphics.getColor();
					this.theGraphics.setColor(this.theBackColor);
					this.theGraphics.fillPolygon(x, y, x.length);
					this.theGraphics.setColor(thatColor);
					this.theGraphics.drawPolygon(x, y, x.length);
				}
				break;
			}

			case IEdge.CIRCLE:
			{
				// c
				final double x = x2 + sx2 / 2 - thisTerminatorHeight / 2;
				final double y = y2 + sy2 / 2 - thisTerminatorHeight / 2;
				if ((thisStyle & IEdge.TOFILL) != 0)
				{
					this.theGraphics.fillOval((int) x, (int) y, (int) thisTerminatorHeight, (int) thisTerminatorHeight);
				}
				else
				{
					final Color thatColor = this.theGraphics.getColor();
					this.theGraphics.setColor(this.theBackColor);
					this.theGraphics.fillOval((int) x, (int) y, (int) thisTerminatorHeight, (int) thisTerminatorHeight);
					this.theGraphics.setColor(thatColor);
					this.theGraphics.drawOval((int) x, (int) y, (int) thisTerminatorHeight, (int) thisTerminatorHeight);
				}
				break;
			}

			case IEdge.DIAMOND:
			{
				// d
				final int x[] = {(int) x2, (int) (x2 + sx2 + dx2), (int) (x2 + sx2 + sx2), (int) (x2 + sx2 - dx2)};
				final int y[] = {(int) y2, (int) (y2 + sy2 + dy2), (int) (y2 + sy2 + sy2), (int) (y2 + sy2 - dy2)};
				if ((thisStyle & IEdge.TOFILL) != 0)
				{
					this.theGraphics.fillPolygon(x, y, x.length);
				}
				else
				{
					final Color thatColor = this.theGraphics.getColor();
					this.theGraphics.setColor(this.theBackColor);
					this.theGraphics.fillPolygon(x, y, x.length);
					this.theGraphics.setColor(thatColor);
					this.theGraphics.drawPolygon(x, y, x.length);
				}
				break;
			}

			case IEdge.ARROW:
			{
				// a
				final int x[] = {(int) (x2 + sx2 + dx2), (int) x2, (int) (x2 + sx2 - dx2)};
				final int y[] = {(int) (y2 + sy2 + dy2), (int) y2, (int) (y2 + sy2 - dy2)};
				this.theGraphics.drawPolyline(x, y, x.length);
				break;
			}

			case IEdge.HOOK:
			{
				// h
				final int x[] = {(int) (x2 + sx2 + dx2), (int) x2};
				final int y[] = {(int) (y2 + sy2 + dy2), (int) y2};
				this.theGraphics.drawPolyline(x, y, x.length);
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
	 * @param thisNode node
	 */
	private void drawSpace(final INode thisNode)
	{
		// color
		Color thisBackColor = thisNode.getBackColor();
		if (thisBackColor == null)
		{
			thisBackColor = this.theNodeBackColor;
		}
		// draw
		final Location thisLocation = thisNode.getLocation();
		drawCircle(thisLocation.euclidean.center.re, thisLocation.euclidean.center.im, thisLocation.euclidean.radius, thisBackColor);

		// draw center
		final int x = xUnitCircleToView(thisLocation.euclidean.center.re);
		final int y = yUnitCircleToView(thisLocation.euclidean.center.im);
		final int left = x - 5;
		final int right = x + 5;
		final int top = y - 5;
		final int bottom = y + 5;
		this.theGraphics.drawLine(left, y, right, y);
		this.theGraphics.drawLine(x, top, x, bottom);
	}

	/**
	 * Draw circle
	 *
	 * @param x         center x-coordinate
	 * @param y         center y-coordinate
	 * @param r         radius
	 * @param thisColor color
	 */
	private void drawCircle(final double x, final double y, final double r, final Color thisColor)
	{
		this.theGraphics.setColor(thisColor);
		this.theGraphics.drawOval(xUnitCircleToView(x - r), yUnitCircleToView(y - r), wUnitCircleToView(2 * r), hUnitCircleToView(2 * r));
	}

	// @formatter:off
	/*
	private void drawOrientation(final double x1, double y1, double theta)
	{
		double x2 = x1 + Math.cos(theta) * 50;
		double y2 = y1 + Math.sin(theta) * 50;
		this.theGraphics.drawLine((int) x1, (int) y1, (int) (x2), (int) (y2));
	}
	*/
	// @formatter:on

	// H E L P E R S

	// s t y l e

	/**
	 * Merge styles
	 *
	 * @param thisDefault default style
	 * @param thatStyle   local style
	 * @return style
	 */
	@SuppressWarnings("boxing")
	static private int mergeStyles(int thisDefault, final Integer thatStyle)
	{
		if (thatStyle == null)
		{
			return thisDefault;
		}
		int thisStyle = thatStyle;
		int thisResult = thisDefault;
		if ((thisStyle & IEdge.HIDDENDEF) != 0)
		{
			thisResult &= ~IEdge.HIDDEN;
			thisResult |= thisStyle & IEdge.HIDDEN;
		}
		if ((thisStyle & IEdge.LINEDEF) != 0)
		{
			thisResult &= ~IEdge.LINE;
			thisResult |= thisStyle & IEdge.LINE;
		}
		if ((thisStyle & IEdge.STROKEDEF) != 0)
		{
			thisResult &= ~IEdge.STROKEMASK;
			thisResult |= thisStyle & IEdge.STROKEMASK;
		}
		if ((thisStyle & IEdge.STROKEWIDTHDEF) != 0)
		{
			thisResult &= ~IEdge.STROKEWIDTHMASK;
			thisResult |= thisStyle & IEdge.STROKEWIDTHMASK;
		}
		if ((thisStyle & IEdge.FROMDEF) != 0)
		{
			thisResult &= ~IEdge.FROMMASK;
			thisResult |= thisStyle & IEdge.FROMMASK;
		}
		if ((thisStyle & IEdge.TODEF) != 0)
		{
			thisResult &= ~IEdge.TOMASK;
			thisResult |= thisStyle & IEdge.TOMASK;
		}
		return thisResult;
	}

	// m i d - p o i n t

	/**
	 * Get middle
	 *
	 * @param x1 from point
	 * @param x2 to mpoint
	 * @return middle
	 */
	private static Point2D getMidPoint(final Point2D x1, final Point2D x2)
	{
		final double x = (x1.getX() + x2.getX()) / 2.0;
		final double y = (x1.getY() + x2.getY()) / 2.0;
		return new Point2D(x, y);
	}

	// i n t e r s e c t i o n

	/**
	 * Get intersection between line and rectangle
	 *
	 * @param thisRect rectangle
	 * @param thisFrom from-point on line
	 * @param thisTo   to-point on line
	 * @return intersection point
	 */
	static private Point2D getIntersection(final Rectangle2D thisRect, final Point2D thisFrom, final Point2D thisTo)
	{
		if (!CROP_EDGES)
		{
			return null;
		}

		if (thisRect == null)
		{
			return null;
		}

		final int thisCode = thisRect.outcode(thisFrom);

		// rectangle
		final double x0 = thisRect.getCenterX();
		final double y0 = thisRect.getCenterY();
		final double x = thisRect.getX();
		final double y = thisRect.getY();
		final double w = thisRect.getWidth();
		final double h = thisRect.getHeight();

		// points on top, bottom, left, right
		//noinspection UnnecessaryLocalVariable
		final double ty = y;
		final double by = y + h;
		//noinspection UnnecessaryLocalVariable
		final double lx = x;
		final double rx = x + w;

		// line
		final double tanAlpha = (thisTo.getY() - thisFrom.getY()) / (thisTo.getX() - thisFrom.getX());

		if ((thisCode & Rectangle2D.OUT_LEFT) != 0)
		{
			// compute left intersection
			final double ly = (lx - x0) * tanAlpha + y0;
			if (ly >= ty && ly <= by)
			{
				// drawPoint(lx, ly, Color.GREEN);
				return new Point2D(lx, ly);
			}
		}
		if ((thisCode & Rectangle2D.OUT_RIGHT) != 0)
		{
			// compute right intersection
			final double ry = (rx - x0) * tanAlpha + y0;
			if (ry >= ty && ry <= by)
			{
				// drawPoint(rx, ry, Color.YELLOW);
				return new Point2D(rx, ry);
			}
		}
		if ((thisCode & Rectangle2D.OUT_BOTTOM) != 0)
		{
			// compute bottom intersection
			final double bx = (by - y0) / tanAlpha + x0;
			if (bx >= lx && bx <= rx)
			{
				// drawPoint(bx, by, Color.ORANGE);
				return new Point2D(bx, by);
			}
		}
		if ((thisCode & Rectangle2D.OUT_TOP) != 0)
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
	 * @param thisRect rectangle
	 * @param thisArc  arc
	 * @return intersection point
	 */
	static private Point2D getIntersection(final Rectangle2D thisRect, final Arc2D thisArc)
	{
		if (!CROP_EDGES)
		{
			return null;
		}

		if (thisRect == null)
		{
			return null;
		}

		// rectangle
		final double x = thisRect.getCenterX();
		final double y = thisRect.getCenterY();
		final double w = thisRect.getWidth() / 2.;
		final double h = thisRect.getHeight() / 2.;

		// arc
		final double ax = thisArc.getCenterX();
		final double ay = thisArc.getCenterY();
		final double a = thisArc.getWidth() / 2.;
		final double b = thisArc.getHeight() / 2.;
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
				if (Painter.isOnArc(lx, ly, thisArc))
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
				if (Painter.isOnArc(rx, ry, thisArc))
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
				if (Painter.isOnArc(bx, by, thisArc))
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
				if (Painter.isOnArc(tx, ty, thisArc))
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
	 * @param thisRect1 rect1
	 * @param thisRect2 rect2
	 * @return true if nodes' rects intersect
	 */
	private static boolean boxesIntersect(final Rectangle2D thisRect1, final Rectangle2D thisRect2)
	{
		if (thisRect1 != null && thisRect2 != null)
		{
			// inflate
			final Rectangle2D thisFromRect2 = new Rectangle2D();
			thisFromRect2.setFrame(thisRect1.getMinX() - 1, thisRect1.getMinY() - 1, (int) thisRect1.getWidth() + 2, (int) thisRect1.getHeight() + 2);
			final Rectangle2D thisToRect2 = new Rectangle2D();
			thisToRect2.setFrame(thisRect2.getMinX() - 1, thisRect2.getMinY() - 1, (int) thisRect2.getWidth() + 2, (int) thisRect2.getHeight() + 2);

			// intersection test
			if (thisFromRect2.intersects(thisToRect2))
			{
				return true;
			}
		}
		return false;
	}

	// i s o n a r c

	/**
	 * Test whether point is on arc
	 *
	 * @param dx      x-offset from arc center
	 * @param dy      y-offset from arc center
	 * @param thisArc arc
	 * @return whether the point is on arc
	 */
	private static boolean isOnArc(final double dx, final double dy, final Arc2D thisArc)
	{
		final double a = Painter.pointToAngle(dx, dy, thisArc);
		return thisArc.containsAngle(Math.toDegrees(a));
	}

	// c o n v e r s i o n

	/**
	 * Compute angle for point
	 *
	 * @param dx      x-offset from arc center
	 * @param dy      y-offset from arc center
	 * @param thisArc arc
	 * @return computed angle
	 */
	private static double pointToAngle(final double dx, final double dy, final Arc2D thisArc)
	{
		return Math.atan2(-dy * thisArc.getWidth(), dx * thisArc.getHeight());
	}

	// l a b e l   h a n d l i n g

	/**
	 * Make text into lines
	 *
	 * @param thisNode node
	 * @return lines
	 */
	private String[] makeLabel(final INode thisNode)
	{
		final String thisLabel = thisNode.toString();
		if (thisLabel == null || thisLabel.isEmpty())
		{
			return null;
		}
		if (this.theLabelMaxLines == 1)
		{
			return new String[]{thisLabel.replaceAll("\\n", " ")};
		}
		return thisLabel.split("\\n", this.theLabelMaxLines);
	}

	static private final String ELLIPSIS = "…"; // ellipsis … ⋯

	/**
	 * Ellipsize text in label
	 *
	 * @param thisNodeData node data
	 * @param dnode        node space diameter
	 * @return new text width
	 */
	private int ellipsizeLabel(final NodeData thisNodeData, final int dnode)
	{
		// compute average character width
		int wunit = this.theGraphics.stringWidth("x");

		// compute trailing dots width
		int wdots = this.theGraphics.stringWidth(ELLIPSIS);

		// compute number of characters that fit before dots
		int thisNChars = (dnode - wdots) / wunit;

		// ensure at least one
		if (thisNChars < 1)
		{
			thisNChars = 1;
		}

		// truncation
		int thisLen = thisNodeData.theLabelLines[0].length();
		if (thisLen > thisNChars)
		{
			// truncate
			thisNodeData.theLabelLines[0] = thisNodeData.theLabelLines[0].substring(0, thisNChars) + ELLIPSIS;

			// recompute label width
			thisNodeData.theLabelLinesW[0] = this.theGraphics.stringWidth(thisNodeData.theLabelLines[0]);
		}
		int w = thisNodeData.theLabelLinesW[0];

		// extra lines
		if (thisNodeData.theLabelLines.length > 1)
		{
			this.theGraphics.setTextSize(thisNodeData.theTextSize * this.theLabelExtraLineFactor);

			// compute average character width
			wunit = this.theGraphics.stringWidth("x");

			// compute trailing dots width
			wdots = this.theGraphics.stringWidth(ELLIPSIS);

			// compute number of characters that fit before dots
			thisNChars = (w - wdots) / wunit;

			// ensure at least one
			if (thisNChars < 1)
			{
				thisNChars = 1;
			}

			for (int i = 1; i < thisNodeData.theLabelLines.length; i++)
			{
				thisLen = thisNodeData.theLabelLines[i].length();
				if (thisLen > thisNChars)
				{
					// truncate
					thisNodeData.theLabelLines[i] = thisNodeData.theLabelLines[i].substring(0, thisNChars) + ELLIPSIS;
					thisNodeData.theLabelLinesW[i] = this.theGraphics.stringWidth(thisNodeData.theLabelLines[i]);
				}
			}
			this.theGraphics.setTextSize(thisNodeData.theTextSize);
		}
		return w;
	}

	/**
	 * Compute label's width
	 *
	 * @param thisNodeData node data
	 * @return label's width
	 */
	private int labelWidth(final NodeData thisNodeData)
	{
		int n = thisNodeData.theLabelLines.length;
		thisNodeData.theLabelLinesW = new int[n];
		thisNodeData.theLabelLinesW[0] = this.theGraphics.stringWidth(thisNodeData.theLabelLines[0]);
		int w = thisNodeData.theLabelLinesW[0];

		if (thisNodeData.theLabelLines.length > 1)
		{
			this.theGraphics.setTextSize(thisNodeData.theTextSize * this.theLabelExtraLineFactor);
			for (int i = 1; i < n; i++)
			{
				thisNodeData.theLabelLinesW[i] = this.theGraphics.stringWidth(thisNodeData.theLabelLines[i]);
				if (thisNodeData.theLabelLinesW[i] > w)
				{
					w = thisNodeData.theLabelLinesW[i];
				}
			}
			this.theGraphics.setTextSize(thisNodeData.theTextSize);
		}
		return w;
	}

	/**
	 * Compute label's height
	 *
	 * @param thisNodeData node data
	 * @return label's height
	 */
	private int labelHeight(final NodeData thisNodeData)
	{
		int h = this.theGraphics.getAscent(); // +fm.getDescent();
		if (thisNodeData.theLabelLines.length > 1)
		{
			this.theGraphics.setTextSize(thisNodeData.theTextSize * this.theLabelExtraLineFactor);
			h += this.theGraphics.getAscent() * (thisNodeData.theLabelLines.length - 1);
			this.theGraphics.setTextSize(thisNodeData.theTextSize);
		}
		return h;
	}

	/**
	 * Draw lines in label
	 *
	 * @param thisNodeData node data
	 */
	private void drawLabel(final NodeData thisNodeData)
	{
		this.theGraphics.setTextSize(thisNodeData.theTextSize);
		int dx = (thisNodeData.theLabelW - thisNodeData.theLabelLinesW[0]) / 2;
		this.theGraphics.drawString(thisNodeData.theLabelLines[0], thisNodeData.theLabelX + dx, thisNodeData.theLabelY);

		if (thisNodeData.theLabelLines.length > 1)
		{
			this.theGraphics.setTextSize(thisNodeData.theTextSize * this.theLabelExtraLineFactor);
			int h = this.theGraphics.getAscent();
			int dy = h;
			for (int i = 1; i < thisNodeData.theLabelLines.length; i++)
			{
				dx = (thisNodeData.theLabelW - thisNodeData.theLabelLinesW[i]) / 2;
				this.theGraphics.drawString(thisNodeData.theLabelLines[i], thisNodeData.theLabelX + dx, thisNodeData.theLabelY + dy);
				dy += h;
			}
			this.theGraphics.setTextSize(thisNodeData.theTextSize);
		}
	}
}
