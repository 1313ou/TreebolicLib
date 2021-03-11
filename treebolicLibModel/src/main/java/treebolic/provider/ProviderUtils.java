/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.provider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Provider utils
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class ProviderUtils
{
	private static final boolean DEBUG = false;

	@Nullable
	static public URL makeURL(@Nullable final String source, final URL base, final Properties extras, @NonNull final IProviderContext context)
	{
		if (source == null)
		{
			if (DEBUG)
			{
				context.warn("URL= null (null source)");
			}
			return null;
		}

		// try to consider it well-formed full-fledged url
		try
		{
			final URL url = new URL(source);
			if (DEBUG)
			{
				context.message("URL=" + url.toString());
			}
			return url;
		}
		catch (@NonNull final MalformedURLException ignored)
		{
			// do nothing
		}

		// default to source relative to a base
		try
		{
			final URL url = new URL(base, source);
			if (DEBUG)
			{
				context.message("URL=" + url.toString()); // + " from BASE URL=" + base.toString());
			}
			return url;
		}
		catch (@NonNull final MalformedURLException ignored)
		{
			// do nothing
		}
		if (DEBUG)
		{
			context.warn("URL= null (fail)");
		}
		return null;
	}
}
