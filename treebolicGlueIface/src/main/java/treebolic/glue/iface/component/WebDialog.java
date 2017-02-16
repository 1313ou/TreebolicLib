package treebolic.glue.iface.component;

import treebolic.glue.iface.ActionListener;

/**
 * Glue interface for WebDialog
 *
 * @author Bernard Bou
 */
public interface WebDialog
{
	/**
	 * Set header and content
	 *
	 * @param header
	 *            header
	 * @param content
	 *            content
	 */
	void set(String header, String content);

	/**
	 * Set hyperlink listener
	 *
	 * @param thisActionListener
	 *            listener
	 */
	void setListener(final ActionListener thisActionListener);

	/**
	 * Set style
	 *
	 * @param style
	 *            style
	 */
	void setStyle(String style);

	/**
	 * Show
	 */
	void display();
}
