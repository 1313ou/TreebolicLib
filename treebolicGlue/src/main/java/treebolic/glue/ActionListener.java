package treebolic.glue;

/**
 * ActionListener
 *
 * @author Bernard Bou
 */
public abstract class ActionListener implements treebolic.glue.iface.ActionListener
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.glue.iface.ActionListener#onAction(java.lang.Object[])
	 */
	@Override
	abstract public boolean onAction(Object... params);
}
