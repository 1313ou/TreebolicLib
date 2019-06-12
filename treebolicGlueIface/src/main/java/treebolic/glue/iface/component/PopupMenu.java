/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.glue.iface.component;

/**
 * Glue interface for PopupMenu
 *
 * @author Bernard Bou
 */
public interface PopupMenu<C, L>
{
	// protected PopupMenu();

	/**
	 * Add item
	 *
	 * @param label    label
	 * @param image    image
	 * @param listener listener
	 */
	void addItem(final String label, final int image, final L listener);

	/**
	 * Popup component at position
	 *
	 * @param component component to popup
	 * @param x         x-position
	 * @param y         y-position
	 */
	void popup(C component, int x, int y);
}
