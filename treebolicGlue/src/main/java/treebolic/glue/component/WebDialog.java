package treebolic.glue.component;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.treebolic.glue.R;

import treebolic.glue.iface.ActionListener;

/**
 * WebDialog
 *
 * @author Bernard Bou
 */
public class WebDialog extends DialogFragment implements treebolic.glue.iface.component.WebDialog
{
	static final String TAG = "WebDialog"; //$NON-NLS-1$

	/**
	 * Save key for header
	 */
	static final String STATE_HEADER = "org.treebolic.header"; //$NON-NLS-1$

	/**
	 * Save key for content
	 */
	static final String STATE_CONTENT = "org.treebolic.content"; //$NON-NLS-1$

	/**
	 * Base URL for webview
	 */
	static private String base = "file:///android_asset/"; //$NON-NLS-1$

	/**
	 * Header
	 */
	private String header;

	/**
	 * Content
	 */
	private String content;

	/**
	 * Style
	 */
	private String style;

	/**
	 * Activity
	 */
	private final Activity activity;

	/**
	 * Action listener
	 */
	private ActionListener actionListener;

	/**
	 * Constructor
	 *
	 * @param activity0
	 *            activity
	 */
	public WebDialog(final Activity activity0)
	{
		super();
		this.activity = activity0;
	}

	/**
	 * Constructor
	 *
	 * @param handle
	 *            activity handle
	 */
	public WebDialog(final Object handle)
	{
		this((Activity) handle);
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
			this.header = savedInstanceState.getString(WebDialog.STATE_HEADER);
			this.content = savedInstanceState.getString(WebDialog.STATE_CONTENT);
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
		outState.putString(WebDialog.STATE_HEADER, this.header);
		outState.putString(WebDialog.STATE_CONTENT, this.content);
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
		final View view = inflater.inflate(R.layout.dialog_layout, frameLayout, false);

		// header
		final TextView headerView = (TextView) view.findViewById(R.id.header);
		headerView.setText(this.header);

		// content
		final WebView webView = (WebView) view.findViewById(R.id.content);
		final StringBuffer html = new StringBuffer();
		html.append("<html><head>"); //$NON-NLS-1$
		html.append("<style type='text/css'>"); //$NON-NLS-1$
		html.append(getDefaultBaseStyle());
		if (this.style != null && !this.style.isEmpty())
		{
			html.append(this.style);
		}
		html.append("</style>"); //$NON-NLS-1$
		html.append("</head><body class='pane'>"); //$NON-NLS-1$
		// if (this.header != null && !this.header.isEmpty())
		// {
		// html.append("<div class='label'>");
		// html.append(this.header);
		// html.append("</div>");
		// }
		html.append("</head><body>"); //$NON-NLS-1$
		html.append(this.content);
		html.append("</body>"); //$NON-NLS-1$

		// client
		final WebViewClient webViewClient = new WebViewClient()
		{
			private boolean intercept = false;

			@Override
			public void onPageFinished(final WebView view0, final String url)
			{
				this.intercept = true;
			}

			@SuppressWarnings("synthetic-access")
			@Override
			public boolean shouldOverrideUrlLoading(final WebView view0, final String url)
			{
				if (this.intercept && url != null)
				{
					Log.d(WebDialog.TAG, "url:" + url); //$NON-NLS-1$
					WebDialog.this.actionListener.onAction(url);
					return true;
				}
				return false;
			}
		};
		webView.setWebViewClient(webViewClient);

		// load
		webView.loadDataWithBaseURL(base, html.toString(), "text/html; charset=UTF-8", "UTF-8", null); //$NON-NLS-1$ //$NON-NLS-2$

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

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.component.WebDialog#set(java.lang.String, java.lang.String)
	 */
	@Override
	public void set(final String header0, final String content0)
	{
		this.header = header0;
		this.content = content0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.component.WebDialog#setStyle(java.lang.String)
	 */
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
		show(this.activity.getFragmentManager(), "info"); //$NON-NLS-1$
	}

	/**
	 * Default style
	 *
	 * @return default style
	 */
	@TargetApi(Build.VERSION_CODES.M)
	@SuppressWarnings({ "boxing", "deprecation" })
	private String getDefaultBaseStyle()
	{
		final Resources resources = getResources();
		final StringBuffer buffer = new StringBuffer();
		buffer.append("body {"); //$NON-NLS-1$
		final int background = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? resources.getColor(R.color.background, null) : resources.getColor(R.color.background);
		buffer.append(String.format("background-color: #%06X;", 0xFFFFFF & background)); //$NON-NLS-1$
		final int foreground = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? resources.getColor(R.color.foreground, null) : resources.getColor(R.color.foreground);
		buffer.append(String.format("color: #%06X;", 0xFFFFFF & foreground)); //$NON-NLS-1$
		buffer.append('}');
		return buffer.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.component.WebDialog#setListener(treebolic.glue.iface.ActionListener)
	 */
	@Override
	public void setListener(final ActionListener actionListener0)
	{
		this.actionListener = actionListener0;
	}

	/**
	 * Set base for webview
	 * 
	 * @param base0
	 *            base URL
	 */
	public static void setBase(String base0)
	{
		WebDialog.base = base0;
	}
}
