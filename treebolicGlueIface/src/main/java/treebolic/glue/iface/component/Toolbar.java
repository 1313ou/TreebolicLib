/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.glue.iface.component;

/**
 * Glue interface for Toolbar
 *
 * @param <L> platform action listener type
 * @author Bernard Bou
 */
public interface Toolbar<L>
{
	/**
	 * Toolbar button index
	 */
	enum Button
	{
		// @formatter:off
		/** Home */	HOME, //
		/** North */ NORTH, /** South */ SOUTH, /** East */ EAST, /** West */ WEST, /** Radial */ RADIAL, //
		/** Expand */ EXPAND, /** Shrink */ SHRINK, /** Reset expansion */ EXPANSIONRESET, //
		/** Widen */ WIDEN, /** Narrow */ NARROW, /** Reset sweep */ SWEEPRESET, //
		/** Reset expansion and sweep */ EXPANSIONSWEEPRESET, //
		/** Zoom in */ ZOOMIN, /** Zoom out */ ZOOMOUT, /** Reset zooming */ ZOOMONE, //
		/** Scale up */ SCALEUP, /** Scale down */ SCALEDOWN, /** Reset scaling */ SCALEONE, //
		/** Refresh */ REFRESH, //
		/** Render edges as arcs */ ARCEDGE, //
		/** Use tooltips */ TOOLTIP, /** Content in tooltips */ TOOLTIPCONTENT, //
		/** Focus on hover */ FOCUSHOVER, //
		/** Separator */ SEPARATOR
		// @formatter:on
	}

	/**
	 * Add button
	 *
	 * @param button   button
	 * @param listener listener
	 */
	void addButton(final Button button, final L listener);

	/**
	 * Get buttons
	 *
	 * @return buttons
	 */
	Button[] getButtons();
}
