package treebolic.glue.component;

import android.annotation.TargetApi;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebResourceRequest;
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
public class WebDialog extends AppCompatDialogFragment implements treebolic.glue.iface.component.WebDialog
{
	private static final String TAG = "WebDialog";

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
	private String header;

	/**
	 * Content
	 */
	@Nullable
	private String content;

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
	 */
	public WebDialog()
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
			this.header = savedInstanceState.getString(WebDialog.STATE_HEADER);
			this.content = savedInstanceState.getString(WebDialog.STATE_CONTENT);
		}

		// colors
		int[] colors = Utils.fetchColors(this.activity, R.attr.treebolic_dialog_background, R.attr.treebolic_dialog_foreground, R.attr.treebolic_dialog_foreground_enhanced, R.attr.treebolic_dialog_foreground_icon);
		this.background = colors[0];
		this.foreground = colors[1];
		// int foregroundEnhanced = colors[2];
		// int iconTint = colors[3];
	}

	@Override
	public void onSaveInstanceState(@NonNull final Bundle outState)
	{
		outState.putString(WebDialog.STATE_HEADER, this.header);
		outState.putString(WebDialog.STATE_CONTENT, this.content);
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
		final View view = inflater.inflate(R.layout.dialog_layout, frameLayout, false);

		// header
		final TextView headerView = view.findViewById(R.id.header);
		headerView.setText(this.header);

		// content
		final WebView webView = view.findViewById(R.id.content);
		final StringBuilder html = new StringBuilder();
		html.append("<html><head>");
		html.append("<style type='text/css'>");
		html.append(getDefaultBaseStyle());
		if (this.style != null && !this.style.isEmpty())
		{
			html.append(this.style);
		}
		html.append("</style>");
		html.append("</head><body><div class='body'>");
		// if (this.header != null && !this.header.isEmpty())
		// {
		// html.append("<div class='label'>");
		// html.append(this.header);
		// html.append("</div>");
		// }
		html.append(this.content);
		html.append("</div></body></html>");
		Log.d(TAG, html.toString());

		// client
		final WebViewClient webViewClient = new WebViewClient()
		{
			private boolean intercept = false;

			@Override
			public void onPageFinished(final WebView view0, final String url)
			{
				this.intercept = true;
			}

			@Override
			public boolean shouldOverrideUrlLoading(final WebView view0, @Nullable final String url)
			{
				if (this.intercept && url != null)
				{
					Log.d(WebDialog.TAG, "url:" + url);
					WebDialog.this.actionListener.onAction(url);
					return true;
				}
				return false;
			}

			@TargetApi(Build.VERSION_CODES.N)
			@Override
			public boolean shouldOverrideUrlLoading(final WebView view, @NonNull final WebResourceRequest request)
			{
				final Uri uri = request.getUrl();
				if (this.intercept && uri != null)
				{
					Log.d(WebDialog.TAG, "url:" + uri);
					WebDialog.this.actionListener.onAction(uri);
					return true;
				}
				return false;
			}
		};
		webView.setWebViewClient(webViewClient);

		// load
		//webView.loadDataWithBaseURL(base, html.toString(), "text/html; charset=UTF-8", "UTF-8", null);
		webView.loadDataWithBaseURL(base, html.toString(), "text/html", "UTF-8", null);

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

		// create the dialog object and return it
		final AppCompatDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	@Override
	public void set(final String header0, final String content0)
	{
		this.header = header0;
		this.content = content0;
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
	@TargetApi(Build.VERSION_CODES.M)
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
		WebDialog.base = base0;
	}
}
