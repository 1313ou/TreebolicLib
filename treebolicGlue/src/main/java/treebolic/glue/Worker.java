/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.glue;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Worker thread
 *
 * @author Bernard Bou
 */
abstract public class Worker implements treebolic.glue.iface.Worker
{
	private final Executor executor = Executors.newSingleThreadExecutor();

	private final Handler handler = new Handler(Looper.getMainLooper());

	@Override
	abstract public void job();

	@Override
	abstract public void onDone();

	@Override
	public void execute()
	{
		this.executor.execute(() -> {
			job();
			this.handler.post(this::onDone);
		});
	}
}
