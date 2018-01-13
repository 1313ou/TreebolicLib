package treebolic.provider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * Provider utils
 *
 * @author Bernard Bou
 */
public class ProviderUtils
{
	static public URL makeURL(final String thisSource, final URL thisBase, final Properties theseExtras, final IProviderContext thisContext)
	{
		final boolean DEBUG = true;
		if (thisSource == null)
		{
			// noinspection ConstantConditions
			if (DEBUG)
			{
				thisContext.warn("URL= null (null source)");
			}
			return null;
		}

		// try to consider it well-formed full-fledged url
		try
		{
			final URL thisUrl = new URL(thisSource);
			//noinspection ConstantConditions
			if (DEBUG)
			{
				thisContext.message("URL=" + thisUrl.toString());
			}
			return thisUrl;
		}
		catch (final MalformedURLException ignored)
		{
			// do nothing
		}

		// default to source relative to a base
		try
		{
			final URL thisUrl = new URL(thisBase, thisSource);
			//noinspection ConstantConditions
			if (DEBUG)
			{
				thisContext.message("URL=" + thisUrl.toString()); // + " from BASE URL=" + thisBase.toString());
			}
			return thisUrl;
		}
		catch (final MalformedURLException ignored)
		{
			// do nothing
		}
		//noinspection ConstantConditions
		if (DEBUG)
		{
			thisContext.warn("URL= null (fail)");
		}
		return null;
	}
}
