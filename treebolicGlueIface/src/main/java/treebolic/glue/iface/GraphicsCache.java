package treebolic.glue.iface;

import android.support.annotation.NonNull;

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
