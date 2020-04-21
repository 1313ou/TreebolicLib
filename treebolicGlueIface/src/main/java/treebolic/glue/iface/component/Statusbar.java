/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.glue.iface.component;

import treebolic.glue.iface.ActionListener;

/**
 * Glue interface for Statusbar
 *
 * @author Bernard Bou
 */
public interface Statusbar<C, L>
{
	// public Statusbar();

	/**
	 * Init
	 *
	 * @param image image
	 */
	void init(final int image);

	/**
	 * Set hyperlink listener
	 *
	 * @param actionListener listener
	 */
	void setListener(final ActionListener actionListener);

	/**
	 * Set colors
	 *
	 * @param backColor back color
	 * @param foreColor fore color
	 */
	@SuppressWarnings("EmptyMethod")
	void setColors(C backColor, C foreColor);

	/**
	 * Set style
	 *
	 * @param style style
	 */
	void setStyle(String style);

	/**
	 * Put status
	 *
	 * @param image     image
	 * @param converter converter
	 * @param label     label
	 * @param content   content
	 */
	void put(final int image, final Converter converter, final String label, final String[] content);

	/**
	 * Put message
	 *
	 * @param message message
	 */
	void put(final String message);

	/**
	 * Add listener
	 *
	 * @param listener listener
	 */
	@SuppressWarnings("EmptyMethod")
	void addListener(final L listener);
}
