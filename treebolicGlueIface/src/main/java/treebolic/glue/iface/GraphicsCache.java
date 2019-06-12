/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.glue.iface;

import androidx.annotation.NonNull;

/**
 * Glue interface for GraphicsCache
 *
 * @author Bernard Bou
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
