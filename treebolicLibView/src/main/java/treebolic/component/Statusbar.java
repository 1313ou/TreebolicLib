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
	@SuppressWarnings("unused")
	private static final long serialVersionUID = -6221942821143613741L;

	/**
	 * Put type
	 */
	public enum PutType
	{
		INFO, LINK, MOUNT, SEARCH
	}

	// colors

	/**
	 * Label background color
	 */
	static private final Color[] theBackColor = new Color[PutType.values().length];

	/**
	 * Label foreground color
	 */
	static private final Color[] theForeColor = new Color[PutType.values().length];

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
	 * @param thisHandle Handle required for component creation
	 */
	public Statusbar(final Object thisHandle)
	{
		super(thisHandle);

		init(null);
	}

	@SuppressWarnings("WeakerAccess")
	public void init(@SuppressWarnings("SameParameterValue") final Settings theseSettings)
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
	 * @param thisLabel   label
	 * @param thisContent content
	 * @param thisType    status type as per below
	 */
	public void put(final String thisLabel, final String thisContent, final PutType thisType)
	{
		setColors(thisType);
		super.put(thisLabel, thisContent, thisType.ordinal());
	}

	/*
	 * Put status
	 *
	 * @param thisMessage
	 *            content
	 */
	// public void put(final String thisMessage);
}
