/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.glue.component;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.treebolic.glue.R;

import java.util.function.BiFunction;
import java.util.function.Function;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import treebolic.glue.iface.ActionListener;

/**
 * Status bar
 * API class
 *
 * @author Bernard Bou
 */
public class Statusbar extends FrameLayout implements treebolic.glue.iface.component.Statusbar
{
	private static final String TAG = "Statusbar";

	/**
	 * Base URL for webview
	 */
	private static String base = "file:///android_asset/";

	/**
	 * Drawables
	 */
	static private final Drawable[] drawables = new Drawable[ImageIndices.values().length];

	/**
	 * Main status view
	 */
	private final TextView statusView;

	/**
	 * Web content status view
	 */
	private final WebView webContentView;

	/**
	 * Text content status view
	 */
	private final TextView textContentView;

	/**
	 * Activity
	 */
	@NonNull
	private final AppCompatActivity activity;

	/**
	 * Horizontal / vertical
	 */
	private final boolean isHorizontal;

	/**
	 * Style
	 */
	private String style;

	/**
	 * Background
	 */
	private final int background;

	/**
	 * Foreground
	 */
	private final int foreground;

	/**
	 * Icon tint
	 */
	private final int iconTint;

	/**
	 * Action listener
	 */
	private ActionListener actionListener;

	// P R O C E S S O R

	/**
	 * Label processor
	 */
	@Nullable
	static private BiFunction<String,View,String> labelProcessor = null;

