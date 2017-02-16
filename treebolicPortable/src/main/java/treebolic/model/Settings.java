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

	public static final String PROP_TOOLBAR = "toolbar"; //$NON-NLS-1$

	public static final String PROP_STATUSBAR = "statusbar"; //$NON-NLS-1$

	public static final String PROP_POPUPMENU = "popupmenu"; //$NON-NLS-1$

	public static final String PROP_TOOLTIP = "tooltip"; //$NON-NLS-1$

	public static final String PROP_TOOLTIP_DISPLAYS_CONTENT = "tooltip-displays-content"; //$NON-NLS-1$

	public static final String PROP_FOCUS = "focus"; //$NON-NLS-1$

	public static final String PROP_FOCUS_ON_HOVER = "focus-on-hover"; //$NON-NLS-1$

	public static final String PROP_XMOVETO = "xmoveto"; //$NON-NLS-1$

	public static final String PROP_YMOVETO = "ymoveto"; //$NON-NLS-1$

	public static final String PROP_XSHIFT = "xshift"; //$NON-NLS-1$

	public static final String PROP_YSHIFT = "yshift"; //$NON-NLS-1$

	public static final String PROP_ORIENTATION = "orientation"; //$NON-NLS-1$

	public static final String PROP_EXPANSION = "expansion"; //$NON-NLS-1$

	public static final String PROP_SWEEP = "sweep"; //$NON-NLS-1$

	public static final String PROP_PRESERVE_ORIENTATION = "preserve-orientation"; //$NON-NLS-1$

	public static final String PROP_FONTFACE = "fontface"; //$NON-NLS-1$

	public static final String PROP_FONTSIZE = "fontsize"; //$NON-NLS-1$

	public static final String PROP_SCALE_FONTS = "scale.fonts"; //$NON-NLS-1$

	public static final String PROP_FONT_SCALER = "font.scaler"; //$NON-NLS-1$

	public static final String PROP_SCALE_IMAGES = "scale.images"; //$NON-NLS-1$

	public static final String PROP_IMAGE_SCALER = "image.scaler"; //$NON-NLS-1$

	public static final String PROP_BACKCOLOR = "backcolor"; //$NON-NLS-1$

	public static final String PROP_FORECOLOR = "forecolor"; //$NON-NLS-1$

	public static final String PROP_BACKGROUND_IMAGE = "background.image"; //$NON-NLS-1$

	public static final String PROP_NODE_BACKCOLOR = "node.backcolor"; //$NON-NLS-1$

	public static final String PROP_NODE_FORECOLOR = "node.forecolor"; //$NON-NLS-1$

	public static final String PROP_NODE_IMAGE = "node.image"; //$NON-NLS-1$

	public static final String PROP_NODE_BORDER = "node.border"; //$NON-NLS-1$

	public static final String PROP_NODE_ELLIPSIZE = "node.ellipsize"; //$NON-NLS-1$

	public static final String PROP_EDGE_AS_ARC = "edge.arc"; //$NON-NLS-1$

	public static final String PROP_EDGE_IMAGE = "edge.image"; //$NON-NLS-1$

	public static final String PROP_TREE_EDGE_IMAGE = "tree.edge.image"; //$NON-NLS-1$

	public static final String PROP_EDGE_COLOR = "edge.color"; //$NON-NLS-1$

	public static final String PROP_TREE_EDGE_COLOR = "tree.edge.color"; //$NON-NLS-1$

	public static final String PROP_EDGE_STROKE = "edge.stroke"; //$NON-NLS-1$

	public static final String PROP_EDGE_STROKEWIDTH = "edge.strokewidth"; //$NON-NLS-1$

	public static final String PROP_EDGE_FROMTERMINATOR = "edge.fromterminator"; //$NON-NLS-1$

	public static final String PROP_EDGE_TOTERMINATOR = "edge.toterminator"; //$NON-NLS-1$

	public static final String PROP_EDGE_LINE = "edge.line"; //$NON-NLS-1$

	public static final String PROP_EDGE_HIDDEN = "edge.hidden"; //$NON-NLS-1$

	public static final String PROP_TREE_EDGE_STROKE = "tree.edge.stroke"; //$NON-NLS-1$

	public static final String PROP_TREE_EDGE_STROKEWIDTH = "tree.edge.strokewidth"; //$NON-NLS-1$

	public static final String PROP_TREE_EDGE_FROMTERMINATOR = "tree.edge.fromterminator"; //$NON-NLS-1$

	public static final String PROP_TREE_EDGE_TOTERMINATOR = "tree.edge.toterminator"; //$NON-NLS-1$

	public static final String PROP_TREE_EDGE_LINE = "tree.edge.line"; //$NON-NLS-1$

	public static final String PROP_TREE_EDGE_HIDDEN = "tree.edge.hidden"; //$NON-NLS-1$

	public static final String PROP_MENUITEM = "menuitem"; //$NON-NLS-1$

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
	public final int theDefaultNodeImageIndex;

	// labels

	/**
	 * Whether labels have borders
	 */
	public Boolean theBorderFlag;

	/**
	 * Whether label texts are ellipsized
	 */
	public Boolean theEllipsizeFlag;

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
	 * @param theseProperties
	 *            properties
	 * @throws Exception
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
		thisStyle = Utils.parseStyle(theseProperties.getProperty(Settings.PROP_EDGE_STROKE), theseProperties.getProperty(Settings.PROP_EDGE_FROMTERMINATOR),
				theseProperties.getProperty(Settings.PROP_EDGE_TOTERMINATOR), theseProperties.getProperty(Settings.PROP_EDGE_LINE),
				theseProperties.getProperty(Settings.PROP_EDGE_HIDDEN));
		if (thisStyle != null)
		{
			this.theEdgeStyle = thisStyle;
		}
		thisStyle = Utils.parseStyle(theseProperties.getProperty(Settings.PROP_TREE_EDGE_STROKE),
				theseProperties.getProperty(Settings.PROP_TREE_EDGE_FROMTERMINATOR), theseProperties.getProperty(Settings.PROP_TREE_EDGE_TOTERMINATOR),
				theseProperties.getProperty(Settings.PROP_TREE_EDGE_LINE), theseProperties.getProperty(Settings.PROP_TREE_EDGE_HIDDEN));
		if (thisStyle != null)
		{
			this.theTreeEdgeStyle = thisStyle;
		}

		// menu
		for (int i = 0; (thisParam = theseProperties.getProperty(Settings.PROP_MENUITEM + i)) != null; i++)
		{
			// label;action;link;target;matchTarget;matchScope;matchMode
			final String[] theseFields = thisParam.split(";"); //$NON-NLS-1$
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
