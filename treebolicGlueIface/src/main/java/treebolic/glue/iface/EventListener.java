package treebolic.glue.iface;

/**
 * Glue interface for EventListener
 *
 * @author Bernard Bou
 */
public interface EventListener
{
	/**
	 * Down event callback
	 *
	 * @param x      screen x-coordinate
	 * @param y      screen y-coordinate
	 * @param rotate rotate requested
	 * @return true if handled
	 */
	@SuppressWarnings("SameReturnValue")
	boolean onDown(int x, int y, boolean rotate);

	/**
	 * Up event callback
	 *
	 * @param x screen x-coordinate
	 * @param y screen y-coordinate
	 * @return true if handled
	 */
	@SuppressWarnings("SameReturnValue")
	boolean onUp(int x, int y);

	/**
	 * Dragged/move event callback
	 *
	 * @param x screen x-coordinate
	 * @param y screen y-coordinate
	 * @return true if handled
	 */
	@SuppressWarnings("SameReturnValue")
	boolean onDragged(int x, int y);

	/**
	 * Select event callback
	 *
	 * @param x screen x-coordinate
	 * @param y screen y-coordinate
	 * @return true if handled
	 */
	@SuppressWarnings("SameReturnValue")
	boolean onSelect(int x, int y);

	/**
	 * Hover event callback
	 *
	 * @param x screen x-coordinate
	 * @param y screen y-coordinate
	 * @return true if handled
	 */
	boolean onHover(int x, int y);

	/**
	 * Long hover event callback
	 *
	 * @return true if handled
	 */
	boolean onLongHover();

	/**
	 * Focus-to-center event callback
	 *
	 * @param x screen x-coordinate
	 * @param y screen y-coordinate
	 * @return true if handled
	 */
	boolean onFocus(int x, int y);

	/**
	 * Context menu event callback
	 *
	 * @param x screen x-coordinate
	 * @param y screen y-coordinate
	 * @return true if handled
	 */
	boolean onMenu(int x, int y);

	/**
	 * Link event callback
	 *
	 * @param x screen x-coordinate
	 * @param y screen y-coordinate
	 * @return true if handled
	 */
	boolean onLink(int x, int y);

	/**
	 * Mount event callback
	 *
	 * @param x screen x-coordinate
	 * @param y screen y-coordinate
	 * @return true if handled
	 */
	boolean onMount(int x, int y);

	/**
	 * Zoom event callback
	 *
	 * @param zf zoom factor (>=0 absolute, <0 relative)
	 * @param zx zoom pivot x
	 * @param zy zoom pivot y
	 */
	void onZoom(float zf, float zx, float zy);

	/**
	 * Scale event callback
	 *
	 * @param msf map scale factor (>=0 absolute, <0 relative)
	 * @param fsf font scale factor (>=0 absolute, <0 relative)
	 * @param isf image scale factor (>=0 absolute, <0 relative)
	 */
	void onScale(float msf, float fsf, float isf);
}
