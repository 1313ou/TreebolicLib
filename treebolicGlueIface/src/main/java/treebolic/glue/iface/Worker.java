/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
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
