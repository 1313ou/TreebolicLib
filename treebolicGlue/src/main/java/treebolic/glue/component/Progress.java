package treebolic.glue.component;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.treebolic.glue.R;

/**
 * Progress panel
 *
 * @author Bernard Bou
 */
public class Progress extends LinearLayout implements treebolic.glue.iface.component.Progress
{
	/**
	 * Handler
	 */
	static class ProgressHandler extends Handler
	{
		private final Progress progress;

		public ProgressHandler(final Progress progress0)
		{
			this.progress = progress0;
		}

		@SuppressWarnings("synthetic-access")
		@Override
		public void handleMessage(final Message m)
		{
			final boolean fail = m.getData().getBoolean("fail");
			String thisMessage = m.getData().getString("text");
			if (fail)
			{
				thisMessage = this.progress.statusView.getText() + "\n" + thisMessage;
				this.progress.progressBar.setIndeterminate(false);
				this.progress.progressBar.setVisibility(View.GONE);
				this.progress.progressIcon.setImageResource(R.drawable.progress_fail);
			}
			else
			{
				this.progress.progressBar.setIndeterminate(true);
				this.progress.progressBar.setVisibility(View.VISIBLE);
			}
			this.progress.statusView.setText(thisMessage);
		}
	}

	/**
	 * Message handler
	 */
	private final Handler handler;

	/**
	 * Status
	 */
	private final TextView statusView;

	/**
	 * Progress bar
	 */
	private final ProgressBar progressBar;

	/**
	 * Icon
	 */
	private final ImageView progressIcon;

	/**
	 * Constructor
	 *
	 * @param context context
	 */
	@SuppressWarnings("WeakerAccess")
	protected Progress(final Context context)
	{
		super(context);
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER);
		this.handler = new ProgressHandler(this);

		// inflate
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ViewGroup wrappedView = (ViewGroup) inflater.inflate(R.layout.progress, this);
		this.progressIcon = (ImageView) wrappedView.findViewById(R.id.progressIcon);
		this.statusView = (TextView) wrappedView.findViewById(R.id.progressStatus);
		this.progressBar = (ProgressBar) wrappedView.findViewById(R.id.progressBar);
		this.statusView.setText(R.string.status_text);
		this.progressBar.setMax(100);
		this.progressBar.setVisibility(View.INVISIBLE);
		this.progressBar.setIndeterminate(false);
	}

	/**
	 * Constructor
	 *
	 * @param handle context
	 */
	protected Progress(final Object handle)
	{
		this((Context) handle);
	}

	@Override
	public void put(final String thisMessage, final boolean fail)
	{
		// setText(thisMessage); (passing it to a handler as only the original thread that created a view hierarchy can touch its views.
		final Message message = this.handler.obtainMessage();
		final Bundle bundle = new Bundle();
		bundle.putString("text", thisMessage);
		bundle.putBoolean("fail", fail);
		message.setData(bundle);
		this.handler.sendMessage(message);
	}
}
