package treebolic;

import androidx.annotation.Nullable;

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
	@Nullable
	Properties getParameters();

	/**
	 * Get style
	 *
	 * @return style string (CSS syntax)
	 */
	@Nullable
	String getStyle();

	/**
	 * Navigate to link
	 *
	 * @param linkUrl    link url
	 * @param linkTarget link target
	 * @return true if the context handles the link, false means the current provider will re-init () with the link as source
	 */
	boolean linkTo(String linkUrl, String linkTarget);

	/**
	 * Get input
	 *
	 * @return input
	 */
	@Nullable
	String getInput();

	/**
	 * Message
	 *
	 * @param message message
	 */
	void status(String message);

	/**
	 * Warn
	 *
	 * @param message warning
	 */
	void warn(String message);
}
