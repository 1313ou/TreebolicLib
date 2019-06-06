package treebolic.view;

import android.support.annotation.Nullable;

import java.util.List;

import treebolic.glue.Color;
import treebolic.glue.Graphics;
import treebolic.glue.Image;
import treebolic.model.IEdge;
import treebolic.model.INode;

/**
 * Painter base class
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractPainter extends Mapper
{
	// B E H A V I O U R

	/**
	 * No scaling
	 */
	static private final float[] SCALENONE = new float[]{1.F};

	// font downscaling

	/**
	 * Font default size in pt
	 */
	@SuppressWarnings("WeakerAccess")
	static public final int FONT_DEFAULT_SIZE = 20;

	/**
	 * Do font down scaling
	 */
	static private final boolean DOWNSCALEFONTS0 = true;

	/**
	 * Font down scaler
	 */
	static private final float[] FONTSCALE0 = new float[]{1.F, .9F, .8F, .7F, .6F};

	// font

	/**
	 * Image scaling
	 */
	static private final float[] IMAGESCALE0 = new float[]{1.F, .9F, .8F, .7F, .6F, .5F, .4F};

	// colors
	/**
	 * Initial default background color
	 */
	static private final Color backColor0 = Color.WHITE;

	/**
	 * Initial default foreground color
	 */
	static private final Color foreColor0 = Color.BLACK;

	/**
	 * Initial default node background color
	 */
	static private final Color nodeBackColor0 = Color.WHITE;

	/**
	 * Initial default node foreground color
	 */
	static private final Color nodeForeColor0 = Color.BLACK;

	/**
	 * Initial default tree edge color
	 */
	static private final Color treeEdgeColor0 = Color.GRAY;

	/**
	 * Initial default tree edge color
	 */
	static private final Color edgeColor0 = Color.DARK_GRAY;

	// D A T A

	// graphics context
	/**
	 * Cached graphics context
	 */
	@SuppressWarnings("WeakerAccess")
	protected Graphics graphics;

	// state

	/**
	 * Whether a dragging operation is pending
	 */
	@SuppressWarnings("WeakerAccess")
	protected boolean isDragging = false;

	// arcs
	/**
	 * Whether to render edges as arc edges
	 */
	@SuppressWarnings("WeakerAccess")
	protected boolean arcEdges = true;

	// label

	/**
	 * Border
	 */
	@SuppressWarnings("WeakerAccess")
	protected boolean border = false;

	/**
	 * Ellipsize labels
	 */
	@SuppressWarnings("WeakerAccess")
	protected boolean ellipsize = false;

	/**
	 * Label max lines
	 */
	@SuppressWarnings("WeakerAccess")
	protected int labelMaxLines = 0;

	/**
	 * Label extra line (excluding first) factor
	 */
	@SuppressWarnings("WeakerAccess")
	protected float labelExtraLineFactor = .6F;

	// scaling

	/**
	 * Zoom
	 */
	@SuppressWarnings("WeakerAccess")
	protected float zoomFactor;

	/**
	 * Zoom pivot X
	 */
	@SuppressWarnings("WeakerAccess")
	protected float zoomPivotX;

	/**
	 * Zoom pivot Y
	 */
	@SuppressWarnings("WeakerAccess")
	protected float zoomPivotY;

	// scaling

	/**
	 * Image scaling
	 */
	@SuppressWarnings("WeakerAccess")
	protected float imageScaleFactor;

	/**
	 * Downscale images
	 */
	@SuppressWarnings("WeakerAccess")
	protected boolean downscaleImages;

	/**
	 * Image down scaler
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected float[] imageDownscaler;

	/**
	 * Font scaling
	 */
	@SuppressWarnings("WeakerAccess")
	protected float fontScaleFactor;

	/**
	 * Downscale fonts
	 */
	@SuppressWarnings("WeakerAccess")
	protected boolean downscaleFonts;

	/**
	 * Font down scaler
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected float[] fontDownscaler;

	// fonts

	/**
	 * Base font face
	 */
	@SuppressWarnings("WeakerAccess")
	protected String fontFace;

	/**
	 * Base font style
	 */
	@SuppressWarnings("WeakerAccess")
	protected int fontStyle;

	/**
	 * Base font size
	 */
	@SuppressWarnings("WeakerAccess")
	protected int fontSize;

	/**
	 * Base font size factor
	 */
	@SuppressWarnings("WeakerAccess")
	protected float fontSizeFactor;

	// colors
	/**
	 * Default background color
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected Color backColor;

	/**
	 * Default foreground color
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected Color foreColor;

	/**
	 * Default node background color
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected Color nodeBackColor;

	/**
	 * Default node foreground color
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected Color nodeForeColor;

	/**
	 * Default tree edge color
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected Color treeEdgeColor;

	/**
	 * Default edge color
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	protected Color edgeColor;

	// styles
	/**
	 * Default tree edge style
	 */
	@SuppressWarnings("WeakerAccess")
	protected int treeEdgeStyle;

	/**
	 * Default edge style
	 */
	@SuppressWarnings("WeakerAccess")
	protected int edgeStyle;

	// images
	/**
	 * Default node image
	 */
	@SuppressWarnings("WeakerAccess")
	public Image defaultNodeImage;

	/**
	 * Default tree edge image
	 */
	@SuppressWarnings("WeakerAccess")
	public Image defaultTreeEdgeImage;

	/**
	 * Default edge image
	 */
	@SuppressWarnings("WeakerAccess")
	public Image defaultEdgeImage;

	/**
	 * Background (tiled) image
	 */
	@SuppressWarnings("WeakerAccess")
	public Image backgroundImage;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 */
	public AbstractPainter()
	{
		this.zoomFactor = 1F;
		this.zoomPivotX = 0F;
		this.zoomPivotY = 0F;

		this.fontScaleFactor = 1F;
		this.downscaleFonts = true;
		this.fontDownscaler = AbstractPainter.FONTSCALE0;

		this.imageScaleFactor = 1F;
		this.downscaleImages = true;
		this.imageDownscaler = AbstractPainter.IMAGESCALE0;
	}

	// P A I N T

	/**
	 * Paint background
	 */
	public abstract void paintBackground();

	/**
	 * Paint
	 *
	 * @param root     starting node
	 * @param edgeList edges
	 */
	public abstract void paint(INode root, List<IEdge> edgeList);

	// S E T U P

	/**
	 * Set up graphics context for the painter to work on
	 *
	 * @param graphics graphics context to work on
	 * @param width    width of the graphics context
	 * @param height   height of the graphics context
	 */
	public void setup(final Graphics graphics, final int width, final int height)
	{
		this.graphics = graphics;
		this.width = width;
		this.height = height;
		this.top = -height / 2;
		this.left = -width / 2;

		// translation
		this.graphics.translate(-this.left, -this.top);

		// font
		this.graphics.setFont(this.fontFace, this.fontStyle);

		// scale
		computeScale();
	}

	// I M A G E S

	/**
	 * Set images scaling
	 *
	 * @param scaleImagesFlag whether to scale images
	 * @param scaler          array of float to act as down scaler (as we move away from center)
	 */
	public void setImageScaling(@Nullable final Boolean scaleImagesFlag, @Nullable final float[] scaler)
	{
		// whether to scale images
		if (scaleImagesFlag != null)
		{
			this.downscaleImages = scaleImagesFlag;
		}

		// scaling factors
		if (scaler != null)
		{
			this.imageDownscaler = scaler;
		}
	}

	// F O N T S

	/**
	 * Set font
	 *
	 * @param fontFace          font
	 * @param fontSize          font size
	 * @param fontSizeFactor    font size factor
	 * @param downscaleFontFlag downscale images flag
	 * @param fontDownscaler    arrays of factors to apply to font size (moving away from center)
	 */
	public void setFont(@Nullable final String fontFace, @Nullable final Integer fontSize, @Nullable final Float fontSizeFactor, @Nullable final Boolean downscaleFontFlag, @Nullable final float[] fontDownscaler)
	{
		// face
		this.fontFace = fontFace == null ? "SansSerif" : fontFace;

		// size
		this.fontSize = fontSize == null ? AbstractPainter.FONT_DEFAULT_SIZE : fontSize;
		this.fontSizeFactor = fontSizeFactor == null ? 1F : fontSizeFactor;

		// downscaling
		this.downscaleFonts = downscaleFontFlag == null ? AbstractPainter.DOWNSCALEFONTS0 : downscaleFontFlag;

		// downscaler
		this.fontDownscaler = this.downscaleFonts ? AbstractPainter.FONTSCALE0 : AbstractPainter.SCALENONE;
		if (this.downscaleFonts && fontDownscaler != null)
		{
			this.fontDownscaler = fontDownscaler;
		}
	}

	// C O L O R S

	/**
	 * Set colors
	 *
	 * @param backColor     background default color
	 * @param foreColor     foreground default color
	 * @param nodeBackColor node background default color
	 * @param nodeForeColor node foreground default color
	 * @param treeEdgeColor tree edge default color
	 * @param edgeColor     edge default color
	 */
	public void setColors(@Nullable final Color backColor, @Nullable final Color foreColor, @Nullable final Color nodeBackColor, @Nullable final Color nodeForeColor, @Nullable final Color treeEdgeColor, @Nullable final Color edgeColor)
	{
		if (backColor != null)
		{
			this.backColor = backColor;
		}
		if (foreColor != null)
		{
			this.foreColor = foreColor;
		}
		if (nodeBackColor != null)
		{
			this.nodeBackColor = nodeBackColor;
		}
		if (nodeForeColor != null)
		{
			this.nodeForeColor = nodeForeColor;
		}
		if (treeEdgeColor != null)
		{
			this.treeEdgeColor = treeEdgeColor;
		}
		if (edgeColor != null)
		{
			this.edgeColor = edgeColor;
		}
	}

	/**
	 * Reset colors
	 */
	public void resetColors()
	{
		this.foreColor = AbstractPainter.foreColor0;
		this.backColor = AbstractPainter.backColor0;
		this.nodeBackColor = AbstractPainter.nodeBackColor0;
		this.nodeForeColor = AbstractPainter.nodeForeColor0;
		this.treeEdgeColor = AbstractPainter.treeEdgeColor0;
		this.edgeColor = AbstractPainter.edgeColor0;
	}

	// S T Y L E S

	/**
	 * Set default edge styles
	 *
	 * @param treeEdgeStyle default tree edge style
	 * @param edgeStyle     default edge style
	 */
	public void setEdgeStyles(@Nullable final Integer treeEdgeStyle, @Nullable final Integer edgeStyle)
	{
		if (treeEdgeStyle != null)
		{
			this.treeEdgeStyle = treeEdgeStyle;
		}
		if (edgeStyle != null)
		{
			this.edgeStyle = edgeStyle;
		}
	}

	/**
	 * Set images
	 *
	 * @param backgroundImage      background image
	 * @param defaultNodeImage     default node image
	 * @param defaultTreeEdgeImage default tree edge image
	 * @param defaultEdgeImage     default edge image
	 */
	public void setImages(final Image backgroundImage, final Image defaultNodeImage, final Image defaultTreeEdgeImage, final Image defaultEdgeImage)
	{
		this.backgroundImage = backgroundImage;
		this.defaultNodeImage = defaultNodeImage;
		this.defaultTreeEdgeImage = defaultTreeEdgeImage;
		this.defaultEdgeImage = defaultEdgeImage;
	}

	// B E H A V I O U R

	/**
	 * Set border mode
	 *
	 * @param flag true if label have borders
	 */
	public void setBorder(@Nullable final Boolean flag)
	{
		if (flag != null)
		{
			this.border = flag;
		}
	}

	/**
	 * Get border mode
	 *
	 * @return true if label have borders
	 */
	public boolean getBorder()
	{
		return this.border;
	}

	/**
	 * Set ellipsize flag
	 *
	 * @param flag true if label texts are ellipsized
	 */
	public void setEllipsize(@Nullable final Boolean flag)
	{
		if (flag != null)
		{
			this.ellipsize = flag;
		}
	}

	/**
	 * Set label max lines
	 *
	 * @param maxLines label max lines
	 */
	public void setLabelMaxLines(@Nullable final Integer maxLines)
	{
		if (maxLines != null)
		{
			this.labelMaxLines = maxLines;
		}
	}

	/**
	 * Set label extra line factor
	 *
	 * @param factor label extra line (excluding first) factor
	 */
	public void setLabelExtraLineFactor(@Nullable final Float factor)
	{
		if (factor != null)
		{
			this.labelExtraLineFactor = factor;
		}
	}

	/**
	 * Get ellipsize flag
	 *
	 * @return true if label texts are ellipsized
	 */
	public boolean getEllipsize()
	{
		return this.ellipsize;
	}

	/**
	 * Set edge rendering mode
	 *
	 * @param flag true if edges are rendered as arcs
	 */
	public void setArcEdges(@Nullable final Boolean flag)
	{
		if (flag != null)
		{
			this.arcEdges = flag;
		}
	}

	/**
	 * Get edge rendering mode
	 *
	 * @return true if edges are rendered as arcs
	 */
	public boolean getArcEdges()
	{
		return this.arcEdges;
	}

	// Z O O M I N G

	/**
	 * Set zoom factor
	 *
	 * @param zoomFactor zoom factor
	 * @param zoomPivotX zoom pivot X
	 * @param zoomPivotY zoom pivot Y
	 */
	public void setZoomFactor(final float zoomFactor, final float zoomPivotX, final float zoomPivotY)
	{
		if (zoomFactor >= 0)
		{
			this.zoomFactor = zoomFactor;
		}
		else
		{
			this.zoomFactor *= -zoomFactor;
		}
		if (zoomPivotX != Float.MAX_VALUE)
		{
			this.zoomPivotX = zoomPivotX;
		}
		if (zoomPivotY != Float.MAX_VALUE)
		{
			this.zoomPivotY = zoomPivotY;
		}
	}

	// S C A L I N G

	/**
	 * Set scale factors
	 *
	 * @param mapScaleFactor   map scale factor (how unit circle is mapped to view)
	 * @param fontScaleFactor  font scale factor
	 * @param imageScaleFactor image scale factor
	 */
	public void setScaleFactors(final float mapScaleFactor, final float fontScaleFactor, final float imageScaleFactor)
	{
		// map
		if (mapScaleFactor != 0)
		{
			if (mapScaleFactor > 0)
			{
				this.mapScaleFactor = mapScaleFactor;
			}
			else
			{
				this.mapScaleFactor *= -mapScaleFactor;
			}
		}

		// fonts
		if (fontScaleFactor != 0)
		{
			if (fontScaleFactor > 0)
			{
				this.fontScaleFactor = fontScaleFactor;
			}
			else
			{
				this.fontScaleFactor *= -fontScaleFactor;
			}
		}

		// images
		if (imageScaleFactor != 0)
		{
			if (imageScaleFactor > 0)
			{
				this.imageScaleFactor = imageScaleFactor;
			}
			else
			{
				this.imageScaleFactor *= -imageScaleFactor;
			}
		}
	}

	/**
	 * Set scale images flag
	 *
	 * @param flag true true if images are scaled
	 */
	public void setScaleImages(@Nullable final Boolean flag)
	{
		if (flag != null)
		{
			this.downscaleImages = flag;
		}
	}

	/**
	 * Get scale images flag
	 *
	 * @return true if images are scaled
	 */
	public boolean getScaleImages()
	{
		return this.downscaleImages;
	}

	// E V E N T

	/**
	 * Enter drag mode
	 */
	public void enterDrag()
	{
		this.isDragging = true;
	}

	/**
	 * Leave drag mode
	 */
	public void leaveDrag()
	{
		this.isDragging = false;
	}
}
