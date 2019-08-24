/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
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
