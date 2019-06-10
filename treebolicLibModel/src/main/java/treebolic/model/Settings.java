package treebolic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import treebolic.glue.Color;

/**
 * Settings
 *
 * @author Bernard Bou
 */
public class Settings implements Serializable
{
	private static final long serialVersionUID = -4310347294902070347L;

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_TOOLBAR = "toolbar";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_STATUSBAR = "statusbar";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_POPUPMENU = "popupmenu";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_TOOLTIP = "tooltip";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_TOOLTIP_DISPLAYS_CONTENT = "tooltip-displays-content";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_FOCUS = "focus";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_FOCUS_ON_HOVER = "focus-on-hover";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_XMOVETO = "xmoveto";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_YMOVETO = "ymoveto";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_XSHIFT = "xshift";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_YSHIFT = "yshift";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_ORIENTATION = "orientation";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_EXPANSION = "expansion";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_SWEEP = "sweep";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_PRESERVE_ORIENTATION = "preserve-orientation";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_FONTFACE = "fontface";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_FONTSIZE = "fontsize";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_FONTSIZE_FACTOR = "fontsize.factor";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_SCALE_FONTS = "scale.fonts";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_FONT_SCALER = "font.scaler";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_SCALE_IMAGES = "scale.images";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_IMAGE_SCALER = "image.scaler";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_BACKCOLOR = "backcolor";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_FORECOLOR = "forecolor";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_BACKGROUND_IMAGE = "background.image";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_NODE_BACKCOLOR = "node.backcolor";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_NODE_FORECOLOR = "node.forecolor";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_NODE_IMAGE = "node.image";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_NODE_BORDER = "node.border";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_NODE_ELLIPSIZE = "node.ellipsize";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_NODE_LABEL_MAX_LINES = "node.label.max-lines";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_NODE_LABEL_EXTRA_LINE_FACTOR = "node.label.extra-line-factor";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_EDGE_AS_ARC = "edge.arc";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_EDGE_IMAGE = "edge.image";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_TREE_EDGE_IMAGE = "tree.edge.image";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_EDGE_COLOR = "edge.color";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_TREE_EDGE_COLOR = "tree.edge.color";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_EDGE_STROKE = "edge.stroke";

	public static final String PROP_EDGE_STROKEWIDTH = "edge.strokewidth";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_EDGE_FROMTERMINATOR = "edge.fromterminator";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_EDGE_TOTERMINATOR = "edge.toterminator";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_EDGE_LINE = "edge.line";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_EDGE_HIDDEN = "edge.hidden";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_TREE_EDGE_STROKE = "tree.edge.stroke";

	public static final String PROP_TREE_EDGE_STROKEWIDTH = "tree.edge.strokewidth";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_TREE_EDGE_FROMTERMINATOR = "tree.edge.fromterminator";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_TREE_EDGE_TOTERMINATOR = "tree.edge.toterminator";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_TREE_EDGE_LINE = "tree.edge.line";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_TREE_EDGE_HIDDEN = "tree.edge.hidden";

	@SuppressWarnings("WeakerAccess")
	public static final String PROP_MENUITEM = "menuitem";

	// V I E W

	/**
	 * Background color
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	public Color backColor;

	/**
	 * Foreground color
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	public Color foreColor;

	/**
	 * Background image file
	 */
	public String backgroundImageFile;

	/**
	 * Background image index
	 */
	public final int backgroundImageIndex;

	// fonts
	/**
	 * Font face
	 */
	public String fontFace;

	/**
	 * Font size
	 */
	public Integer fontSize;

	/**
	 * Font size factor
	 */
	@Nullable
	public Float fontSizeFactor;

	// downscaling
	/**
	 * Whether to downscale fonts
	 */
	public Boolean downscaleFontsFlag;

	/**
	 * Font size downscaler (as per hyperbolic distance to center)
	 */
	@Nullable
	public float[] fontDownscaler;

	/**
	 * Whether to downscale fonts
	 */
	public Boolean downscaleImagesFlag;

	/**
	 * Image size downscaler (as per hyperbolic distance to center)
	 */
	@Nullable
	public float[] imageDownscaler;

	// T R E E

	/**
	 * Tree orientation
	 */
	public String orientation;

	/**
	 * Expansion
	 */
	public Float expansion;

	/**
	 * Sweep
	 */
	public Float sweep;

	/**
	 * Whether orientation is preserved across transforms
	 */
	public Boolean preserveOrientationFlag;

	// B E H A V I O U R

	// control and status
	/**
	 * Whether toolbar is enabled
	 */
	public Boolean hasToolbarFlag;

	/**
	 * Whether status bar is enabled
	 */
	public Boolean hasStatusbarFlag;

	/**
	 * Whether popup menus are enabled
	 */
	public Boolean hasPopUpMenuFlag;

