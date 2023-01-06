/*
 * Copyright (c) 2019-2023. Bernard Bou
 */

package treebolic.glue;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Worker thread
 *
 * @author Bernard Bou
 */
abstract public class Worker implements treebolic.glue.iface.Worker
{
	// E X E C U T O R

	private static final int CORE_POOL_SIZE = 5;

	private static final int MAXIMUM_POOL_SIZE = 32;

	private static final int KEEP_ALIVE = 1;

	private static final BlockingQueue<Runnable> POOL_WORK_QUEUE = new LinkedBlockingQueue<>(10);

	private static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, POOL_WORK_QUEUE);

	private static final Executor EXECUTOR = THREAD_POOL_EXECUTOR;

	// H A N D L E R

	private static final Handler HANDLER = new Handler(Looper.getMainLooper());

	// A B S T R A C T S

	@Override
	abstract public void job();

	@Override
	abstract public void onDone();

	// E X E C U T E

	@Override
	public void execute()
	{
		EXECUTOR.execute(() -> {
			job();
			HANDLER.post(this::onDone);
		});
	}
}
