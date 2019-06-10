package treebolic.glue;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Worker thread
 *
 * @author Bernard Bou
 */
abstract public class Worker extends AsyncTask<Void, Void, Void> implements treebolic.glue.iface.Worker
{
	@Override
	abstract public void job();

	@Override
	abstract public void onDone();

	@Override
	public void execute()
	{
		super.execute();
	}

	@Nullable
	@Override
	protected Void doInBackground(final Void... params)
	{
		try
		{
			job();
		}
		catch (@NonNull final Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(final Void result)
	{
		// super.onPostExecute(result);
		onDone();
	}
}
