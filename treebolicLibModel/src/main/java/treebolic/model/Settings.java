package treebolic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import treebolic.glue.Color;

/**
 * Settings
 *
 * @author Bernard Bou
 */
public class Settings implements Serializable
{
	private static final long serialVersionUID = -4310347294902070347L;

	public static final String PROP_TOOLBAR = "toolbar";

	public static final String PROP_STATUSBAR = "statusbar";

	public static final String PROP_POPUPMENU = "popupmenu";

	public static final String PROP_TOOLTIP = "tooltip";

	public static final String PROP_TOOLTIP_DISPLAYS_CONTENT = "tooltip-displays-content";

	public static final String PROP_FOCUS = "focus";

	public static final String PROP_FOCUS_ON_HOVER = "focus-on-hover";

	public static final String PROP_XMOVETO = "xmoveto";

	public static final String PROP_YMOVETO = "ymoveto";

	public static final String PROP_XSHIFT = "xshift";

	public static final String PROP_YSHIFT = "yshift";

	public static final String PROP_ORIENTATION = "orientation";

	public static final String PROP_EXPANSION = "expansion";

	public static final String PROP_SWEEP = "sweep";

	public static final String PROP_PRESERVE_ORIENTATION = "preserve-orientation";

	public static final String PROP_FONTFACE = "fontface";

	public static final String PROP_FONTSIZE = "fontsize";

	public static final String PROP_SCALE_FONTS = "scale.fonts";

	public static final String PROP_FONT_SCALER = "font.scaler";

	public static final String PROP_SCALE_IMAGES = "scale.images";

	public static final String PROP_IMAGE_SCALER = "image.scaler";

	public static final String PROP_BACKCOLOR = "backcolor";

	public static final String PROP_FORECOLOR = "forecolor";

	public static final String PROP_BACKGROUND_IMAGE = "background.image";

	public static final String PROP_NODE_BACKCOLOR = "node.backcolor";

	public static final String PROP_NODE_FORECOLOR = "node.forecolor";

	public static final String PROP_NODE_IMAGE = "node.image";

	public static final String PROP_NODE_BORDER = "node.border";

	public static final String PROP_NODE_ELLIPSIZE = "node.ellipsize";

	public static final String PROP_NODE_LABEL_MAX_LINES = "node.label.max-lines";

	public static final String PROP_NODE_LABEL_EXTRA_LINE_FACTOR = "node.label.extra-line-factor";

	public static final String PROP_EDGE_AS_ARC = "edge.arc";

	public static final String PROP_EDGE_IMAGE = "edge.image";

	public static final String PROP_TREE_EDGE_IMAGE = "tree.edge.image";

	public static final String PROP_EDGE_COLOR = "edge.color";

	public static final String PROP_TREE_EDGE_COLOR = "tree.edge.color";

	public static final String PROP_EDGE_STROKE = "edge.stroke";

	public static final String PROP_EDGE_STROKEWIDTH = "edge.strokewidth";

	public static final String PROP_EDGE_FROMTERMINATOR = "edge.fromterminator";

	public static final String PROP_EDGE_TOTERMINATOR = "edge.toterminator";

	public static final String PROP_EDGE_LINE = "edge.line";

	public static final String PROP_EDGE_HIDDEN = "edge.hidden";

	public static final String PROP_TREE_EDGE_STROKE = "tree.edge.stroke";

	public static final String PROP_TREE_EDGE_STROKEWIDTH = "tree.edge.strokewidth";

	public static final String PROP_TREE_EDGE_FROMTERMINATOR = "tree.edge.fromterminator";

	public static final String PROP_TREE_EDGE_TOTERMINATOR = "tree.edge.toterminator";

	public static final String PROP_TREE_EDGE_LINE = "tree.edge.line";

	public static final String PROP_TREE_EDGE_HIDDEN = "tree.edge.hidden";

	public static final String PROP_MENUITEM = "menuitem";

	// V I E W

	/**
	 * Background color
	 */
	public Color theBackColor;

	/**
	 * Foreground color
	 */
	public Color theForeColor;

