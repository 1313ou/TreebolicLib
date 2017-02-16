/**
 * Title : Treebolic
 * Description : Treebolic
 * Version : 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 *
 * Update : Mon Mar 10 00:00:00 CEST 2008
 */
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
	public Properties getParameters();

	/**
	 * Get style
	 *
	 * @return style string (CSS syntax)
	 */
	public String getStyle();

	/**
	 * Navigate to link
	 *
	 * @param thisLinkUrl
	 *            link url
	 * @param thisLinkTarget
	 *            link target
	 * @return true if the context handles the link, false means the current provider will re-init () with the link as source
	 */
	public boolean linkTo(String thisLinkUrl, String thisLinkTarget);

	/**
	 * Get input
	 *
	 * @return input
	 */
	public String getInput();

	/**
	 * Message
	 *
	 * @param thisString
	 *            message
	 */
	public void status(String thisString);

	/**
	 * Warn
	 *
	 * @param thisMessage
	 *            warning
	 */
	public void warn(String thisMessage);
}