	/**
	 * Whether tooltips are enabled
	 */
	public Boolean hasToolTipFlag;

	/**
	 * Whether tooltips display contents
	 */
	public Boolean toolTipDisplaysContentFlag;

	// focus
	/**
	 * Whether hovering on node triggers gaining focus
	 */
	public Boolean focusOnHoverFlag;

	/**
	 * Focus
	 */
	public String focus;

	// initial move
	/**
	 * Initial move to x position (0,1)
	 */
	public Float xMoveTo;

	/**
	 * Initial move to y position (0,1)
	 */
	public Float yMoveTo;

	// shift
	/**
	 * Painting shift on x
	 */
	public Float xShift;

	/**
	 * Painting shift on y
	 */
	public Float yShift;

	// N O D E S

	// color
	/**
	 * Node default background color
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	public Color nodeBackColor;

	/**
	 * Node default foreground color
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	public Color nodeForeColor;

	// images
	/**
	 * Default node image
	 */
	public String defaultNodeImage;

	/**
	 * Default node image index
	 */
	@SuppressWarnings("CanBeFinal")
	public int defaultNodeImageIndex;

	// labels

	/**
	 * Whether labels have borders
	 */
	@Nullable
	public Boolean borderFlag;

	/**
	 * Whether label texts are ellipsized
	 */
	@Nullable
	public Boolean ellipsizeFlag;

	/**
	 * Maximum lines in label (default=0: unlimited, 1:'\n' is replaced with space)
	 */
	@Nullable
	public Integer labelMaxLines;

	/**
	 * Label font factor in extra line (not first line)
	 */
	@Nullable
	public Float labelExtraLineFactor;

	// T R E E . E D G E S

	/**
	 * Tree edge default color
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	public Color treeEdgeColor;

	/**
	 * Tree edge default style
	 */
	@Nullable
	public Integer treeEdgeStyle;

	/**
	 * Tree edge default image
	 */
	public String defaultTreeEdgeImage;

	/**
	 * Tree edge default image index
	 */
	public final int defaultTreeEdgeImageIndex;

	// E D G E S

	/**
	 * Edge default color
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	public Color edgeColor;

	/**
	 * Edge default style
	 */
	@Nullable
	public Integer edgeStyle;

	/**
	 * Default edge image
	 */
	public String defaultEdgeImage;

	/**
	 * Default edge image index
	 */
	public final int defaultEdgeImageIndex;

	/**
	 * Whether edges are represented as arcs (or straight lines)
	 */
	public Boolean edgesAsArcsFlag;

	// M E N U

	/**
	 * Menu
	 */
	public List<MenuItem> menu;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 */
	public Settings()
	{
		this.backgroundImageIndex = -1;
		this.defaultNodeImageIndex = -1;
		this.defaultTreeEdgeImageIndex = -1;
		this.defaultEdgeImageIndex = -1;
	}

