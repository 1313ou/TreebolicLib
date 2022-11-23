/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.glue.iface.component;

/**
 * Glue interface for Progress panel
 *
 * @author Bernard Bou
 */
public interface Progress
{
	/**
	 * Put progress
	 *
	 * @param message message to display
	 * @param fail    whether fail condition has been reached
	 */
	void put(final String message, final boolean fail);
}
