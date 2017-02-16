package treebolic.glue.iface;

/**
 * Glue interface for worker thread
 *
 * @author Bernard Bou
 */
public interface Worker
{
	/**
	 * Job to do
	 *
	 * @throws Exception
	 */
	void job();

	/**
	 * Done callback
	 */
	void onDone();

	/**
	 * Start execution
	 */
	void execute();
}
