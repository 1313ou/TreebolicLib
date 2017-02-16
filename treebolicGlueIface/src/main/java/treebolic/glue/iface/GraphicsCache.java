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
	public G getGraphics();

	/**
	 * Put cache to graphics context
	 *
	 * @param g
	 *            graphics context
	 */
	public void put(G g);
}
