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
	public void job() throws Exception;

	/**
	 * Done callback
	 */
	public void onDone();

	/**
	 * Start execution
	 */
	public void execute();
}
