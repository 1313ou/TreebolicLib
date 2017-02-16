/**
 * Title : Treebolic
 * Description : Treebolic
 * Version : 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 *
 * Update : Mon Mar 10 00:00:00 CEST 2008
 */
package treebolic.component;

import android.annotation.SuppressLint;

import treebolic.glue.component.Component;

/**
 * Progress panel
 *
 * @author Bernard Bou
 */
@SuppressLint("ViewConstructor")
public class Progress extends treebolic.glue.component.Progress implements Component
{
	/**
	 * Constructor
	 *
	 * @param thisHandle
	 *            Handle required for component creation
	 */
	public Progress(final Object thisHandle)
	{
		super(thisHandle);
	}

	/**
	 * Put message
	 *
	 * @param thisMessage
	 *            message
	 */
	// public void put(final String thisMessage, final boolean fail);
}
