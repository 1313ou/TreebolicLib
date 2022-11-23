/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.glue.iface.component;

/**
 * Glue interface for Surface (basis to view)
 *
 * @param <G> platform graphics context type
 * @param <L> platform action listener type
 * @author Bernard Bou
 */
public interface Surface<G, L>
{
	/**
	 * Paint
	 *
	 * @param g graphics context
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
	 * @param cursor cursor type (one of the constants)
	 */
	@SuppressWarnings("EmptyMethod")
	void setCursor(final int cursor);

	/**
	 * Set tooltip
	 *
	 * @param string tooltip string
	 */
	@SuppressWarnings("EmptyMethod")
	void setToolTipText(@SuppressWarnings("unused") final String string);

	/**
	 * Add event listener
	 *
	 * @param listener listener
	 */
	void addEventListener(final L listener);

	/**
	 * Set whether to fire hover events
	 *
	 * @param flag whether to fire hover events
	 */
	void setFireHover(final boolean flag);

	/**
	 * Get factor used when evaluating min distance of node finder
	 * Best results depend on platform.
	 *
	 * @return factor used when evaluating min distance of node finder
	 */
	@SuppressWarnings("SameReturnValue")
	float getFinderDistanceEpsilonFactor();
}
