/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.glue.iface.component;

import treebolic.glue.iface.ActionListener;

/**
 * Glue interface for Dialog
 *
 * @author Bernard Bou
 */
public interface Dialog
{
	/**
	 * Set handle
	 *
	 * @param handle (opaque) handle
	 */
	void setHandle(final Object handle);

	/**
	 * Set header and content
	 *
	 * @param header  header
	 * @param content content
	 */
	void set(final CharSequence header, final CharSequence... content);

	/**
	 * Set converter
	 *
	 * @param converter converter
	 */
	void setConverter(final Converter converter);

	/**
	 * Set hyperlink listener
	 *
	 * @param actionListener listener
	 */
	void setListener(final ActionListener actionListener);

	/**
	 * Set style
	 *
	 * @param style style
	 */
	void setStyle(final String style);

	/**
	 * Show
	 */
	void display();
}
