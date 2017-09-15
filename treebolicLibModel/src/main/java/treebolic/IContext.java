package treebolic;

import java.util.Properties;

/**
 * Context interface (to be implemented by the application/applet/window that is hosting the widget
 *
 * @author Bernard Bou
 */
public interface IContext extends ILocator
{
	/**
	 * Get parameters
	 *
	 * @return parameters
	 */
	Properties getParameters();

	/**
	 * Get style
	 *
	 * @return style string (CSS syntax)
	 */
	String getStyle();

	/**
	 * Navigate to link
	 *
	 * @param thisLinkUrl    link url
	 * @param thisLinkTarget link target
	 * @return true if the context handles the link, false means the current provider will re-init () with the link as source
	 */
	boolean linkTo(String thisLinkUrl, String thisLinkTarget);

	/**
	 * Get input
	 *
	 * @return input
	 */
	String getInput();

	/**
	 * Message
	 *
	 * @param thisString message
	 */
	void status(String thisString);

	/**
	 * Warn
	 *
	 * @param thisMessage warning
	 */
	void warn(String thisMessage);
}
