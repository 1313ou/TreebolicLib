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
package treebolic.provider;

/**
 * Provider context
 *
 * @author Bernard Bou
 */
public interface IProviderContext
{
	/**
	 * Put status string (such as error)
	 *
	 * @param thisString
	 *            message
	 */
	public void message(String thisString);

	/**
	 * Warn
	 *
	 * @param thisMessage
	 *            warning
	 */
	public void warn(String thisMessage);

	/**
	 * Put progress
	 *
	 * @param thisString
	 *            message
	 */
	public void progress(String thisString, boolean fail);
}
