package treebolic.glue.iface.component;

/**
 * Glue interface for Toolbar
 *
 * @author Bernard Bou
 */
public interface Toolbar<L>
{
	/**
	 * Add button
	 *
	 * @param iconIndex
	 *            icon index
	 * @param toolTip
	 *            button tip
	 * @param listener
	 *            listener
	 */
	public void addButton(final int iconIndex, final String toolTip, final L listener);

	/**
	 * Add toggle button
	 *
	 * @param iconIndex
	 *            icon index
	 * @param selectedIconIndex
	 *            selected state icon index
	 * @param toolTip
	 *            button tip
	 * @param state
	 *            initial state
	 * @param listener
	 *            listener
	 */
	public void addToggle(final int iconIndex, final int selectedIconIndex, final String toolTip, final boolean state, final L listener);

	/**
	 * Add separator
	 */
	public void addSeparator();
}
