package treebolic.glue.component;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

import org.treebolic.glue.R;

/**
 * Tip dialog
 *
 * @author Bernard Bou
 */
public class Tip extends DialogFragment
{
	/**
	 * Text name (used when saving instance)
	 */
	static final String STATE_TEXT = "org.treebolic.tip"; //$NON-NLS-1$

	/**
	 * Text
	 */
	private String text;

	/**
	 * Constructor
	 */
	public Tip()
	{
		//
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.DialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null)
		{
			// Restore value of members from saved state
			this.text = savedInstanceState.getString(Tip.STATE_TEXT);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.DialogFragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(final Bundle outState)
	{
		outState.putString(Tip.STATE_TEXT, this.text);
		super.onSaveInstanceState(outState);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState)
	{
		// use the Builder class for convenient dialog construction
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// get the layout inflater
		final LayoutInflater inflater = getActivity().getLayoutInflater();

		// inflate layout for the dialog
		final FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(android.R.id.custom);
		final View view = inflater.inflate(R.layout.tip_layout, frameLayout, false);

		// data
		final WebView webView = (WebView) view.findViewById(R.id.text);
		webView.loadData(this.text, "text/html; charset=UTF-8", "utf-8"); //$NON-NLS-1$ //$NON-NLS-2$

		// set the layout for the dialog
		builder.setView(view) //
		// .setMessage(R.string.treebolic) //
		// .setNegativeButton(R.string.action_dismiss, new DialogInterface.OnClickListener()
		// {
		// @Override
		// public void onClick(DialogInterface dialog, int id)
		// {
		// // user cancelled the dialog
		// }
		// })
		;

		// create the AlertDialog object and return it
		final Dialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	/**
	 * Set text
	 *
	 * @param text0
	 *            text
	 */
	public void setText(final String text0)
	{
		this.text = text0;
	}

	/**
	 * Convenience method to display tip
	 *
	 * @param activity
	 *            activity
	 * @param text
	 *            text to display
	 */
	static public void tip(final Activity activity, final String text)
	{
		// Tip tip = new Tip();
		// tip.setText(text);
		// tip.show(activity.getFragmentManager(), STATE_TEXT);
	}
}
