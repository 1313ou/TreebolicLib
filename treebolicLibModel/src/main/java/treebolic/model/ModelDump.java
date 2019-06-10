package treebolic.model;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import treebolic.glue.Image;

/**
 * Model dump
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class ModelDump
{
	/**
	 * Stringify model
	 *
	 * @param model model
	 * @return string for model
	 */
	static public String toString(@Nullable final Model model)
	{
		if (model == null)
		{
			return "null";
		}
		return ModelDump.toString(model.tree) + ModelDump.toString(model.settings) + ModelDump.toString(model.images);
	}

	/**
	 * Stringify tree
	 *
	 * @param tree tree
	 * @return string for tree
	 */
	@SuppressWarnings("WeakerAccess")
	static public String toString(@Nullable final Tree tree)
	{
		if (tree == null)
		{
			return "null";
		}
		return "NODES\n" + ModelDump.toString(tree.getRoot(), 0) + "EDGES\n" + ModelDump.toString(tree.getEdges());
	}

	/**
	 * Stringify node and children
	 *
	 * @param node  node
	 * @param level level
	 * @return string for node
	 */
	@SuppressWarnings("WeakerAccess")
	static public String toString(@Nullable final INode node, final int level)
	{
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++)
		{
			sb.append('\t');
		}

		if (node == null)
		{
			sb.append("[null node]");
		}
		else
		{
			// id
			sb.append('#');
			sb.append(node.getId());

			// label
			sb.append(" '");
			sb.append(node.toString());

			// parent
			sb.append("' ^");
			final INode parent = node.getParent();
			if (parent != null)
			{
				sb.append('#');
				sb.append(parent.getId());
			}

			// image
			final String imageFile = node.getImageFile();
			final int imageIndex = node.getImageIndex();
			if (imageFile != null)
			{
				sb.append(" !");
				sb.append(imageFile);
			}
			if (imageIndex != -1)
			{
				sb.append(" !");
				sb.append(imageIndex);
			}

			// children
			sb.append('\n');
			final List<INode> childNodes = node.getChildren();
			if (childNodes != null)
			{
				for (final INode childNode : childNodes)
				{
					sb.append(ModelDump.toString(childNode, level + 1));
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Stringify edge list
	 *
	 * @param edgeList edge list
	 * @return string for edge list
	 */
	@SuppressWarnings("WeakerAccess")
	static public String toString(@Nullable final List<IEdge> edgeList)
	{
		final StringBuilder sb = new StringBuilder();
		if (edgeList != null)
		{
			for (final IEdge edge : edgeList)
			{
				sb.append(edge.toString());
				sb.append('\n');
			}
		}
		return sb.toString();
	}

	/**
	 * Stringify image list
	 *
	 * @param images images
	 * @return string for images
	 */
	@SuppressWarnings("WeakerAccess")
	static public String toString(@Nullable final Image[] images)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("IMAGES\n");
		int i = 0;
		if (images != null)
		{
			for (Image image : images)
			{
				sb.append(i);
				sb.append('-');
				sb.append(image.getHeight() + 'x' + image.getHeight());
				sb.append('\n');
				i++;
			}
		}
		return sb.toString();
	}

	/**
	 * Stringify settings
	 *
	 * @param settings settings
	 * @return string for settings
	 */
	@SuppressWarnings("WeakerAccess")
	static public String toString(@NonNull final Settings settings)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("SETTINGS\n");
		sb.append("BackColor=").append(Utils.colorToString(settings.backColor)).append('\n');
		sb.append("ForeColor=").append(Utils.colorToString(settings.foreColor)).append('\n');
		sb.append("BackgroundImage=").append(settings.backgroundImageFile).append('\n');
		sb.append("FontFace=").append(settings.fontFace).append('\n');
		sb.append("FontSize=").append(settings.fontSize).append('\n');
		sb.append("FontSizeFactor=").append(settings.fontSizeFactor).append('\n');
		sb.append("DownScaleFonts=").append(settings.downscaleFontsFlag).append('\n');
		sb.append("FontScaler=").append(toString(settings.fontDownscaler)).append('\n');
		sb.append("DownScaleImages=").append(settings.downscaleImagesFlag).append('\n');
		sb.append("ImageScaler=").append(toString(settings.imageDownscaler)).append('\n');
		sb.append("Orientation=").append(settings.orientation).append('\n');
		sb.append("Expansion=").append(settings.expansion).append('\n');
		sb.append("Sweep=").append(settings.sweep).append('\n');
		sb.append("PreserveOrientationFlag=").append(settings.preserveOrientationFlag).append('\n');
		sb.append("HasToolbarFlag=").append(settings.hasToolbarFlag).append('\n');
		sb.append("HasStatusbarFlag=").append(settings.hasStatusbarFlag).append('\n');
		sb.append("HasPopUpMenuFlag=").append(settings.hasPopUpMenuFlag).append('\n');
		sb.append("HasToolTipFlag=").append(settings.hasToolTipFlag).append('\n');
		sb.append("ToolTipDisplaysContentFlag=").append(settings.toolTipDisplaysContentFlag).append('\n');
		sb.append("FocusOnHoverFlag=").append(settings.focusOnHoverFlag).append('\n');
		sb.append("Focus=").append(settings.focus).append('\n');
		sb.append("XMoveTo=").append(settings.xMoveTo).append('\n');
		sb.append("YMoveTo=").append(settings.yMoveTo).append('\n');
		sb.append("XShift=").append(settings.xMoveTo).append('\n');
		sb.append("YShift=").append(settings.yMoveTo).append('\n');
		sb.append("NodeBackColor=").append(Utils.colorToString(settings.nodeBackColor)).append('\n');
		sb.append("NodeForeColor=").append(Utils.colorToString(settings.nodeForeColor)).append('\n');
		sb.append("DefaultNodeImage=").append(settings.defaultNodeImage).append('\n');
		sb.append("BorderFlag=").append(settings.borderFlag).append('\n');
		sb.append("EllipsizeFlag=").append(settings.ellipsizeFlag).append('\n');
		sb.append("LabelMaxLines=").append(settings.labelMaxLines).append('\n');
		sb.append("LabelExtraLineFactor=").append(settings.labelExtraLineFactor).append('\n');
		sb.append("TreeEdgeColor=").append(Utils.colorToString(settings.treeEdgeColor)).append('\n');
		sb.append("TreeEdgeStyle=").append(ModelDump.toString(settings.treeEdgeStyle)).append('\n');
		sb.append("DefaultTreeEdgeImage=").append(settings.defaultTreeEdgeImage).append('\n');
		sb.append("EdgesAsArcsFlag=").append(settings.edgesAsArcsFlag).append('\n');
		sb.append("EdgeColor=").append(Utils.colorToString(settings.edgeColor)).append('\n');
		sb.append("EdgeStyle=").append(ModelDump.toString(settings.edgeStyle)).append('\n');
		sb.append("DefaultEdgeImage=").append(settings.defaultEdgeImage).append('\n');
		if (settings.menu == null)
		{
			sb.append("Menu=null");
		}
		else
		{
			for (final MenuItem menuItem : settings.menu)
			{
				sb.append("MenuItem");
				sb.append(" action=").append(menuItem.action);
				sb.append(" link=").append(menuItem.link);
				sb.append(" target=").append(menuItem.matchTarget);
				sb.append(" scope=").append(menuItem.matchScope);
				sb.append(" mode=").append(menuItem.matchMode);
				sb.append(" label=").append(menuItem.label);
			}
		}
		sb.append('\n');
		return sb.toString();
	}

	/**
	 * Stringify style
	 *
	 * @param style style
	 * @return string for style
	 */
	@SuppressWarnings("WeakerAccess")
	static public String toString(final Integer style)
	{
		final StringBuilder sb = new StringBuilder();
		final String[] strings = Utils.toStrings(style);
		sb.append("hidden=").append(strings[0]);
		sb.append(" line=").append(strings[1]);
		sb.append(" stroke=").append(strings[2]);
		sb.append(" width=").append(strings[3]);
		sb.append(" fromterminator=").append(strings[4]);
		sb.append(" toterminator=").append(strings[5]);
		return sb.toString();
	}

	static private String toString(@Nullable final float[] floats)
	{
		final StringBuilder sb = new StringBuilder();
		if (floats != null)
		{
			boolean first = true;
			for (float f : floats)
			{
				if (first)
				{
					first = false;
				}
				else
				{
					sb.append(' ');
				}
				sb.append(f);
			}
		}
		return sb.toString();
	}
}
