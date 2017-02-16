package treebolic.glue;

import android.os.AsyncTask;

/**
 * Worker thread
 *
 * @author Bernard Bou
 */
abstract public class Worker extends AsyncTask<Void, Void, Void> implements treebolic.glue.iface.Worker
{
	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.Worker#job()
	 */
	@Override
	abstract public void job();

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.Worker#onDone()
	 */
	@Override
	abstract public void onDone();

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.Worker#execute()
	 */
	@Override
	public void execute()
	{
		super.execute();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Void doInBackground(final Void... params)
	{
		try
		{
			job();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(final Void result)
	{
		// super.onPostExecute(result);
		onDone();
	}
}