	/**
	 * Background image file
	 */
	public String theBackgroundImageFile;

	/**
	 * Background image index
	 */
	public final int theBackgroundImageIndex;

	// fonts
	/**
	 * Font face
	 */
	public String theFontFace;

	/**
	 * Font size
	 */
	public Integer theFontSize;

	// downscaling
	/**
	 * Whether to downscale fonts
	 */
	public Boolean theDownscaleFontsFlag;

	/**
	 * Font size downscaler (as per hyperbolic distance to center)
	 */
	public float[] theFontDownscaler;

	/**
	 * Whether to downscale fonts
	 */
	public Boolean theDownscaleImagesFlag;

	/**
	 * Image size downscaler (as per hyperbolic distance to center)
	 */
	public float[] theImageDownscaler;

	// T R E E

	/**
	 * Tree orientation
	 */
	public String theOrientation;

	/**
	 * Expansion
	 */
	public Float theExpansion;

	/**
	 * Sweep
	 */
	public Float theSweep;

	/**
	 * Whether orientation is preserved across transforms
	 */
	public Boolean thePreserveOrientationFlag;

	// B E H A V I O U R

	// control and status
	/**
	 * Whether toolbar is enabled
	 */
	public Boolean theHasToolbarFlag;

	/**
	 * Whether status bar is enabled
	 */
	public Boolean theHasStatusbarFlag;

	/**
	 * Whether popup menus are enabled
	 */
	public Boolean theHasPopUpMenuFlag;

	/**
	 * Whether tooltips are enabled
	 */
	public Boolean theHasToolTipFlag;

	/**
	 * Whether tooltips display contents
	 */
	public Boolean theToolTipDisplaysContentFlag;

	// focus
	/**
	 * Whether hovering on node triggers gaining focus
	 */
	public Boolean theFocusOnHoverFlag;

	/**
	 * Focus
	 */
	public String theFocus;

	// initial move
	/**
	 * Initial move to x position (0,1)
	 */
	public Float theXMoveTo;

	/**
	 * Initial move to y position (0,1)
	 */
	public Float theYMoveTo;

	// shift
	/**
	 * Painting shift on x
	 */
	public Float theXShift;

	/**
	 * Painting shift on y
	 */
	public Float theYShift;

	// N O D E S

	// color
	/**
	 * Node default background color
	 */
	public Color theNodeBackColor;

	/**
	 * Node default foreground color
	 */
	public Color theNodeForeColor;

	// images
	/**
	 * Default node image
	 */
	public String theDefaultNodeImage;

	/**
	 * Default node image index
	 */
	public int theDefaultNodeImageIndex;

	// labels

	/**
	 * Whether labels have borders
	 */
	public Boolean theBorderFlag;

	/**
	 * Whether label texts are ellipsized
	 */
	public Boolean theEllipsizeFlag;

	/**
	 * Maximum lines in label (default=0: unlimited, 1:'\n' is replaced with space)
	 */
	public Integer theLabelMaxLines;

	/**
	 * Label font factor in extra line (not first line)
	 */
	public Float theLabelExtraLineFactor;

	// T R E E . E D G E S

	/**
	 * Tree edge default color
	 */
	public Color theTreeEdgeColor;

	/**
	 * Tree edge default style
	 */
	public Integer theTreeEdgeStyle;

	/**
	 * Tree edge default image
	 */
	public String theDefaultTreeEdgeImage;

	/**
	 * Tree edge default image index
	 */
	public final int theDefaultTreeEdgeImageIndex;

	// E D G E S

	/**
	 * Edge default color
	 */
	public Color theEdgeColor;

	/**
	 * Edge default style
	 */
	public Integer theEdgeStyle;

	/**
	 * Default edge image
	 */
	public String theDefaultEdgeImage;

	/**
	 * Default edge image index
	 */
	public final int theDefaultEdgeImageIndex;

	/**
	 * Whether edges are represented as arcs (or straight lines)
	 */
	public Boolean theEdgesAsArcsFlag;

	// M E N U

