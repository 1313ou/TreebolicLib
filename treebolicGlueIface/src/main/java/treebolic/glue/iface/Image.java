/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.glue.iface;

/**
 * Glue interface for
 *
 * @author Bernard Bou
 */
public interface Image
{
	// public Image(URL resource);

	/**
	 * Image width
	 *
	 * @return image width
	 */
	int getWidth();

	/**
	 * Image height
	 *
	 * @return height
	 */
	int getHeight();
}
