/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.glue.iface;

import treebolic.annotations.NonNull;

/**
 * Glue interface for GraphicsCache
 *
 * @author Bernard Bou
 * @param <G> platform graphics context
 */
public interface GraphicsCache<G>
{
	/**
	 * Obtain cache graphics context
	 *
	 * @return cache graphics context
	 */
	@NonNull
	G getGraphics();

	/**
	 * Put cache to graphics context
	 *
	 * @param g graphics context
	 */
	void put(G g);
}
