package treebolic;

import java.net.URL;

import androidx.annotation.Nullable;

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
