/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
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
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 4362329721644133327L;

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
