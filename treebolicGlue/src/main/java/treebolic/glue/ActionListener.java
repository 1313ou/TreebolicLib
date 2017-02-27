package treebolic.glue;

/**
 * ActionListener
 *
 * @author Bernard Bou
 */
public abstract class ActionListener implements treebolic.glue.iface.ActionListener
{

	@Override
	abstract public boolean onAction(Object... params);
}
