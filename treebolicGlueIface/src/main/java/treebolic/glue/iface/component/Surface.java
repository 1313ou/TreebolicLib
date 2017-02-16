package treebolic.glue.iface.component;

/**
 * Glue interface for Surface (basis to view)
 *
 * @author Bernard Bou
 */
public interface Surface<G, L>
{
	/**
	 * Paint
	 *
	 * @param g
	 *            graphics context
	 */
	public void paint(final G g);

	/**
	 * Repaint (triggers repaint action)
	 */
	public void repaint();

	/**
	 * Width
	 *
	 * @return surface width
	 */
	public int getWidth();

	/**
	 * Height
	 *
	 * @return surface height
	 */
	public int getHeight();

	/**
	 * Default cursor type
	 */
	public static final int DEFAULTCURSOR = 0;

	/**
	 * Hot node cursor type
	 */
	public static final int HOTCURSOR = 1;

	/**
	 * Set cursor
	 *
	 * @param cursor
	 *            cursor type (one of the constants)
	 */
	public void setCursor(final int cursor);

	/**
	 * Set tooltip
	 *
	 * @param string
	 *            tooltip string
	 */
	public void setToolTipText(final String string);

	/**
	 * Add event listener
	 *
	 * @param listener
	 *            listener
	 */
	public void addEventListener(final L listener);

	/**
	 * Set whether to fire hover events
	 *
	 * @param flag
	 *            whether to fire hover events
	 */
	public void setFireHover(final boolean flag);
}
