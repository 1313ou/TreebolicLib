package treebolic.glue.iface;

/**
 * Glue interface for ActionListener
 *
 * @author Bernard Bou
 */
public interface ActionListener
{
	/**
	 * Action callback
	 *
	 * @param theseParams parameter
	 * @return true if handled
	 */
	@SuppressWarnings("UnusedReturnValue")
	boolean onAction(Object... theseParams);
}
