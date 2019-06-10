package treebolic.provider;

import androidx.annotation.Nullable;

import java.net.URL;
import java.util.Properties;

import treebolic.ILocator;
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
	void setLocator(ILocator locator);

	/**
	 * Called by framework to pass provider context reference (may default to void action if provider does not need it)
	 *
	 * @param providerContext provider context
	 */
	void setContext(IProviderContext providerContext);

	/**
	 * Called by framework to pass provider handle (may default to void action if provider does not need it)
	 *
	 * @param handle handle as passed to widget
	 */
	@SuppressWarnings("EmptyMethod")
	void setHandle(Object handle);

	/**
	 * Make model
	 *
	 * @param source     source (a string that refers to the data in a was that the provider implementation will understand)
	 * @param base       base (base for source)
	 * @param parameters extra parameters
	 * @return model
	 */
	@Nullable
	Model makeModel(String source, URL base, Properties parameters);

	/**
	 * Make tree. Settings are not parsed. This is used in mounting.
	 *
	 * @param source         source (a string that refers to the data in a was that the provider implementation will understand)
	 * @param base           base (base for source)
	 * @param parameters     extra parameters
	 * @param checkRecursion whether immediate recursion is checked (avoid mount-now infinite recursion)
	 * @return tree
	 */
	@Nullable
	Tree makeTree(String source, URL base, Properties parameters, @SuppressWarnings("SameParameterValue") boolean checkRecursion);
}
