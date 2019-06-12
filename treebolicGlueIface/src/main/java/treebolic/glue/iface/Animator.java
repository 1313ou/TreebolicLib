/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.glue.iface;

/**
 * Glue interface for Animator
 *
 * @author Bernard Bou
 */
public interface Animator<A>
{
	/**
	 * Run animation
	 *
	 * @param animation  animation
	 * @param steps      number of steps
	 * @param startDelay start delay
	 * @return true if successful
	 */
	@SuppressWarnings("SameReturnValue")
	boolean run(A animation, int steps, int startDelay);

	/**
	 * Running status
	 *
	 * @return running status
	 */
	boolean isRunning();
}
