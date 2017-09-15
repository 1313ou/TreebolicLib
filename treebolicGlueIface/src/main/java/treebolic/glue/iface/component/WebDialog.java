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
	 * Set handle
	 *
	 * @param handle (opaque) handle
	 */
	void setHandle(final Object handle);

	/**
	 * Set header and content
	 *
	 * @param header  header
	 * @param content content
	 */
	void set(final String header, final String content);

	/**
	 * Set hyperlink listener
	 *
	 * @param thisActionListener listener
	 */
	void setListener(final ActionListener thisActionListener);

	/**
	 * Set style
	 *
	 * @param style style
	 */
	void setStyle(final String style);

	/**
	 * Show
	 */
	void display();
}
