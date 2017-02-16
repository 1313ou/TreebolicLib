package treebolic.glue.iface;

/**
 * Glue interface for
 *
 * @author Bernard Bou
 */
public interface Color<C>
{
	/**
	 * Set value
	 *
	 * @param r
	 *            red
	 * @param g
	 *            green
	 * @param b
	 *            blue
	 */
	public void set(int r, int g, int b);

	/**
	 * Set value
	 *
	 * @param rgb
	 *            value
	 */
	public void set(int rgb);

	/**
	 * Get value
	 *
	 * @return value
	 */
	public int getRGB();

	/**
	 * Whether color is null
	 *
	 * @return true if color is null
	 */
	public boolean isNull();

	/**
	 * Parse color from string
	 *
	 * @param string
	 *            string to parse
	 */
	public void parse(final String string);

	/**
	 * Make brighter color
	 *
	 * @return brighter color
	 */
	public C makeBrighter();

	/**
	 * Make darker color
	 *
	 * @return darker color
	 */
	public C makeDarker();
}
