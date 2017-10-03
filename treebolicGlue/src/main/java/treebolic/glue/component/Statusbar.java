package treebolic.glue.component;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.treebolic.glue.R;

import treebolic.glue.ActionListener;
import treebolic.glue.Color;

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
		final Display display = windowManager.getDefaultDisplay();
		final Point size = new Point();
		display.getSize(size);
		final boolean isHorizontalScreen = size.x >= size.y;
		this.isHorizontal = !isHorizontalScreen;

		// inflate
		final LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ViewGroup wrappedView = (ViewGroup) inflater.inflate(isHorizontalScreen ? R.layout.status_h : R.layout.status_v, this);
		this.statusView = (TextView) wrappedView.findViewById(R.id.status);
		this.contentView = (WebView) wrappedView.findViewById(R.id.content);
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
	public void addListener(ActionListener arg0)
	{
		// does not fire events
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
	public void put(final String label, final String content, final int image)
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
		this.statusView.setText(label == null ? "" : label);

		// content
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
			html.append("</div></body>");
			Log.d(TAG, html.toString());
			this.contentView.loadDataWithBaseURL(Statusbar.base, html.toString(), "text/html; charset=UTF-8", "UTF-8", null);
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