	/**
	 * Content processor
	 */
	@Nullable
	static private BiFunction<String,View,String> contentProcessor = null;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param activity0 activity
	 */
	@SuppressLint("CutPasteId")
	@TargetApi(Build.VERSION_CODES.M)
	@SuppressWarnings({"WeakerAccess"})
	protected Statusbar(@NonNull final AppCompatActivity activity0)
	{
		super(activity0);
		this.activity = activity0;

		// determine orientation
		final Point size = Utils.screenSize(this.activity);
		final boolean isHorizontalScreen = size.x >= size.y;
		this.isHorizontal = !isHorizontalScreen;

		// colors
		final int[] colors = Utils.fetchColors(this.activity, R.attr.treebolic_statusbar_background, R.attr.treebolic_statusbar_foreground, R.attr.treebolic_statusbar_foreground_icon);
		this.background = colors[0];
		this.foreground = colors[1];
		this.iconTint = colors[2];

		// inflate
		final LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		assert inflater != null;
		TextView statusView0;
		WebView webContentView0 = null;
		TextView textContentView0 = null;
		try
		{
			final ViewGroup wrappedView = (ViewGroup) inflater.inflate(isHorizontalScreen ? R.layout.status_h : R.layout.status_v, this);
			statusView0 = wrappedView.findViewById(R.id.status);
			webContentView0 = wrappedView.findViewById(R.id.content);
			webContentView0.setFocusable(false);
			webContentView0.setBackgroundColor(this.background);
			webContentView0.getSettings().setAllowFileAccess(true);
			webContentView0.setWebViewClient(new WebViewClient()
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
						Log.d(Statusbar.TAG, "url:" + url);
						Statusbar.this.actionListener.onAction(url);
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
						Log.d(Statusbar.TAG, "url:" + uri);
						Statusbar.this.actionListener.onAction(uri.toString());
						return true;
					}
					return false;
				}
			});
		}
		catch (InflateException e)
		{
			final ViewGroup wrappedView = (ViewGroup) inflater.inflate(isHorizontalScreen ? R.layout.status_h_text : R.layout.status_v_text, this);
			statusView0 = wrappedView.findViewById(R.id.status);
			textContentView0 = wrappedView.findViewById(R.id.content);
		}
		this.statusView = statusView0;
		this.webContentView = webContentView0;
		this.textContentView = textContentView0;
	}

	/**
	 * Constructor from handle
	 * API
	 *
	 * @param handle activity
	 */
	public Statusbar(final Object handle)
	{
		this((AppCompatActivity) handle);
	}

	@Override
	public void init(final int image)
	{
		// drawable
		final Drawable drawable = getDrawable(image);

		// tint drawable
		Utils.tint(drawable, this.iconTint);

		// set
		if (this.isHorizontal)
		{
			this.statusView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
		}
		else
		{
			this.statusView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
		}
	}

	@SuppressWarnings("EmptyMethod")
	@Override
	public void addListener(final treebolic.glue.iface.ActionListener arg0)
	{
		// does not fire events
	}

	@Override
	public void setListener(final ActionListener actionListener0)
	{
		this.actionListener = actionListener0;
	}

	/**
	 * Set label processor
	 *
	 * @param processor processor
	 */
	static public void setLabelProcessor(@Nullable @SuppressWarnings("SameParameterValue") final BiFunction<String,View,String> processor)
	{
		Statusbar.labelProcessor = processor;
	}

	/**
	 * Set content processor
	 *
	 * @param processor processor
	 */
	static public void setContentProcessor(@Nullable final BiFunction<String,View,String> processor)
	{
		Statusbar.contentProcessor = processor;
	}

	@SuppressWarnings({"EmptyMethod", "WeakerAccess"})
	@Override
	public void setColors(@Nullable final Integer backColor, @Nullable final Integer foreColor)
	{
		//
	}

	@Override
	public void setStyle(final String style0)
	{
		this.style = style0;
	}

	@Override
	public void put(final int image, @Nullable final Function<CharSequence[], String> converter, final String label0, final String[] content0)
	{
		// icon
		final Drawable drawable = getDrawable(image);

		// tint drawable
		Utils.tint(drawable, this.iconTint);

		// set
		if (this.isHorizontal)
		{
			this.statusView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
		}
		else
		{
			this.statusView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
		}

		// label
		String label = label0;
		if (Statusbar.labelProcessor != null)
		{
			label = labelProcessor.apply(label, this);
		}
		this.statusView.setText(label == null ? "" : label);

		// content
		if (this.webContentView != null)
		{
			String content = converter == null ? Utils.join("<br>", content0) : converter.apply(content0);
			if (Statusbar.contentProcessor != null)
			{
				content = contentProcessor.apply(content, this);
			}

			if (content == null)
			{
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
				{
					this.webContentView.loadUrl("about:blank");
				}
				else
				{
					this.webContentView.clearView();
				}
			}
			else
			{
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
				html.append(content);
				html.append("</div></body></html>");
				// Log.d(TAG, html.toString());

				this.webContentView.loadDataWithBaseURL(Statusbar.base, html.toString(), "text/html", "UTF-8", null);
			}
		}
		else
		{
			this.textContentView.setText(Utils.join("\n", content0));
		}
	}

	@Override
	public void put(final String message)
	{
		this.statusView.setText(message);
	}

	/**
	 * Get drawable from index
	 *
	 * @param index index
	 * @return drawable
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private Drawable getDrawable(final int index)
	{
		if (Statusbar.drawables[index] == null)
		{
			int resId = -1;
			switch (ImageIndices.values()[index])
			{
				case INFO:
					resId = R.drawable.status_info;
					break;
				case LINK:
					resId = R.drawable.status_link;
					break;
				case MOUNT:
					resId = R.drawable.status_mount;
					break;
				case SEARCH:
					resId = R.drawable.status_search;
					break;
				default:
					break;
			}
			if (resId != -1)
			{
				Statusbar.drawables[index] = Utils.getDrawable(this.activity, resId);
			}
		}
		return Statusbar.drawables[index];
	}

	/**
	 * Default style
	 *
	 * @return default style
	 */
	@NonNull
	@SuppressWarnings("boxing")
	private String getDefaultBaseStyle()
	{
		return "body {" + String.format("color: #%06X;", 0xFFFFFF & this.foreground) + //
				String.format("background-color: #%06X;", 0xFFFFFF & this.background) + //
				'}';
	}

	/**
	 * Set base for status bar
	 *
	 * @param base0 base URL
	 */
	public static void setBase(String base0)
	{
		Statusbar.base = base0;
	}
}
