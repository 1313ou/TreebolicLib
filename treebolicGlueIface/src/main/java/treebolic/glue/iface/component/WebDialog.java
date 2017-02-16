package treebolic.glue.iface.component;

import treebolic.glue.iface.ActionListener;

/**
 * Glue interface for WebDialg
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
	public void set(String header, String content);

	/**
	 * Set hyperlink listener
	 *
	 * @param thisActionListener
	 *            listener
	 */
	public void setListener(final ActionListener thisActionListener);

	/**
	 * Set style
	 *
	 * @param style
	 *            style
	 */
	public void setStyle(String style);

	/**
	 * Show
	 */
	public void display();
}
