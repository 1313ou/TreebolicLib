/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic;

import java.net.URL;

import treebolic.annotations.Nullable;

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
