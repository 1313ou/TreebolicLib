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
	 * @param theseParams
	 *            parameter
	 * @return true if handled
	 */
	public boolean onAction(Object... theseParams);
}
