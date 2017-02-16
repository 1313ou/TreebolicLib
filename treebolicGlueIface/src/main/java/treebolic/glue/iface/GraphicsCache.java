package treebolic.glue.iface;

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
	G getGraphics();

	/**
	 * Put cache to graphics context
	 *
	 * @param g
	 *            graphics context
	 */
	void put(G g);
}