	/**
	 * Menu
	 */
	public List<MenuItem> theMenu;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 */
	public Settings()
	{
		this.theBackgroundImageIndex = -1;
		this.theDefaultNodeImageIndex = -1;
		this.theDefaultTreeEdgeImageIndex = -1;
		this.theDefaultEdgeImageIndex = -1;
	}

	/**
	 * Load settings from properties
	 *
	 * @param theseProperties properties
	 */
	@SuppressWarnings("boxing")
	public void load(final Properties theseProperties)
	{
		String thisParam;
		Color thisColor;
		Integer thisStyle;

		// top
		thisParam = theseProperties.getProperty(Settings.PROP_TOOLBAR);
		if (thisParam != null)
		{
			this.theHasToolbarFlag = Boolean.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_STATUSBAR);
		if (thisParam != null)
		{
			this.theHasStatusbarFlag = Boolean.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_POPUPMENU);
		if (thisParam != null)
		{
			this.theHasPopUpMenuFlag = Boolean.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_TOOLTIP);
		if (thisParam != null)
		{
			this.theHasToolTipFlag = Boolean.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_TOOLTIP_DISPLAYS_CONTENT);
		if (thisParam != null)
		{
			this.theToolTipDisplaysContentFlag = Boolean.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_FOCUS);
		if (thisParam != null)
		{
			this.theFocus = thisParam;
		}
		thisParam = theseProperties.getProperty(Settings.PROP_FOCUS_ON_HOVER);
		if (thisParam != null)
		{
			this.theFocusOnHoverFlag = Boolean.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_XMOVETO);
		if (thisParam != null)
		{
			this.theXMoveTo = Float.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_YMOVETO);
		if (thisParam != null)
		{
			this.theYMoveTo = Float.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_XSHIFT);
		if (thisParam != null)
		{
			this.theXShift = Float.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_YSHIFT);
		if (thisParam != null)
		{
			this.theYShift = Float.valueOf(thisParam);
		}

		// tree
		thisParam = theseProperties.getProperty(Settings.PROP_ORIENTATION);
		if (thisParam != null)
		{
			this.theOrientation = thisParam;
		}
		thisParam = theseProperties.getProperty(Settings.PROP_EXPANSION);
		if (thisParam != null)
		{
			this.theExpansion = Float.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_SWEEP);
		if (thisParam != null)
		{
			this.theSweep = Float.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_PRESERVE_ORIENTATION);
		if (thisParam != null)
		{
			this.thePreserveOrientationFlag = Boolean.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_FONTFACE);
		if (thisParam != null)
		{
			this.theFontFace = thisParam;
		}
		thisParam = theseProperties.getProperty(Settings.PROP_FONTSIZE);
		if (thisParam != null)
		{
			this.theFontSize = Integer.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_SCALE_FONTS);
		if (thisParam != null)
		{
			this.theDownscaleFontsFlag = Boolean.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_FONT_SCALER);
		if (thisParam != null)
		{
			this.theFontDownscaler = Utils.stringToFloats(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_SCALE_IMAGES);
		if (thisParam != null)
		{
			this.theDownscaleImagesFlag = Boolean.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_IMAGE_SCALER);
		if (thisParam != null)
		{
			this.theImageDownscaler = Utils.stringToFloats(thisParam);
		}
		thisColor = Utils.stringToColor(theseProperties.getProperty(Settings.PROP_BACKCOLOR));
		if (thisColor != null)
		{
			this.theBackColor = thisColor;
		}
		thisColor = Utils.stringToColor(theseProperties.getProperty(Settings.PROP_FORECOLOR));
		if (thisColor != null)
		{
			this.theForeColor = thisColor;
		}
		thisParam = theseProperties.getProperty(Settings.PROP_BACKGROUND_IMAGE);
		if (thisParam != null)
		{
			this.theBackgroundImageFile = thisParam;
		}

		// nodes
		thisColor = Utils.stringToColor(theseProperties.getProperty(Settings.PROP_NODE_BACKCOLOR));
		if (thisColor != null)
		{
			this.theNodeBackColor = thisColor;
		}
		thisColor = Utils.stringToColor(theseProperties.getProperty(Settings.PROP_NODE_FORECOLOR));
		if (thisColor != null)
		{
			this.theNodeForeColor = thisColor;
		}
		thisParam = theseProperties.getProperty(Settings.PROP_NODE_IMAGE);
		if (thisParam != null)
		{
			this.theDefaultNodeImage = thisParam;
		}
		thisParam = theseProperties.getProperty(Settings.PROP_NODE_BORDER);
		if (thisParam != null)
		{
			this.theBorderFlag = Boolean.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_NODE_ELLIPSIZE);
		if (thisParam != null)
		{
			this.theEllipsizeFlag = Boolean.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_NODE_LABEL_MAX_LINES);
		if (thisParam != null)
		{
			this.theLabelMaxLines = Integer.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_NODE_LABEL_EXTRA_LINE_FACTOR);
		if (thisParam != null)
		{
			this.theLabelExtraLineFactor = Float.valueOf(thisParam);
		}

		// edges
		thisParam = theseProperties.getProperty(Settings.PROP_EDGE_AS_ARC);
		if (thisParam != null)
		{
			this.theEdgesAsArcsFlag = Boolean.valueOf(thisParam);
		}
		thisParam = theseProperties.getProperty(Settings.PROP_EDGE_IMAGE);
		if (thisParam != null)
		{
			this.theDefaultEdgeImage = thisParam;
		}
		thisParam = theseProperties.getProperty(Settings.PROP_TREE_EDGE_IMAGE);
		if (thisParam != null)
		{
			this.theDefaultTreeEdgeImage = thisParam;
		}
		thisColor = Utils.stringToColor(theseProperties.getProperty(Settings.PROP_EDGE_COLOR));
		if (thisColor != null)
		{
			this.theEdgeColor = thisColor;
		}
		thisColor = Utils.stringToColor(theseProperties.getProperty(Settings.PROP_TREE_EDGE_COLOR));
		if (thisColor != null)
		{
			this.theTreeEdgeColor = thisColor;
		}
		thisStyle = Utils.parseStyle(theseProperties.getProperty(Settings.PROP_EDGE_STROKE), theseProperties.getProperty(Settings.PROP_EDGE_FROMTERMINATOR), theseProperties.getProperty(Settings.PROP_EDGE_TOTERMINATOR),
				theseProperties.getProperty(Settings.PROP_EDGE_LINE), theseProperties.getProperty(Settings.PROP_EDGE_HIDDEN));
		if (thisStyle != null)
		{
			this.theEdgeStyle = thisStyle;
		}
		thisStyle = Utils.parseStyle(theseProperties.getProperty(Settings.PROP_TREE_EDGE_STROKE), theseProperties.getProperty(Settings.PROP_TREE_EDGE_FROMTERMINATOR), theseProperties.getProperty(Settings.PROP_TREE_EDGE_TOTERMINATOR),
				theseProperties.getProperty(Settings.PROP_TREE_EDGE_LINE), theseProperties.getProperty(Settings.PROP_TREE_EDGE_HIDDEN));
		if (thisStyle != null)
		{
			this.theTreeEdgeStyle = thisStyle;
		}

		// menu
		for (int i = 0; (thisParam = theseProperties.getProperty(Settings.PROP_MENUITEM + i)) != null; i++)
		{
			// label;action;link;target;matchTarget;matchScope;matchMode
			final String[] theseFields = thisParam.split(";");
			if (theseFields.length != 7)
			{
				continue;
			}

			// menu item
			final MenuItem thisMenuItem = new MenuItem();
			thisMenuItem.theLabel = theseFields[0];
			thisMenuItem.theLink = theseFields[2];
			thisMenuItem.theTarget = theseFields[3];
			thisMenuItem.theMatchTarget = theseFields[4];
			Utils.parseMenuItem(thisMenuItem, theseFields[1], theseFields[5], theseFields[6]); // action,scope,mode

			// add
			if (this.theMenu == null)
			{
				this.theMenu = new ArrayList<>();
			}
			this.theMenu.add(thisMenuItem);
		}
	}
}
