/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.glue.component;

import android.app.Activity;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.treebolic.glue.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

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
		final Activity activity = requireActivity();
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		// get the layout inflater
		final LayoutInflater inflater = activity.getLayoutInflater();

		final FrameLayout frameLayout = activity.findViewById(android.R.id.custom);

		// get the layout inflater
		View view;
		try
		{
			// inflate layout for the dialog
			view = inflater.inflate(R.layout.tip_layout, frameLayout, false);

			// data
			final WebView webView = view.findViewById(R.id.text);
			webView.loadData(this.text == null ? "" : this.text, "text/html; charset=UTF-8", "utf-8");
		}
		catch(InflateException e)
		{
			// inflate layout for the dialog
			view = inflater.inflate(R.layout.tip_layout_text, frameLayout, false);

			// data
			final TextView textView = view.findViewById(R.id.text_text);
			textView.setText(this.text);
		}

		// attach view to the dialog
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
	@SuppressWarnings("WeakerAccess")
	public void setText(@Nullable final String text0)
	{
		this.text = text0;
	}

	/**
	 * Convenience method to display tip
	 *
	 * @param manager fragment manager
	 * @param text    text to display
	 */
	static public void tip(@NonNull FragmentManager manager, final String text)
	{
		Tip tip = new Tip();
		tip.setText(text);
		tip.show(manager, STATE_TEXT);
	}
}
