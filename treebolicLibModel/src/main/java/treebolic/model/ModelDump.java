package treebolic.model;

import java.util.List;

import treebolic.glue.Image;

/**
 * Model dump
 *
 * @author Bernard Bou
 */
public class ModelDump
{
	/**
	 * Stringify model
	 *
	 * @param thisModel model
	 * @return string for model
	 */
	static public String toString(final Model thisModel)
	{
		return ModelDump.toString(thisModel.theTree) +
				ModelDump.toString(thisModel.theSettings) +
				ModelDump.toString(thisModel.theImages);
	}

	/**
	 * Stringify tree
	 *
	 * @param thisTree tree
	 * @return string for tree
	 */
	static public String toString(final Tree thisTree)
	{
		return "NODES\n" +
				ModelDump.toString(thisTree.getRoot(), 0) +
				"EDGES\n" +
				ModelDump.toString(thisTree.getEdges());
	}

	/**
	 * Stringify node and children
	 *
	 * @param thisNode  node
	 * @param thisLevel level
	 * @return string for node
	 */
	static public String toString(final INode thisNode, final int thisLevel)
	{
		final StringBuilder thisBuffer = new StringBuilder();
		for (int i = 0; i < thisLevel; i++)
		{
			thisBuffer.append('\t');
		}

		if (thisNode == null)
		{
			thisBuffer.append("[null node]");
		}
		else
		{
			// id
			thisBuffer.append('#');
			thisBuffer.append(thisNode.getId());

			// label
			thisBuffer.append(" '");
			thisBuffer.append(thisNode.toString());

			// parent
			thisBuffer.append("' ^");
			final INode thisParent = thisNode.getParent();
			if (thisParent != null)
			{
				thisBuffer.append('#');
				thisBuffer.append(thisParent.getId());
			}

			// image
			final String thisImageFile = thisNode.getImageFile();
			final int thisImageIndex = thisNode.getImageIndex();
			if (thisImageFile != null)
			{
				thisBuffer.append(" !");
				thisBuffer.append(thisImageFile);
			}
			if (thisImageIndex != -1)
			{
				thisBuffer.append(" !");
				thisBuffer.append(thisImageIndex);
			}

			// children
			thisBuffer.append('\n');
			for (final INode thisChildNode : thisNode.getChildren())
			{
				thisBuffer.append(ModelDump.toString(thisChildNode, thisLevel + 1));
			}
		}
		return thisBuffer.toString();
	}

	/**
	 * Stringify edge list
	 *
	 * @param thisEdgeList edge list
	 * @return string for edge list
	 */
	static public String toString(final List<IEdge> thisEdgeList)
	{
		final StringBuilder thisBuilder = new StringBuilder();
		if (thisEdgeList != null)
		{
			for (final IEdge thisEdge : thisEdgeList)
			{
				thisBuilder.append(thisEdge.toString());
				thisBuilder.append('\n');
			}
		}
		return thisBuilder.toString();
	}

	/**
	 * Stringify image list
	 *
	 * @param theseImages images
	 * @return string for images
	 */
	static public String toString(final Image[] theseImages)
	{
		final StringBuilder thisBuilder = new StringBuilder();
		thisBuilder.append("IMAGES\n");
		int i = 0;
		if (theseImages != null)
		{
			for (Image thisImage : theseImages)
			{
				thisBuilder.append(i);
				thisBuilder.append('-');
				thisBuilder.append(thisImage.getHeight() + 'x' + thisImage.getHeight());
				thisBuilder.append('\n');
				i++;
			}
		}
		return thisBuilder.toString();
	}

