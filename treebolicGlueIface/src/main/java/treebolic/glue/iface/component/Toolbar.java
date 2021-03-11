/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.glue.iface.component;

/**
 * Glue interface for Toolbar
 *
 * @author Bernard Bou
 */
public interface Toolbar<L>
{
	enum Button
	{
		HOME, //
		NORTH, SOUTH, EAST, WEST, RADIAL, //
		EXPAND, SHRINK, EXPANSIONRESET, //
		WIDEN, NARROW, SWEEPRESET, //
		EXPANSIONSWEEPRESET, //
		ZOOMIN, ZOOMOUT, ZOOMONE, //
		SCALEUP, SCALEDOWN, SCALEONE, //
		REFRESH, //
		ARCEDGE, //
		TOOLTIP, TOOLTIPCONTENT, //
		FOCUSHOVER, //
		SEPARATOR
	}

	/**
	 * Add button
	 *
	 * @param button   button
	 * @param listener listener
	 */
	void addButton(final Button button, final L listener);
}
