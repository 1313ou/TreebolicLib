/**
 * Title : Treebolic
 * Description : Treebolic mutable
 * Version : 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 *
 * Update : Mon Mar 10 00:00:00 CEST 2008
 */
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
	 * @param thisModel
	 *            model
	 * @return string for model
	 */
	static public String toString(final Model thisModel)
	{
		final StringBuffer thisBuffer = new StringBuffer();
		thisBuffer.append(ModelDump.toString(thisModel.theTree));
		thisBuffer.append(ModelDump.toString(thisModel.theSettings));
		thisBuffer.append(ModelDump.toString(thisModel.theImages));
		return thisBuffer.toString();
	}

	/**
	 * Stringify tree
	 *
	 * @param thisTree
	 *            tree
	 * @return string for tree
	 */
	static public String toString(final Tree thisTree)
	{
		final StringBuffer thisBuffer = new StringBuffer();
		thisBuffer.append("NODES\n"); //$NON-NLS-1$
		thisBuffer.append(ModelDump.toString(thisTree.getRoot(), 0));
		thisBuffer.append("EDGES\n"); //$NON-NLS-1$
		thisBuffer.append(ModelDump.toString(thisTree.getEdges()));
		return thisBuffer.toString();
	}

	/**
	 * Stringify node and children
	 *
	 * @param thisNode
	 *            node
	 * @param thisLevel
	 *            level
	 * @return string for node
	 */
	static public String toString(final INode thisNode, final int thisLevel)
	{
		final StringBuffer thisBuffer = new StringBuffer();
		for (int i = 0; i < thisLevel; i++)
		{
			thisBuffer.append('\t');
		}

		if (thisNode == null)
		{
			thisBuffer.append("[null node]"); //$NON-NLS-1$
		}
		else
		{
			// id
			thisBuffer.append('#');
			thisBuffer.append(thisNode.getId());

			// label
			thisBuffer.append(" '"); //$NON-NLS-1$
			thisBuffer.append(thisNode.toString());

			// parent
			thisBuffer.append("' ^"); //$NON-NLS-1$
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
				thisBuffer.append(" !"); //$NON-NLS-1$
				thisBuffer.append(thisImageFile);
			}
			if (thisImageIndex != -1)
			{
				thisBuffer.append(" !"); //$NON-NLS-1$
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
	 * @param thisEdgeList
	 *            edge list
	 * @return string for edge list
	 */
	static public String toString(final List<IEdge> thisEdgeList)
	{
		final StringBuffer thisBuffer = new StringBuffer();
		if (thisEdgeList != null)
		{
			for (final IEdge thisEdge : thisEdgeList)
			{
				thisBuffer.append(thisEdge.toString());
				thisBuffer.append('\n');
			}
		}
		return thisBuffer.toString();
	}

	/**
	 * Stringify image list
	 *
	 * @param theseImages
	 *            images
	 * @return string for images
	 */
	static public String toString(final Image[] theseImages)
	{
		final StringBuffer thisBuffer = new StringBuffer();
		thisBuffer.append("IMAGES\n"); //$NON-NLS-1$
		int i = 0;
		if (theseImages != null)
			for (Image thisImage : theseImages)
			{
				thisBuffer.append(i);
				thisBuffer.append('-');
				thisBuffer.append(thisImage.getHeight() + 'x' + thisImage.getHeight());
				thisBuffer.append('\n');
				i++;
			}
		return thisBuffer.toString();
	}

	/**
	 * Stringify settings
	 *
	 * @param theseSettings
	 *            settings
	 * @return string for settings
	 */
	static public String toString(final Settings theseSettings)
	{
		final StringBuffer thisBuffer = new StringBuffer();
		thisBuffer.append("SETTINGS\n"); //$NON-NLS-1$
		thisBuffer.append("BackColor=" + Utils.colorToString(theseSettings.theBackColor) + '\n'); //$NON-NLS-1$
		thisBuffer.append("ForeColor=" + Utils.colorToString(theseSettings.theForeColor) + '\n'); //$NON-NLS-1$
		thisBuffer.append("BackgroundImage=" + theseSettings.theBackgroundImageFile + '\n'); //$NON-NLS-1$
		thisBuffer.append("FontFace=" + theseSettings.theFontFace + '\n'); //$NON-NLS-1$
		thisBuffer.append("FontSize=" + theseSettings.theFontSize + '\n'); //$NON-NLS-1$
		thisBuffer.append("DownScaleFonts=" + theseSettings.theDownscaleFontsFlag + '\n'); //$NON-NLS-1$
		thisBuffer.append("FontScaler=" + theseSettings.theFontDownscaler + '\n'); //$NON-NLS-1$
		thisBuffer.append("DownScaleImages=" + theseSettings.theDownscaleImagesFlag + '\n'); //$NON-NLS-1$
		thisBuffer.append("ImageScaler=" + theseSettings.theImageDownscaler + '\n'); //$NON-NLS-1$
		thisBuffer.append("Orientation=" + theseSettings.theOrientation + '\n'); //$NON-NLS-1$
		thisBuffer.append("Expansion=" + theseSettings.theExpansion + '\n'); //$NON-NLS-1$
		thisBuffer.append("Sweep=" + theseSettings.theSweep + '\n'); //$NON-NLS-1$
		thisBuffer.append("PreserveOrientationFlag=" + theseSettings.thePreserveOrientationFlag + '\n'); //$NON-NLS-1$
		thisBuffer.append("HasToolbarFlag=" + theseSettings.theHasToolbarFlag + '\n'); //$NON-NLS-1$
		thisBuffer.append("HasStatusbarFlag=" + theseSettings.theHasStatusbarFlag + '\n'); //$NON-NLS-1$
		thisBuffer.append("HasPopUpMenuFlag=" + theseSettings.theHasPopUpMenuFlag + '\n'); //$NON-NLS-1$
		thisBuffer.append("HasToolTipFlag=" + theseSettings.theHasToolTipFlag + '\n'); //$NON-NLS-1$
		thisBuffer.append("ToolTipDisplaysContentFlag=" + theseSettings.theToolTipDisplaysContentFlag + '\n'); //$NON-NLS-1$
		thisBuffer.append("FocusOnHoverFlag=" + theseSettings.theFocusOnHoverFlag + '\n'); //$NON-NLS-1$
		thisBuffer.append("Focus=" + theseSettings.theFocus + '\n'); //$NON-NLS-1$
		thisBuffer.append("XMoveTo=" + theseSettings.theXMoveTo + '\n'); //$NON-NLS-1$
		thisBuffer.append("YMoveTo=" + theseSettings.theYMoveTo + '\n'); //$NON-NLS-1$
		thisBuffer.append("XShift=" + theseSettings.theXMoveTo + '\n'); //$NON-NLS-1$
		thisBuffer.append("YShift=" + theseSettings.theYMoveTo + '\n'); //$NON-NLS-1$
		thisBuffer.append("NodeBackColor=" + Utils.colorToString(theseSettings.theNodeBackColor) + '\n'); //$NON-NLS-1$
		thisBuffer.append("NodeForeColor=" + Utils.colorToString(theseSettings.theNodeForeColor) + '\n'); //$NON-NLS-1$
		thisBuffer.append("DefaultNodeImage=" + theseSettings.theDefaultNodeImage + '\n'); //$NON-NLS-1$
		thisBuffer.append("BorderFlag=" + theseSettings.theBorderFlag + '\n'); //$NON-NLS-1$
		thisBuffer.append("EllipsizeFlag=" + theseSettings.theEllipsizeFlag + '\n'); //$NON-NLS-1$
		thisBuffer.append("TreeEdgeColor=" + Utils.colorToString(theseSettings.theTreeEdgeColor) + '\n'); //$NON-NLS-1$
		thisBuffer.append("TreeEdgeStyle=" + ModelDump.toString(theseSettings.theTreeEdgeStyle) + '\n'); //$NON-NLS-1$
		thisBuffer.append("DefaultTreeEdgeImage=" + theseSettings.theDefaultTreeEdgeImage + '\n'); //$NON-NLS-1$
		thisBuffer.append("EdgesAsArcsFlag=" + theseSettings.theEdgesAsArcsFlag + '\n'); //$NON-NLS-1$
		thisBuffer.append("EdgeColor=" + Utils.colorToString(theseSettings.theEdgeColor) + '\n'); //$NON-NLS-1$
		thisBuffer.append("EdgeStyle=" + ModelDump.toString(theseSettings.theEdgeStyle) + '\n'); //$NON-NLS-1$
		thisBuffer.append("DefaultEdgeImage=" + theseSettings.theDefaultEdgeImage + '\n'); //$NON-NLS-1$
		if (theseSettings.theMenu == null)
		{
			thisBuffer.append("Menu=null"); //$NON-NLS-1$
		}
		else
		{
			for (final MenuItem thisMenuItem : theseSettings.theMenu)
			{
				thisBuffer.append("MenuItem"); //$NON-NLS-1$
				thisBuffer.append(" action=" + thisMenuItem.theAction); //$NON-NLS-1$
				thisBuffer.append(" link=" + thisMenuItem.theLink); //$NON-NLS-1$
				thisBuffer.append(" target=" + thisMenuItem.theMatchTarget); //$NON-NLS-1$
				thisBuffer.append(" scope=" + thisMenuItem.theMatchScope); //$NON-NLS-1$
				thisBuffer.append(" mode=" + thisMenuItem.theMatchMode); //$NON-NLS-1$
				thisBuffer.append(" label=" + thisMenuItem.theLabel); //$NON-NLS-1$
			}
		}
		thisBuffer.append('\n');
		return thisBuffer.toString();
	}

	/**
	 * Stringify style
	 *
	 * @param thisStyle
	 *            style
	 * @return string for style
	 */
	static public String toString(final Integer thisStyle)
	{
		final StringBuffer thisBuffer = new StringBuffer();
		final String[] theseStrings = Utils.toStrings(thisStyle);
		thisBuffer.append("hidden=" + theseStrings[0]); //$NON-NLS-1$
		thisBuffer.append(" line=" + theseStrings[1]); //$NON-NLS-1$
		thisBuffer.append(" stroke=" + theseStrings[2]); //$NON-NLS-1$
		thisBuffer.append(" width=" + theseStrings[3]); //$NON-NLS-1$
		thisBuffer.append(" fromterminator=" + theseStrings[4]); //$NON-NLS-1$
		thisBuffer.append(" toterminator=" + theseStrings[5]); //$NON-NLS-1$
		return thisBuffer.toString();
	}
}
