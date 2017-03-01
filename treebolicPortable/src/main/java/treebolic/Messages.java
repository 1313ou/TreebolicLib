/**
 * Title : Treebolic
 * Description: Treebolic
 * Version: 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 * Update : Jan 1, 2016
 */

package treebolic;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

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

	public static String getString(String key)
	{
		try
		{
			return RESOURCE_BUNDLE.getString(key);
		}
		catch (MissingResourceException e)
		{
			return '!' + key + '!';
		}
	}
}
