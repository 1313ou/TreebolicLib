package treebolic.glue.iface;

/**
 * Glue interface for
 *
 * @author Bernard Bou
 */
public interface Image
{
	// static public Image make(URL thisResource) throws IOException;

	// static public Image try_make(URL thisResource);

	/**
	 * Image width
	 *
	 * @return image width
	 */
	public int getWidth();

	/**
	 * Image height
	 *
	 * @return height
	 */
	public int getHeight();
}
