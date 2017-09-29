package treebolic.glue.iface.component;

/**
 * Glue interface for Statusbar
 *
 * @author Bernard Bou
 */
public interface Statusbar<C, L>
{
	// public Statusbar();

	/**
	 * Init
	 *
	 * @param image image
	 */
	void init(final int image);

	/**
	 * Set colors
	 *
	 * @param backColor back color
	 * @param foreColor fore color
	 */
	@SuppressWarnings("EmptyMethod")
	void setColors(@SuppressWarnings("UnusedParameters") C backColor, @SuppressWarnings("UnusedParameters") C foreColor);

	/**
	 * Set style
	 *
	 * @param style style
	 */
	void setStyle(String style);

	/**
	 * Put status
	 *
	 * @param label   label
	 * @param content content
	 * @param image   image
	 */
	void put(final String label, final String content, final int image);

	/**
	 * Put message
	 *
	 * @param message message
	 */
	void put(final String message);

	/**
	 * Add listener
	 *
	 * @param listener listener
	 */
	@SuppressWarnings("EmptyMethod")
	void addListener(@SuppressWarnings("UnusedParameters") final L listener);
}
