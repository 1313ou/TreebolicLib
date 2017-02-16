package treebolic.glue.iface.component;

/**
 * Glue interface for Surface (basis to view)
 *
 * @author Bernard Bou
 */
@SuppressWarnings("EmptyMethod")
public interface Surface<G, L>
{
	/**
	 * Paint
	 *
	 * @param g
	 *            graphics context
	 */
	void paint(final G g);

	/**
	 * Repaint (triggers repaint action)
	 */
	void repaint();

	/**
	 * Width
	 *
	 * @return surface width
	 */
	int getWidth();

	/**
	 * Height
	 *
	 * @return surface height
	 */
	int getHeight();

	/**
	 * Default cursor type
	 */
	int DEFAULTCURSOR = 0;

	/**
	 * Hot node cursor type
	 */
	int HOTCURSOR = 1;

	/**
	 * Set cursor
	 *
	 * @param cursor
	 *            cursor type (one of the constants)
	 */
	void setCursor(final int cursor);

	/**
	 * Set tooltip
	 *
	 * @param string
	 *            tooltip string
	 */
	void setToolTipText(final String string);

	/**
	 * Add event listener
	 *
	 * @param listener
	 *            listener
	 */
	void addEventListener(final L listener);

	/**
	 * Set whether to fire hover events
	 *
	 * @param flag
	 *            whether to fire hover events
	 */
	void setFireHover(final boolean flag);
}
