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
	 * @param image
	 *            image
	 */
	public void init(final int image);

	/**
	 * Set colors
	 *
	 * @param backColor
	 *            back color
	 * @param foreColor
	 *            fore color
	 */
	public void setColors(C backColor, C foreColor);

	/**
	 * Set style
	 *
	 * @param style
	 *            style
	 */
	public void setStyle(String style);

	/**
	 * Put status
	 *
	 * @param label
	 *            label
	 * @param content
	 *            content
	 * @param image
	 *            image
	 */
	public void put(final String label, final String content, final int image);

	/**
	 * Put message
	 *
	 * @param thisMessage
	 */
	public void put(final String thisMessage);

	/**
	 * Add listener
	 *
	 * @param listener
	 *            listener
	 */
	public void addListener(final L listener);
}
