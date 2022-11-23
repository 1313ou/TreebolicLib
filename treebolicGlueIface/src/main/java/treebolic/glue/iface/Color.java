/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.glue.iface;

import treebolic.annotations.NonNull;

/**
 * Glue interface for
 *
 * @author Bernard Bou
 * @param <C> platform color type
 */
public interface Color<C>
{
	/**
	 * Set value
	 *
	 * @param r red
	 * @param g green
	 * @param b blue
	 */
	void set(int r, int g, int b);

	/**
	 * Set value
	 *
	 * @param rgb value
	 */
	void set(int rgb);

	/**
	 * Get value
	 *
	 * @return value
	 */
	int getRGB();

	/**
	 * Whether color is null
	 *
	 * @return true if color is null
	 */
	boolean isNull();

	/**
	 * Parse color from string
	 *
	 * @param string string to parse
	 */
	void parse(final String string);

	/**
	 * Make brighter color
	 *
	 * @return brighter color
	 */
	@NonNull
	C makeBrighter();

	/**
	 * Make darker color
	 *
	 * @return darker color
	 */
	@NonNull
	C makeDarker();
}
