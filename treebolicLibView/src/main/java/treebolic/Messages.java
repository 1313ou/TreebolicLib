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
		catch (MissingResourceException ignored)
		{
			return '!' + key + '!';
		}
	}
}
