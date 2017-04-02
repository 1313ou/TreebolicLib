package treebolic.provider;

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
	 * @param thisContext
	 *            context
	 */
	void setup(ILocator thisContext);

	/**
	 * Called by framework to pass provider context reference (may default to void action if provider does not need it)
	 *
	 * @param thisProviderContext
	 *            provider context
	 */
	void setup(IProviderContext thisProviderContext);

	/**
	 * Make model
	 *
	 * @param thisSource
	 *            source (a string that refers to the data in a was that the provider implementation will understand)
	 * @param thisBase
	 *            base (base for source)
	 * @param theseParameters
	 *            extra parameters
	 * @return model
	 */
	Model makeModel(String thisSource, URL thisBase, Properties theseParameters);

	/**
	 * Make tree. Settings are not parsed. This is used in mounting.
	 *
	 * @param thisSource
	 *            source (a string that refers to the data in a was that the provider implementation will understand)
	 * @param thisBase
	 *            base (base for source)
	 * @param theseParameters
	 *            extra parameters
	 * @param checkRecursion
	 *            whether immediate recursion is checked (avoid mount-now infinite recursion)
	 * @return tree
	 */
	Tree makeTree(String thisSource, URL thisBase, Properties theseParameters, boolean checkRecursion);
}
