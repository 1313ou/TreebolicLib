package treebolic.glue.component;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
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
@SuppressWarnings("EmptyMethod")
public class Tip extends AppCompatDialogFragment
{
	/**
	 * Text name (used when saving instance)
	 */
	private static final String STATE_TEXT = "org.treebolic.tip";

	/**
	 * Text
	 */
	@Nullable
	private String text;

	/**
	 * Constructor
	 */
	public Tip()
	{
		//
	}

	@Override
	public void onCreate(@Nullable final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null)
		{
			// Restore value of members from saved state
			this.text = savedInstanceState.getString(Tip.STATE_TEXT);
		}
	}

	@Override
	public void onSaveInstanceState(@NonNull final Bundle outState)
	{
		outState.putString(Tip.STATE_TEXT, this.text);
		super.onSaveInstanceState(outState);
	}

	@NonNull
	@Override
	public AppCompatDialog onCreateDialog(final Bundle savedInstanceState)
	{
		// use the Builder class for convenient dialog construction
		final Activity activity = getActivity();
		assert activity != null;
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		// get the layout inflater
		final LayoutInflater inflater = activity.getLayoutInflater();

		// inflate layout for the dialog
		final FrameLayout frameLayout = activity.findViewById(android.R.id.custom);
		final View view = inflater.inflate(R.layout.tip_layout, frameLayout, false);

		// data
		final WebView webView = view.findViewById(R.id.text);
		webView.loadData(this.text, "text/html; charset=UTF-8", "utf-8");

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

		// create the dialog and return it
		final AppCompatDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	/**
	 * Set text
	 *
	 * @param text0 text
	 */
	public void setText(@Nullable final String text0)
	{
		this.text = text0;
	}

	/**
	 * Convenience method to display tip
	 *
	 * @param activity activity
	 * @param text     text to display
	 */
	static public void tip(final AppCompatActivity activity, final String text)
	{
		// Tip tip = new Tip();
		// tip.setText(text);
		// tip.show(activity.getSupportFragmentManager(), STATE_TEXT);
	}
}
