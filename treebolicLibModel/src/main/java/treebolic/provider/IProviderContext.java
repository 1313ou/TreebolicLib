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
	void message(String thisString);

	/**
	 * Warn
	 *
	 * @param thisMessage
	 *            warning
	 */
	void warn(String thisMessage);

	/**
	 * Put progress
	 *
	 * @param thisString
	 *            message
	 */
	void progress(String thisString, boolean fail);
}
