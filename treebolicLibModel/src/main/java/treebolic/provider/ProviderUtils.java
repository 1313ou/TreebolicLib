package treebolic.provider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * Provider utils
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class ProviderUtils
{
	static public URL makeURL(@Nullable final String source, final URL base, final Properties extras, @NonNull final IProviderContext context)
	{
		final boolean DEBUG = true;
		if (source == null)
		{
			// noinspection ConstantConditions
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
			//noinspection ConstantConditions
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
			//noinspection ConstantConditions
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
		//noinspection ConstantConditions
		if (DEBUG)
		{
			context.warn("URL= null (fail)");
		}
		return null;
	}
}
