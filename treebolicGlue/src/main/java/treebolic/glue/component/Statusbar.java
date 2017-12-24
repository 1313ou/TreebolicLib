package treebolic.glue.component;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.treebolic.glue.R;

import treebolic.glue.Color;
import treebolic.glue.iface.ActionListener;

/**
 * Status bar
 *
 * @author Bernard Bou
 */
@SuppressLint("ViewConstructor")
public class Statusbar extends FrameLayout implements treebolic.glue.iface.component.Statusbar<Color, ActionListener>
{
	private static final String TAG = "Statusbar";

	/**
	 * Image index enum
	 */
	public enum ImageIndices
	{
		INFO, LINK, MOUNT, SEARCH
	}

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
	 * Content status view
	 */
	private final WebView contentView;

	/**
	 * Activity
	 */
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

	public interface Processor
	{
		/**
		 * Process text
		 *
		 * @param in   text
		 * @param view view
		 * @return out text
		 */
		String process(final String in, final View view);
	}

	/**
	 * Label processor
	 */
	static private Processor labelProcessor = null;

	/**
	 * Content processor
	 */
	static private Processor contentProcessor = null;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param activity0 activity
	 */
	@TargetApi(Build.VERSION_CODES.M)
	@SuppressWarnings({"deprecation", "WeakerAccess"})
	protected Statusbar(final AppCompatActivity activity0)
	{
		super(activity0);
		this.activity = activity0;

		// determine orientation
		final WindowManager windowManager = (WindowManager) this.activity.getSystemService(Context.WINDOW_SERVICE);
		assert windowManager != null;
		final Display display = windowManager.getDefaultDisplay();
		final Point size = new Point();
		display.getSize(size);
		final boolean isHorizontalScreen = size.x >= size.y;
		this.isHorizontal = !isHorizontalScreen;

		// inflate
		final LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		assert inflater != null;
		final ViewGroup wrappedView = (ViewGroup) inflater.inflate(isHorizontalScreen ? R.layout.status_h : R.layout.status_v, this);
		this.statusView = wrappedView.findViewById(R.id.status);
		this.contentView = wrappedView.findViewById(R.id.content);
		this.contentView.setFocusable(false);

		// colors
		int[] colors = Utils.fetchColors(this.activity, R.attr.treebolic_statusbar_background, R.attr.treebolic_statusbar_foreground, R.attr.treebolic_statusbar_foreground_enhanced, R.attr.treebolic_statusbar_foreground_icon);
		this.background = colors[0];
		this.foreground = colors[1];
		// int foregroundEnhanced = colors[2];
		this.iconTint = colors[3];

		this.contentView.setBackgroundColor(this.background);

	}

	/**
	 * Constructor from handle
	 *
	 * @param handle activity
	 */
	protected Statusbar(final Object handle)
	{
		//noinspection ConstantConditions
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

	@Override
	public void addListener(final ActionListener arg0)
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
	static public void setLabelProcessor(@SuppressWarnings("SameParameterValue") final Processor processor)
	{
		Statusbar.labelProcessor = processor;
	}

	/**
	 * Set content processor
	 *
	 * @param processor processor
	 */
	static public void setContentProcessor(final Processor processor)
	{
		Statusbar.contentProcessor = processor;
	}

	@Override
	public void setColors(final Color backColor, final Color foreColor)
	{
		//
	}

	@Override
	public void setStyle(final String style0)
	{
		this.style = style0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void put(final String label0, final String content0, final int image)
	{
		// System.out.println("put("+label+","+content+");");

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
			label = Statusbar.labelProcessor.process(label, this);
		}
		this.statusView.setText(label == null ? "" : label);

		// content
		String content = content0;
		if (Statusbar.contentProcessor != null)
		{
			content = Statusbar.contentProcessor.process(content, this);
		}

		if (content == null)
		{
			if (Build.VERSION.SDK_INT < 18)
			{
				this.contentView.clearView();
			}
			else
			{
				this.contentView.loadUrl("about:blank");
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

			// client
			final WebViewClient webViewClient = new WebViewClient()
			{
				private boolean intercept = false;

				@Override
				public void onPageFinished(final WebView view0, final String url)
				{
					this.intercept = true;
				}

				@SuppressWarnings({"synthetic-access", "deprecation"})
				@Override
				public boolean shouldOverrideUrlLoading(final WebView view0, final String url)
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
				public boolean shouldOverrideUrlLoading(final WebView view, final WebResourceRequest request)
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
			};
			this.contentView.setWebViewClient(webViewClient);
			//this.contentView.loadDataWithBaseURL(Statusbar.base, html.toString(), "text/html; charset=UTF-8", "UTF-8", null);
			this.contentView.loadDataWithBaseURL(Statusbar.base, html.toString(), "text/html", "UTF-8", null);
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
	@SuppressWarnings("deprecation")
	private Drawable getDrawable(final int index)
	{
		if (Statusbar.drawables[index] == null)
		{
			int res = -1;
			switch (ImageIndices.values()[index])
			{
				case INFO:
					res = R.drawable.status_info;
					break;
				case LINK:
					res = R.drawable.status_link;
					break;
				case MOUNT:
					res = R.drawable.status_mount;
					break;
				case SEARCH:
					res = R.drawable.status_search;
					break;
				default:
					break;
			}
			if (res != -1)
			{
				Statusbar.drawables[index] = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP //
						? this.activity.getResources().getDrawable(res, null) : this.activity.getResources().getDrawable(res);
			}
		}
		return Statusbar.drawables[index];
	}

	/**
	 * Default style
	 *
	 * @return default style
	 */
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
