/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.glue.iface.component;

import java.util.function.Function;

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
	void setHandle(@SuppressWarnings("unused") final Object handle);

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
	void setConverter(final Function<CharSequence[], String> converter);

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
