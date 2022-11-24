/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.component;

import java.util.function.Function;

import treebolic.annotations.NonNull;
import treebolic.annotations.Nullable;
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
	// private static final long serialVersionUID = -6221942821143613741L;

	/**
	 * Put type
	 */
	public enum PutType
	{
		/**
		 * Tag as info
		 */
		INFO,
		/**
		 * Tag as link
		 */
		LINK,
		/**
		 * Tag as mount info
		 */
		MOUNT,
		/**
		 * Tag as search info
		 */
		SEARCH
	}

	// colors

	/**
	 * Label background color
	 */
	static private final Color[] backColor = new Color[PutType.values().length];

	/**
	 * Label foreground color
	 */
	static private final Color[] foreColor = new Color[PutType.values().length];

	// init
	static
	{
		for (int i = 0; i < PutType.values().length; i++)
		{
			Statusbar.backColor[i] = Color.WHITE;
			Statusbar.foreColor[i] = Color.BLACK;
		}
	}

	/**
	 * Constructor
	 *
	 * @param handle Handle required for component creation
	 */
	public Statusbar(final Object handle)
	{
		super(handle);

		init(null);
	}

	/**
	 * Init
	 *
	 * @param settings settings
	 */
	@SuppressWarnings("WeakerAccess")
	public void init(@Nullable @SuppressWarnings("SameParameterValue") final Settings settings)
	{
		// colors
		if (settings != null)
		{
			if (settings.backColor != null)
			{
				Statusbar.backColor[PutType.INFO.ordinal()] = settings.backColor;
				Statusbar.backColor[PutType.LINK.ordinal()] = settings.backColor;
				Statusbar.backColor[PutType.MOUNT.ordinal()] = settings.backColor;
				Statusbar.backColor[PutType.SEARCH.ordinal()] = settings.backColor;
			}
			if (settings.foreColor != null)
			{
				Statusbar.foreColor[PutType.INFO.ordinal()] = settings.foreColor;
				Statusbar.foreColor[PutType.LINK.ordinal()] = settings.foreColor;
				Statusbar.foreColor[PutType.MOUNT.ordinal()] = settings.foreColor;
				Statusbar.foreColor[PutType.SEARCH.ordinal()] = settings.foreColor;
			}
		}

		// super
		super.init(PutType.INFO.ordinal());
	}

	private void setColors(@NonNull final PutType type)
	{
		final Color backColor = Statusbar.backColor[type.ordinal()];
		final Color foreColor = Statusbar.foreColor[type.ordinal()];
		setColors(backColor, foreColor);
	}

	/**
	 * Put status
	 *
	 * @param type      status type as per below
	 * @param converter converter
	 * @param label     label
	 * @param contents  contents
	 */
	public void put(@NonNull final PutType type, @Nullable final Function<CharSequence[], String> converter, final String label, final String[] contents)
	{
		setColors(type);
		super.put(type.ordinal(), converter, label, contents);
	}

	/*
	 * Put status
	 *
	 * @param message
	 *            content
	 */
	// public void put(final String message);
}
