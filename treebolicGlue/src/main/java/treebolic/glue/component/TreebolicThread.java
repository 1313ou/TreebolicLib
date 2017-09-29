package treebolic.glue.component;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import treebolic.glue.Graphics;

public class TreebolicThread extends Thread
{
	/**
	 * Log tag
	 */
	static private final boolean LOG = false;
	static private final String TAG = "Treebolic Thread";

	/**
	 * Draw cycle number
	 */
	static private int drawCycle = 0;

	// M E M B E R S

	/**
	 * Handle to the surface
	 */
	private Surface surface;

	/**
	 * Handle to the surface manager object we interact with
	 */
	private SurfaceHolder surfaceHolder;

	/**
	 * Synchronizer
	 */
	final private Object synchronizer;

	/**
	 * Sleep lock
	 */
	private final Object lock = new Object();

	/**
	 * Whether the thread should exit service loop
	 */
	private volatile boolean terminateFlag = true;

	/**
	 * Whether the thread should wait for job within service loop
	 */
	private volatile boolean pauseFlag = true;

	/**
	 * Constructor
	 *
	 * @param surface0
	 *            surface
	 * @param surfaceHolder0
	 *            surface holder
	 */
	public TreebolicThread(final Surface surface0, final SurfaceHolder surfaceHolder0)
	{
		// get handles
		this.surface = surface0;
		this.surfaceHolder = surfaceHolder0;
		this.synchronizer = new Object();
	}

	/**
	 * Used to signal the thread whether it should be running or not. Passing true allows the thread to run; passing false will shut it down if it's already
	 * running. Calling start() after this was most recently called with false will result in an immediate shutdown.
	 *
	 * @param terminateFlag0
	 *            true to run, false to shut down
	 */
	public void setTerminate(@SuppressWarnings("SameParameterValue") final boolean terminateFlag0)
	{
		this.terminateFlag = terminateFlag0;
	}

	/**
	 * Pause thread and wait for task
	 */
	@SuppressWarnings("WeakerAccess")
	public void terminate()
	{
		this.terminateFlag = true;
		unpause();
	}

	/**
	 * Wait for termination
	 */
	public void waitForTermination()
	{
		// tell thread to shut down
		boolean retry = true;
		terminate();

		// and wait for it to finish
		while (retry)
		{
			try
			{
				join();
				retry = false;
			}
			catch (final InterruptedException e)
			{
				// do nothing
			}
		}
	}

	/*
	  Pause thread and wait for task (unused)
	 */
//	public void pause()
//	{
//		this.pauseFlag = true;
//	}

	/**
	 * Resume from a pause
	 */
	public void unpause()
	{
		synchronized (this.lock)
		{
			this.pauseFlag = false;
			this.lock.notify();
		}
		if (LOG)
			Log.d(TreebolicThread.TAG, "wake up");
	}

	@Override
	public void run()
	{
		while (!this.terminateFlag)
		{
			// draw cycle
			Canvas canvas = null;
			try
			{
				canvas = this.surfaceHolder.lockCanvas(null);
				synchronized (this.synchronizer)
				{
					if (LOG)
						Log.d(TreebolicThread.TAG, "task started " + ++TreebolicThread.drawCycle);
					doDraw(canvas);
					if (LOG)
						Log.d(TreebolicThread.TAG, "task done " + TreebolicThread.drawCycle);
				}
			}
			finally
			{
				// do this in a finally so that if an exception is thrown during the above, we don't leave the Surface in an inconsistent state
				if (canvas != null)
				{
					this.surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}

			// pause
			// we do not pause if we have been signaled in the mean time
			if (LOG)
				Log.d(TreebolicThread.TAG, "pause");
			if (this.pauseFlag)
			{
				try
				{
					synchronized (this.lock)
					{
						while (this.pauseFlag && !this.terminateFlag)
						{
							this.lock.wait();
						}
					}
				}
				catch (final InterruptedException e)
				{
					Log.d(TreebolicThread.TAG, "interrupted");
				}
				
				if (LOG)
					Log.d(TreebolicThread.TAG, "resume");
			}

			// for next round
			this.pauseFlag = true;
		}

		// exiting thread : release references to surface
		this.surface = null;
		this.surfaceHolder = null;

		if (LOG)
			Log.d(TreebolicThread.TAG, "terminated");
	}

	/**
	 * Draws to the provided Canvas.
	 */
	private void doDraw(final Canvas canvas)
	{
		// Draw the background image. Operations on the Canvas accumulate so this is like clearing the screen.
		if (canvas != null)
		{
			canvas.save();

			// Paint thisPaint = new Paint();
			// thisPaint.setColor(Color.LTGRAY);
			// int w = canvas.getWidth() / 2;
			// int h = canvas.getHeight() / 2;
			// canvas.drawCircle(w, h, Math.min(w, h), thisPaint);

			final Graphics g = new Graphics(canvas);
			this.surface.paint(g);

			canvas.restore();
		}
	}
}