	/**
	 * Load settings from properties
	 *
	 * @param properties properties
	 */
	public void load(@NonNull final Properties properties)
	{
		String param;
		Color color;
		Integer style;

		// top
		param = properties.getProperty(Settings.PROP_TOOLBAR);
		if (param != null)
		{
			this.hasToolbarFlag = Boolean.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_STATUSBAR);
		if (param != null)
		{
			this.hasStatusbarFlag = Boolean.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_POPUPMENU);
		if (param != null)
		{
			this.hasPopUpMenuFlag = Boolean.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_TOOLTIP);
		if (param != null)
		{
			this.hasToolTipFlag = Boolean.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_TOOLTIP_DISPLAYS_CONTENT);
		if (param != null)
		{
			this.toolTipDisplaysContentFlag = Boolean.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_FOCUS);
		if (param != null)
		{
			this.focus = param;
		}
		param = properties.getProperty(Settings.PROP_FOCUS_ON_HOVER);
		if (param != null)
		{
			this.focusOnHoverFlag = Boolean.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_XMOVETO);
		if (param != null)
		{
			this.xMoveTo = Float.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_YMOVETO);
		if (param != null)
		{
			this.yMoveTo = Float.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_XSHIFT);
		if (param != null)
		{
			this.xShift = Float.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_YSHIFT);
		if (param != null)
		{
			this.yShift = Float.valueOf(param);
		}

		// tree
		param = properties.getProperty(Settings.PROP_ORIENTATION);
		if (param != null)
		{
			this.orientation = param;
		}
		param = properties.getProperty(Settings.PROP_EXPANSION);
		if (param != null)
		{
			this.expansion = Float.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_SWEEP);
		if (param != null)
		{
			this.sweep = Float.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_PRESERVE_ORIENTATION);
		if (param != null)
		{
			this.preserveOrientationFlag = Boolean.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_FONTFACE);
		if (param != null)
		{
			this.fontFace = param;
		}
		param = properties.getProperty(Settings.PROP_FONTSIZE);
		if (param != null)
		{
			this.fontSize = Integer.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_FONTSIZE_FACTOR);
		if (param != null)
		{
			this.fontSizeFactor = Float.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_SCALE_FONTS);
		if (param != null)
		{
			this.downscaleFontsFlag = Boolean.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_FONT_SCALER);
		if (param != null)
		{
			this.fontDownscaler = Utils.stringToFloats(param);
		}
		param = properties.getProperty(Settings.PROP_SCALE_IMAGES);
		if (param != null)
		{
			this.downscaleImagesFlag = Boolean.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_IMAGE_SCALER);
		if (param != null)
		{
			this.imageDownscaler = Utils.stringToFloats(param);
		}
		color = Utils.stringToColor(properties.getProperty(Settings.PROP_BACKCOLOR));
		if (color != null)
		{
			this.backColor = color;
		}
		color = Utils.stringToColor(properties.getProperty(Settings.PROP_FORECOLOR));
		if (color != null)
		{
			this.foreColor = color;
		}
		param = properties.getProperty(Settings.PROP_BACKGROUND_IMAGE);
		if (param != null)
		{
			this.backgroundImageFile = param;
		}

		// nodes
		color = Utils.stringToColor(properties.getProperty(Settings.PROP_NODE_BACKCOLOR));
		if (color != null)
		{
			this.nodeBackColor = color;
		}
		color = Utils.stringToColor(properties.getProperty(Settings.PROP_NODE_FORECOLOR));
		if (color != null)
		{
			this.nodeForeColor = color;
		}
		param = properties.getProperty(Settings.PROP_NODE_IMAGE);
		if (param != null)
		{
			this.defaultNodeImage = param;
		}
		param = properties.getProperty(Settings.PROP_NODE_BORDER);
		if (param != null)
		{
			this.borderFlag = Boolean.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_NODE_ELLIPSIZE);
		if (param != null)
		{
			this.ellipsizeFlag = Boolean.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_NODE_LABEL_MAX_LINES);
		if (param != null)
		{
			this.labelMaxLines = Integer.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_NODE_LABEL_EXTRA_LINE_FACTOR);
		if (param != null)
		{
			this.labelExtraLineFactor = Float.valueOf(param);
		}

		// edges
		param = properties.getProperty(Settings.PROP_EDGE_AS_ARC);
		if (param != null)
		{
			this.edgesAsArcsFlag = Boolean.valueOf(param);
		}
		param = properties.getProperty(Settings.PROP_EDGE_IMAGE);
		if (param != null)
		{
			this.defaultEdgeImage = param;
		}
		param = properties.getProperty(Settings.PROP_TREE_EDGE_IMAGE);
		if (param != null)
		{
			this.defaultTreeEdgeImage = param;
		}
		color = Utils.stringToColor(properties.getProperty(Settings.PROP_EDGE_COLOR));
		if (color != null)
		{
			this.edgeColor = color;
		}
		color = Utils.stringToColor(properties.getProperty(Settings.PROP_TREE_EDGE_COLOR));
		if (color != null)
		{
			this.treeEdgeColor = color;
		}
		style = Utils.parseStyle(properties.getProperty(Settings.PROP_EDGE_STROKE), properties.getProperty(Settings.PROP_EDGE_FROMTERMINATOR), properties.getProperty(Settings.PROP_EDGE_TOTERMINATOR), properties.getProperty(Settings.PROP_EDGE_LINE), properties.getProperty(Settings.PROP_EDGE_HIDDEN));
		if (style != null)
		{
			this.edgeStyle = style;
		}
		style = Utils.parseStyle(properties.getProperty(Settings.PROP_TREE_EDGE_STROKE), properties.getProperty(Settings.PROP_TREE_EDGE_FROMTERMINATOR), properties.getProperty(Settings.PROP_TREE_EDGE_TOTERMINATOR), properties.getProperty(Settings.PROP_TREE_EDGE_LINE), properties.getProperty(Settings.PROP_TREE_EDGE_HIDDEN));
		if (style != null)
		{
			this.treeEdgeStyle = style;
		}

		// menu
		for (int i = 0; (param = properties.getProperty(Settings.PROP_MENUITEM + i)) != null; i++)
		{
			// label;action;link;target;matchTarget;matchScope;matchMode
			final String[] fields = param.split(";");
			if (fields.length != 7)
			{
				continue;
			}

			// menu item
			final MenuItem menuItem = new MenuItem();
			menuItem.label = fields[0];
			menuItem.link = fields[2];
			menuItem.target = fields[3];
			menuItem.matchTarget = fields[4];
			Utils.parseMenuItem(menuItem, fields[1], fields[5], fields[6]); // action,scope,mode

			// add
			if (this.menu == null)
			{
				this.menu = new ArrayList<>();
			}
			this.menu.add(menuItem);
		}
	}
}
