package treebolic.view;

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
	static private final float FONTSCALE0[] = new float[]{1.F, .9F, .8F, .7F, .6F};

	// font

	/**
	 * Image scaling
	 */
	static private final float IMAGESCALE0[] = new float[]{1.F, .9F, .8F, .7F, .6F, .5F, .4F};

	// colors
	/**
	 * Initial default background color
	 */
	static private final Color theBackColor0 = Color.WHITE;

	/**
	 * Initial default foreground color
	 */
	static private final Color theForeColor0 = Color.BLACK;

	/**
	 * Initial default node background color
	 */
	static private final Color theNodeBackColor0 = Color.WHITE;

	/**
	 * Initial default node foreground color
	 */
	static private final Color theNodeForeColor0 = Color.BLACK;

	/**
	 * Initial default tree edge color
	 */
	static private final Color theTreeEdgeColor0 = Color.GRAY;

	/**
	 * Initial default tree edge color
	 */
	static private final Color theEdgeColor0 = Color.DARK_GRAY;

	// D A T A

	// graphics context
	/**
	 * Cached graphics context
	 */
	@SuppressWarnings("WeakerAccess")
	protected Graphics theGraphics;

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
	protected int theLabelMaxLines = 0;

	/**
	 * Label extra line (excluding first) factor
	 */
	@SuppressWarnings("WeakerAccess")
	protected float theLabelExtraLineFactor = .6F;

	// scaling

	/**
	 * Zoom
	 */
	@SuppressWarnings("WeakerAccess")
	protected float theZoomFactor;

	/**
	 * Zoom pivot X
	 */
	@SuppressWarnings("WeakerAccess")
	protected float theZoomPivotX;

	/**
	 * Zoom pivot Y
	 */
	@SuppressWarnings("WeakerAccess")
	protected float theZoomPivotY;

	// scaling

	/**
	 * Image scaling
	 */
	@SuppressWarnings("WeakerAccess")
	protected float theImageScaleFactor;

	/**
	 * Downscale images
	 */
	@SuppressWarnings("WeakerAccess")
	protected boolean downscaleImages = true;

	/**
	 * Image down scaler
	 */
	@SuppressWarnings("WeakerAccess")
	protected float theImageDownscaler[];

	/**
	 * Font scaling
	 */
	@SuppressWarnings("WeakerAccess")
	protected float theFontScaleFactor;

	/**
	 * Downscale fonts
	 */
	@SuppressWarnings("WeakerAccess")
	protected boolean downscaleFonts = true;

	/**
	 * Font down scaler
	 */
	@SuppressWarnings("WeakerAccess")
	protected float theFontDownscaler[];

	// fonts

	/**
	 * Base font
	 */
	@SuppressWarnings("WeakerAccess")
	protected String theFontFace;

	@SuppressWarnings("WeakerAccess")
	protected int theFontStyle;

	@SuppressWarnings("WeakerAccess")
	protected int theFontSize;

	// colors
	/**
	 * Default background color
	 */
	@SuppressWarnings("WeakerAccess")
	protected Color theBackColor;

	/**
	 * Default foreground color
	 */
	@SuppressWarnings("WeakerAccess")
	protected Color theForeColor;

	/**
	 * Default node background color
	 */
	@SuppressWarnings("WeakerAccess")
	protected Color theNodeBackColor;

	/**
	 * Default node foreground color
	 */
	@SuppressWarnings("WeakerAccess")
	protected Color theNodeForeColor;

	/**
	 * Default tree edge color
	 */
	@SuppressWarnings("WeakerAccess")
	protected Color theTreeEdgeColor;

	/**
	 * Default edge color
	 */
	@SuppressWarnings("WeakerAccess")
	protected Color theEdgeColor;

	// styles
	/**
	 * Default tree edge style
	 */
	@SuppressWarnings("WeakerAccess")
	protected int theTreeEdgeStyle;

	/**
	 * Default edge style
	 */
	@SuppressWarnings("WeakerAccess")
	protected int theEdgeStyle;

	// images
	/**
	 * Default node image
	 */
	@SuppressWarnings("WeakerAccess")
	public Image theDefaultNodeImage;

	/**
	 * Default tree edge image
	 */
	@SuppressWarnings("WeakerAccess")
	public Image theDefaultTreeEdgeImage;

	/**
	 * Default edge image
	 */
	@SuppressWarnings("WeakerAccess")
	public Image theDefaultEdgeImage;

	/**
	 * Background (tiled) image
	 */
	@SuppressWarnings("WeakerAccess")
	public Image theBackgroundImage;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 */
	public AbstractPainter()
	{
		this.theZoomFactor = 1F;
		this.theZoomPivotX = 0F;
		this.theZoomPivotY = 0F;

		this.theFontScaleFactor = 1F;
		this.downscaleFonts = true;
		this.theFontDownscaler = AbstractPainter.FONTSCALE0;

		this.theImageScaleFactor = 1F;
		this.downscaleImages = true;
		this.theImageDownscaler = AbstractPainter.IMAGESCALE0;
	}

	// P A I N T

	/**
	 * Paint background
	 */
	public abstract void paintBackground();

	/**
	 * Paint
	 *
	 * @param thisRoot     starting node
	 * @param thisEdgeList edges
	 */
	public abstract void paint(INode thisRoot, List<IEdge> thisEdgeList);

	// S E T U P

	/**
	 * Set up graphics context for the painter to work on
	 *
	 * @param thisGraphics graphics context to work on
	 * @param thisWidth    width of the graphics context
	 * @param thisHeight   height of the graphics context
	 */
	public void setup(final Graphics thisGraphics, final int thisWidth, final int thisHeight)
	{
		this.theGraphics = thisGraphics;
		this.theWidth = thisWidth;
		this.theHeight = thisHeight;
		this.theTop = -thisHeight / 2;
		this.theLeft = -thisWidth / 2;

		// translation
		this.theGraphics.translate(-this.theLeft, -this.theTop);

		// font
		this.theGraphics.setFont(this.theFontFace, this.theFontStyle);

		// scale
		computeScale();
	}

	// I M A G E S

	/**
	 * Set images scaling
	 *
	 * @param thisScaleImagesFlag whether to scale images
	 * @param thisScaler          array of float to act as down scaler (as we move away from center)
	 */
	@SuppressWarnings("boxing")
	public void setImageScaling(final Boolean thisScaleImagesFlag, final float[] thisScaler)
	{
		// whether to scale images
		if (thisScaleImagesFlag != null)
		{
			this.downscaleImages = thisScaleImagesFlag;
		}

		// scaling factors
		if (thisScaler != null)
		{
			this.theImageDownscaler = thisScaler;
		}
	}

	// F O N T S

	/**
	 * Set font
	 *
	 * @param thisFontFace          font
	 * @param thisFontSize          font size
	 * @param thisDownscaleFontFlag downscale images flag
	 * @param thisFontDownscaler    arrays of factors to apply to font size (moving away from center)
	 */
	@SuppressWarnings("boxing")
	public void setFont(final String thisFontFace, final Integer thisFontSize, final Boolean thisDownscaleFontFlag, final float[] thisFontDownscaler)
	{
		// face
		this.theFontFace = thisFontFace == null ? "SansSerif" : thisFontFace;

		// size
		this.theFontSize = thisFontSize == null ? AbstractPainter.FONT_DEFAULT_SIZE : thisFontSize;

		// downscaling
		this.downscaleFonts = thisDownscaleFontFlag == null ? AbstractPainter.DOWNSCALEFONTS0 : thisDownscaleFontFlag;

		// downscaler
		this.theFontDownscaler = this.downscaleFonts ? AbstractPainter.FONTSCALE0 : AbstractPainter.SCALENONE;
		if (this.downscaleFonts && thisFontDownscaler != null)
		{
			this.theFontDownscaler = thisFontDownscaler;
		}
	}

	// C O L O R S

	/**
	 * Set colors
	 *
	 * @param thisBackColor     background default color
	 * @param thisForeColor     foreground default color
	 * @param thisNodeBackColor node background default color
	 * @param thisNodeForeColor node foreground default color
	 * @param thisTreeEdgeColor tree edge default color
	 * @param thisEdgeColor     edge default color
	 */
	public void setColors(final Color thisBackColor, final Color thisForeColor, final Color thisNodeBackColor, final Color thisNodeForeColor, final Color thisTreeEdgeColor, final Color thisEdgeColor)
	{
		if (thisBackColor != null)
		{
			this.theBackColor = thisBackColor;
		}
		if (thisForeColor != null)
		{
			this.theForeColor = thisForeColor;
		}
		if (thisNodeBackColor != null)
		{
			this.theNodeBackColor = thisNodeBackColor;
		}
		if (thisNodeForeColor != null)
		{
			this.theNodeForeColor = thisNodeForeColor;
		}
		if (thisTreeEdgeColor != null)
		{
			this.theTreeEdgeColor = thisTreeEdgeColor;
		}
		if (thisEdgeColor != null)
		{
			this.theEdgeColor = thisEdgeColor;
		}
	}

	/**
	 * Reset colors
	 */
	public void resetColors()
	{
		this.theForeColor = AbstractPainter.theForeColor0;
		this.theBackColor = AbstractPainter.theBackColor0;
		this.theNodeBackColor = AbstractPainter.theNodeBackColor0;
		this.theNodeForeColor = AbstractPainter.theNodeForeColor0;
		this.theTreeEdgeColor = AbstractPainter.theTreeEdgeColor0;
		this.theEdgeColor = AbstractPainter.theEdgeColor0;
	}

	// S T Y L E S

	/**
	 * Set default edge styles
	 *
	 * @param thisTreeEdgeStyle default tree edge style
	 * @param thisEdgeStyle     default edge style
	 */
	@SuppressWarnings("boxing")
	public void setEdgeStyles(final Integer thisTreeEdgeStyle, final Integer thisEdgeStyle)
	{
		if (thisTreeEdgeStyle != null)
		{
			this.theTreeEdgeStyle = thisTreeEdgeStyle;
		}
		if (thisEdgeStyle != null)
		{
			this.theEdgeStyle = thisEdgeStyle;
		}
	}

	/**
	 * Set images
	 *
	 * @param thisBackgroundImage      background image
	 * @param thisDefaultNodeImage     default node image
	 * @param thisDefaultTreeEdgeImage default tree edge image
	 * @param thisDefaultEdgeImage     default edge image
	 */
	public void setImages(final Image thisBackgroundImage, final Image thisDefaultNodeImage, final Image thisDefaultTreeEdgeImage, final Image thisDefaultEdgeImage)
	{
		this.theBackgroundImage = thisBackgroundImage;
		this.theDefaultNodeImage = thisDefaultNodeImage;
		this.theDefaultTreeEdgeImage = thisDefaultTreeEdgeImage;
		this.theDefaultEdgeImage = thisDefaultEdgeImage;
	}

	// B E H A V I O U R

	/**
	 * Set border mode
	 *
	 * @param thisFlag true if label have borders
	 */
	@SuppressWarnings("boxing")
	public void setBorder(final Boolean thisFlag)
	{
		if (thisFlag != null)
		{
			this.border = thisFlag;
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
	 * @param thisFlag true if label texts are ellipsized
	 */
	@SuppressWarnings("boxing")
	public void setEllipsize(final Boolean thisFlag)
	{
		if (thisFlag != null)
		{
			this.ellipsize = thisFlag;
		}
	}

	/**
	 * Set label max lines
	 *
	 * @param thisMaxLines label max lines
	 */
	@SuppressWarnings("boxing")
	public void setLabelMaxLines(final Integer thisMaxLines)
	{
		if (thisMaxLines != null)
		{
			this.theLabelMaxLines = thisMaxLines;
		}
	}

	/**
	 * Set label extra line factor
	 *
	 * @param thisFactor label extra line (excluding first) factor
	 */
	@SuppressWarnings("boxing")
	public void setLabelExtraLineFactor(final Float thisFactor)
	{
		if (thisFactor != null)
		{
			this.theLabelExtraLineFactor = thisFactor;
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
	 * @param thisFlag true if edges are rendered as arcs
	 */
	@SuppressWarnings("boxing")
	public void setArcEdges(final Boolean thisFlag)
	{
		if (thisFlag != null)
		{
			this.arcEdges = thisFlag;
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
	 * @param thisZoomFactor zoom factor
	 * @param thisZoomPivotX zoom pivot X
	 * @param thisZoomPivotY zoom pivot Y
	 */
	public void setZoomFactor(final float thisZoomFactor, final float thisZoomPivotX, final float thisZoomPivotY)
	{
		if (thisZoomFactor >= 0)
		{
			this.theZoomFactor = thisZoomFactor;
		}
		else
		{
			this.theZoomFactor *= -thisZoomFactor;
		}
		if (thisZoomPivotX != Float.MAX_VALUE)
		{
			this.theZoomPivotX = thisZoomPivotX;
		}
		if (thisZoomPivotY != Float.MAX_VALUE)
		{
			this.theZoomPivotY = thisZoomPivotY;
		}
	}

	// S C A L I N G

	/**
	 * Set scale factors
	 *
	 * @param thisMapScaleFactor   map scale factor (how unit circle is mapped to view)
	 * @param thisFontScaleFactor  font scale factor
	 * @param thisImageScaleFactor image scale factor
	 */
	public void setScaleFactors(final float thisMapScaleFactor, final float thisFontScaleFactor, final float thisImageScaleFactor)
	{
		// map
		if (thisMapScaleFactor != 0)
		{
			if (thisMapScaleFactor > 0)
			{
				this.theMapScaleFactor = thisMapScaleFactor;
			}
			else
			{
				this.theMapScaleFactor *= -thisMapScaleFactor;
			}
		}

		// fonts
		if (thisFontScaleFactor != 0)
		{
			if (thisFontScaleFactor > 0)
			{
				this.theFontScaleFactor = thisFontScaleFactor;
			}
			else
			{
				this.theFontScaleFactor *= -thisFontScaleFactor;
			}
		}

		// images
		if (thisImageScaleFactor != 0)
		{
			if (thisImageScaleFactor > 0)
			{
				this.theImageScaleFactor = thisImageScaleFactor;
			}
			else
			{
				this.theImageScaleFactor *= -thisImageScaleFactor;
			}
		}
	}

	/**
	 * Set scale images flag
	 *
	 * @param thisFlag true true if images are scaled
	 */
	@SuppressWarnings("boxing")
	public void setScaleImages(final Boolean thisFlag)
	{
		if (thisFlag != null)
		{
			this.downscaleImages = thisFlag;
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
