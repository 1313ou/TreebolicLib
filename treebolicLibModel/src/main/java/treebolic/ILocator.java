package treebolic;

import android.support.annotation.NonNull;

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
	@NonNull
	URL getBase();

	/**
	 * Get images base URL
	 *
	 * @return images base url
	 */
	@NonNull
	URL getImagesBase();
}
