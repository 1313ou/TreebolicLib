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

import treebolic.glue.Color;
import treebolic.glue.component.Component;
import treebolic.model.Settings;

/**
 * Status bar
 *
 * @author Bernard Bou
 */
public class Statusbar extends treebolic.glue.component.Statusbar implements Component
{
	/**
	 * Put type
	 */
	public static enum PutType
	{
		INFO, LINK, MOUNT, SEARCH
	}

	// colors

	/**
	 * Label background color
	 */
	static private Color[] theBackColor = new Color[PutType.values().length];

	/**
	 * Label foreground color
	 */
	static private Color[] theForeColor = new Color[PutType.values().length];

	// init
	static
	{
		for (int i = 0; i < PutType.values().length; i++)
		{
			Statusbar.theBackColor[i] = Color.WHITE;
			Statusbar.theForeColor[i] = Color.BLACK;
		}
	}

	/**
	 * Constructor
	 *
	 * @param thisHandle
	 *            Handle required for component creation
	 */
	public Statusbar(final Object thisHandle)
	{
		super(thisHandle);

		init(null);
	}

	public void init(final Settings theseSettings)
	{
		// colors
		if (theseSettings != null)
		{
			if (theseSettings.theBackColor != null)
			{
				Statusbar.theBackColor[PutType.INFO.ordinal()] = theseSettings.theBackColor;
				Statusbar.theBackColor[PutType.LINK.ordinal()] = theseSettings.theBackColor;
				Statusbar.theBackColor[PutType.MOUNT.ordinal()] = theseSettings.theBackColor;
				Statusbar.theBackColor[PutType.SEARCH.ordinal()] = theseSettings.theBackColor;
			}
			if (theseSettings.theForeColor != null)
			{
				Statusbar.theForeColor[PutType.INFO.ordinal()] = theseSettings.theForeColor;
				Statusbar.theForeColor[PutType.LINK.ordinal()] = theseSettings.theForeColor;
				Statusbar.theForeColor[PutType.MOUNT.ordinal()] = theseSettings.theForeColor;
				Statusbar.theForeColor[PutType.SEARCH.ordinal()] = theseSettings.theForeColor;
			}
		}

		// super
		super.init(PutType.INFO.ordinal());
	}

	private void setColors(final PutType thisType)
	{
		final Color thisBackColor = Statusbar.theBackColor[thisType.ordinal()];
		final Color thisForeColor = Statusbar.theForeColor[thisType.ordinal()];
		setColors(thisBackColor, thisForeColor);
	}

	/**
	 * Put status
	 *
	 * @param thisLabel
	 *            label
	 * @param thisContent
	 *            content
	 * @param thisType
	 *            status type as per below
	 */
	public void put(final String thisLabel, final String thisContent, final PutType thisType)
	{
		setColors(thisType);
		super.put(thisLabel, thisContent, thisType.ordinal());
	}

	/**
	 * Put status
	 *
	 * @param thisMessage
	 *            content
	 */
	// public void put(final String thisMessage);
}
