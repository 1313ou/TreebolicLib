package treebolic;

import java.net.URL;

/**
 * Locator interface
 *
 * @author Bernard Bou
 */
public interface ILocator
{
	/**
	 * Get base URL
	 *
	 * @return base url
	 */
	URL getBase();

	/**
	 * Get images base URL
	 *
	 * @return images base url
	 */
	URL getImagesBase();
}
