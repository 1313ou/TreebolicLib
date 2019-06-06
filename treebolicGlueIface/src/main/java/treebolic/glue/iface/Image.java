package treebolic.glue.iface;

/**
 * Glue interface for
 *
 * @author Bernard Bou
 */
public interface Image
{
	// static public Image make(URL resource) throws IOException;

	// static public Image try_make(URL resource);

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