	/**
	 * Stringify settings
	 *
	 * @param theseSettings settings
	 * @return string for settings
	 */
	static public String toString(final Settings theseSettings)
	{
		final StringBuilder thisBuilder = new StringBuilder();
		thisBuilder.append("SETTINGS\n");
		thisBuilder.append("BackColor=").append(Utils.colorToString(theseSettings.theBackColor)).append('\n');
		thisBuilder.append("ForeColor=").append(Utils.colorToString(theseSettings.theForeColor)).append('\n');
		thisBuilder.append("BackgroundImage=").append(theseSettings.theBackgroundImageFile).append('\n');
		thisBuilder.append("FontFace=").append(theseSettings.theFontFace).append('\n');
		thisBuilder.append("FontSize=").append(theseSettings.theFontSize).append('\n');
		thisBuilder.append("DownScaleFonts=").append(theseSettings.theDownscaleFontsFlag).append('\n');
		thisBuilder.append("FontScaler=").append(toString(theseSettings.theFontDownscaler)).append('\n');
		thisBuilder.append("DownScaleImages=").append(theseSettings.theDownscaleImagesFlag).append('\n');
		thisBuilder.append("ImageScaler=").append(toString(theseSettings.theImageDownscaler)).append('\n');
		thisBuilder.append("Orientation=").append(theseSettings.theOrientation).append('\n');
		thisBuilder.append("Expansion=").append(theseSettings.theExpansion).append('\n');
		thisBuilder.append("Sweep=").append(theseSettings.theSweep).append('\n');
		thisBuilder.append("PreserveOrientationFlag=").append(theseSettings.thePreserveOrientationFlag).append('\n');
		thisBuilder.append("HasToolbarFlag=").append(theseSettings.theHasToolbarFlag).append('\n');
		thisBuilder.append("HasStatusbarFlag=").append(theseSettings.theHasStatusbarFlag).append('\n');
		thisBuilder.append("HasPopUpMenuFlag=").append(theseSettings.theHasPopUpMenuFlag).append('\n');
		thisBuilder.append("HasToolTipFlag=").append(theseSettings.theHasToolTipFlag).append('\n');
		thisBuilder.append("ToolTipDisplaysContentFlag=").append(theseSettings.theToolTipDisplaysContentFlag).append('\n');
		thisBuilder.append("FocusOnHoverFlag=").append(theseSettings.theFocusOnHoverFlag).append('\n');
		thisBuilder.append("Focus=").append(theseSettings.theFocus).append('\n');
		thisBuilder.append("XMoveTo=").append(theseSettings.theXMoveTo).append('\n');
		thisBuilder.append("YMoveTo=").append(theseSettings.theYMoveTo).append('\n');
		thisBuilder.append("XShift=").append(theseSettings.theXMoveTo).append('\n');
		thisBuilder.append("YShift=").append(theseSettings.theYMoveTo).append('\n');
		thisBuilder.append("NodeBackColor=").append(Utils.colorToString(theseSettings.theNodeBackColor)).append('\n');
		thisBuilder.append("NodeForeColor=").append(Utils.colorToString(theseSettings.theNodeForeColor)).append('\n');
		thisBuilder.append("DefaultNodeImage=").append(theseSettings.theDefaultNodeImage).append('\n');
		thisBuilder.append("BorderFlag=").append(theseSettings.theBorderFlag).append('\n');
		thisBuilder.append("EllipsizeFlag=").append(theseSettings.theEllipsizeFlag).append('\n');
		thisBuilder.append("TreeEdgeColor=").append(Utils.colorToString(theseSettings.theTreeEdgeColor)).append('\n');
		thisBuilder.append("TreeEdgeStyle=").append(ModelDump.toString(theseSettings.theTreeEdgeStyle)).append('\n');
		thisBuilder.append("DefaultTreeEdgeImage=").append(theseSettings.theDefaultTreeEdgeImage).append('\n');
		thisBuilder.append("EdgesAsArcsFlag=").append(theseSettings.theEdgesAsArcsFlag).append('\n');
		thisBuilder.append("EdgeColor=").append(Utils.colorToString(theseSettings.theEdgeColor)).append('\n');
		thisBuilder.append("EdgeStyle=").append(ModelDump.toString(theseSettings.theEdgeStyle)).append('\n');
		thisBuilder.append("DefaultEdgeImage=").append(theseSettings.theDefaultEdgeImage).append('\n');
		if (theseSettings.theMenu == null)
		{
			thisBuilder.append("Menu=null");
		}
		else
		{
			for (final MenuItem thisMenuItem : theseSettings.theMenu)
			{
				thisBuilder.append("MenuItem");
				thisBuilder.append(" action=").append(thisMenuItem.theAction);
				thisBuilder.append(" link=").append(thisMenuItem.theLink);
				thisBuilder.append(" target=").append(thisMenuItem.theMatchTarget);
				thisBuilder.append(" scope=").append(thisMenuItem.theMatchScope);
				thisBuilder.append(" mode=").append(thisMenuItem.theMatchMode);
				thisBuilder.append(" label=").append(thisMenuItem.theLabel);
			}
		}
		thisBuilder.append('\n');
		return thisBuilder.toString();
	}

	/**
	 * Stringify style
	 *
	 * @param thisStyle style
	 * @return string for style
	 */
	static public String toString(final Integer thisStyle)
	{
		final StringBuilder thisBuilder = new StringBuilder();
		final String[] theseStrings = Utils.toStrings(thisStyle);
		thisBuilder.append("hidden=").append(theseStrings[0]);
		thisBuilder.append(" line=").append(theseStrings[1]);
		thisBuilder.append(" stroke=").append(theseStrings[2]);
		thisBuilder.append(" width=").append(theseStrings[3]);
		thisBuilder.append(" fromterminator=").append(theseStrings[4]);
		thisBuilder.append(" toterminator=").append(theseStrings[5]);
		return thisBuilder.toString();
	}

	static private String toString(final float[] theseFloats)
	{
		final StringBuilder thisBuilder = new StringBuilder();
		if (theseFloats != null)
		{
			boolean first = true;
			for (float thisFloat : theseFloats)
			{
				if (first)
				{
					first = false;
				}
				else
				{
					thisBuilder.append(' ');
				}
				thisBuilder.append(thisFloat);
			}
		}
		return thisBuilder.toString();
	}
}
