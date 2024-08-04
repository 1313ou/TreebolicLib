/*
 * Copyright (c) 2019-2023. Bernard Bou
 */

package treebolic.glue;

/**
 * ActionListener
 *
 * @author Bernard Bou
 */
public abstract class ActionListener implements treebolic.glue.iface.ActionListener
{
	@SuppressWarnings("UnusedReturnValue")
	@Override
	abstract public boolean onAction(Object... params);
}
