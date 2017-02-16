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
	static public URL makeURL(final String thisSource, final URL thisBase, @SuppressWarnings("unused") final Properties theseExtras, final IProviderContext thisContext)
	{
		final boolean debug = true;
		if (thisSource == null)
		{
			if (debug)
			{
				thisContext.warn("URL= null (null source)"); //$NON-NLS-1$
			}
			return null;
		}

		// try to consider it well-formed full-fledged url
		try
		{
			final URL thisUrl = new URL(thisSource);
			if (debug)
			{
				thisContext.message("URL=" + thisUrl.toString()); //$NON-NLS-1$
			}
			return thisUrl;
		}
		catch (final MalformedURLException e)
		{
			// do nothing
		}

		// default to source relative to a base
		try
		{
			final URL thisUrl = new URL(thisBase, thisSource);
			if (debug)
			{
				thisContext.message("URL=" + thisUrl.toString()); // + " from BASE URL=" + thisBase.toString()); //$NON-NLS-1$
			}
			return thisUrl;
		}
		catch (final MalformedURLException e)
		{
			// do nothing
		}
		if (debug)
		{
			thisContext.warn("URL= null (fail)"); //$NON-NLS-1$
		}
		return null;
	}
}
