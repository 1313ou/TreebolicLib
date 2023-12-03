/*
 * Copyright (c) 2019-2023. Bernard Bou
 */

package treebolic.glue.component;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.treebolic.glue.R;

import java.util.function.Function;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import treebolic.glue.iface.ActionListener;

/**
 * Dialog
 * API class
 *
 * @author Bernard Bou
 * @noinspection WeakerAccess
 */
public class Dialog extends AppCompatDialogFragment implements treebolic.glue.iface.component.Dialog
{
	private static final String TAG = "Dialog";

	/**
	 * Save key for header
	 */
	private static final String STATE_HEADER = "org.treebolic.web.header";

	/**
	 * Save key for content
	 */
	private static final String STATE_CONTENT = "org.treebolic.web.content";

	/**
	 * Base URL for webview
	 */
	static private String base = "file:///android_asset/";

	/**
	 * Header
	 */
	@Nullable
	private CharSequence header;

	/**
	 * Content
	 */
	@Nullable
	private String[] content;

	/**
	 * Converter
	 */
	@Nullable
	private Function<String[], String> converter;

	/**
	 * Style
	 */
	private String style;

	/**
	 * Activity
	 */
	private AppCompatActivity activity;

	/**
	 * Action listener
	 */
	private ActionListener actionListener;

	/**
	 * Background color
	 */
	private int background;

	/**
	 * Foreground color
	 */
	private int foreground;

	/**
	 * Constructor
	 * API
	 */
	public Dialog()
	{
		super();
	}

	@Override
	public void setHandle(final Object handle)
	{
		this.activity = (AppCompatActivity) handle;
	}

	@Override
	public void onCreate(@Nullable final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null)
		{
			// Restore value of members from saved state
			this.header = savedInstanceState.getString(Dialog.STATE_HEADER);
			this.content = savedInstanceState.getStringArray(Dialog.STATE_CONTENT);
		}

		// colors
		@NonNull int[] colors = Utils.fetchColors(this.activity, R.attr.treebolic_dialog_background, R.attr.treebolic_dialog_foreground, R.attr.treebolic_dialog_foreground_enhanced, R.attr.treebolic_dialog_foreground_icon);
		this.background = colors[0];
		this.foreground = colors[1];
		// int foregroundEnhanced = colors[2];
		// int iconTint = colors[3];
	}

	@Override
	public void onSaveInstanceState(@NonNull final Bundle outState)
	{
		outState.putCharSequence(Dialog.STATE_HEADER, this.header);
		outState.putCharSequenceArray(Dialog.STATE_CONTENT, this.content);
		super.onSaveInstanceState(outState);
	}

	@NonNull
	@Override
	public AppCompatDialog onCreateDialog(final Bundle savedInstanceState)
	{
		// use the Builder class for convenient dialog construction
		@NonNull final Activity activity = requireActivity();
		@NonNull final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		// get the layout inflater
		final LayoutInflater inflater = activity.getLayoutInflater();

		// inflate layout for the dialog
		final FrameLayout frameLayout = activity.findViewById(android.R.id.custom);
		View view;
		try
		{
			view = inflater.inflate(R.layout.dialog_layout, frameLayout, false);

			// header
			final TextView headerView = view.findViewById(R.id.header);
			headerView.setText(this.header);

			// content
			final WebView webView = view.findViewById(R.id.content);
			@NonNull final StringBuilder html = new StringBuilder();
			html.append("<html><head>");
			html.append("<style type='text/css'>");
			html.append(getDefaultBaseStyle());
			if (this.style != null && !this.style.isEmpty())
			{
				html.append(this.style);
			}
			html.append("</style>");
			html.append("</head><body><div class='body'>");
			if (this.converter != null)
			{
				html.append(this.converter.apply(this.content));
			}
			else
			{
				html.append(Utils.join("<br>", this.content));
			}

			html.append("</div></body></html>");
			// Log.d(TAG, html.toString());

			// client
			webView.setWebViewClient(makeWebViewClient());

			// load
			//webView.loadDataWithBaseURL(base, html.toString(), "text/html; charset=UTF-8", "UTF-8", null);
			webView.loadDataWithBaseURL(base, html.toString(), "text/html", "UTF-8", null);
		}
		catch (InflateException e)
		{
			view = inflater.inflate(R.layout.dialog_layout_text, frameLayout, false);

			// header
			final TextView headerView = view.findViewById(R.id.header_text);
			headerView.setText(this.header);

			// content
			final TextView textView = view.findViewById(R.id.content_text);
			textView.setText(Utils.join("\n", this.content));
		}

		// set the layout for the dialog
		builder.setView(view) //
		// .setMessage(R.string.title) //
		// .setNegativeButton(R.string.dismiss, (dialog, id) -> {})
		;

		// create the dialog object and return it
		@NonNull final AppCompatDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	@NonNull
	private WebViewClient makeWebViewClient()
	{
		//noinspection RedundantSuppression
		return new WebViewClient()
		{
			private boolean intercept = false;

			@Override
			public void onPageFinished(final WebView view0, final String url)
			{
				this.intercept = true;
			}

			@SuppressWarnings("deprecation")
			@Override
			public boolean shouldOverrideUrlLoading(final WebView view0, @Nullable final String url)
			{
				if (this.intercept && url != null)
				{
					Log.d(Dialog.TAG, "url:" + url);
					Dialog.this.actionListener.onAction(url);
					return true;
				}
				return false;
			}

			@SuppressLint("ObsoleteSdkInt")
			@TargetApi(Build.VERSION_CODES.N)
			@Override
			public boolean shouldOverrideUrlLoading(final WebView view, @NonNull final WebResourceRequest request)
			{
				final Uri uri = request.getUrl();
				if (this.intercept && uri != null)
				{
					Log.d(Dialog.TAG, "url:" + uri);
					Dialog.this.actionListener.onAction(uri);
					return true;
				}
				return false;
			}
		};
	}

	@Override
	public void set(final CharSequence header0, final String... content0)
	{
		this.header = header0;
		this.content = content0;
	}

	@Override
	public void setConverter(@Nullable final Function<String[], String> converter)
	{
		this.converter = converter;
	}

	@Override
	public void setStyle(final String style0)
	{
		this.style = style0;
	}

	/**
	 * Show
	 */
	@Override
	public void display()
	{
		show(this.activity.getSupportFragmentManager(), "info");
	}

	/**
	 * Default style
	 *
	 * @return default style
	 */
	@NonNull
	@SuppressWarnings({"boxing"})
	private String getDefaultBaseStyle()
	{
		return "body {" + //
				String.format("color: #%06X;", 0xFFFFFF & this.foreground) + //
				String.format("background-color: #%06X;", 0xFFFFFF & this.background) + //
				'}';
	}

	@Override
	public void setListener(final ActionListener actionListener0)
	{
		this.actionListener = actionListener0;
	}

	/**
	 * Set base for webview
	 *
	 * @param base0 base URL
	 */
	public static void setBase(String base0)
	{
		Dialog.base = base0;
	}
}
