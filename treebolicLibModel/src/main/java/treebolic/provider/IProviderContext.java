/*
 * Copyright (c) 2019-2022. Bernard Bou
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
	 * @param message message
	 */
	void message(String message);

	/**
	 * Warn
	 *
	 * @param message warning
	 */
	void warn(String message);

	/**
	 * Put progress
	 *
	 * @param message message
	 * @param fail    whether failure occurred
	 */
	void progress(String message, boolean fail);
}
