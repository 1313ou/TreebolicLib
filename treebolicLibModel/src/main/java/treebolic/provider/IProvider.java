/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.provider;

import java.net.URL;
import java.util.Properties;

import treebolic.ILocator;
import treebolic.annotations.Nullable;
import treebolic.model.Model;
import treebolic.model.Tree;

/**
 * Provider interface
 *
 * @author Bernard Bou
 */
public interface IProvider
{
	/**
	 * Called by framework to pass context reference (may default to void action if provider does not need it)
	 *
	 * @param locator locator context
	 */
	@SuppressWarnings("EmptyMethod")
	void setLocator(@SuppressWarnings("unused") ILocator locator);

	/**
	 * Called by framework to pass provider context reference (may default to void action if provider does not need it)
	 *
	 * @param providerContext provider context
	 */
	@SuppressWarnings("EmptyMethod")
	void setContext(IProviderContext providerContext);

	/**
	 * Called by framework to pass provider handle (may default to void action if provider does not need it)
	 *
	 * @param handle handle as passed to widget
	 */
	@SuppressWarnings("EmptyMethod")
	void setHandle(@SuppressWarnings("unused") Object handle);

	/**
	 * Make model
	 *
	 * @param source     source (a string that refers to the data in a way that the provider implementation will understand)
	 * @param base       base (base for source)
	 * @param parameters extra parameters
	 * @return model
	 */
	@Nullable
	Model makeModel(String source, URL base, Properties parameters);

	/**
	 * Make tree. Settings are not parsed. This is used in mounting.
	 *
	 * @param source         source (a string that refers to the data in a way that the provider implementation will understand)
	 * @param base           base (base for source)
	 * @param parameters     extra parameters
	 * @param checkRecursion whether immediate recursion is checked (avoid mount-now infinite recursion)
	 * @return tree
	 */
	@Nullable
	Tree makeTree(String source, URL base, Properties parameters, @SuppressWarnings("SameParameterValue") boolean checkRecursion);
}
