/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.component;

import treebolic.glue.component.Component;

/**
 * Progress panel
 *
 * @author Bernard Bou
 */
public class Progress extends treebolic.glue.component.Progress implements Component
{
	/**
	 * Constructor
	 *
	 * @param handle handle required for component creation
	 */
	public Progress(final Object handle)
	{
		super(handle);
	}

	/*
	 * Put message
	 *
	 * @param message
	 *            message
	 */
	// public void put(final String message, final boolean fail);
}
