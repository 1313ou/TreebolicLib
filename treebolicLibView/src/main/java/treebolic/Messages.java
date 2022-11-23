/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import treebolic.annotations.NonNull;

/**
 * Language dependency
 *
 * @author Bernard Bou
 */
public class Messages
{
	private static final String BUNDLE_NAME = "treebolic.messages";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private Messages()
	{
	}

	/**
	 * Get localized message
	 *
	 * @param key message key
	 * @return localized message
	 */
	@NonNull
	public static String getString(@NonNull String key)
	{
		try
		{
			return RESOURCE_BUNDLE.getString(key);
		}
		catch (MissingResourceException ignored)
		{
			return '!' + key + '!';
		}
	}
}
