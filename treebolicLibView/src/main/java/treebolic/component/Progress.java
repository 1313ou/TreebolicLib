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
	private static final long serialVersionUID = 4362329721644133327L;

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

	/*
	 * Put message
	 *
	 * @param thisMessage
	 *            message
	 */
	// public void put(final String thisMessage, final boolean fail);
}
