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
