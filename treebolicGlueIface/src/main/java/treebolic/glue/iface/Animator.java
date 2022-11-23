/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.glue.iface;

/**
 * Glue interface for Animator
 *
 * @param <A> platform action listener
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
	boolean run(A animation, @SuppressWarnings("unused") int steps, int startDelay);

	/**
	 * Running status
	 *
	 * @return running status
	 */
	boolean isRunning();
}
