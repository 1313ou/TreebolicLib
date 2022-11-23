/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

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
	 * @param params parameters
	 * @return true if handled
	 */
	@SuppressWarnings({"UnusedReturnValue", "EmptyMethod"})
	boolean onAction(Object... params);
}
