/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

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
	@SuppressWarnings("SameReturnValue")
	@Nullable
	URL getBase();

	/**
	 * Get images base URL
	 *
	 * @return images base url
	 */
	@SuppressWarnings("SameReturnValue")
	@Nullable
	URL getImagesBase();
}
