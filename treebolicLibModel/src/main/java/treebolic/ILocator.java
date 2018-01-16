package treebolic;

import android.support.annotation.Nullable;

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
	@Nullable
	URL getBase();

	/**
	 * Get images base URL
	 *
	 * @return images base url
	 */
	@Nullable
	URL getImagesBase();
}
