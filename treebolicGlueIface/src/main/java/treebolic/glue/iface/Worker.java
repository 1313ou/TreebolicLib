/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

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
